// src/main/java/ide/vox/code/bl/ChatService.java
package ide.vox.code.bl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import ide.vox.code.dto.ChatResponseDTO;
import ide.vox.code.dto.OutParamDTO;
import ide.vox.code.dto.GroupListDTO;
import ide.vox.code.dto.FunctionListDTO;

import jakarta.json.*;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ChatService {
    private static final Logger LOGGER = Logger.getLogger(ChatService.class);

    @ConfigProperty(name = "openai.api.key")
    String apiKey;

    @ConfigProperty(name = "openai.api.url")
    String apiUrl;

    @Inject GroupService groupService;
    @Inject FunctionService functionService;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public ChatResponseDTO chat(String message) throws Exception {
        LOGGER.infof("Incoming (ES): %s", message);

        // 1) Contexto
        List<GroupListDTO> groups      = groupService.listGroups();
        List<FunctionListDTO> functions = functionService.listFunctions();

        // 2) Nuevo system prompt
        StringBuilder sys = new StringBuilder()
                .append("Eres un asistente para un IDE accesible. ")
                .append("Los datos de BD están en inglés; los mensajes llegan en español. ")
                .append("DEVUELVE EXACTAMENTE un solo JSON con estos campos:\n")
                .append("  match: true|false,\n")
                .append("  allParams: true|false,\n")
                .append("  functionKey: string|null,\n")
                .append("  groupKey: string|null,\n")
                .append("  outParams: [{paramKey:string,value:string},...],\n")
                .append("  summary: string  (en español, saludo natural al usuario)\n\n")
                .append("IMPORTANTE:\n")
                .append("- `outParams` contiene solo los parámetros de **salida**.  \n")
                .append("- Si faltan parámetros de **entrada**, menciónalos **solo** en el campo `summary`, en español y de forma natural.\n")
                .append("- No incluyas los parámetros de entrada en `outParams` ni crees otros campos JSON.\n")
                .append("- Usa estos `paramKey` exactamente en `outParams`:\n")
                .append("    • #LINE_NUMBER  \n")
                .append("    • #GENERATED_CODE  \n\n")
                .append("Cuando la función corresponde a terminal o editor, el contenido generado va en `#GENERATED_CODE`.\n\n")
                .append("EJEMPLOS:\n")
                .append("1) Éxito completo:\n")
                .append("{\n")
                .append("  \"match\": true,\n")
                .append("  \"allParams\": true,\n")
                .append("  \"functionKey\": \"#INSERT_CODE\",\n")
                .append("  \"groupKey\": \"#EDITOR_WINDOW\",\n")
                .append("  \"outParams\": [\n")
                .append("    {\"paramKey\":\"#LINE_NUMBER\",\"value\":\"5\"},\n")
                .append("    {\"paramKey\":\"#GENERATED_CODE\",\"value\":\"def is_prime(n): ...\"}\n")
                .append("  ],\n")
                .append("  \"summary\": \"He insertado el código para verificar números primos en la línea 5.\"\n")
                .append("}\n\n")
                .append("2) Coincidencia pero faltan entradas:\n")
                .append("{\n")
                .append("  \"match\": true,\n")
                .append("  \"allParams\": false,\n")
                .append("  \"functionKey\": \"#INSERT_CODE\",\n")
                .append("  \"groupKey\": \"#EDITOR_WINDOW\",\n")
                .append("  \"outParams\": [],\n")
                .append("  \"summary\": \"He identificado la acción de insertar código, pero faltan el número de línea y la descripción del código a generar.\"\n")
                .append("}\n\n")
                .append("3) Sin coincidencia:\n")
                .append("{\n")
                .append("  \"match\": false,\n")
                .append("  \"allParams\": false,\n")
                .append("  \"functionKey\": null,\n")
                .append("  \"groupKey\": null,\n")
                .append("  \"outParams\": [],\n")
                .append("  \"summary\": \"No reconozco ninguna acción válida para el IDE.\"\n")
                .append("}\n\n")
                .append("FUNCIONES DISPONIBLES:\n");
        for (FunctionListDTO f : functions) {
            sys.append(String.format(
                    "- %s (grupo: %s) in=%d out=%d%n",
                    f.getKey(), f.getGroupKey(),
                    f.getInputCount(), f.getOutputCount()
            ));
        }

        LOGGER.debug("System Prompt:\n" + sys);

        // 3) Construcción de mensajes
        JsonArrayBuilder arr = Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                        .add("role", "system")
                        .add("content", sys.toString()))
                .add(Json.createObjectBuilder()
                        .add("role", "user")
                        .add("content", message));

        JsonObject body = Json.createObjectBuilder()
                .add("model", "gpt-4")
                .add("messages", arr)
                .build();

        LOGGER.debug("Request JSON: " + body);

        // 4) Llamada OpenAI
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        LOGGER.infof("OpenAI HTTP %d", resp.statusCode());
        LOGGER.trace("Raw body: " + resp.body());

        // 5) Extraer content
        try (JsonReader jr = Json.createReader(new StringReader(resp.body()))) {
            JsonObject msg = jr.readObject()
                    .getJsonArray("choices")
                    .getJsonObject(0)
                    .getJsonObject("message");
            String content = msg.getString("content").trim();
            LOGGER.debug("Content reply: " + content);

            // 6) Parsear JSON de content
            JsonObject out = Json.createReader(new StringReader(content)).readObject();
            boolean match     = out.getBoolean("match");
            boolean allParams = out.getBoolean("allParams");
            String fnKey      = out.isNull("functionKey") ? null : out.getString("functionKey");
            String grpKey     = out.isNull("groupKey")    ? null : out.getString("groupKey");

            List<OutParamDTO> ops = new ArrayList<>();
            for (JsonValue v : out.getJsonArray("outParams")) {
                JsonObject o = v.asJsonObject();
                ops.add(new OutParamDTO(o.getString("paramKey"), o.getString("value")));
            }
            String summary = out.getString("summary");
            return new ChatResponseDTO(match, allParams, fnKey, grpKey, ops, summary);
        }
    }
}

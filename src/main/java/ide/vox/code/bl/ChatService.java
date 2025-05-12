package ide.vox.code.bl;

import ide.vox.code.dto.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class ChatService {
    private static final Logger LOGGER = Logger.getLogger(ChatService.class);

    @ConfigProperty(name = "openai.api.key")
    String apiKey;

    @ConfigProperty(name = "openai.api.url")
    String apiUrl;

    @Inject
    GroupService groupService;

    @Inject
    FunctionService functionService;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public ChatResponseDTO chat(String message) throws Exception {
        LOGGER.infof("Incoming (ES): %s", message);

        // 1) Load groups and functions
        List<GroupListDTO> groups      = groupService.listGroups();
        List<FunctionListDTO> functions = functionService.listFunctions();

// 2) Simplified list for logging / lookup
        List<Map<String,String>> simpleFuncs = functions.stream()
                .map(f -> Map.<String,String>of(
                        "key",         f.getKey(),
                        "groupKey",    f.getGroupKey(),
                        "description", f.getDescription()
                ))
                .collect(Collectors.toList());

        LOGGER.infof("Simple functions list: %s", simpleFuncs);

        // 2) First call: match only
        StringBuilder matchPrompt = new StringBuilder()
                .append("You are an assistant for an accessible IDE. ")
                .append("Database entries are in English; user messages are voice transcriptions in Spanish ")
                .append("(except code/commands or if the user explicitly requests English). ")
                .append("The transcription may contain errors; interpret intent correctly.\n\n")
                .append("Receive the following user message (in Spanish) and respond ONLY with one of the function keys ")
                .append("from the list or '#NONE' if no match:\n\nFunctions available: ")
                .append(simpleFuncs).append("\n");

        matchPrompt.append("\n\nUser message: \"").append(message).append("\"");

        HttpRequest matchRequest = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        Json.createObjectBuilder()
                                .add("model", "gpt-3.5-turbo")
                                .add("messages", Json.createArrayBuilder()
                                        .add(Json.createObjectBuilder()
                                                .add("role", "system")
                                                .add("content", matchPrompt.toString()))
                                        .build())
                                .build()
                                .toString()
                ))
                .build();

        HttpResponse<String> matchResp = httpClient.send(matchRequest, HttpResponse.BodyHandlers.ofString());
        LOGGER.debugf("Match response: %s", matchResp.body());

        String rawKey;
        try (JsonReader jr = Json.createReader(new StringReader(matchResp.body()))) {
            JsonObject choice = jr.readObject()
                    .getJsonArray("choices").getJsonObject(0)
                    .getJsonObject("message");
            rawKey = choice.getString("content").trim();
        }

        if (!rawKey.startsWith("#") || "#NONE".equalsIgnoreCase(rawKey)) {
            return new ChatResponseDTO(
                    false, false, null, null,
                    Collections.emptyList(),
                    "No reconozco ninguna acción válida para el IDE."
            );
        }

        String functionKey = rawKey;
        String groupKey = functions.stream()
                .filter(f -> f.getKey().equals(functionKey))
                .map(FunctionListDTO::getGroupKey)
                .findFirst().orElse(null);

        // 3) Load parameter definitions
// 3) Load parameter definitions
        FunctionParamsDTO fp = functionService.getFunctionParams(functionKey);

// Listado de entradas con descripción
        List<ParamRequestDTO> inDefs = fp.getRequestParams();
        List<String> inputDefs = inDefs.stream()
                .map(r -> String.format("%s: %s", r.getKey(), r.getDescription()))
                .toList();

// Listado de salidas con descripción
        List<ParamResponseDTO> outDefs = fp.getResponseParams();
        List<String> outputDefs = outDefs.stream()
                .map(r -> String.format("%s: %s", r.getKey(), r.getDescription()))
                .toList();

// debug print
        LOGGER.info("Input parameters (key: description): " + inputDefs);
        LOGGER.info("Output parameters (key: description): " + outputDefs);

        // 4) Second call: build detail prompt
        StringBuilder detailPrompt = new StringBuilder()
                .append("You are an assistant for an accessible IDE.\n")
                .append("Identified function: ").append(functionKey).append("\n")
                .append("Required inputs parameters (all are mandatory): ").append(inputDefs).append("\n")
                .append("Original user message (Spanish and voice recorded): \"").append(message).append("\"\n\n")
                .append("Respond with a VALID JSON only, no extra text, containing:\n")
                .append("  match: true,\n")
                .append("  allParams: true|false,\n")
                .append("  functionKey: \"").append(functionKey).append("\",\n")
                .append("  groupKey: \"").append(groupKey).append("\",\n")
                .append("  outParams: [{paramKey:string,value:string},...],\n")
                .append("  summary: string (in Spanish, natural greeting)\n\n")
                .append("IMPORTANT:\n")
                .append("- First step is to verify if al input params are included or mention in the user message. If not, you cannot continue and you have to give allParams: false, no outPparams and in the summary message indicate wich params are missing and retry\n")
                .append("- Only if If all inputs present in the user message, allParams=true and continue.\n")
                .append("- outParams MUST include EXACTLY all defined output parameters, no more, no less.\n")
                .append("Required output parameters (all are mandatory and no structure changable): ").append(outputDefs).append("\n")
//                .append("- Just in these paramKeyOut values (if are present in re output parameters list) generate new code :\n")
//                .append("    • #COMMAND for IA identified terminal commands\n")
//                .append("    • #GENERATED_CODE for IA generated code\n\n")
//                .append("    • #GENERATED_CODE for IA generated code\n\n")
                .append("Also for out params consider that not exclaclty the message recieved has to be put on due to the message is voice recorded in Spanish so some thing could be miss [samples: the user says ache te te pe but it could mean http. Be ele ele could mean Bl. hola mundo punto yava could mean HolaMundo.java and so on. And some thimes there could be more text than needes for the params (ommit)]\n\n");


        HttpRequest detailRequest = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        Json.createObjectBuilder()
                                .add("model", "gpt-4-turbo")
                                .add("messages", Json.createArrayBuilder()
                                        .add(Json.createObjectBuilder()
                                                .add("role", "system")
                                                .add("content", detailPrompt.toString()))
                                        .build())
                                .build()
                                .toString()
                ))
                .build();

        HttpResponse<String> detailResp = httpClient.send(detailRequest, HttpResponse.BodyHandlers.ofString());
        LOGGER.infof("Detail response: %s", detailResp.body());

        String detailContent;
        try (JsonReader jr = Json.createReader(new StringReader(detailResp.body()))) {
            JsonObject choice = jr.readObject()
                    .getJsonArray("choices").getJsonObject(0)
                    .getJsonObject("message");
            detailContent = choice.getString("content").trim();
        }

        int start = detailContent.indexOf('{'), end = detailContent.lastIndexOf('}');
        String jsonOnly = detailContent.substring(start, end + 1);

        JsonObject out = Json.createReader(new StringReader(jsonOnly)).readObject();
        boolean allParamsOut = out.getBoolean("allParams");
        JsonArray arrOut = out.getJsonArray("outParams");

        List<OutParamDTO> outParams = new ArrayList<>();
        for (var v : arrOut) {
            JsonObject o = (JsonObject) v;
            outParams.add(new OutParamDTO(o.getString("paramKey"), o.getString("value")));
        }
        String summary = out.getString("summary");

        return new ChatResponseDTO(
                true, allParamsOut,
                functionKey, groupKey,
                outParams, summary
        );
    }
}

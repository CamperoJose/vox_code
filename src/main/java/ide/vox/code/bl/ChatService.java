package ide.vox.code.bl;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import ide.vox.code.dto.ChatResponseDTO;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@ApplicationScoped
public class ChatService {

    @ConfigProperty(name = "openai.api.key")
    String apiKey;

    @ConfigProperty(name = "openai.api.url")
    String apiUrl;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public ChatResponseDTO chat(String message) throws Exception {
        // Preparo el array de mensajes con un solo mensaje de rol "user"
        JsonArrayBuilder messagesArr = Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                        .add("role", "user")
                        .add("content", message));

        // Construyo body JSON
        JsonObject requestBody = Json.createObjectBuilder()
                .add("model", "gpt-3.5-turbo")
                .add("messages", messagesArr)
                .build();

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

        HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());

        // Parseo la respuesta de OpenAI
        try (JsonReader reader = Json.createReader(new StringReader(resp.body()))) {
            JsonObject obj = reader.readObject();
            String reply = obj
                    .getJsonArray("choices")
                    .getJsonObject(0)
                    .getJsonObject("message")
                    .getString("content")
                    .trim();
            return new ChatResponseDTO(reply);
        }
    }
}
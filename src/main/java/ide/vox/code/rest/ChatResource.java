package ide.vox.code.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import ide.vox.code.bl.ChatService;
import ide.vox.code.dto.ChatRequestDTO;
import ide.vox.code.dto.ChatResponseDTO;

@Path("/chat")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ChatResource {

    @Inject
    ChatService chatService;

    @POST
    public ChatResponseDTO chat(ChatRequestDTO request) {
        try {
            return chatService.chat(request.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error calling OpenAI: " + e.getMessage(), e);
        }
    }
}
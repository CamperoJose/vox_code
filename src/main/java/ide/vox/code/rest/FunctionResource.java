package ide.vox.code.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import ide.vox.code.bl.FunctionService;
import ide.vox.code.dto.FunctionListDTO;

import java.util.List;

@Path("/function")
@Produces(MediaType.APPLICATION_JSON)
public class FunctionResource {

    @Inject
    FunctionService functionService;

    @GET
    @Path("/list")
    public List<FunctionListDTO> list() {
        return functionService.listFunctions();
    }
}
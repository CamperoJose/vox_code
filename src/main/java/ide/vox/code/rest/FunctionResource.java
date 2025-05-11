package ide.vox.code.rest;

import ide.vox.code.dto.FunctionKeyDTO;
import ide.vox.code.dto.FunctionParamsDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
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

    @GET
    @Path("/params")
    public FunctionParamsDTO getParams(FunctionKeyDTO request) {
        return functionService.getFunctionParams(request.getKey());
    }
}
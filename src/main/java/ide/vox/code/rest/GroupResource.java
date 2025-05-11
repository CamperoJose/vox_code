package ide.vox.code.rest;


import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import ide.vox.code.bl.GroupService;
import ide.vox.code.dto.GroupListDTO;

import java.util.List;

@Path("/group")
@Produces(MediaType.APPLICATION_JSON)
public class GroupResource {

    @Inject
    GroupService groupService;

    @GET
    @Path("/list")
    public List<GroupListDTO> list() {
        return groupService.listGroups();
    }
}

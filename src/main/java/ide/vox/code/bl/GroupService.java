package ide.vox.code.bl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ide.vox.code.dto.GroupListDTO;
import ide.vox.code.data.entity.GroupEntity;
import ide.vox.code.repository.GroupRepository;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class GroupService {

    @Inject
    GroupRepository groupRepository;

    @Transactional
    public List<GroupListDTO> listGroups() {
        return groupRepository
                .list("status", true)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private GroupListDTO toDto(GroupEntity g) {
        int count = g.getFunctions() != null ? g.getFunctions().size() : 0;
        return new GroupListDTO(
                g.getKey(),
                g.getName(),
                g.getDescription(),
                count
        );
    }
}

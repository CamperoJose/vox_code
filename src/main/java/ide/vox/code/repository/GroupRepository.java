package ide.vox.code.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import ide.vox.code.data.entity.GroupEntity;

@ApplicationScoped
public class GroupRepository implements PanacheRepository<GroupEntity> {
    // you can add custom query methods here if needed
}

package ide.vox.code.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import ide.vox.code.data.entity.FunctionEntity;

@ApplicationScoped
public class FunctionRepository implements PanacheRepository<FunctionEntity> {
    // custom queries if needed
}
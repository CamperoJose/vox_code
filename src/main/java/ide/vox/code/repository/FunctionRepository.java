package ide.vox.code.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import ide.vox.code.data.entity.FunctionEntity;

import java.util.Optional;

@ApplicationScoped
public class FunctionRepository implements PanacheRepository<FunctionEntity> {
    public Optional<FunctionEntity> findByKey(String key) {
        return find("key", key).firstResultOptional();
    }
}
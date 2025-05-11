package ide.vox.code.bl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ide.vox.code.dto.FunctionListDTO;
import ide.vox.code.data.entity.FunctionEntity;
import ide.vox.code.repository.FunctionRepository;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class FunctionService {

    @Inject
    FunctionRepository functionRepository;

    @Transactional
    public List<FunctionListDTO> listFunctions() {
        return functionRepository
                .list("status", true)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private FunctionListDTO toDto(FunctionEntity f) {
        String groupKey = f.getGroup() != null ? f.getGroup().getKey() : null;
        int inCount  = f.getParamRequests()  != null ? f.getParamRequests().size()  : 0;
        int outCount = f.getParamResponses() != null ? f.getParamResponses().size() : 0;
        return new FunctionListDTO(
                groupKey,
                f.getKey(),
                f.getName(),
                f.getDescription(),
                inCount,
                outCount
        );
    }
}
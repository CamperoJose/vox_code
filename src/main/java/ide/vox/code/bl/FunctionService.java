package ide.vox.code.bl;

import ide.vox.code.dto.FunctionParamsDTO;
import ide.vox.code.dto.ParamRequestDTO;
import ide.vox.code.dto.ParamResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ide.vox.code.dto.FunctionListDTO;
import ide.vox.code.data.entity.FunctionEntity;
import ide.vox.code.repository.FunctionRepository;
import jakarta.ws.rs.NotFoundException;

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

    @Transactional
    public FunctionParamsDTO getFunctionParams(String key) {
        FunctionEntity fn = functionRepository.findByKey(key)
                .orElseThrow(() -> new NotFoundException("Function not found: " + key));

        var reqDtos = fn.getParamRequests().stream()
                .filter(p -> Boolean.TRUE.equals(p.getStatus()))
                .map(p -> {
                    ParamRequestDTO dto = new ParamRequestDTO(
                            p.getId(),
                            p.getKey(),
                            p.getDescription(),
                            Boolean.TRUE.equals(p.getRequired())
                    );
                    return dto;
                })
                .collect(Collectors.toList());

        var respDtos = fn.getParamResponses().stream()
                .filter(p -> Boolean.TRUE.equals(p.getStatus()))
                .map(p -> {
                    ParamResponseDTO dto = new ParamResponseDTO(
                            p.getId(),
                            p.getKey(),
                            p.getDescription(),
                            p.getDataType(),
                            Boolean.TRUE.equals(p.getRequired())
                    );
                    return dto;
                })
                .collect(Collectors.toList());

        return new FunctionParamsDTO(fn.getKey(), reqDtos, respDtos);
    }
}
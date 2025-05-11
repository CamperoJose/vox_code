package ide.vox.code.dto;

import java.util.List;

public class FunctionParamsDTO {
    private String functionKey;
    private List<ParamRequestDTO> requestParams;
    private List<ParamResponseDTO> responseParams;

    public FunctionParamsDTO(String functionKey,
                             List<ParamRequestDTO> requestParams,
                             List<ParamResponseDTO> responseParams) {
        this.functionKey = functionKey;
        this.requestParams = requestParams;
        this.responseParams = responseParams;
    }

    public String getFunctionKey() {
        return functionKey;
    }

    public void setFunctionKey(String functionKey) {
        this.functionKey = functionKey;
    }

    public List<ParamRequestDTO> getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(List<ParamRequestDTO> requestParams) {
        this.requestParams = requestParams;
    }

    public List<ParamResponseDTO> getResponseParams() {
        return responseParams;
    }

    public void setResponseParams(List<ParamResponseDTO> responseParams) {
        this.responseParams = responseParams;
    }
}
package ide.vox.code.dto;

import java.util.List;

public class ChatResponseDTO {
    private boolean match;
    private boolean allParams;
    private String functionKey;
    private String groupKey;
    private List<OutParamDTO> outParams;
    private String summary;

    public ChatResponseDTO() {}

    public ChatResponseDTO(boolean match,
                           boolean allParams,
                           String functionKey,
                           String groupKey,
                           List<OutParamDTO> outParams,
                           String summary) {
        this.match = match;
        this.allParams = allParams;
        this.functionKey = functionKey;
        this.groupKey = groupKey;
        this.outParams = outParams;
        this.summary = summary;
    }

    // getters & setters

    public boolean isMatch() {
        return match;
    }

    public void setMatch(boolean match) {
        this.match = match;
    }

    public boolean isAllParams() {
        return allParams;
    }

    public void setAllParams(boolean allParams) {
        this.allParams = allParams;
    }

    public String getFunctionKey() {
        return functionKey;
    }

    public void setFunctionKey(String functionKey) {
        this.functionKey = functionKey;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public List<OutParamDTO> getOutParams() {
        return outParams;
    }

    public void setOutParams(List<OutParamDTO> outParams) {
        this.outParams = outParams;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
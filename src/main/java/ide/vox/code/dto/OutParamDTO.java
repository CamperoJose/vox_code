package ide.vox.code.dto;

public class OutParamDTO {
    private String paramKey;
    private String value;

    public OutParamDTO() {}

    public OutParamDTO(String paramKey, String value) {
        this.paramKey = paramKey;
        this.value = value;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
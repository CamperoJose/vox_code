package ide.vox.code.dto;


public class ParamResponseDTO {
    private Integer id;
    private String key;
    private String description;
    private String dataType;
    private boolean required;

    public ParamResponseDTO(Integer id, String key, String description, String dataType, boolean required) {
        this.id = id;
        this.key = key;
        this.description = description;
        this.dataType = dataType;
        this.required = required;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
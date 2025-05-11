package ide.vox.code.dto;

public class ParamRequestDTO {
    private Integer id;
    private String key;
    private String description;
    private boolean required;

    public ParamRequestDTO(Integer id, String key, String description, boolean required) {
        this.id = id;
        this.key = key;
        this.description = description;
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

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
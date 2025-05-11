package ide.vox.code.dto;

public class GroupListDTO {
    private String key;
    private String name;
    private String description;
    private Integer functionCount;

    public GroupListDTO(String key, String name, String description, Integer functionCount) {
        this.key = key;
        this.name = name;
        this.description = description;
        this.functionCount = functionCount;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getFunctionCount() {
        return functionCount;
    }

    public void setFunctionCount(Integer functionCount) {
        this.functionCount = functionCount;
    }
}

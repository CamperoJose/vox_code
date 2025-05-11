package ide.vox.code.dto;

public class FunctionListDTO {
    private String groupKey;
    private String key;
    private String name;
    private String description;
    private int inputCount;
    private int outputCount;

    public FunctionListDTO(String groupKey, String key, String name, String description,
                           int inputCount, int outputCount) {
        this.groupKey = groupKey;
        this.key = key;
        this.name = name;
        this.description = description;
        this.inputCount = inputCount;
        this.outputCount = outputCount;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
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

    public int getInputCount() {
        return inputCount;
    }

    public void setInputCount(int inputCount) {
        this.inputCount = inputCount;
    }

    public int getOutputCount() {
        return outputCount;
    }

    public void setOutputCount(int outputCount) {
        this.outputCount = outputCount;
    }
}

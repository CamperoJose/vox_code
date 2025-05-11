package ide.vox.code.data.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "`group`")
public class GroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50)
    private String key;

    @Column(length = 50)
    private String name;

    @Column(length = 500)
    private String description;

    private Boolean status;

    private Integer version;

    @OneToMany(mappedBy = "group")
    private List<FunctionEntity> functions;

    public GroupEntity() {
    }

    public GroupEntity(Integer id, String key, String name, String description, Boolean status, Integer version, List<FunctionEntity> functions) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.description = description;
        this.status = status;
        this.version = version;
        this.functions = functions;
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<FunctionEntity> getFunctions() {
        return functions;
    }

    public void setFunctions(List<FunctionEntity> functions) {
        this.functions = functions;
    }
}

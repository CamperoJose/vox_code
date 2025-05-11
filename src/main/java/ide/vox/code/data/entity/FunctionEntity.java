package ide.vox.code.data.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "function")
public class FunctionEntity {
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

    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupEntity group;

    @OneToMany(mappedBy = "function")
    private List<ParamRequestEntity> paramRequests;

    @OneToMany(mappedBy = "function")
    private List<ParamResponseEntity> paramResponses;

    public FunctionEntity() {
    }

    public FunctionEntity(Integer id, String key, String name, String description, Boolean status, Integer version, GroupEntity group, List<ParamRequestEntity> paramRequests, List<ParamResponseEntity> paramResponses) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.description = description;
        this.status = status;
        this.version = version;
        this.group = group;
        this.paramRequests = paramRequests;
        this.paramResponses = paramResponses;
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

    public GroupEntity getGroup() {
        return group;
    }

    public void setGroup(GroupEntity group) {
        this.group = group;
    }

    public List<ParamRequestEntity> getParamRequests() {
        return paramRequests;
    }

    public void setParamRequests(List<ParamRequestEntity> paramRequests) {
        this.paramRequests = paramRequests;
    }

    public List<ParamResponseEntity> getParamResponses() {
        return paramResponses;
    }

    public void setParamResponses(List<ParamResponseEntity> paramResponses) {
        this.paramResponses = paramResponses;
    }
}

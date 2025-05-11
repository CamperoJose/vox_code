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
}

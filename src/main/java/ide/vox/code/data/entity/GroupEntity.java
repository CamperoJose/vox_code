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
}

package ide.vox.code.data.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "param_request")
public class ParamRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50)
    private String key;

    @Column(length = 500)
    private String description;

    private Boolean required;

    private Boolean status;

    private Integer version;

    @ManyToOne
    @JoinColumn(name = "function_id")
    private FunctionEntity function;
}

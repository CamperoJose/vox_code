package ide.vox.code.data.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "param_response")
public class ParamResponseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50)
    private String key;

    @Column(length = 500)
    private String description;

    private Boolean required;

    @Column(name = "data_type", length = 50)
    private String dataType;

    private Boolean status;

    private Integer version;

    @ManyToOne
    @JoinColumn(name = "function_id")
    private FunctionEntity function;
}

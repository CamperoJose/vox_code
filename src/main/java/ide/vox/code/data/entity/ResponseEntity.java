package ide.vox.code.data.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "response")
public class ResponseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "responded_at")
    private LocalDateTime respondedAt;

    @Column(name = "agent_model", length = 50)
    private String agentModel;

    @Lob
    @Column(name = "json_response")
    private String jsonResponse;

    @Column(name = "available_match")
    private Boolean availableMatch;

    private Boolean status;

    @Column(name = "`user`", length = 100)
    private String user;

    private Integer version;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private RequestEntity request;

    @ManyToOne
    @JoinColumn(name = "function_id")
    private FunctionEntity function;
}

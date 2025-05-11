package ide.vox.code.data.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "`request`")
public class RequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "requested_at")
    private LocalDateTime requestedAt;

    @Lob
    private String transcript;

    private Boolean status;

    @Column(name = "`user`", length = 100)
    private String user;

    private Integer version;

    @OneToMany(mappedBy = "request")
    private List<ResponseEntity> responses;
}

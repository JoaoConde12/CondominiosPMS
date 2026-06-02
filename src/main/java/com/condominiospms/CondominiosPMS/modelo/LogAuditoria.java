package com.condominiospms.CondominiosPMS.modelo;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "log_auditoria")
@Getter @Setter
public class LogAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(name = "timestamp_evento")
    private LocalDateTime timestampEvento;

    private String ip;

    private String accion;

    private String resultado;
}
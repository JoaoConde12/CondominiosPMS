package com.condominiospms.CondominiosPMS.modelo;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import java.util.Date;

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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp_evento")
    @ReadOnly
    private Date timestampEvento;

    @ReadOnly
    private String ip;

    @ReadOnly
    private String accion;

    @ReadOnly
    private String resultado;

    @PrePersist
    public void alCrear() {
        if (timestampEvento == null) {
            timestampEvento = new Date();
        }
    }
}
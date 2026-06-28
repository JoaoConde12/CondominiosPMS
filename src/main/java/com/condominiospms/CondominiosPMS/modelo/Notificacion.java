package com.condominiospms.CondominiosPMS.modelo;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import java.util.Date;

@Entity
@Table(name = "notificacion")
@Getter @Setter
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private Long id;

    @Column(name = "destinatario_correo", nullable = false)
    @Required
    private String destinatarioCorreo;

    @Required
    private String asunto;

    @Column(columnDefinition = "TEXT")
    @Stereotype("MEMO")
    private String cuerpo;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_envio")
    @ReadOnly
    private Date fechaEnvio;

    @ReadOnly
    private Boolean enviada = false;

    @ReadOnly
    private Integer intentos = 0;

    @PrePersist
    public void alCrear() {
        if (enviada == null) enviada = false;
        if (intentos == null) intentos = 0;
    }
}
package com.condominiospms.CondominiosPMS.modelo;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificacion")
@Getter @Setter
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private Long id;

    @Column(name = "destinatario_correo")
    private String destinatarioCorreo;

    private String asunto;

    @Column(columnDefinition = "TEXT")
    private String cuerpo;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    private Boolean enviada;

    private Integer intentos;
}
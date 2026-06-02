package com.condominiospms.CondominiosPMS.modelo;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bitacora_incidencia")
@Getter @Setter
public class BitacoraIncidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_incidencia", nullable = false)
    private Incidencia incidencia;

    @Column(columnDefinition = "TEXT")
    private String texto;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    private String autor;
}
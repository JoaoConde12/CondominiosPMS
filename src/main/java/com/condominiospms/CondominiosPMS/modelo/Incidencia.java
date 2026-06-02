package com.condominiospms.CondominiosPMS.modelo;

import com.condominiospms.CondominiosPMS.modelo.enums.EstadoIncidencia;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "incidencia")
@Getter @Setter
public class Incidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private Long id;

    @Column(unique = true, nullable = false)
    private String codigo;

    @ManyToOne
    @JoinColumn(name = "id_copropietario")
    private Copropietario copropietario;

    @ManyToOne
    @JoinColumn(name = "id_administrador")
    private Administrador administrador;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private String categoria;

    @Column(name = "area_afectada")
    private String areaAfectada;

    @Enumerated(EnumType.STRING)
    private EstadoIncidencia estado;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;

    @Column(name = "solucion_aplicada", columnDefinition = "TEXT")
    private String solucionAplicada;

    @Column(name = "motivo_cancelacion", columnDefinition = "TEXT")
    private String motivoCancelacion;

    @Column(name = "ruta_imagen")
    private String rutaImagen;

    @Column(name = "responsable_asignado")
    private String responsableAsignado;
}
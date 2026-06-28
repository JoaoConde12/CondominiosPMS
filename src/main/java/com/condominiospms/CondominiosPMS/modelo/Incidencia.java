package com.condominiospms.CondominiosPMS.modelo;

import com.condominiospms.CondominiosPMS.modelo.enums.EstadoIncidencia;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import java.util.Date;

@Entity
@Table(name = "incidencia")
@Getter @Setter
public class Incidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private Long id;

    @Column(unique = true, nullable = false)
    @ReadOnly
    private String codigo;

    @ManyToOne
    @JoinColumn(name = "id_copropietario")
    @Required
    private Copropietario copropietario;

    @ManyToOne
    @JoinColumn(name = "id_administrador")
    private Administrador administrador;

    @Column(nullable = false)
    @Required
    private String titulo;

    @Column(columnDefinition = "TEXT")
    @Stereotype("MEMO")
    private String descripcion;

    @Required
    private String categoria;

    @Column(name = "area_afectada")
    private String areaAfectada;

    @Enumerated(EnumType.STRING)
    @ReadOnly
    private EstadoIncidencia estado = EstadoIncidencia.PENDIENTE;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion")
    @ReadOnly
    private Date fechaCreacion;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_resolucion")
    private Date fechaResolucion;

    @Column(name = "solucion_aplicada", columnDefinition = "TEXT")
    @Stereotype("MEMO")
    private String solucionAplicada;

    @Column(name = "motivo_cancelacion", columnDefinition = "TEXT")
    @Stereotype("MEMO")
    private String motivoCancelacion;

    @Column(name = "ruta_imagen")
    private String rutaImagen;

    @Column(name = "responsable_asignado")
    private String responsableAsignado;

    @PrePersist
    public void alCrear() {
        fechaCreacion = new Date();
        estado = EstadoIncidencia.PENDIENTE;
        codigo = generarCodigo();
    }

    private String generarCodigo() {
        int anio = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        long timestamp = System.currentTimeMillis() % 1000;
        return String.format("INC-%d-%03d", anio, timestamp);
    }

    public void pasarAEnProceso(String responsable) {
        if (estado != EstadoIncidencia.PENDIENTE) {
            throw new RuntimeException("Solo se puede procesar una incidencia PENDIENTE.");
        }
        this.estado = EstadoIncidencia.EN_PROCESO;
        this.responsableAsignado = responsable;
    }

    public void resolver(String solucion) {
        if (estado != EstadoIncidencia.EN_PROCESO) {
            throw new RuntimeException("Solo se puede resolver una incidencia EN_PROCESO.");
        }
        this.estado = EstadoIncidencia.RESUELTA;
        this.solucionAplicada = solucion;
        this.fechaResolucion = new Date();
    }

    public void cancelar(String motivo) {
        if (estado == EstadoIncidencia.RESUELTA) {
            throw new RuntimeException("No se puede cancelar una incidencia ya resuelta.");
        }
        this.estado = EstadoIncidencia.CANCELADA;
        this.motivoCancelacion = motivo;
    }
}
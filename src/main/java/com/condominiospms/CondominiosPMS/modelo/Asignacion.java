package com.condominiospms.CondominiosPMS.modelo;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import java.time.LocalDate;

@Entity
@Table(name = "asignacion")
@Getter @Setter
public class Asignacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_copropietario", nullable = false)
    @Required
    private Copropietario copropietario;

    @ManyToOne
    @JoinColumn(name = "id_unidad", nullable = false)
    @Required
    private UnidadHabitacional unidad;

    @Column(name = "fecha_inicio")
    private java.util.Date fechaInicio;

    @Column(name = "fecha_fin")
    private java.util.Date fechaFin;

    private Boolean activa = true;

    @PrePersist
    public void alCrear() {
        if (fechaInicio == null) {
            fechaInicio = new java.util.Date();
        }
        if (unidad != null) {
            unidad.setEstado(com.condominiospms.CondominiosPMS.modelo.enums.EstadoUnidad.OCUPADA);
        }
    }

    @PreRemove
    public void alEliminar() {
        if (unidad != null) {
            unidad.setEstado(com.condominiospms.CondominiosPMS.modelo.enums.EstadoUnidad.DISPONIBLE);
        }
    }
}
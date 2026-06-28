package com.condominiospms.CondominiosPMS.modelo;

import com.condominiospms.CondominiosPMS.modelo.enums.EstadoUnidad;
import com.condominiospms.CondominiosPMS.modelo.enums.TipoUnidad;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;

@Entity
@Table(name = "unidad_habitacional")
@Getter @Setter
public class UnidadHabitacional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private Long id;

    @Column(unique = true, nullable = false)
    @Required
    private String codigo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    @Required
    private TipoUnidad tipo;

    private Integer piso;

    @Column(name = "area")
    private Double area;

    @Enumerated(EnumType.STRING)
    private EstadoUnidad estado = EstadoUnidad.DISPONIBLE;

    private String descripcion;

    @Override
    public String toString() {
        return codigo + " - " + (tipo != null ? tipo.name() : "") +
                " (Piso " + piso + ")";
    }
}
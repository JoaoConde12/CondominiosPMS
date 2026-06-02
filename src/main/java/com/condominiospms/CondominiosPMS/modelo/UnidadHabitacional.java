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
    private String codigo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoUnidad tipo;

    private Integer piso;

    @Column(name = "area")
    private Double area;

    @Enumerated(EnumType.STRING)
    private EstadoUnidad estado;

    private String descripcion;
}
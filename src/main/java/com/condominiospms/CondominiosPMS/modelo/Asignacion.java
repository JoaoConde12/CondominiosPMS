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
    private Copropietario copropietario;

    @ManyToOne
    @JoinColumn(name = "id_unidad", nullable = false)
    private UnidadHabitacional unidad;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    private Boolean activa;
}
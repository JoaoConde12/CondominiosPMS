package com.condominiospms.CondominiosPMS.modelo;

import com.condominiospms.CondominiosPMS.modelo.enums.EstadoAlicuota;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "alicuota",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"id_unidad", "periodo_mes", "periodo_anio"}
        )
)
@Getter @Setter
public class Alicuota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_unidad", nullable = false)
    @Required
    private UnidadHabitacional unidad;

    @Column(name = "periodo_mes", nullable = false)
    @Required
    private Integer periodoMes;

    @Column(name = "periodo_anio", nullable = false)
    @Required
    private Integer periodoAnio;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_vencimiento")
    private Date fechaVencimiento;

    @Column(name = "monto_total", precision = 10, scale = 2)
    private BigDecimal montoTotal;

    @Column(name = "monto_pagado", precision = 10, scale = 2)
    private BigDecimal montoPagado = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private EstadoAlicuota estado = EstadoAlicuota.PENDIENTE;

    @PrePersist
    public void alCrear() {
        if (montoPagado == null) montoPagado = BigDecimal.ZERO;
        if (estado == null) estado = EstadoAlicuota.PENDIENTE;
    }

    public void calcularEstado() {
        if (montoPagado == null) montoPagado = BigDecimal.ZERO;
        if (montoTotal == null) return;

        if (montoPagado.compareTo(BigDecimal.ZERO) == 0) {
            Date hoy = new Date();
            if (fechaVencimiento != null && hoy.after(fechaVencimiento)) {
                estado = EstadoAlicuota.MOROSA;
            } else {
                estado = EstadoAlicuota.PENDIENTE;
            }
        } else if (montoPagado.compareTo(montoTotal) >= 0) {
            estado = EstadoAlicuota.PAGADA;
        } else {
            estado = EstadoAlicuota.PAGO_PARCIAL;
        }
    }
}
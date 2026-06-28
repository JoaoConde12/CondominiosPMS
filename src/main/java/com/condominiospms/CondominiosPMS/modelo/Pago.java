package com.condominiospms.CondominiosPMS.modelo;

import com.condominiospms.CondominiosPMS.modelo.enums.EstadoPago;
import com.condominiospms.CondominiosPMS.modelo.enums.MetodoPago;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "pago")
@Getter @Setter
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_alicuota", nullable = false)
    @Required
    private Alicuota alicuota;

    @ManyToOne
    @JoinColumn(name = "id_administrador")
    private Administrador administrador;

    @Column(nullable = false, precision = 10, scale = 2)
    @Required
    private BigDecimal monto;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_pago")
    private Date fechaPago;

    @Enumerated(EnumType.STRING)
    @Required
    private MetodoPago metodo;

    @Column(name = "referencia_bancaria")
    private String referenciaBancaria;

    @Enumerated(EnumType.STRING)
    private EstadoPago estado = EstadoPago.ACTIVO;

    @Column(name = "motivo_anulacion")
    private String motivoAnulacion;

    @PrePersist
    public void alCrear() {
        if (fechaPago == null) fechaPago = new Date();
        if (estado == null) estado = EstadoPago.ACTIVO;

        // Actualizar monto pagado en la alícuota
        if (alicuota != null && monto != null) {
            BigDecimal montoPagadoActual = alicuota.getMontoPagado();
            if (montoPagadoActual == null) montoPagadoActual = BigDecimal.ZERO;
            alicuota.setMontoPagado(montoPagadoActual.add(monto));
            alicuota.calcularEstado();
        }
    }

    @PreUpdate
    public void alActualizar() {
        // Si se anula el pago, revertir el monto en la alícuota
        if (estado == EstadoPago.ANULADO && alicuota != null && monto != null) {
            BigDecimal montoPagadoActual = alicuota.getMontoPagado();
            if (montoPagadoActual == null) montoPagadoActual = BigDecimal.ZERO;
            alicuota.setMontoPagado(montoPagadoActual.subtract(monto));
            alicuota.calcularEstado();
        }
    }
}
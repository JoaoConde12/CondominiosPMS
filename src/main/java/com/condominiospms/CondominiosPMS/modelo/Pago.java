package com.condominiospms.CondominiosPMS.modelo;

import com.condominiospms.CondominiosPMS.modelo.enums.EstadoPago;
import com.condominiospms.CondominiosPMS.modelo.enums.MetodoPago;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import java.math.BigDecimal;
import java.time.LocalDate;

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
    private Alicuota alicuota;

    @ManyToOne
    @JoinColumn(name = "id_administrador")
    private Administrador administrador;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "fecha_pago")
    private LocalDate fechaPago;

    @Enumerated(EnumType.STRING)
    private MetodoPago metodo;

    @Column(name = "referencia_bancaria")
    private String referenciaBancaria;

    @Enumerated(EnumType.STRING)
    private EstadoPago estado;

    @Column(name = "motivo_anulacion")
    private String motivoAnulacion;
}
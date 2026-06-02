package com.condominiospms.CondominiosPMS.modelo;

import com.condominiospms.CondominiosPMS.modelo.enums.TipoUnidad;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tarifa_alicuota")
@Getter @Setter
public class TarifaAlicuota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_unidad", nullable = false)
    private TipoUnidad tipoUnidad;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "fecha_vigencia")
    private LocalDate fechaVigencia;

    private Boolean activa;
}
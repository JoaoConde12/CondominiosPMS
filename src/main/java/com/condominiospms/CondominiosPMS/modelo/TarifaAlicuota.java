package com.condominiospms.CondominiosPMS.modelo;

import com.condominiospms.CondominiosPMS.modelo.enums.TipoUnidad;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import java.math.BigDecimal;
import java.util.Date;

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
    @Required
    private TipoUnidad tipoUnidad;

    @Column(nullable = false, precision = 10, scale = 2)
    @Required
    private BigDecimal monto;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_vigencia")
    private Date fechaVigencia;

    private Boolean activa = true;

    @PrePersist
    public void alCrear() {
        if (activa == null) activa = true;
        if (fechaVigencia == null) fechaVigencia = new Date();
    }
}
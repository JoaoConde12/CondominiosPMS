package com.condominiospms.CondominiosPMS.modelo;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import java.util.Date;

@Entity
@Table(name = "comprobante")
@Getter @Setter
public class Comprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_pago", nullable = false)
    @Required
    private Pago pago;

    @Column(unique = true, nullable = false)
    @ReadOnly
    private String codigo;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_generacion")
    @ReadOnly
    private Date fechaGeneracion;

    @Column(name = "ruta_pdf")
    @ReadOnly
    private String rutaPdf;

    @PrePersist
    public void antesDeGuardar() {
        this.fechaGeneracion = new Date();
        String anio = new java.text.SimpleDateFormat("yyyy").format(this.fechaGeneracion);
        Long idPago = (this.pago != null && this.pago.getId() != null) ? this.pago.getId() : 0L;
        this.codigo = "COMP-" + anio + "-" + String.format("%05d", idPago);
    }
}
package com.condominiospms.CondominiosPMS.modelo;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import java.time.LocalDateTime;

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
    private Pago pago;

    @Column(unique = true, nullable = false)
    private String codigo;

    @Column(name = "fecha_generacion")
    private LocalDateTime fechaGeneracion;

    @Column(name = "ruta_pdf")
    private String rutaPdf;
}
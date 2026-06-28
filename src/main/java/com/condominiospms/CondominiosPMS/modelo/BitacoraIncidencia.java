package com.condominiospms.CondominiosPMS.modelo;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import java.util.Date;

@Entity
@Table(name = "bitacora_incidencia")
@Getter @Setter
public class BitacoraIncidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_incidencia", nullable = false)
    @Required
    private Incidencia incidencia;

    @Column(columnDefinition = "TEXT")
    @Required
    @Stereotype("MEMO")
    private String texto;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_hora")
    @ReadOnly
    private Date fechaHora;

    @ReadOnly
    private String autor;

    @PrePersist
    public void alCrear() {
        fechaHora = new Date();
        if (autor == null || autor.isEmpty()) {
            autor = "Sistema";
        }
    }
}
package com.condominiospms.CondominiosPMS.modelo;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "copropietario")
@Getter @Setter
@PrimaryKeyJoinColumn(name = "id_usuario")
public class Copropietario extends Usuario {

    @Column(unique = true, nullable = false)
    private String cedula;
}
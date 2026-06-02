package com.condominiospms.CondominiosPMS.modelo;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "administrador")
@Getter @Setter
@PrimaryKeyJoinColumn(name = "id_usuario")
public class Administrador extends Usuario {

    private String cargo;
}
package com.condominiospms.CondominiosPMS.modelo;

import com.condominiospms.CondominiosPMS.modelo.enums.EstadoUsuario;
import com.condominiospms.CondominiosPMS.modelo.enums.Rol;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import java.util.Date;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private Long id;

    @Column(unique = true, nullable = false)
    @Required
    @javax.validation.constraints.Email
    private String correo;

    @Column(name = "contrasena_hash", nullable = false)
    @Hidden
    private String contrasenaHash = "";

    @Transient
    @Password
    private String contrasena;

    @Column(name = "nombre_completo", nullable = false)
    private String nombreCompleto;

    private String telefono;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @Enumerated(EnumType.STRING)
    private EstadoUsuario estado;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_registro")
    private Date fechaRegistro;

    @Column(name = "intentos_fallidos")
    private Integer intentosFallidos = 0;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_bloqueo")
    private Date fechaBloqueo;

    @PrePersist
    @PreUpdate
    public void hashearContrasena() {
        if (contrasena != null && !contrasena.isEmpty()) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hash = md.digest(contrasena.getBytes(StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                for (byte b : hash) {
                    sb.append(String.format("%02x", b));
                }
                this.contrasenaHash = sb.toString();
            } catch (NoSuchAlgorithmException e) {
                this.contrasenaHash = contrasena;
            }
        }
    }
}
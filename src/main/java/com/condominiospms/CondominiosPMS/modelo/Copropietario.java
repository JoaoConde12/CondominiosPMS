package com.condominiospms.CondominiosPMS.modelo;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;

@Entity
@Table(name = "copropietario")
@Getter @Setter
@PrimaryKeyJoinColumn(name = "id_usuario")
public class Copropietario extends Usuario {

    @Column(unique = true, nullable = false, length = 13)
    @Required
    private String cedula;

    @PrePersist
    @PreUpdate
    public void validarCedula() {
        if (cedula == null || cedula.trim().isEmpty()) return;
        cedula = cedula.trim();

        if (!cedula.matches("\\d{10}")) {
            throw new RuntimeException("La cédula debe tener exactamente 10 dígitos numéricos.");
        }

        int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};
        int suma = 0;
        for (int i = 0; i < 9; i++) {
            int digito = Character.getNumericValue(cedula.charAt(i));
            int resultado = digito * coeficientes[i];
            if (resultado >= 10) resultado -= 9;
            suma += resultado;
        }
        int digitoVerificador = (10 - (suma % 10)) % 10;

        if (digitoVerificador != Character.getNumericValue(cedula.charAt(9))) {
            throw new RuntimeException("La cédula ingresada no es válida.");
        }
    }
}
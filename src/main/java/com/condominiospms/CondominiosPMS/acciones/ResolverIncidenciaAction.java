package com.condominiospms.CondominiosPMS.acciones;

import com.condominiospms.CondominiosPMS.modelo.Incidencia;
import com.condominiospms.CondominiosPMS.servicios.ServicioNotificaciones;
import org.openxava.actions.ViewBaseAction;
import org.openxava.jpa.XPersistence;

public class ResolverIncidenciaAction extends ViewBaseAction {

    @Override
    public void execute() throws Exception {
        Incidencia incidencia = (Incidencia) getView().getEntity();
        String solucion = (String) getView().getValue("solucionAplicada");

        if (solucion == null || solucion.trim().isEmpty()) {
            addError("Debe ingresar la solución aplicada.");
            return;
        }

        incidencia.resolver(solucion);
        XPersistence.getManager().merge(incidencia);
        addMessage("Incidencia resuelta correctamente.");

        // Notificar al copropietario
        if (incidencia.getCopropietario() != null &&
                incidencia.getCopropietario().getCorreo() != null) {
            ServicioNotificaciones.enviarCambioEstadoIncidencia(
                    incidencia.getCopropietario().getCorreo(),
                    incidencia.getCodigo(),
                    "RESUELTA"
            );
        }
    }
}
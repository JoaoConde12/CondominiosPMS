package com.condominiospms.CondominiosPMS.acciones;

import com.condominiospms.CondominiosPMS.modelo.Incidencia;
import com.condominiospms.CondominiosPMS.servicios.ServicioNotificaciones;
import org.openxava.actions.ViewBaseAction;
import org.openxava.jpa.XPersistence;

public class PasarAEnProcesoAction extends ViewBaseAction {

    @Override
    public void execute() throws Exception {
        Incidencia incidencia = (Incidencia) getView().getEntity();
        String responsable = (String) getView().getValue("responsableAsignado");

        if (responsable == null || responsable.trim().isEmpty()) {
            addError("Debe ingresar el responsable asignado.");
            return;
        }

        incidencia.pasarAEnProceso(responsable);
        XPersistence.getManager().merge(incidencia);
        addMessage("Incidencia en proceso. Responsable: " + responsable);

        // Notificar al copropietario
        if (incidencia.getCopropietario() != null &&
                incidencia.getCopropietario().getCorreo() != null) {
            ServicioNotificaciones.enviarCambioEstadoIncidencia(
                    incidencia.getCopropietario().getCorreo(),
                    incidencia.getCodigo(),
                    "EN PROCESO"
            );
        }
    }
}
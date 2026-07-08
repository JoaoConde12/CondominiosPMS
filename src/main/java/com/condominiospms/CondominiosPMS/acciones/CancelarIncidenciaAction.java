package com.condominiospms.CondominiosPMS.acciones;

import com.condominiospms.CondominiosPMS.modelo.Incidencia;
import com.condominiospms.CondominiosPMS.servicios.ServicioNotificaciones;
import org.openxava.actions.ViewBaseAction;
import org.openxava.jpa.XPersistence;

public class CancelarIncidenciaAction extends ViewBaseAction {

    @Override
    public void execute() throws Exception {
        Incidencia incidencia = (Incidencia) getView().getEntity();
        String motivo = (String) getView().getValue("motivoCancelacion");

        if (motivo == null || motivo.trim().isEmpty()) {
            addError("Debe ingresar el motivo de cancelación.");
            return;
        }

        incidencia.cancelar(motivo);
        XPersistence.getManager().merge(incidencia);
        addMessage("Incidencia cancelada.");

        // Notificar al copropietario
        if (incidencia.getCopropietario() != null &&
                incidencia.getCopropietario().getCorreo() != null) {
            ServicioNotificaciones.enviarCambioEstadoIncidencia(
                    incidencia.getCopropietario().getCorreo(),
                    incidencia.getCodigo(),
                    "CANCELADA"
            );
        }
    }
}
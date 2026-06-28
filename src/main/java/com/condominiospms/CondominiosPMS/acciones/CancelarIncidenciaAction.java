package com.condominiospms.CondominiosPMS.acciones;

import com.condominiospms.CondominiosPMS.modelo.Incidencia;
import org.openxava.actions.ViewBaseAction;

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
        javax.persistence.EntityManager em =
                org.openxava.jpa.XPersistence.getManager();
        em.merge(incidencia);
        addMessage("Incidencia cancelada.");
    }
}
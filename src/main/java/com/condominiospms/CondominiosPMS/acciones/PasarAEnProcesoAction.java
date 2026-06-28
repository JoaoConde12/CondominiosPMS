package com.condominiospms.CondominiosPMS.acciones;

import com.condominiospms.CondominiosPMS.modelo.Incidencia;
import org.openxava.actions.ViewBaseAction;

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
        javax.persistence.EntityManager em =
                org.openxava.jpa.XPersistence.getManager();
        em.merge(incidencia);
        addMessage("Incidencia en proceso. Responsable: " + responsable);
    }
}
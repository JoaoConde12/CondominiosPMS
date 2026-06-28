package com.condominiospms.CondominiosPMS.acciones;

import com.condominiospms.CondominiosPMS.modelo.Incidencia;
import org.openxava.actions.ViewBaseAction;

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
        javax.persistence.EntityManager em =
                org.openxava.jpa.XPersistence.getManager();
        em.merge(incidencia);
        addMessage("Incidencia resuelta correctamente.");
    }
}
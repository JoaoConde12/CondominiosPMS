package com.condominiospms.CondominiosPMS.acciones;

import com.condominiospms.CondominiosPMS.modelo.Alicuota;
import com.condominiospms.CondominiosPMS.modelo.enums.EstadoAlicuota;
import com.condominiospms.CondominiosPMS.servicios.GeneradorPDFService;
import org.openxava.actions.ViewBaseAction;
import org.openxava.jpa.XPersistence;
import java.util.List;

public class GenerarReporteMorosidadAction extends ViewBaseAction {

    @Override
    public void execute() throws Exception {
        try {
            List<Alicuota> morosas = XPersistence.getManager()
                    .createQuery(
                            "SELECT a FROM Alicuota a WHERE a.estado = :estado",
                            Alicuota.class)
                    .setParameter("estado", EstadoAlicuota.MOROSA)
                    .getResultList();

            if (morosas.isEmpty()) {
                addMessage("No hay alícuotas morosas en el sistema.");
                return;
            }

            String ruta = GeneradorPDFService.generarReporteMorosidad(morosas);
            addMessage("Reporte generado con " + morosas.size() +
                    " registros: " + ruta);

        } catch (Exception e) {
            addError("Error al generar reporte: " + e.getMessage());
        }
    }
}
package com.condominiospms.CondominiosPMS.acciones;

import com.condominiospms.CondominiosPMS.modelo.Comprobante;
import com.condominiospms.CondominiosPMS.modelo.Pago;
import com.condominiospms.CondominiosPMS.servicios.GeneradorPDFService;
import org.openxava.actions.ViewBaseAction;
import org.openxava.jpa.XPersistence;
import java.util.Date;

public class GenerarComprobanteAction extends ViewBaseAction {

    @Override
    public void execute() throws Exception {
        Pago pago = (Pago) getView().getEntity();
        if (pago == null || pago.getId() == null) {
            addError("Debe guardar el pago antes de generar el comprobante.");
            return;
        }

        try {
            String rutaPdf = GeneradorPDFService.generarComprobante(pago);

            // Crear registro de comprobante
            Comprobante comprobante = new Comprobante();
            comprobante.setPago(pago);
            comprobante.setCodigo("COMP-" + pago.getId());
            comprobante.setFechaGeneracion(new Date());
            comprobante.setRutaPdf(rutaPdf);

            XPersistence.getManager().persist(comprobante);
            addMessage("Comprobante generado: " + rutaPdf);

        } catch (Exception e) {
            addError("Error al generar comprobante: " + e.getMessage());
        }
    }
}
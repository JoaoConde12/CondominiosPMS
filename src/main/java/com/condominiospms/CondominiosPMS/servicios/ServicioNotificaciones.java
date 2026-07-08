package com.condominiospms.CondominiosPMS.servicios;

import com.condominiospms.CondominiosPMS.modelo.Notificacion;
import org.openxava.jpa.XPersistence;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Date;
import java.util.Properties;

public class ServicioNotificaciones {

    // Configura estos valores con tu servidor SMTP
    private static final String SMTP_HOST = "smtp.resend.com";
    private static final String SMTP_PORT = "587";
    private static final String SMTP_USER = "resend";
    private static final String SMTP_PASSWORD = "";
    private static final String SMTP_FROM = "Condominios PMS <onboarding@resend.dev>";

    public static void enviar(String destinatario, String asunto, String cuerpo) {
        // Registrar notificación en BD primero
        Notificacion notificacion = new Notificacion();
        notificacion.setDestinatarioCorreo(destinatario);
        notificacion.setAsunto(asunto);
        notificacion.setCuerpo(cuerpo);

        try {
            XPersistence.getManager().persist(notificacion);
        } catch (Exception e) {
            System.err.println("Error guardando notificación: " + e.getMessage());
        }

        // Intentar envío
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SMTP_USER, SMTP_PASSWORD);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SMTP_FROM));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(destinatario));
            message.setSubject(asunto);
            message.setContent(cuerpo, "text/html; charset=utf-8");

            Transport.send(message);

            // Marcar como enviada
            notificacion.setEnviada(true);
            notificacion.setFechaEnvio(new Date());
            notificacion.setIntentos(1);

        } catch (Exception e) {
            // Encolar para reintento ? no interrumpe el flujo principal
            notificacion.setEnviada(false);
            notificacion.setIntentos(
                    notificacion.getIntentos() == null ? 1 :
                            notificacion.getIntentos() + 1);
            System.err.println("SMTP no disponible, notificación encolada: "
                    + e.getMessage());
        }
    }

    public static void enviarBienvenida(String destinatario, String nombre,
                                        String contrasenaInicial) {
        String asunto = "Bienvenido a Condominios PMS";
        String cuerpo = "<h2>Bienvenido, " + nombre + "</h2>" +
                "<p>Su cuenta ha sido creada exitosamente.</p>" +
                "<p><b>Correo:</b> " + destinatario + "</p>" +
                "<p><b>Contraseña inicial:</b> " + contrasenaInicial + "</p>" +
                "<p>Por favor cambie su contraseña al primer inicio de sesión.</p>" +
                "<br><p>Condominios PMS</p>";
        enviar(destinatario, asunto, cuerpo);
    }

    public static void enviarCambioEstadoIncidencia(String destinatario,
                                                    String codigoIncidencia, String nuevoEstado) {
        String asunto = "Actualización de Incidencia " + codigoIncidencia;
        String cuerpo = "<h2>Actualización de Incidencia</h2>" +
                "<p>La incidencia <b>" + codigoIncidencia +
                "</b> ha cambiado al estado: <b>" + nuevoEstado + "</b></p>" +
                "<br><p>Condominios PMS</p>";
        enviar(destinatario, asunto, cuerpo);
    }

    public static void enviarBloqueo(String destinatario, String nombre) {
        String asunto = "Cuenta bloqueada - Condominios PMS";
        String cuerpo = "<h2>Cuenta Bloqueada</h2>" +
                "<p>Estimado/a " + nombre + ",</p>" +
                "<p>Su cuenta ha sido bloqueada por 30 minutos debido a " +
                "5 intentos fallidos de inicio de sesión.</p>" +
                "<p>Si no fue usted, contacte al administrador.</p>" +
                "<br><p>Condominios PMS</p>";
        enviar(destinatario, asunto, cuerpo);
    }
}
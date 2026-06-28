package com.condominiospms.CondominiosPMS.acciones;

import com.condominiospms.CondominiosPMS.modelo.LogAuditoria;
import com.condominiospms.CondominiosPMS.modelo.Usuario;
import com.condominiospms.CondominiosPMS.modelo.enums.EstadoUsuario;
import org.openxava.actions.ViewBaseAction;
import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Date;
import java.util.List;

public class AutenticadorAction extends ViewBaseAction {

    private static final int MAX_INTENTOS = 5;
    private static final long TIEMPO_BLOQUEO_MS = 30 * 60 * 1000L; // 30 minutos

    @Override
    public void execute() throws Exception {
        String correo = (String) getView().getValue("correo");
        String contrasena = (String) getView().getValue("contrasena");

        if (correo == null || correo.trim().isEmpty() ||
                contrasena == null || contrasena.trim().isEmpty()) {
            addError("Correo y contraseña son obligatorios.");
            return;
        }

        EntityManagerFactory emf = javax.persistence.Persistence
                .createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();

        try {
            // Buscar usuario por correo
            List<Usuario> usuarios = em.createQuery(
                            "SELECT u FROM Usuario u WHERE u.correo = :correo", Usuario.class)
                    .setParameter("correo", correo.trim())
                    .getResultList();

            if (usuarios.isEmpty()) {
                addError("Usuario o contraseña incorrectos.");
                registrarLog(em, null, "LOGIN_FALLIDO", obtenerIp());
                return;
            }

            Usuario usuario = usuarios.get(0);

            // Verificar si está bloqueado
            if (usuario.getEstado() == EstadoUsuario.BLOQUEADO) {
                Date ahora = new Date();
                Date fechaBloqueo = usuario.getFechaBloqueo();
                if (fechaBloqueo != null &&
                        (ahora.getTime() - fechaBloqueo.getTime()) < TIEMPO_BLOQUEO_MS) {
                    addError("Cuenta bloqueada por 30 minutos. Intente más tarde.");
                    return;
                } else {
                    // Desbloquear si ya pasaron 30 min
                    em.getTransaction().begin();
                    usuario.setEstado(EstadoUsuario.ACTIVO);
                    usuario.setIntentosFallidos(0);
                    usuario.setFechaBloqueo(null);
                    em.merge(usuario);
                    em.getTransaction().commit();
                }
            }

            // Verificar si está inactivo
            if (usuario.getEstado() == EstadoUsuario.INACTIVO) {
                addError("Su cuenta está desactivada. Contacte al administrador.");
                return;
            }

            // Verificar contraseña
            String hashIngresado = hashSHA256(contrasena);
            if (!hashIngresado.equals(usuario.getContrasenaHash())) {
                em.getTransaction().begin();
                int intentos = (usuario.getIntentosFallidos() == null ? 0
                        : usuario.getIntentosFallidos()) + 1;
                usuario.setIntentosFallidos(intentos);

                if (intentos >= MAX_INTENTOS) {
                    usuario.setEstado(EstadoUsuario.BLOQUEADO);
                    usuario.setFechaBloqueo(new Date());
                    addError("Cuenta bloqueada por 5 intentos fallidos.");
                } else {
                    addError("Usuario o contraseña incorrectos. Intento " +
                            intentos + " de " + MAX_INTENTOS + ".");
                }
                em.merge(usuario);
                em.getTransaction().commit();
                registrarLog(em, usuario, "LOGIN_FALLIDO", obtenerIp());
                return;
            }

            // Login exitoso
            em.getTransaction().begin();
            usuario.setIntentosFallidos(0);
            em.merge(usuario);
            em.getTransaction().commit();

            registrarLog(em, usuario, "LOGIN_EXITOSO", obtenerIp());

            // Guardar en sesión
            getRequest().getSession().setAttribute("usuarioId", usuario.getId());
            getRequest().getSession().setAttribute("usuarioRol",
                    usuario.getRol().name());
            getRequest().getSession().setAttribute("usuarioNombre",
                    usuario.getNombreCompleto());

            addMessage("Bienvenido, " + usuario.getNombreCompleto());

        } finally {
            em.close();
            emf.close();
        }
    }

    private void registrarLog(EntityManager em, Usuario usuario,
                              String accion, String ip) {
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
            LogAuditoria log = new LogAuditoria();
            log.setUsuario(usuario);
            log.setAccion(accion);
            log.setIp(ip);
            log.setResultado(accion);
            log.setTimestampEvento(new Date());
            em.persist(log);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }

    private String obtenerIp() {
        try {
            return getRequest().getRemoteAddr();
        } catch (Exception e) {
            return "desconocida";
        }
    }

    private String hashSHA256(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
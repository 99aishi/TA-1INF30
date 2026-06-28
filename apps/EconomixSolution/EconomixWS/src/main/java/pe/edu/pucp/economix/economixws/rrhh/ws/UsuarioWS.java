package pe.edu.pucp.economix.economixws.rrhh.ws;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.economix.rrhh.boi.UsuarioBOImpl;
import pe.edu.pucp.economix.rrhh.ibo.IUsuarioBO;
import pe.edu.pucp.economix.rrhh.model.Usuario;

@Path("UsuarioWS")
public class UsuarioWS {
    private final IUsuarioBO usuarioBO = new UsuarioBOImpl();

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest request) {
        try {
            Usuario usuario = usuarioBO.validarUsuario(request.getCorreo(), request.getPassword());
            return Response.ok(usuario).build();
        } catch (Exception e) {
            String mensaje = e.getMessage();
            if (mensaje != null && (mensaje.toLowerCase().contains("bloqueada") || mensaje.toLowerCase().contains("inactivo"))) {
                return Response.status(Response.Status.FORBIDDEN)
                    .entity(new ErrorResponse(mensaje))
                    .build();
            }
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity(new ErrorResponse(mensaje != null ? mensaje : "Usuario o contraseña incorrectos."))
                .build();
        }
    }

    //Clase auxiliar para el login
    public static class LoginRequest {
        private String correo;
        private String password;

        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    //Clase auxiliar para respuestas de error
    public static class ErrorResponse {
        private String mensaje;

        public ErrorResponse() {}

        public ErrorResponse(String mensaje) {
            this.mensaje = mensaje;
        }

        public String getMensaje() { return mensaje; }
        public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    }
}

package pe.edu.pucp.economix.economixws.rrhh.ws;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
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
    public Usuario login(LoginRequest request) {
        try {
            Usuario usuario = usuarioBO.validarUsuario(request.getCorreo(), request.getPassword());
            return usuario;
        } catch (Exception e) {
            return null;
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
}
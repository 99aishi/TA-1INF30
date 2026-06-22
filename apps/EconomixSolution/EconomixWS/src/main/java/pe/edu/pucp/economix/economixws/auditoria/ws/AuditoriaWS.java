package pe.edu.pucp.economix.economixws.auditoria.ws;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.economix.auditoria.boi.AuditoriaBOImpl;
import pe.edu.pucp.economix.auditoria.ibo.IAuditoriaBO;
import pe.edu.pucp.economix.auditoria.model.AuditoriaLogDto;

import java.util.List;
import java.util.Map;

@Path("AuditoriaWS")
public class AuditoriaWS {
    private IAuditoriaBO auditoriaBO = new AuditoriaBOImpl();

    @GET
    @Path("ListarRecientes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarRecientes(@QueryParam("limite") @DefaultValue("50") int limite) {
        try {
            List<AuditoriaLogDto> logs = auditoriaBO.listarRecientes(limite);
            return Response.ok(logs).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }
}

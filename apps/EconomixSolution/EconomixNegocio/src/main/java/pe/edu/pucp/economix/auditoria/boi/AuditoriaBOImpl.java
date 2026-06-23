package pe.edu.pucp.economix.auditoria.boi;

import java.util.List;

import pe.edu.pucp.economix.auditoria.ibo.IAuditoriaBO;
import pe.edu.pucp.economix.auditoria.idao.IAuditoriaDAO;
import pe.edu.pucp.economix.auditoria.daoi.AuditoriaDAOImpl;
import pe.edu.pucp.economix.auditoria.model.AuditoriaLogDto;

public class AuditoriaBOImpl implements IAuditoriaBO {
    private final IAuditoriaDAO auditoriaDAO;

    public AuditoriaBOImpl() {
        this.auditoriaDAO = new AuditoriaDAOImpl();
    }

    @Override
    public List<AuditoriaLogDto> listarRecientes(int limite) throws Exception {
        if (limite <= 0) {
            limite = 50;
        }
        return auditoriaDAO.listarRecientes(limite);
    }

    @Override
    public void registrarLogin(String correo, Integer idUsuario, boolean exitoso,
                               int intentosFallidos, boolean bloqueado) throws Exception {
        auditoriaDAO.registrarLogin(correo, idUsuario, exitoso, intentosFallidos, bloqueado);
    }
}

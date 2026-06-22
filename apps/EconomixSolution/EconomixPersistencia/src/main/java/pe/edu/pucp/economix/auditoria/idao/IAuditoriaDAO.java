package pe.edu.pucp.economix.auditoria.idao;

import java.sql.SQLException;
import java.util.List;

import pe.edu.pucp.economix.auditoria.model.AuditoriaLogDto;

public interface IAuditoriaDAO {
    List<AuditoriaLogDto> listarRecientes(int limite) throws SQLException;
}

package pe.edu.pucp.economix.auditoria.ibo;

import java.util.List;

import pe.edu.pucp.economix.auditoria.model.AuditoriaLogDto;

public interface IAuditoriaBO {
    List<AuditoriaLogDto> listarRecientes(int limite) throws Exception;
}

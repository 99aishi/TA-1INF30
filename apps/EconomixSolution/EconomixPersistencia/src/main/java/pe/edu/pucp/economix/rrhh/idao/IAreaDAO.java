package pe.edu.pucp.economix.rrhh.idao;

import java.sql.SQLException;
import java.util.List;

import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.rrhh.model.Empleado;

public interface IAreaDAO {
    int insertar(Area area, int idUsuarioAccion) throws SQLException;
    int modificar(Area area, int idUsuarioAccion) throws SQLException;
    int asignarJefe(Area area, Empleado empleado, int idUsuarioAccion) throws SQLException;
    int eliminar(int id, int idUsuarioAccion) throws SQLException;
    Area buscarPorId(int id) throws SQLException;
    List<Area> listarTodas() throws SQLException;
    List<Area> listarActivas() throws SQLException;
    int recuperar(int id, int idUsuarioAccion) throws SQLException;
}

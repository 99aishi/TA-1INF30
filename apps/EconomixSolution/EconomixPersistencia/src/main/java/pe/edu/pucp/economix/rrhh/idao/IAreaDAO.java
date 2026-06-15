package pe.edu.pucp.economix.rrhh.idao;

import java.sql.SQLException;
import java.util.List;

import pe.edu.pucp.economix.idao.IDAO;
import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.rrhh.model.Empleado;

public interface IAreaDAO extends IDAO<Area> {
    int asignarJefe(Area area, Empleado empleado) throws SQLException;
    List<Area> listarActivas() throws SQLException;
    int recuperar(int id) throws SQLException;
}

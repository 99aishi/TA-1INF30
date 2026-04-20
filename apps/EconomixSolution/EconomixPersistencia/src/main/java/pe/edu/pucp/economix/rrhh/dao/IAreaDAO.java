package pe.edu.pucp.economix.rrhh.dao;

import pe.edu.pucp.economix.dao.IDAO;
import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.rrhh.model.Empleado;

public interface IAreaDAO extends IDAO<Area> {
    int asignarJefe(Area area, Empleado empleado);
}

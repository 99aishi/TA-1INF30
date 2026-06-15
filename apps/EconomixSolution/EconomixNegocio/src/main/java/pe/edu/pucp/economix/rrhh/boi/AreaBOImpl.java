package pe.edu.pucp.economix.rrhh.boi;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.rrhh.ibo.IAreaBO;
import pe.edu.pucp.economix.rrhh.idao.IAreaDAO;
import pe.edu.pucp.economix.rrhh.idao.IEmpleadoDAO;
import pe.edu.pucp.economix.rrhh.daoi.AreaDAOImpl;
import pe.edu.pucp.economix.rrhh.daoi.EmpleadoDAOImpl;
import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.tesoreria.idao.ICajaChicaDAO;
import pe.edu.pucp.economix.tesoreria.daoi.CajaChicaDAOImpl;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;
import pe.edu.pucp.economix.tesoreria.model.EstadoFondo;

import java.util.List;

public class AreaBOImpl implements IAreaBO {
    private final IAreaDAO areaDAO;
    private final IEmpleadoDAO empleadoDAO;
    private final ICajaChicaDAO cajaChicaDAO;
    public AreaBOImpl(){
        areaDAO=new AreaDAOImpl();
        empleadoDAO=new EmpleadoDAOImpl();
        cajaChicaDAO=new CajaChicaDAOImpl();
    }
    @Override
    public int insertar(Area area) throws Exception {
        validar(area,false);
        validarCajaChica(area.getCajaChica());
        try {
            DBManager.getDBManager().iniciarTransaccion();
            areaDAO.insertar(area);
            CajaChica cc = area.getCajaChica();
            cc.setAreaAsignada(area);
            cc.setNombre("Fondo - " + area.getNombre());
            cc.setEstado(EstadoFondo.Activo);
            cajaChicaDAO.insertar(cc);
            DBManager.getDBManager().confirmarTransaccion();
        } catch (Exception ex) {
            DBManager.getDBManager().cancelarTransaccion();
            throw ex;
        }
        return area.getIdArea();
    }

    @Override
    public int modificar(Area area) throws Exception {
        validar(area,true);
        return areaDAO.modificar(area);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id del area debe ser mayor que cero.");
        }
        return  areaDAO.eliminar(id);
    }

    @Override
    public Area buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id del area debe ser mayor que cero.");
        }
        Area area = areaDAO.buscarPorId(id);
        if (area != null
                && area.getCajaChica() != null
                && area.getCajaChica().getIdFondo() > 0) {
            CajaChica cc = cajaChicaDAO.buscarPorId(area.getCajaChica().getIdFondo());
            area.setCajaChica(cc);
        }
        return area;
    }

    @Override
    public List<Area> listarTodas() throws Exception {
        List<Area> areas = areaDAO.listarTodas();
        if (areas != null) {
            for (Area area : areas) {
                if (area.getCajaChica() != null
                        && area.getCajaChica().getIdFondo() > 0) {
                    CajaChica cc = cajaChicaDAO.buscarPorId(area.getCajaChica().getIdFondo());
                    area.setCajaChica(cc);
                }
            }
        }
        return areas;
    }
    public void validar(Area area, boolean esModificacion) throws Exception{
        if(area==null){
            throw new Exception("El area no puede ser nulo.");

        }
        if (esModificacion && area.getIdArea() <= 0) {
            throw new Exception("El id del area es obligatorio para la modificación.");
        }

        validarNombre(area.getNombre());
        validarDescripcion(area.getDescripcion());

    }


    public void validarNombre(String nombre) throws Exception{
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("El nombre del area es obligatorio.");
        }
    }
    public void validarDescripcion(String descripcion) throws Exception{
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new Exception("La descripcion del area es obligatoria.");
        }
    }
    public void validarCajaChica(CajaChica cc) throws Exception{
        if (cc == null) {
            throw new Exception("La caja chica es obligatoria para crear un area.");
        }
        if (cc.getMontoTecho() <= 0) {
            throw new Exception("El monto techo de la caja chica debe ser mayor que cero.");
        }
    }


}

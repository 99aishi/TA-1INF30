package pe.edu.pucp.economix.tesoreria.boi;

import pe.edu.pucp.economix.rrhh.idao.IAreaDAO;
import pe.edu.pucp.economix.rrhh.daoi.AreaDAOImpl;
import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.tesoreria.ibo.ICajaChicaBO;
import pe.edu.pucp.economix.tesoreria.idao.ICajaChicaDAO;
import pe.edu.pucp.economix.tesoreria.daoi.CajaChicaDAOImpl;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;

import java.util.List;

public class CajaChicaBOImpl implements ICajaChicaBO {
    private final ICajaChicaDAO cajaDAO;
    private final IAreaDAO areaDAO;
    public CajaChicaBOImpl(){
        cajaDAO=new CajaChicaDAOImpl();
        areaDAO=new AreaDAOImpl();
    }
    private void validarIdUsuarioAccion(int idUsuarioAccion) throws Exception {
        if (idUsuarioAccion <= 0) {
            throw new Exception("El usuario de acción debe ser mayor que cero.");
        }
    }

    @Override
    public int insertar(CajaChica caja, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        validar(caja,false);
        return cajaDAO.insertar(caja, idUsuarioAccion);
    }

    @Override
    public int modificar(CajaChica caja, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        validar(caja,true);
        return cajaDAO.modificar(caja, idUsuarioAccion);
    }

    @Override
    public int eliminar(int id, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        if (id <= 0) {
            throw new Exception("El id de la caja chica debe ser mayor que cero.");
        }
        return cajaDAO.eliminar(id, idUsuarioAccion);
    }

    @Override
    public CajaChica buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id de la caja chica debe ser mayor que cero.");
        }
        return cajaDAO.buscarPorId(id);
    }

    @Override
    public List<CajaChica> listarTodas() throws Exception {
        return cajaDAO.listarTodas();
    }

    @Override
    public List<CajaChica> listarActivas() throws Exception {
        return cajaDAO.listarActivas();
    }
    public void validar(CajaChica caja,boolean esModificacion) throws Exception{
        if(caja==null){
            throw new Exception("La caja chica no puede ser nula.");

        }
        if (esModificacion && caja.getIdFondo() <= 0) {
            throw new Exception("El id de la caja chica es obligatoria para la modificación.");
        }

        validarArea(caja.getAreaAsignada());
        validarNombre(caja.getNombre());
        validarMontoTecho(caja.getMontoTecho());

    }
    public void validarArea(Area area) throws Exception{
        if (area == null) {
            throw new Exception("El area de la caja chica es obligatorio.");
        }

        if (area.getIdArea() <= 0) {
            throw new Exception("El area de la caja chica no es válido.");
        }
        if(areaDAO.buscarPorId(area.getIdArea())==null){
            throw new Exception("El area de la caja chica no existe.");
        }

    }
    public void validarNombre(String nombre) throws Exception{
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("El nombre de la caja chica es obligatorio.");
        }

    }
    public void validarMontoTecho(double monto) throws Exception{
        if (monto <= 0) {
            throw new Exception("El monto techo de la caja chica debe ser mayor que cero.");
        }
    }
}

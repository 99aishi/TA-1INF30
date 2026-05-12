package pe.edu.pucp.economix.tesoreria.bo;

import pe.edu.pucp.economix.rrhh.dao.IAreaDAO;
import pe.edu.pucp.economix.rrhh.implement.AreaImplement;
import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.tesoreria.boi.ICajaChicaBO;
import pe.edu.pucp.economix.tesoreria.dao.ICajaChicaDAO;
import pe.edu.pucp.economix.tesoreria.implement.CajaChicaImplement;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;

import java.util.List;

public class CajaChicaBOImpl implements ICajaChicaBO {
    private final ICajaChicaDAO cajaDAO;
    private final IAreaDAO areaDAO;
    public CajaChicaBOImpl(){
        cajaDAO=new CajaChicaImplement();
        areaDAO=new AreaImplement();
    }
    @Override
    public int insertar(CajaChica caja) throws Exception {
        validar(caja,false);
        return cajaDAO.insertar(caja);
    }

    @Override
    public int modificar(CajaChica caja) throws Exception {
        validar(caja,true);
        return cajaDAO.modificar(caja);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id de la caja chica debe ser mayor que cero.");
        }
        return cajaDAO.eliminar(id);
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
        return List.of();
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

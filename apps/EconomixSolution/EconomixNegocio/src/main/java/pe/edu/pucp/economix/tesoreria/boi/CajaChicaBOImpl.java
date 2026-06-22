package pe.edu.pucp.economix.tesoreria.boi;

import pe.edu.pucp.economix.tesoreria.ibo.ICajaChicaBO;
import pe.edu.pucp.economix.tesoreria.idao.ICajaChicaDAO;
import pe.edu.pucp.economix.tesoreria.idao.ICuentaBancariaDAO;
import pe.edu.pucp.economix.tesoreria.daoi.CajaChicaDAOImpl;
import pe.edu.pucp.economix.tesoreria.daoi.CuentaBancariaDAOImpl;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;
import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;

import java.util.List;

public class CajaChicaBOImpl implements ICajaChicaBO {
    private final ICajaChicaDAO cajaDAO;
    private final ICuentaBancariaDAO cuentaBancariaDAO;
    public CajaChicaBOImpl(){
        cajaDAO=new CajaChicaDAOImpl();
        cuentaBancariaDAO=new CuentaBancariaDAOImpl();
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

    @Override
    public List<CajaChica> listarPorCuentaBancaria(int idCuentaBancaria) throws Exception {
        if (idCuentaBancaria <= 0) {
            throw new Exception("El id de la cuenta bancaria debe ser mayor que cero.");
        }
        return cajaDAO.listarPorCuentaBancaria(idCuentaBancaria);
    }

    public void validar(CajaChica caja,boolean esModificacion) throws Exception{
        if(caja==null){
            throw new Exception("La caja chica no puede ser nula.");

        }
        if (esModificacion && caja.getIdFondo() <= 0) {
            throw new Exception("El id de la caja chica es obligatoria para la modificación.");
        }

        validarCuentaBancaria(caja.getCuentaBancaria());
        validarNombre(caja.getNombre());
        validarMontoTecho(caja.getMontoTecho());

    }
    public void validarCuentaBancaria(CuentaBancaria cuentaBancaria) throws Exception{
        if (cuentaBancaria == null) {
            throw new Exception("La cuenta bancaria de la caja chica es obligatoria.");
        }

        if (cuentaBancaria.getIdCuenta() <= 0) {
            throw new Exception("La cuenta bancaria de la caja chica no es válida.");
        }
        if(cuentaBancariaDAO.buscarPorId(cuentaBancaria.getIdCuenta())==null){
            throw new Exception("La cuenta bancaria de la caja chica no existe.");
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

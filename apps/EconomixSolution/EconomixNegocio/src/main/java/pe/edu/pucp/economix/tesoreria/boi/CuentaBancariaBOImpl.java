package pe.edu.pucp.economix.tesoreria.boi;

import pe.edu.pucp.economix.rrhh.idao.IAreaDAO;
import pe.edu.pucp.economix.rrhh.idao.IEmpleadoDAO;
import pe.edu.pucp.economix.rrhh.daoi.AreaDAOImpl;
import pe.edu.pucp.economix.rrhh.daoi.EmpleadoDAOImpl;
import pe.edu.pucp.economix.tesoreria.ibo.ICuentaBancariaBO;
import pe.edu.pucp.economix.tesoreria.idao.ICuentaBancariaDAO;
import pe.edu.pucp.economix.tesoreria.idao.IMonedaDAO;
import pe.edu.pucp.economix.tesoreria.daoi.CuentaBancariaDAOImpl;
import pe.edu.pucp.economix.tesoreria.daoi.MonedaDAOImpl;
import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

import java.util.List;

public class CuentaBancariaBOImpl implements ICuentaBancariaBO {
    private final ICuentaBancariaDAO cuentaDAO;
    private final IEmpleadoDAO empleadoDAO;
    private final IAreaDAO areaDAO;
    private final IMonedaDAO monedaDAO;
    public CuentaBancariaBOImpl(){
        cuentaDAO=new CuentaBancariaDAOImpl();
        empleadoDAO= new EmpleadoDAOImpl();
        areaDAO=new AreaDAOImpl();
        monedaDAO=new MonedaDAOImpl();
    }
    private void validarIdUsuarioAccion(int idUsuarioAccion) throws Exception {
        if (idUsuarioAccion <= 0) {
            throw new Exception("El usuario de acción debe ser mayor que cero.");
        }
    }

    @Override
    public int insertar(CuentaBancaria cuenta, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        validar(cuenta,false);
        return cuentaDAO.insertar(cuenta, idUsuarioAccion);
    }

    @Override
    public int modificar(CuentaBancaria cuenta, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        validar(cuenta,true);
        return cuentaDAO.modificar(cuenta, idUsuarioAccion);
    }

    @Override
    public int eliminar(int id, int idUsuarioAccion) throws Exception {
        validarIdUsuarioAccion(idUsuarioAccion);
        if (id <= 0) {
            throw new Exception("El id de la cuenta bancaria debe ser mayor que cero.");
        }
        return cuentaDAO.eliminar(id, idUsuarioAccion);
    }

    @Override
    public CuentaBancaria buscarPorId(int id) throws Exception {
        if(id<=0){
            throw new Exception("El id de la cuenta bancaria debe ser mayor que cero.");
        }
        return cuentaDAO.buscarPorId(id);
    }

    @Override
    public List<CuentaBancaria> listarTodas() throws Exception {
        return cuentaDAO.listarTodas();
    }

    @Override
    public List<CuentaBancaria> listarActivas() throws Exception {
        return cuentaDAO.listarActivas();
    }
    public void validar(CuentaBancaria cuenta, boolean esModificacion)throws Exception{
        if(cuenta==null){
            throw new Exception("La cuenta bancaria no puede ser nula.");

        }
        if (esModificacion && cuenta.getIdCuenta() <= 0) {
            throw new Exception("El id de la cuenta bancaria es obligatorio para la modificación.");
        }
        validarMoneda(cuenta.getMoneda());
        if(cuenta.getAreaAdministradora()==null && cuenta.getEmpleadoAdministrador()==null) {
            throw new Exception("La cuenta debe tener un dueño");
        }
        validarNombreBanco(cuenta.getNombreBanco());
        valiarNumeroBancario(cuenta.getNumeroBancario());
        validarCCI(cuenta.getCci());
    }

    public void validarMoneda(Moneda moneda) throws Exception{
        if (moneda == null) {
            throw new Exception("La moneda de la cuenta bancaria es obligatoria.");
        }

        if (moneda.getIdMoneda() <= 0) {
            throw new Exception("La moneda de la cuenta bancaria no es válida.");
        }
        if(monedaDAO.buscarPorId(moneda.getIdMoneda())==null){
            throw new Exception("El moneda de la cuenta bancaria no existe.");
        }


    }

    public void validarNombreBanco(String nombre) throws Exception{
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("El nombre del banco de la cuenta bancaria es obligatorio.");
        }

    }
    public void valiarNumeroBancario(String numero)throws Exception{
        if (numero == null || numero.trim().isEmpty()) {
            throw new Exception("El numero bancario de la cuenta bancaria es obligatorio.");
        }

        numero= numero.trim();

        if (numero.length() < 10) {
            throw new Exception("El numero bancario de la cuenta bancaria debe tener al menos 10 caracteres.");
        }


    }
    public void validarCCI(String cci) throws Exception{
        if (cci == null || cci.trim().isEmpty()) {
            throw new Exception("El CCI de la cuenta bancaria es obligatorio.");
        }

        cci= cci.trim();

        if (cci.length() < 20) {
            throw new Exception("El CCI de la cuenta bancaria debe tener al menos 20 caracteres.");
        }
        if (cci.length() > 20) {
            throw new Exception("El CCI de la cuenta bancaria no debe exceder los 20 caracteres.");
        }


    }

}

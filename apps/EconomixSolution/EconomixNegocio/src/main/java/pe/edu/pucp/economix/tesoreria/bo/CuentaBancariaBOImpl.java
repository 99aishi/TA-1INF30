package pe.edu.pucp.economix.tesoreria.bo;

import pe.edu.pucp.economix.rrhh.dao.IAreaDAO;
import pe.edu.pucp.economix.rrhh.dao.IEmpleadoDAO;
import pe.edu.pucp.economix.rrhh.implement.AreaImplement;
import pe.edu.pucp.economix.rrhh.implement.EmpleadoImplement;
import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.rrhh.model.Empleado;
import pe.edu.pucp.economix.tesoreria.boi.ICuentaBancariaBO;
import pe.edu.pucp.economix.tesoreria.dao.ICuentaBancariaDAO;
import pe.edu.pucp.economix.tesoreria.dao.IMonedaDAO;
import pe.edu.pucp.economix.tesoreria.implement.CuentaBancariaImplement;
import pe.edu.pucp.economix.tesoreria.implement.MonedaImplement;
import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

import java.util.List;

public class CuentaBancariaBOImpl implements ICuentaBancariaBO {
    private final ICuentaBancariaDAO cuentaDAO;
    private final IEmpleadoDAO empleadoDAO;
    private final IAreaDAO areaDAO;
    private final IMonedaDAO monedaDAO;
    public CuentaBancariaBOImpl(){
        cuentaDAO=new CuentaBancariaImplement();
        empleadoDAO= new EmpleadoImplement();
        areaDAO=new AreaImplement();
        monedaDAO=new MonedaImplement();
    }
    @Override
    public int insertar(CuentaBancaria cuenta) throws Exception {
        validar(cuenta,false);
        return cuentaDAO.insertar(cuenta);
    }

    @Override
    public int modificar(CuentaBancaria cuenta) throws Exception {
        validar(cuenta,true);
        return cuentaDAO.modificar(cuenta);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id de la cuenta bancaria debe ser mayor que cero.");
        }
        return cuentaDAO.eliminar(id);
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
        return List.of();
    }
    public void validar(CuentaBancaria cuenta, boolean esModificacion)throws Exception{
        if(cuenta==null){
            throw new Exception("La cuenta bancaria no puede ser nula.");

        }
        if (esModificacion && cuenta.getIdCuenta() <= 0) {
            throw new Exception("El id de la cuenta bancaria es obligatorio para la modificación.");
        }
        validarAdministrador(cuenta.getEmpleadoAdministrador());
        validarMoneda(cuenta.getMoneda());
        validarArea(cuenta.getAreaAdministradora());
        validarNombreBanco(cuenta.getNombreBanco());
        valiarNumeroBancario(cuenta.getNumeroBancario());
        validarCCI(cuenta.getCci());
    }
    public void validarAdministrador(Empleado empleado) throws Exception{
        if (empleado == null) {
            throw new Exception("El empleado administrador de la cuenta bancaria es obligatorio.");
        }

        if (empleado.getUsuarioID() <= 0) {
            throw new Exception("El empleado administrador de la cuenta bancaria no es válido.");
        }
        if(empleadoDAO.buscarPorId(empleado.getUsuarioID())==null){
            throw new Exception("El empleado administrador de la cuenta bancaria no existe.");
        }

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
    public void  validarArea(Area area) throws Exception{
        if (area == null) {
            throw new Exception("El area de la cuenta bancaria es obligatorio.");
        }

        if (area.getIdArea() <= 0) {
            throw new Exception("El area de la cuenta bancaria no es válido.");
        }
        if(areaDAO.buscarPorId(area.getIdArea())==null){
            throw new Exception("El area de la cuenta bancaria no existe.");
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

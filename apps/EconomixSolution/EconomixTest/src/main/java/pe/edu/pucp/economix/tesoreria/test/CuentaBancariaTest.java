package pe.edu.pucp.economix.tesoreria.test;

import pe.edu.pucp.economix.rrhh.model.Empleado;
import pe.edu.pucp.economix.tesoreria.implement.CuentaBancariaImplement;
import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

import java.util.List;

public class CuentaBancariaTest {

        public static List<CuentaBancaria> probarInsercion(Empleado emp1, Moneda sol){
            CuentaBancariaImplement cuentaDAO=new CuentaBancariaImplement();
            CuentaBancaria cuenta= new CuentaBancaria();
            cuenta.setNombreBanco("BCP");
            cuenta.setNumeroBancario("444");
            cuenta.setCci("555");
            cuenta.setIdCuenta(2);
            cuenta.setMoneda(sol);
            cuenta.setAdministrador(emp1);
            cuenta.setIdCuenta(cuentaDAO.insertar(cuenta));


            List<CuentaBancaria> cuentasBancarias =cuentaDAO.listarTodas();
            for(CuentaBancaria acc : cuentasBancarias){
                System.out.println(acc);
            }
            return cuentasBancarias;
        }
}

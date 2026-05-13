package pe.edu.pucp.economix.tesoreria.test;

import java.sql.SQLException;
import java.util.List;

import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.rrhh.model.Empleado;
import pe.edu.pucp.economix.tesoreria.bo.CuentaBancariaBOImpl;
import pe.edu.pucp.economix.tesoreria.boi.ICuentaBancariaBO;
import pe.edu.pucp.economix.tesoreria.implement.CuentaBancariaImplement;
import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

public class CuentaBancariaTest {
        private final static ICuentaBancariaBO cuentaBO=new CuentaBancariaBOImpl();
        public static List<CuentaBancaria> pruebaInsercion(Empleado emp1, Moneda sol,
                                                            Empleado emp2, Moneda usd,
                                                           Area area1) throws Exception {
            CuentaBancaria cuenta= new CuentaBancaria();
            cuenta.setNombreBanco("BCP");
            cuenta.setNumeroBancario("44444444444");
            cuenta.setCci("55555555555555555555");
            cuenta.setMoneda(sol);
            cuenta.setEmpleadoAdministrador(emp1);
            cuenta.setIdCuenta(cuentaBO.insertar(cuenta));

            CuentaBancaria cuentaUSD = new CuentaBancaria();
            cuentaUSD.setNombreBanco("BBVA");
            cuentaUSD.setNumeroBancario("77777777777");
            cuentaUSD.setCci("67676767676767676767");
            cuentaUSD.setMoneda(usd);
            cuentaUSD.setEmpleadoAdministrador((emp2));
            cuentaUSD.setIdCuenta(cuentaBO.insertar(cuentaUSD));

            CuentaBancaria cuenta3 = new CuentaBancaria();
            cuenta3.setNombreBanco("Interbank");
            cuenta3.setNumeroBancario("6767676767");
            cuenta3.setCci("76767676767676767676");
            cuenta3.setMoneda(sol);
            cuenta3.setAreaAdministradora(area1);
            cuenta3.setIdCuenta(cuentaBO.insertar(cuenta3));

            List<CuentaBancaria> cuentasBancarias =cuentaBO.listarTodas();
            for(CuentaBancaria acc : cuentasBancarias){
                System.out.println(acc);
            }
            System.out.println();
            return cuentasBancarias;
        }
}

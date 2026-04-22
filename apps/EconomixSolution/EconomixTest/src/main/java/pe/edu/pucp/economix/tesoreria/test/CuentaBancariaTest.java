package pe.edu.pucp.economix.tesoreria.test;

import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.rrhh.model.Empleado;
import pe.edu.pucp.economix.tesoreria.implement.CuentaBancariaImplement;
import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

import java.util.List;

public class CuentaBancariaTest {
        private static CuentaBancariaImplement cuentaDAO=new CuentaBancariaImplement();
        public static List<CuentaBancaria> pruebaInsercion(Empleado emp1, Moneda sol,
                                                            Empleado emp2, Moneda usd,
                                                           Area area1){
            CuentaBancaria cuenta= new CuentaBancaria();
            cuenta.setNombreBanco("BCP");
            cuenta.setNumeroBancario("444");
            cuenta.setCci("555");
            cuenta.setMoneda(sol);
            cuenta.setAdministrador(emp1);
            cuenta.setIdCuenta(cuentaDAO.insertar(cuenta));

            CuentaBancaria cuentaUSD = new CuentaBancaria();
            cuentaUSD.setNombreBanco("BBVA");
            cuentaUSD.setNumeroBancario("777");
            cuentaUSD.setCci("67");
            cuentaUSD.setMoneda(usd);
            cuentaUSD.setAdministrador(emp2);
            cuentaUSD.setIdCuenta(cuentaDAO.insertar(cuentaUSD));

            CuentaBancaria cuenta3 = new CuentaBancaria();
            cuenta3.setNombreBanco("Interbank");
            cuenta3.setNumeroBancario("676767");
            cuenta3.setCci("76");
            cuenta3.setMoneda(sol);
            cuenta3.setAreaAdministradora(area1);
            cuenta3.setIdCuenta(cuentaDAO.insertar(cuenta3));

            List<CuentaBancaria> cuentasBancarias =cuentaDAO.listarTodas();
            for(CuentaBancaria acc : cuentasBancarias){
                System.out.println(acc);
            }
            System.out.println();
            return cuentasBancarias;
        }
}

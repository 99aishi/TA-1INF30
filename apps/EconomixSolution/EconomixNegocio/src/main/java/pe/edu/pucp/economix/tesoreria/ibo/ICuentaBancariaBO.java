package pe.edu.pucp.economix.tesoreria.ibo;

import pe.edu.pucp.economix.ibo.IBaseBO;
import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;

import java.util.List;

public interface ICuentaBancariaBO extends IBaseBO<CuentaBancaria> {
    List<CuentaBancaria> listarActivas() throws Exception;
}

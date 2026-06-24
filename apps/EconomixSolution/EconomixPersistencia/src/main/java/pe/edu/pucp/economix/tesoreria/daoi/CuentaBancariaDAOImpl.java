package pe.edu.pucp.economix.tesoreria.daoi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.rrhh.model.Empleado;
import pe.edu.pucp.economix.rrhh.model.RolFlujo;
import pe.edu.pucp.economix.tesoreria.idao.ICuentaBancariaDAO;
import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

public class CuentaBancariaDAOImpl implements ICuentaBancariaDAO{
    private ResultSet rs;

    @Override
    public int insertar(CuentaBancaria cuentaBancaria, int idUsuarioAccion) throws SQLException {
        Map<String,Object> parametrosSalida = new HashMap<>();
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosSalida.put("p_id_cuenta_bancaria", Types.INTEGER);
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_nombre_banco", cuentaBancaria.getNombreBanco());
        parametrosEntrada.put("p_numero_cuenta", cuentaBancaria.getNumeroBancario());
        parametrosEntrada.put("p_cci", cuentaBancaria.getCci());
        if(cuentaBancaria.getMoneda() == null)
            parametrosEntrada.put("p_id_moneda", null);
        else
            parametrosEntrada.put("p_id_moneda", cuentaBancaria.getMoneda().getIdMoneda());
        if(cuentaBancaria.getEmpleadoAdministrador() == null)
            parametrosEntrada.put("p_id_usuario_titular", null);
        else
            parametrosEntrada.put("p_id_usuario_titular", cuentaBancaria.getEmpleadoAdministrador().getUsuarioID());
        if(cuentaBancaria.getAreaAdministradora() == null)
            parametrosEntrada.put("p_id_area_titular", null);
        else
            parametrosEntrada.put("p_id_area_titular", cuentaBancaria.getAreaAdministradora().getIdArea());

        DBManager.getDBManager().ejecutarProcedimiento("pa_insertar_cuenta_bancaria", parametrosEntrada, parametrosSalida);
        cuentaBancaria.setIdCuenta((int)parametrosSalida.get("p_id_cuenta_bancaria"));

        return cuentaBancaria.getIdCuenta();
    }

    @Override
    public int modificar(CuentaBancaria cuentaBancaria, int idUsuarioAccion) throws SQLException {
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_cuenta_bancaria", cuentaBancaria.getIdCuenta());
        parametrosEntrada.put("p_nombre_banco", cuentaBancaria.getNombreBanco());
        parametrosEntrada.put("p_numero_cuenta", cuentaBancaria.getNumeroBancario());
        parametrosEntrada.put("p_cci", cuentaBancaria.getCci());
        if(cuentaBancaria.getMoneda() == null)
            parametrosEntrada.put("p_id_moneda", null);
        else
            parametrosEntrada.put("p_id_moneda", cuentaBancaria.getMoneda().getIdMoneda());
        if(cuentaBancaria.getEmpleadoAdministrador() == null)
            parametrosEntrada.put("p_id_usuario_titular", null);
        else
            parametrosEntrada.put("p_id_usuario_titular", cuentaBancaria.getEmpleadoAdministrador().getUsuarioID());
        if(cuentaBancaria.getAreaAdministradora() == null)
            parametrosEntrada.put("p_id_area_titular", null);
        else
            parametrosEntrada.put("p_id_area_titular", cuentaBancaria.getAreaAdministradora().getIdArea());
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_modificar_cuenta_bancaria", parametrosEntrada, null);
        return resultado;
    }

    @Override
    public int eliminar(int idCuentaBancaria, int idUsuarioAccion) throws SQLException {
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_cuenta_bancaria", idCuentaBancaria);
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_eliminar_cuenta_bancaria", parametrosEntrada, null);
        return resultado;
    }

    @Override
    public CuentaBancaria buscarPorId(int idCuentaBancaria) throws SQLException {
        CuentaBancaria cuentaBancaria=null;
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_cuenta_bancaria", idCuentaBancaria);
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_buscar_cuenta_bancaria_por_id", parametrosEntrada);
        try{
            if(rs.next()){
                cuentaBancaria = mapearCuentaBancaria(rs, new HashMap<>());
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar cuenta bancaria por id: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return cuentaBancaria;
    }

    @Override
    public List<CuentaBancaria> listarActivas() throws SQLException {
        List<CuentaBancaria>cuentas=null;
        CuentaBancaria cuentaBancaria;
        Map<Class<?>, Map<Integer, Object>> cache = new HashMap<>();
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_cuentas_bancarias_activas", null);
        try{
            while(rs.next()){
                if(cuentas == null) cuentas = new ArrayList<>();
                cuentaBancaria = mapearCuentaBancaria(rs, cache);
                cuentas.add(cuentaBancaria);
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar cuenta bancaria por id: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return cuentas;
    }

    @Override
    public List<CuentaBancaria> listarTodas() throws SQLException {
        List<CuentaBancaria>cuentas=null;
        CuentaBancaria cuentaBancaria;
        Map<Class<?>, Map<Integer, Object>> cache = new HashMap<>();
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_cuentas_bancarias", null);
        try{
            while(rs.next()){
                if(cuentas == null) cuentas = new ArrayList<>();
                cuentaBancaria = mapearCuentaBancaria(rs, cache);
                cuentas.add(cuentaBancaria);
            }
        }catch(SQLException ex){
            System.out.println("Error al listar cuentas bancarias: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return cuentas;
    }

    @SuppressWarnings("unchecked")
    private <T> T getOrCreate(Map<Class<?>, Map<Integer, Object>> cache, Class<T> type, int id, Supplier<T> factory) {
        if (id <= 0) return null;
        return (T) cache.computeIfAbsent(type, k -> new HashMap<>())
                .computeIfAbsent(id, k -> factory.get());
    }

    @Override
    public List<CuentaBancaria> listarPorEmpleado(int idEmpleado) throws SQLException {
        List<CuentaBancaria> cuentas = null;
        CuentaBancaria cuentaBancaria;
        Map<Class<?>, Map<Integer, Object>> cache = new HashMap<>();
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario", idEmpleado);
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_cuentas_bancarias_por_empleado", parametrosEntrada);
        try {
            while (rs.next()) {
                if (cuentas == null) cuentas = new ArrayList<>();
                cuentaBancaria = mapearCuentaBancaria(rs, cache);
                cuentas.add(cuentaBancaria);
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar cuentas bancarias por empleado: " + ex.getMessage());
        } finally {
            DBManager.getDBManager().cerrarConexion();
        }
        return cuentas;
    }

    private CuentaBancaria mapearCuentaBancaria(ResultSet rs, Map<Class<?>, Map<Integer, Object>> cache) throws SQLException {
        int idCuenta = rs.getInt("id_cuenta_bancaria");
        if (rs.wasNull() || idCuenta <= 0) return null;
        CuentaBancaria cuentaBancaria = getOrCreate(cache, CuentaBancaria.class, idCuenta, () -> new CuentaBancaria());
        cuentaBancaria.setIdCuenta(idCuenta);
        cuentaBancaria.setNumeroBancario(rs.getString("numero_cuenta"));
        cuentaBancaria.setNombreBanco(rs.getString("nombre_banco"));
        cuentaBancaria.setCci(rs.getString("cci"));
        cuentaBancaria.setMoneda(mapearMonedaCompleta(rs, "mon_", cache));
        cuentaBancaria.setAreaAdministradora(mapearAreaCompleta(rs, "area_", cache));
        cuentaBancaria.setEmpleadoAdministrador(mapearEmpleadoBasico(rs, "emp_", cache));
        cuentaBancaria.setActiva(rs.getBoolean("activa"));
        return cuentaBancaria;
    }

    private Moneda mapearMonedaCompleta(ResultSet rs, String prefijo, Map<Class<?>, Map<Integer, Object>> cache) throws SQLException {
        int id = rs.getInt(prefijo + "id_moneda");
        if (rs.wasNull() || id <= 0) return null;
        return getOrCreate(cache, Moneda.class, id, () -> {
            Moneda moneda = new Moneda();
            moneda.setIdMoneda(id);
            try {
                moneda.setCodigoISO(rs.getString(prefijo + "codigo_iso"));
                moneda.setSimbolo(rs.getString(prefijo + "simbolo"));
                moneda.setNombre(rs.getString(prefijo + "nombre"));
                moneda.setDescripcion(rs.getString(prefijo + "descripcion"));
                moneda.setActiva(rs.getBoolean(prefijo + "activa"));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            return moneda;
        });
    }

    private Area mapearAreaCompleta(ResultSet rs, String prefijo, Map<Class<?>, Map<Integer, Object>> cache) throws SQLException {
        int id = rs.getInt(prefijo + "id_area");
        if (rs.wasNull() || id <= 0) return null;
        return getOrCreate(cache, Area.class, id, () -> {
            Area area = new Area();
            area.setIdArea(id);
            try {
                area.setNombre(rs.getString(prefijo + "nombre"));
                area.setDescripcion(rs.getString(prefijo + "descripcion"));
                area.setEstaActivo(rs.getBoolean(prefijo + "esta_activo"));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            return area;
        });
    }

    private Empleado mapearEmpleadoBasico(ResultSet rs, String prefijo, Map<Class<?>, Map<Integer, Object>> cache) throws SQLException {
        int id = rs.getInt(prefijo + "id_usuario");
        if (rs.wasNull() || id <= 0) return null;
        return getOrCreate(cache, Empleado.class, id, () -> {
            Empleado empleado = new Empleado();
            empleado.setUsuarioID(id);
            try {
                empleado.setNombres(rs.getString(prefijo + "nombres"));
                empleado.setApellidoPaterno(rs.getString(prefijo + "apellido_paterno"));
                empleado.setApellidoMaterno(rs.getString(prefijo + "apellido_materno"));
                empleado.setCorreo(rs.getString(prefijo + "correo"));
                empleado.setNumeroCelular(rs.getString(prefijo + "numero_celular"));
                String rolFlujo = rs.getString(prefijo + "rol_flujo");
                if (rolFlujo != null)
                    empleado.setRolFlujo(RolFlujo.valueOf(rolFlujo));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            return empleado;
        });
    }

}

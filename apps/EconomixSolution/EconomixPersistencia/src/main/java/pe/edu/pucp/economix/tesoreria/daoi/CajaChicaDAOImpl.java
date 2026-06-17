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
import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;
import pe.edu.pucp.economix.tesoreria.model.Moneda;
import pe.edu.pucp.economix.tesoreria.idao.ICajaChicaDAO;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;
import pe.edu.pucp.economix.tesoreria.model.EstadoFondo;

public class CajaChicaDAOImpl implements ICajaChicaDAO{
    private ResultSet rs;

    @Override
    public int insertar(CajaChica cajaChica, int idUsuarioAccion) throws SQLException {
        Map<String,Object> parametrosSalida = new HashMap<>();
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosSalida.put("p_id_fondo", Types.INTEGER);
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_nombre_fondo", cajaChica.getNombre());
        parametrosEntrada.put("p_estado_fondo", cajaChica.getEstado().toString());

        DBManager.getDBManager().ejecutarProcedimiento("pa_insertar_fondo", parametrosEntrada, parametrosSalida);
        cajaChica.setIdFondo((int)parametrosSalida.get("p_id_fondo"));

        parametrosSalida = new HashMap<>();
        parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_fondo", cajaChica.getIdFondo());
        parametrosEntrada.put("p_monto_techo", (Double)cajaChica.getMontoTecho());
        if(cajaChica.getAreaAsignada() == null)
            parametrosEntrada.put("p_id_area", null);
        else
            parametrosEntrada.put("p_id_area", cajaChica.getAreaAsignada().getIdArea());
        if(cajaChica.getMoneda() != null)
            parametrosEntrada.put("p_id_moneda", cajaChica.getMoneda().getIdMoneda());
        else
            parametrosEntrada.put("p_id_moneda", null);
        if(cajaChica.getCuentaOrigen() != null)
            parametrosEntrada.put("p_id_cuenta_origen", cajaChica.getCuentaOrigen().getIdCuenta());
        else
            parametrosEntrada.put("p_id_cuenta_origen", null);

        DBManager.getDBManager().ejecutarProcedimiento("pa_insertar_caja_chica", parametrosEntrada, null);

        return cajaChica.getIdFondo();
    }

    @Override
    public int modificar(CajaChica cajaChica, int idUsuarioAccion) throws SQLException{
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_fondo", cajaChica.getIdFondo());
        parametrosEntrada.put("p_nombre_fondo", cajaChica.getNombre());
        parametrosEntrada.put("p_estado_fondo", cajaChica.getEstado().toString());
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_modificar_fondo", parametrosEntrada, null);

        parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_fondo", cajaChica.getIdFondo());
        parametrosEntrada.put("p_monto_techo", (Double)cajaChica.getMontoTecho());
        if(cajaChica.getAreaAsignada() == null)
            parametrosEntrada.put("p_id_area", null);
        else
            parametrosEntrada.put("p_id_area", cajaChica.getAreaAsignada().getIdArea());
        if(cajaChica.getMoneda() != null)
            parametrosEntrada.put("p_id_moneda", cajaChica.getMoneda().getIdMoneda());
        else
            parametrosEntrada.put("p_id_moneda", null);
        if(cajaChica.getCuentaOrigen() != null)
            parametrosEntrada.put("p_id_cuenta_origen", cajaChica.getCuentaOrigen().getIdCuenta());
        else
            parametrosEntrada.put("p_id_cuenta_origen", null);

        DBManager.getDBManager().ejecutarProcedimiento("pa_modificar_caja_chica", parametrosEntrada, null);
        return resultado;
    }

    @Override
    public int eliminar(int id, int idUsuarioAccion) throws SQLException{
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_fondo", id);
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_eliminar_caja_chica", parametrosEntrada, null);
        return resultado;
    }

    @Override
    public CajaChica buscarPorId(int idCajaChica) throws SQLException{
        CajaChica caja=null;
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_fondo", idCajaChica);
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_buscar_por_id_caja_chica", parametrosEntrada);
        try{
            if(rs.next()){
                caja = mapearCajaChica(rs, new HashMap<>());
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar caja chica por id: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return caja;
    }

    @Override
    public List<CajaChica> listarActivas() throws SQLException{
        List<CajaChica> cajas = null;
        CajaChica caja;
        Map<Class<?>, Map<Integer, Object>> cache = new HashMap<>();
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_cajas_chicas_activas", null);
        try{
            while(rs.next()){
                if(cajas == null) cajas = new ArrayList<>();
                caja = mapearCajaChica(rs, cache);
                cajas.add(caja);
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar cajas chicas: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return cajas;
    }

    @Override
    public List<CajaChica> listarTodas() throws SQLException{
        List<CajaChica> cajas = null;
        CajaChica caja;
        Map<Class<?>, Map<Integer, Object>> cache = new HashMap<>();
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_todas_cajas_chicas", null);
        try{
            while(rs.next()){
                if(cajas == null) cajas = new ArrayList<>();
                caja = mapearCajaChica(rs, cache);
                cajas.add(caja);
            }
        }catch(SQLException ex){
            System.out.println("Error al listar cajas chicas: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return cajas;
    }

    @SuppressWarnings("unchecked")
    private <T> T getOrCreate(Map<Class<?>, Map<Integer, Object>> cache, Class<T> type, int id, Supplier<T> factory) {
        if (id <= 0) return null;
        return (T) cache.computeIfAbsent(type, k -> new HashMap<>())
                .computeIfAbsent(id, k -> factory.get());
    }

    private CajaChica mapearCajaChica(ResultSet rs, Map<Class<?>, Map<Integer, Object>> cache) throws SQLException {
        int idFondo = rs.getInt("id_fondo");
        if (rs.wasNull() || idFondo <= 0) return null;
        CajaChica caja = getOrCreate(cache, CajaChica.class, idFondo, () -> new CajaChica());
        caja.setIdFondo(idFondo);
        caja.setNombre(rs.getString("nombre_fondo"));
        String estadoFondo = rs.getString("estado_fondo");
        if (estadoFondo != null)
            caja.setEstado(EstadoFondo.valueOf(estadoFondo));
        caja.setMontoTecho(rs.getDouble("monto_techo"));

        Area area = mapearAreaCompleta(rs, "area_", cache);
        if (area != null) {
            area.setCajaChica(caja);
            caja.setAreaAsignada(area);
        }

        Moneda moneda = mapearMonedaCompleta(rs, "mon_", cache);
        if (moneda != null)
            caja.setMoneda(moneda);

        CuentaBancaria cuentaOrigen = mapearCuentaBancariaCompleta(rs, "cb_", cache);
        if (cuentaOrigen != null)
            caja.setCuentaOrigen(cuentaOrigen);

        return caja;
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

    private CuentaBancaria mapearCuentaBancariaCompleta(ResultSet rs, String prefijo, Map<Class<?>, Map<Integer, Object>> cache) throws SQLException {
        int id = rs.getInt(prefijo + "id_cuenta");
        if (rs.wasNull() || id <= 0) return null;
        return getOrCreate(cache, CuentaBancaria.class, id, () -> {
            CuentaBancaria cuentaBancaria = new CuentaBancaria();
            cuentaBancaria.setIdCuenta(id);
            try {
                cuentaBancaria.setNumeroBancario(rs.getString(prefijo + "numero_cuenta"));
                cuentaBancaria.setNombreBanco(rs.getString(prefijo + "nombre_banco"));
                cuentaBancaria.setCci(rs.getString(prefijo + "cci"));
                cuentaBancaria.setActiva(rs.getBoolean(prefijo + "activa"));

                Moneda moneda = mapearMonedaCompleta(rs, "cbm_", cache);
                if (moneda != null) cuentaBancaria.setMoneda(moneda);

                Area area = mapearAreaCompleta(rs, "cba_", cache);
                if (area != null) cuentaBancaria.setAreaAdministradora(area);

                Empleado empleado = mapearEmpleadoBasico(rs, "cbe_", cache);
                if (empleado != null) cuentaBancaria.setEmpleadoAdministrador(empleado);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            return cuentaBancaria;
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

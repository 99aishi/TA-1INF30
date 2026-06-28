package pe.edu.pucp.economix.operaciones.daoi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.operaciones.idao.ITransaccionDAO;
import pe.edu.pucp.economix.operaciones.model.enums.MedioPago;
import pe.edu.pucp.economix.operaciones.model.enums.TipoTransaccion;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoTransaccion;
import pe.edu.pucp.economix.operaciones.model.Transaccion;
import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoCicloCaja;
import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.rrhh.model.Empleado;
import pe.edu.pucp.economix.rrhh.model.RolFlujo;
import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;
import pe.edu.pucp.economix.tesoreria.model.CajaChica;
import pe.edu.pucp.economix.tesoreria.model.Moneda;
import pe.edu.pucp.economix.tesoreria.model.TipoCambio;

public class TransaccionDAOImpl implements ITransaccionDAO {
    private ResultSet rs;

    @Override
    public int insertar(Transaccion transaccion, int idUsuarioAccion) throws SQLException {
        Map<String, Object> parametrosSalida = new HashMap<>();
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosSalida.put("p_id_generado", Types.INTEGER);
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_tipo_operacion", transaccion.getTipoTransaccion() != null ? transaccion.getTipoTransaccion().toString() : null);
        parametrosEntrada.put("p_momento_operacion", transaccion.getFecha());
        parametrosEntrada.put("p_monto_transaccion", transaccion.getMonto());
        parametrosEntrada.put("p_numero_operacion_bancaria", transaccion.getNumeroOperacionBancaria());
        parametrosEntrada.put("p_medio_pago", transaccion.getMedioPago() != null ? transaccion.getMedioPago().toString() : null);
        if (transaccion.getTipoCambio() != null && transaccion.getTipoCambio().getIdTipoCambio() > 0) {
            parametrosEntrada.put("p_id_tipo_cambio", transaccion.getTipoCambio().getIdTipoCambio());
        } else {
            parametrosEntrada.put("p_id_tipo_cambio", null);
        }
        if (transaccion.getCuentaOrigen() != null) {
            parametrosEntrada.put("p_id_cuenta_origen", transaccion.getCuentaOrigen().getIdCuenta());
        } else {
            parametrosEntrada.put("p_id_cuenta_origen", null);
        }
        if (transaccion.getCuentaDestino() != null) {
            parametrosEntrada.put("p_id_cuenta_destino", transaccion.getCuentaDestino().getIdCuenta());
        } else {
            parametrosEntrada.put("p_id_cuenta_destino", null);
        }
        if (transaccion.getMoneda() != null) {
            parametrosEntrada.put("p_id_moneda", transaccion.getMoneda().getIdMoneda());
        } else {
            parametrosEntrada.put("p_id_moneda", null);
        }
        if (transaccion.getBeneficiario() != null) {
            parametrosEntrada.put("p_id_beneficiario", transaccion.getBeneficiario().getUsuarioID());
        } else {
            parametrosEntrada.put("p_id_beneficiario", null);
        }
        if (transaccion.getEstadoTransaccion() != null) {
            parametrosEntrada.put("p_estado_transaccion", transaccion.getEstadoTransaccion().toString());
        } else {
            parametrosEntrada.put("p_estado_transaccion", "REGISTRADA");
        }
        if (transaccion.getIdSolicitudGasto() > 0) {
            parametrosEntrada.put("p_id_solicitud_gasto", transaccion.getIdSolicitudGasto());
        } else {
            parametrosEntrada.put("p_id_solicitud_gasto", null);
        }

        DBManager.getDBManager().ejecutarProcedimiento("pa_insertar_transaccion", parametrosEntrada, parametrosSalida);
        transaccion.setIdTransaccion((int) parametrosSalida.get("p_id_generado"));

        return transaccion.getIdTransaccion();
    }

    @Override
    public int modificar(Transaccion transaccion, int idUsuarioAccion) throws SQLException {
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_transaccion", transaccion.getIdTransaccion());
        parametrosEntrada.put("p_tipo_operacion", transaccion.getTipoTransaccion() != null ? transaccion.getTipoTransaccion().toString() : null);
        parametrosEntrada.put("p_momento_operacion", transaccion.getFecha());
        parametrosEntrada.put("p_monto_transaccion", transaccion.getMonto());
        parametrosEntrada.put("p_numero_operacion_bancaria", transaccion.getNumeroOperacionBancaria());
        parametrosEntrada.put("p_medio_pago", transaccion.getMedioPago() != null ? transaccion.getMedioPago().toString() : null);
        if (transaccion.getTipoCambio() != null && transaccion.getTipoCambio().getIdTipoCambio() > 0) {
            parametrosEntrada.put("p_id_tipo_cambio", transaccion.getTipoCambio().getIdTipoCambio());
        } else {
            parametrosEntrada.put("p_id_tipo_cambio", null);
        }
        if (transaccion.getCuentaOrigen() != null) {
            parametrosEntrada.put("p_id_cuenta_origen", transaccion.getCuentaOrigen().getIdCuenta());
        } else {
            parametrosEntrada.put("p_id_cuenta_origen", null);
        }
        if (transaccion.getCuentaDestino() != null) {
            parametrosEntrada.put("p_id_cuenta_destino", transaccion.getCuentaDestino().getIdCuenta());
        } else {
            parametrosEntrada.put("p_id_cuenta_destino", null);
        }
        if (transaccion.getMoneda() != null) {
            parametrosEntrada.put("p_id_moneda", transaccion.getMoneda().getIdMoneda());
        } else {
            parametrosEntrada.put("p_id_moneda", null);
        }
        if (transaccion.getBeneficiario() != null) {
            parametrosEntrada.put("p_id_beneficiario", transaccion.getBeneficiario().getUsuarioID());
        } else {
            parametrosEntrada.put("p_id_beneficiario", null);
        }
        if (transaccion.getEstadoTransaccion() != null) {
            parametrosEntrada.put("p_estado_transaccion", transaccion.getEstadoTransaccion().toString());
        } else {
            parametrosEntrada.put("p_estado_transaccion", "REGISTRADA");
        }
        if (transaccion.getIdSolicitudGasto() > 0) {
            parametrosEntrada.put("p_id_solicitud_gasto", transaccion.getIdSolicitudGasto());
        } else {
            parametrosEntrada.put("p_id_solicitud_gasto", null);
        }

        return DBManager.getDBManager().ejecutarProcedimiento("pa_modificar_transaccion", parametrosEntrada, null);
    }

    @Override
    public int eliminar(int idTransaccion, int idUsuarioAccion) throws SQLException {
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_transaccion", idTransaccion);
        return DBManager.getDBManager().ejecutarProcedimiento("pa_eliminar_transaccion", parametrosEntrada, null);
    }

    @Override
    public Transaccion buscarPorId(int idTransaccion) throws SQLException {
        Transaccion transaccion = null;
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_transaccion", idTransaccion);
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_buscar_transaccion_por_id", parametrosEntrada);
        try {
            if (rs.next()) {
                transaccion = mapearTransaccion(rs, new HashMap<>());
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar transaccion por id: " + ex.getMessage());
        } finally {
            DBManager.getDBManager().cerrarConexion();
        }
        return transaccion;
    }

    @Override
    public List<Transaccion> listarTodas() throws SQLException {
        List<Transaccion> transacciones = null;
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_transacciones", null);
        try {
            while (rs.next()) {
                if (transacciones == null) {
                    transacciones = new ArrayList<>();
                }
                transacciones.add(mapearTransaccion(rs, new HashMap<>()));
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar transacciones: " + ex.getMessage());
        } finally {
            DBManager.getDBManager().cerrarConexion();
        }
        return transacciones;
    }

    @Override
    public List<Transaccion> listarActivas() throws SQLException {
        List<Transaccion> transacciones = null;
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_transacciones_activas", null);
        try {
            while (rs.next()) {
                if (transacciones == null) {
                    transacciones = new ArrayList<>();
                }
                transacciones.add(mapearTransaccion(rs, new HashMap<>()));
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar transacciones activas: " + ex.getMessage());
        } finally {
            DBManager.getDBManager().cerrarConexion();
        }
        return transacciones;
    }

    @Override
    public List<Transaccion> listarPorJefe(int idJefe) throws SQLException {
        List<Transaccion> transacciones = null;
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_jefe", idJefe);
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_transacciones_por_jefe", parametrosEntrada);
        try {
            while (rs.next()) {
                if (transacciones == null) {
                    transacciones = new ArrayList<>();
                }
                transacciones.add(mapearTransaccion(rs, new HashMap<>()));
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar transacciones por jefe: " + ex.getMessage());
            throw ex;
        } finally {
            DBManager.getDBManager().cerrarConexion();
        }
        return transacciones;
    }

    @Override
    public List<Transaccion> listarPorEmpleado(int idEmpleado) throws SQLException {
        List<Transaccion> transacciones = null;
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_empleado", idEmpleado);
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_transacciones_por_empleado", parametrosEntrada);
        try {
            while (rs.next()) {
                if (transacciones == null) {
                    transacciones = new ArrayList<>();
                }
                transacciones.add(mapearTransaccion(rs, new HashMap<>()));
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar transacciones por empleado: " + ex.getMessage());
            throw ex;
        } finally {
            DBManager.getDBManager().cerrarConexion();
        }
        return transacciones;
    }

    @SuppressWarnings("unchecked")
    private <T> T getOrCreate(Map<Class<?>, Map<Integer, Object>> cache, Class<T> type, int id, Supplier<T> factory) {
        if (id <= 0) return null;
        return (T) cache.computeIfAbsent(type, k -> new HashMap<>())
                .computeIfAbsent(id, k -> factory.get());
    }

    private Transaccion mapearTransaccion(ResultSet rs, Map<Class<?>, Map<Integer, Object>> cache) throws SQLException {
        int idTransaccion = rs.getInt("id_transaccion");
        Transaccion transaccion = getOrCreate(cache, Transaccion.class, idTransaccion, () -> new Transaccion());
        transaccion.setIdTransaccion(idTransaccion);
        transaccion.setTipoTransaccion(TipoTransaccion.valueOf(rs.getString("tipo_operacion")));
        transaccion.setFecha(rs.getTimestamp("momento_operacion"));
        transaccion.setMonto(rs.getDouble("monto_transaccion"));
        transaccion.setNumeroOperacionBancaria(rs.getString("numero_operacion_bancaria"));
        String medioPago = rs.getString("medio_pago");
        if (medioPago != null) {
            transaccion.setMedioPago(MedioPago.valueOf(medioPago));
        } else {
            transaccion.setMedioPago(null);
        }

        int idTipoCambio = rs.getInt("id_tipo_cambio");
        if (!rs.wasNull()) {
            TipoCambio tipoCambio = getOrCreate(cache, TipoCambio.class, idTipoCambio, () -> new TipoCambio());
            tipoCambio.setIdTipoCambio(idTipoCambio);
            Moneda monedaOrigen = mapearMoneda(rs, "id_moneda_origen", "tc_mo_", cache);
            if (monedaOrigen != null) {
                tipoCambio.setMonedaOrigen(monedaOrigen);
            }
            Moneda monedaDestino = mapearMoneda(rs, "id_moneda_destino", "tc_md_", cache);
            if (monedaDestino != null) {
                tipoCambio.setMonedaDestino(monedaDestino);
            }
            tipoCambio.setValor(rs.getDouble("valor_tipo_cambio"));
            tipoCambio.setFecha(rs.getDate("fecha_tipo_cambio"));
            transaccion.setTipoCambio(tipoCambio);
        } else {
            transaccion.setTipoCambio(null);
        }

        String estado = rs.getString("estado_transaccion");
        if (estado != null) {
            transaccion.setEstadoTransaccion(EstadoTransaccion.valueOf(estado));
        }

        int idCuentaOrigen = rs.getInt("id_cuenta_origen");
        if (!rs.wasNull()) {
            CuentaBancaria cuentaOrigen = getOrCreate(cache, CuentaBancaria.class, idCuentaOrigen, () -> new CuentaBancaria());
            cuentaOrigen.setIdCuenta(idCuentaOrigen);
            cuentaOrigen.setNumeroBancario(rs.getString("co_numero_cuenta"));
            cuentaOrigen.setNombreBanco(rs.getString("co_nombre_banco"));
            cuentaOrigen.setCci(rs.getString("co_cci"));
            cuentaOrigen.setActiva(rs.getBoolean("co_activa"));
            int idMonedaOrigen = rs.getInt("co_id_moneda");
            if (!rs.wasNull()) {
                Moneda monedaOrigenCB = getOrCreate(cache, Moneda.class, idMonedaOrigen, () -> new Moneda());
                monedaOrigenCB.setIdMoneda(idMonedaOrigen);
                cuentaOrigen.setMoneda(monedaOrigenCB);
            }
            int idAreaOrigen = rs.getInt("co_id_area");
            if (!rs.wasNull()) {
                Area areaOrigen = getOrCreate(cache, Area.class, idAreaOrigen, () -> new Area());
                areaOrigen.setIdArea(idAreaOrigen);
                cuentaOrigen.setAreaAdministradora(areaOrigen);
            }
            int idEmpleadoOrigen = rs.getInt("co_id_usuario");
            if (!rs.wasNull()) {
                Empleado empOrigen = getOrCreate(cache, Empleado.class, idEmpleadoOrigen, () -> new Empleado());
                empOrigen.setUsuarioID(idEmpleadoOrigen);
                cuentaOrigen.setEmpleadoAdministrador(empOrigen);
            }
            transaccion.setCuentaOrigen(cuentaOrigen);
        } else {
            transaccion.setCuentaOrigen(null);
        }

        int idCuentaDestino = rs.getInt("id_cuenta_destino");
        if (!rs.wasNull()) {
            CuentaBancaria cuentaDestino = getOrCreate(cache, CuentaBancaria.class, idCuentaDestino, () -> new CuentaBancaria());
            cuentaDestino.setIdCuenta(idCuentaDestino);
            cuentaDestino.setNumeroBancario(rs.getString("cd_numero_cuenta"));
            cuentaDestino.setNombreBanco(rs.getString("cd_nombre_banco"));
            cuentaDestino.setCci(rs.getString("cd_cci"));
            cuentaDestino.setActiva(rs.getBoolean("cd_activa"));
            int idMonedaDestino = rs.getInt("cd_id_moneda");
            if (!rs.wasNull()) {
                Moneda monedaDestinoCB = getOrCreate(cache, Moneda.class, idMonedaDestino, () -> new Moneda());
                monedaDestinoCB.setIdMoneda(idMonedaDestino);
                cuentaDestino.setMoneda(monedaDestinoCB);
            }
            int idAreaDestino = rs.getInt("cd_id_area");
            if (!rs.wasNull()) {
                Area areaDestino = getOrCreate(cache, Area.class, idAreaDestino, () -> new Area());
                areaDestino.setIdArea(idAreaDestino);
                cuentaDestino.setAreaAdministradora(areaDestino);
            }
            int idEmpleadoDestino = rs.getInt("cd_id_usuario");
            if (!rs.wasNull()) {
                Empleado empDestino = getOrCreate(cache, Empleado.class, idEmpleadoDestino, () -> new Empleado());
                empDestino.setUsuarioID(idEmpleadoDestino);
                cuentaDestino.setEmpleadoAdministrador(empDestino);
            }
            transaccion.setCuentaDestino(cuentaDestino);
        } else {
            transaccion.setCuentaDestino(null);
        }

        Moneda moneda = mapearMoneda(rs, "id_moneda", "m_", cache);
        if (moneda != null) {
            transaccion.setMoneda(moneda);
        } else {
            transaccion.setMoneda(null);
        }

        Empleado beneficiario = mapearEmpleadoBasico(rs, "id_beneficiario", "ben_", cache);
        transaccion.setBeneficiario(beneficiario);

        int idSolicitudGasto = rs.getInt("id_solicitud_gasto");
        if (!rs.wasNull()) {
            transaccion.setIdSolicitudGasto(idSolicitudGasto);
        }

        int ccIdCiclo = rs.getInt("cc_id_ciclo");
        if (!rs.wasNull() && ccIdCiclo > 0) {
            CicloCajaChica ciclo = getOrCreate(cache, CicloCajaChica.class, ccIdCiclo, () -> new CicloCajaChica());
            ciclo.setIdCicloCaja(ccIdCiclo);
            ciclo.setNumeroSemana(rs.getInt("cc_numero_semana"));

            java.sql.Timestamp ccFechaApertura = rs.getTimestamp("cc_fecha_apertura");
            if (ccFechaApertura != null) ciclo.setFechaApertura(ccFechaApertura);

            java.sql.Timestamp ccFechaCierre = rs.getTimestamp("cc_fecha_cierre");
            if (ccFechaCierre != null) ciclo.setFechaCierre(ccFechaCierre);

            String ccEstado = rs.getString("cc_estado_ciclo");
            if (ccEstado != null) {
                ciclo.setEstado(EstadoCicloCaja.valueOf(ccEstado));
            }

            ciclo.setSaldoInicial(rs.getDouble("cc_monto_saldo_inicial"));

            int ccaId = rs.getInt("cc_cca_id_fondo");
            if (!rs.wasNull() && ccaId > 0) {
                CajaChica cajaChica = getOrCreate(cache, CajaChica.class, ccaId, () -> new CajaChica());
                cajaChica.setIdFondo(ccaId);
                cajaChica.setNombre(rs.getString("cc_cca_nombre"));
                ciclo.setCajaChica(cajaChica);
            }

            transaccion.setCiclo(ciclo);
        } else {
            transaccion.setCiclo(null);
        }

        return transaccion;
    }

    private Moneda mapearMoneda(ResultSet rs, String idColumna, String prefijo,
                                Map<Class<?>, Map<Integer, Object>> cache) throws SQLException {
        int id = rs.getInt(idColumna);
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

    private Empleado mapearEmpleadoBasico(ResultSet rs, String idColumna, String prefijo,
                                          Map<Class<?>, Map<Integer, Object>> cache) throws SQLException {
        int id = rs.getInt(idColumna);
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

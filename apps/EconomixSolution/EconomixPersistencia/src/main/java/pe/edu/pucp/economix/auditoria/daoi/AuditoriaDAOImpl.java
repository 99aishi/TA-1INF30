package pe.edu.pucp.economix.auditoria.daoi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import pe.edu.pucp.economix.auditoria.idao.IAuditoriaDAO;
import pe.edu.pucp.economix.auditoria.model.AuditoriaLogDto;
import pe.edu.pucp.economix.config.DBManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuditoriaDAOImpl implements IAuditoriaDAO {
    private ResultSet rs;
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<AuditoriaLogDto> listarRecientes(int limite) throws SQLException {
        List<AuditoriaLogDto> logs = new ArrayList<>();
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_limite", limite);

        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_auditoria_recientes", parametrosEntrada);
        try {
            while (rs.next()) {
                logs.add(mapearAuditoriaLog(rs));
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar auditoria reciente: " + ex.getMessage());
        } finally {
            DBManager.getDBManager().cerrarConexion();
        }
        return logs;
    }

    private AuditoriaLogDto mapearAuditoriaLog(ResultSet rs) throws SQLException {
        AuditoriaLogDto dto = new AuditoriaLogDto();
        dto.setIdAuditoria(rs.getInt("id_auditoria"));
        dto.setNombreTabla(rs.getString("nombre_tabla"));
        dto.setTipoEvento(rs.getString("tipo_evento"));
        dto.setIdRegistroAfectado(rs.getString("id_registro_afectado"));
        dto.setValoresAntiguos(rs.getString("valores_antiguos"));
        dto.setValoresNuevos(rs.getString("valores_nuevos"));
        Timestamp ts = rs.getTimestamp("creado_at");
        if (ts != null) {
            dto.setCreadoAt(ts.toLocalDateTime());
        }
        dto.setIdUsuarioAuditoria(rs.getInt("id_usuario_auditoria"));
        dto.setUsuarioNombres(rs.getString("usuario_nombres"));
        dto.setUsuarioApellidoPaterno(rs.getString("usuario_apellido_paterno"));

        // Generar descripcion amigable para el usuario
        generarDescripcionUsuario(dto);

        return dto;
    }

    @Override
    public void registrarLogin(String correo, Integer idUsuario, boolean exitoso,
                               int intentosFallidos, boolean bloqueado) throws SQLException {
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_nombre_tabla", "rrhh_usuario");
        parametrosEntrada.put("p_tipo_evento", exitoso ? "LOGIN_SUCCESS" : "LOGIN_FAILED");
        parametrosEntrada.put("p_id_registro", correo);
        parametrosEntrada.put("p_valores_antiguos", null);

        String valoresNuevos = String.format(
            "{\"correo\":\"%s\",\"exitoso\":%s,\"intentos_fallidos\":%d,\"bloqueado\":%s}",
            correo,
            exitoso,
            intentosFallidos,
            bloqueado
        );
        parametrosEntrada.put("p_valores_nuevos", valoresNuevos);
        parametrosEntrada.put("p_id_usuario_accion", idUsuario);

        try {
            DBManager.getDBManager().ejecutarProcedimiento(
                "pa_insertar_auditoria", parametrosEntrada, null);
        } catch (SQLException ex) {
            System.out.println("Error al registrar auditoria de login: " + ex.getMessage());
            throw ex;
        }
    }

    private void generarDescripcionUsuario(AuditoriaLogDto dto) {
        String tabla = dto.getNombreTabla();
        String accion = dto.getTipoEvento();
        String valoresNuevos = dto.getValoresNuevos();
        String valoresAntiguos = dto.getValoresAntiguos();

        // Mapeo de nombres de tablas a entidades amigables
        Map<String, String> entidades = new HashMap<>();
        entidades.put("rrhh_empleado", "Empleado");
        entidades.put("rrhh_usuario", "Usuario");
        entidades.put("rrhh_area", "Área");
        entidades.put("rrhh_rol", "Rol");
        entidades.put("rrhh_administrador", "Administrador");
        entidades.put("tes_cuenta_bancaria", "Cuenta bancaria");
        entidades.put("tes_caja_chica", "Caja chica");
        entidades.put("tes_fondo", "Fondo");
        entidades.put("tes_moneda", "Moneda");
        entidades.put("tes_tipo_cambio", "Tipo de cambio");
        entidades.put("ope_ciclo_caja", "Ciclo de caja");
        entidades.put("ope_solicitud_gasto", "Solicitud de gasto");
        entidades.put("ope_comprobante_pago", "Comprobante de pago");
        entidades.put("ope_rendicion", "Rendición");
        entidades.put("ope_transaccion", "Transacción");

        String entidad = entidades.getOrDefault(tabla, tabla);
        dto.setEntidadNombre(entidad);

        // Mapeo de acciones
        Map<String, String> acciones = new HashMap<>();
        acciones.put("INSERT", "Creó");
        acciones.put("UPDATE", "Modificó");
        acciones.put("DELETE", "Eliminó");
        acciones.put("LOGIN_FAILED", "Intento de login fallido");
        acciones.put("LOGIN_SUCCESS", "Inicio de sesión exitoso");
        String accionVerb = acciones.getOrDefault(accion, accion);
        dto.setAccionNombre(accionVerb);

        // Eventos de login: descripcion personalizada
        if ("LOGIN_FAILED".equals(accion) || "LOGIN_SUCCESS".equals(accion)) {
            StringBuilder descripcionLogin = new StringBuilder();
            descripcionLogin.append(accionVerb).append(" para el usuario '").append(dto.getIdRegistroAfectado()).append("'");
            if ("LOGIN_FAILED".equals(accion)) {
                descripcionLogin.append(" (intentos fallidos: ").append(extraerIntentos(valoresNuevos)).append(")");
            }
            dto.setDescripcion(descripcionLogin.toString());
            return;
        }

        // Extraer nombre del registro afectado
        String nombreRegistro = extraerNombre(valoresNuevos, valoresAntiguos, tabla);

        StringBuilder descripcion = new StringBuilder();
        descripcion.append(accionVerb).append(" ").append(entidad.toLowerCase());
        if (nombreRegistro != null && !nombreRegistro.isEmpty()) {
            descripcion.append(" '").append(nombreRegistro).append("'");
        }

        // Detalles adicionales según la tabla
        if ("UPDATE".equals(accion) && valoresAntiguos != null && valoresNuevos != null) {
            String cambios = extraerCambiosClave(valoresAntiguos, valoresNuevos, tabla);
            if (cambios != null) {
                descripcion.append(" (").append(cambios).append(")");
            }
        }

        dto.setDescripcion(descripcion.toString());
    }

    private String extraerIntentos(String valoresNuevos) {
        try {
            if (valoresNuevos == null || valoresNuevos.isEmpty()) return "?";
            JsonNode node = mapper.readTree(valoresNuevos);
            return node.path("intentos_fallidos").asText("?");
        } catch (Exception e) {
            return "?";
        }
    }

    private String extraerNombre(String valoresNuevos, String valoresAntiguos, String tabla) {
        try {
            String json = valoresNuevos != null ? valoresNuevos : valoresAntiguos;
            if (json == null || json.isEmpty()) return null;

            JsonNode node = mapper.readTree(json);

            // Campos comunes según tabla
            switch (tabla) {
                case "rrhh_empleado":
                case "rrhh_usuario":
                case "rrhh_administrador":
                    return node.path("nombres").asText(null);
                case "rrhh_area":
                    return node.path("nombre").asText(null);
                case "rrhh_rol":
                    return node.path("titulo_rol").asText(null);
                case "tes_cuenta_bancaria":
                    return node.path("nombre_banco").asText(null);
                case "tes_caja_chica":
                case "tes_fondo":
                    return node.path("nombre").asText(null);
                case "tes_moneda":
                    return node.path("nombre_moneda").asText(null);
                case "ope_solicitud_gasto":
                    return "Solicitud #" + node.path("id_solicitud_gasto").asText("?");
                case "ope_comprobante_pago":
                    return "Comprobante #" + node.path("id_comprobante").asText("?");
                case "ope_rendicion":
                    return "Rendición #" + node.path("id_rendicion").asText("?");
                case "ope_transaccion":
                    return "Transacción #" + node.path("id_transaccion").asText("?");
                case "ope_ciclo_caja":
                    return "Ciclo #" + node.path("id_ciclo_caja").asText("?");
                default:
                    return node.path("nombre").asText(null);
            }
        } catch (Exception e) {
            return null;
        }
    }

    private String extraerCambiosClave(String valoresAntiguos, String valoresNuevos, String tabla) {
        try {
            JsonNode oldNode = mapper.readTree(valoresAntiguos);
            JsonNode newNode = mapper.readTree(valoresNuevos);

            List<String> cambios = new ArrayList<>();

            // Campos importantes a monitorear según tabla
            String[][] camposRelevantes = {
                {"estado", "Estado"},
                {"esta_activo", "Activo"},
                {"estaActivo", "Activo"},
                {"monto_techo", "Monto techo"},
                {"montoTecho", "Monto techo"},
                {"rol_flujo", "Rol de flujo"},
                {"rolFlujo", "Rol de flujo"},
                {"id_jefe", "Jefe"},
                {"id_area", "Área"},
                {"id_rol", "Rol"},
                {"estado_fondo", "Estado"},
                {"estado_solicitud", "Estado"},
                {"estado_rendicion", "Estado"},
                {"estado_ciclo", "Estado"},
                {"estado_comprobante", "Estado"},
                {"estado_transaccion", "Estado"}
            };

            for (String[] campo : camposRelevantes) {
                JsonNode oldVal = oldNode.path(campo[0]);
                JsonNode newVal = newNode.path(campo[0]);
                if (!oldVal.isMissingNode() && !newVal.isMissingNode() && !oldVal.equals(newVal)) {
                    cambios.add(campo[1] + ": " + oldVal.asText("-") + " → " + newVal.asText("-"));
                }
            }

            if (cambios.isEmpty()) return null;
            return String.join(", ", cambios);
        } catch (Exception e) {
            return null;
        }
    }
}

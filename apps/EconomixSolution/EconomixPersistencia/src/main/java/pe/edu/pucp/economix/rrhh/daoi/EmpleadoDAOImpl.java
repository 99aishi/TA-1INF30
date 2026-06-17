package pe.edu.pucp.economix.rrhh.daoi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.rrhh.idao.IEmpleadoDAO;
import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.rrhh.model.Empleado;
import pe.edu.pucp.economix.rrhh.model.EstadoUsuario;
import pe.edu.pucp.economix.rrhh.model.Rol;
import pe.edu.pucp.economix.rrhh.model.RolFlujo;

public class EmpleadoDAOImpl implements IEmpleadoDAO{
    private ResultSet rs;

    @Override
    public int insertar(Empleado empleado, int idUsuarioAccion) throws SQLException {
        Map<String,Object> parametrosSalida = new HashMap<>();
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosSalida.put("p_id_generado", Types.INTEGER);
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_nombres", empleado.getNombres());
        parametrosEntrada.put("p_apellido_paterno", empleado.getApellidoPaterno());
        parametrosEntrada.put("p_apellido_materno", empleado.getApellidoMaterno());
        parametrosEntrada.put("p_password_hash", empleado.getPassword());
        parametrosEntrada.put("p_correo", empleado.getCorreo());

        DBManager.getDBManager().ejecutarProcedimiento("pa_insertar_usuario", parametrosEntrada, parametrosSalida);
        empleado.setUsuarioID((int)parametrosSalida.get("p_id_generado"));

        parametrosSalida = new HashMap<>();
        parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_usuario", empleado.getUsuarioID());
        parametrosEntrada.put("p_numero_celular", empleado.getNumeroCelular());
        parametrosEntrada.put("p_id_area", empleado.getArea().getIdArea());
        parametrosEntrada.put("p_id_rol", empleado.getRol().getRolID());
        if(empleado.getJefeDirecto() != null)
            parametrosEntrada.put("p_id_jefe_directo", empleado.getJefeDirecto().getUsuarioID());
        else
            parametrosEntrada.put("p_id_jefe_directo", null);
        parametrosEntrada.put("p_rol_flujo", empleado.getRolFlujo() != null ? empleado.getRolFlujo().toString() : null);

        DBManager.getDBManager().ejecutarProcedimiento("pa_insertar_empleado", parametrosEntrada, parametrosSalida);

        return empleado.getUsuarioID();
    }

    @Override
    public int modificar(Empleado empleado, int idUsuarioAccion) throws SQLException {
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_usuario", empleado.getUsuarioID());
        parametrosEntrada.put("p_nombres", empleado.getNombres());
        parametrosEntrada.put("p_apellido_paterno", empleado.getApellidoPaterno());
        parametrosEntrada.put("p_apellido_materno", empleado.getApellidoMaterno());
        parametrosEntrada.put("p_password_hash", empleado.getPassword());
        parametrosEntrada.put("p_correo", empleado.getCorreo());
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_modificar_usuario", parametrosEntrada, null);

        parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_usuario", empleado.getUsuarioID());
        parametrosEntrada.put("p_numero_celular", empleado.getNumeroCelular());
        parametrosEntrada.put("p_id_area", empleado.getArea().getIdArea());
        parametrosEntrada.put("p_id_rol", empleado.getRol().getRolID());
        if(empleado.getJefeDirecto() != null)
            parametrosEntrada.put("p_id_jefe_directo", empleado.getJefeDirecto().getUsuarioID());
        else
            parametrosEntrada.put("p_id_jefe_directo", null);
        parametrosEntrada.put("p_rol_flujo", empleado.getRolFlujo() != null ? empleado.getRolFlujo().toString() : null);

        DBManager.getDBManager().ejecutarProcedimiento("pa_modificar_empleado", parametrosEntrada, null);
        return resultado;
    }
    @Override
    public int eliminar(int idEmpleado, int idUsuarioAccion) throws SQLException {
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario_accion", idUsuarioAccion);
        parametrosEntrada.put("p_id_usuario", idEmpleado);
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_eliminar_empleado", parametrosEntrada, null);
        return resultado;
    }

    @Override
    public Empleado buscarPorId(int idEmpleado) throws SQLException {
        Empleado empleado = null;
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario", idEmpleado);
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_buscar_empleado_por_id", parametrosEntrada);
        try{
            if(rs.next()){
                empleado = mapearEmpleado(rs, new HashMap<>());
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar empleado por id: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return empleado;
    }

    @Override
    public List<Empleado> listarActivas() throws SQLException {
        List<Empleado> empleados = null;
        Empleado empleado;
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_empleados", null);
        try{
            while(rs.next()){
                if(empleados == null) empleados = new ArrayList<>();
                empleado = mapearEmpleado(rs, new HashMap<>());
                if (empleado != null) empleados.add(empleado);
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar empleados: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return empleados;

    }
    @Override
    public List<Empleado> listarTodas() throws SQLException {
        List<Empleado> empleados = null;
        Empleado empleado;
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_todos_empleados", null);
        try{
            while(rs.next()){
                if(empleados == null) empleados = new ArrayList<>();
                empleado = mapearEmpleado(rs, new HashMap<>());
                if (empleado != null) empleados.add(empleado);
            }
        }catch(SQLException ex){
            System.out.println("Error al listar empleados: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return empleados;
    }
    @Override
    public int verificarCuenta(String correo, String password)  throws  SQLException{
        int resultado=0;
        Empleado empleado = null;
        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_correo", correo);
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_obtener_password_empleado_por_correo", parametrosEntrada);
        try{
            if(rs.next()){
                empleado = new Empleado();
                empleado.setPasswordHash(rs.getString("password_hash"));
                if(empleado.validarPassword(password))resultado=1;
            }
        }catch(SQLException ex){
            System.out.println("Error al verificar cuenta" + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return resultado;
    }

    @Override
    public List<Empleado> listarPorNombreApellido(String busqueda) throws SQLException {
        List<Empleado> empleados = null;
        Empleado empleado;

        Map<String, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_busqueda", busqueda);

        rs = DBManager.getDBManager().ejecutarProcedimientoLectura(
                "pa_buscar_empleados_por_nombre_apellido",
                parametrosEntrada
        );

        try {
            while (rs.next()) {
                if (empleados == null) empleados = new ArrayList<>();

                empleado = new Empleado();

                empleado.setNombres(rs.getString("nombres"));
                empleado.setApellidoPaterno(rs.getString("apellido_paterno"));
                empleado.setApellidoMaterno(rs.getString("apellido_materno"));
                empleado.setCorreo(rs.getString("correo"));

                empleado.setPasswordHash(rs.getString("password_hash"));

                empleados.add(empleado);
            }

        } catch (SQLException ex) {
            System.out.println("Error al buscar empleados: " + ex.getMessage());
        } finally {
            DBManager.getDBManager().cerrarConexion();
        }

        return empleados;
    }

    @SuppressWarnings("unchecked")
    private <T> T getOrCreate(Map<Class<?>, Map<Integer, Object>> cache, Class<T> type, int id, Supplier<T> factory) {
        if (id <= 0) return null;
        return (T) cache.computeIfAbsent(type, k -> new HashMap<>())
                .computeIfAbsent(id, k -> factory.get());
    }

    private Empleado mapearEmpleado(ResultSet rs, Map<Class<?>, Map<Integer, Object>> cache) throws SQLException {
        Empleado empleado = mapearEmpleadoBasico(rs, "", cache);
        if (empleado == null) return null;

        empleado.setPassword(rs.getString("password_hash"));
        String estadoStr = rs.getString("estado_usuario");
        empleado.setEstado(estadoStr != null ? EstadoUsuario.valueOf(estadoStr) : EstadoUsuario.ACTIVO);

        int idArea = rs.getInt("area_id_area");
        if (!rs.wasNull()) {
            Area area = getOrCreate(cache, Area.class, idArea, () -> new Area());
            area.setIdArea(idArea);
            area.setNombre(rs.getString("area_nombre"));
            area.setDescripcion(rs.getString("area_descripcion"));
            area.setEstaActivo(rs.getBoolean("area_esta_activo"));

            int idJefeArea = rs.getInt("area_id_jefe");
            if (!rs.wasNull() && idJefeArea == empleado.getUsuarioID()) {
                area.setJefe(empleado);
            }

            empleado.setArea(area);
        }

        int idRol = rs.getInt("rol_id_rol");
        if (!rs.wasNull()) {
            Rol rol = getOrCreate(cache, Rol.class, idRol, () -> new Rol());
            rol.setRolID(idRol);
            rol.setTitulo(rs.getString("rol_titulo"));
            rol.setDescripcion(rs.getString("rol_descripcion"));
            rol.setEstaActivo(rs.getBoolean("rol_esta_activo"));
            empleado.setRol(rol);
        }

        Empleado jefe = mapearEmpleadoBasico(rs, "jefe_", cache);
        if (jefe != null) {
            empleado.setJefeDirecto(jefe);
        }

        return empleado;
    }

    private Empleado mapearEmpleadoBasico(ResultSet rs, String prefijo, Map<Class<?>, Map<Integer, Object>> cache) throws SQLException {
        int id = rs.getInt(prefijo + "id_usuario");
        if (rs.wasNull() || id <= 0) return null;
        return getOrCreate(cache, Empleado.class, id, () -> {
            Empleado e = new Empleado();
            e.setUsuarioID(id);
            try {
                e.setNombres(rs.getString(prefijo + "nombres"));
                e.setApellidoPaterno(rs.getString(prefijo + "apellido_paterno"));
                e.setApellidoMaterno(rs.getString(prefijo + "apellido_materno"));
                e.setCorreo(rs.getString(prefijo + "correo"));
                e.setNumeroCelular(rs.getString(prefijo + "numero_celular"));
                String rolFlujo = rs.getString(prefijo + "rol_flujo");
                if (rolFlujo != null)
                    e.setRolFlujo(RolFlujo.valueOf(rolFlujo));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            return e;
        });
    }
}

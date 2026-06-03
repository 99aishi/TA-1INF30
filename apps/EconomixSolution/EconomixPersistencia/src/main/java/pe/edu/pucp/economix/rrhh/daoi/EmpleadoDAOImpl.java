package pe.edu.pucp.economix.rrhh.daoi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.rrhh.idao.IEmpleadoDAO;
import pe.edu.pucp.economix.rrhh.model.Area;
import pe.edu.pucp.economix.rrhh.model.Empleado;
import pe.edu.pucp.economix.rrhh.model.EstadoUsuario;
import pe.edu.pucp.economix.rrhh.model.Rol;

public class EmpleadoDAOImpl implements IEmpleadoDAO{
    private ResultSet rs;

    @Override
    public int insertar(Empleado empleado) throws SQLException {
        Map<String,Object> parametrosSalida = new HashMap<>();
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosSalida.put("p_id_generado", Types.INTEGER);
        parametrosEntrada.put("p_nombres", empleado.getNombres());
        parametrosEntrada.put("p_apellido_paterno", empleado.getApellidoPaterno());
        parametrosEntrada.put("p_apellido_materno", empleado.getApellidoMaterno());
        parametrosEntrada.put("p_password_hash", empleado.getPassword());

        DBManager.getDBManager().ejecutarProcedimiento("pa_insertar_usuario", parametrosEntrada, parametrosSalida);
        empleado.setUsuarioID((int)parametrosSalida.get("p_id_generado"));

        parametrosSalida = new HashMap<>();
        parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario", empleado.getUsuarioID());
        parametrosEntrada.put("p_correo_institucional", empleado.getCorreoInstitucional());
        parametrosEntrada.put("p_numero_celular", empleado.getNumeroCelular());
        parametrosEntrada.put("p_id_area", empleado.getArea().getIdArea());
        parametrosEntrada.put("p_id_rol", empleado.getRol().getRolID());
        if(empleado.getJefeDirecto() != null)
            parametrosEntrada.put("p_id_jefe_directo", empleado.getJefeDirecto().getUsuarioID());
        else
            parametrosEntrada.put("p_id_jefe_directo", null);

        DBManager.getDBManager().ejecutarProcedimiento("pa_insertar_empleado", parametrosEntrada, parametrosSalida);

        return empleado.getUsuarioID();
    }

    @Override
    public int modificar(Empleado empleado) throws SQLException {
        Map<String,Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario", empleado.getUsuarioID());
        parametrosEntrada.put("p_nombres", empleado.getNombres());
        parametrosEntrada.put("p_apellido_paterno", empleado.getApellidoPaterno());
        parametrosEntrada.put("p_apellido_materno", empleado.getApellidoMaterno());
        parametrosEntrada.put("p_password_hash", empleado.getPassword());
        int resultado = DBManager.getDBManager().ejecutarProcedimiento("pa_modificar_usuario", parametrosEntrada, null);
        parametrosEntrada = new HashMap<>();
        parametrosEntrada.put("p_id_usuario", empleado.getUsuarioID());
        parametrosEntrada.put("p_correo_institucional", empleado.getCorreoInstitucional());
        parametrosEntrada.put("p_numero_celular", empleado.getNumeroCelular());
        parametrosEntrada.put("p_id_area", empleado.getArea().getIdArea());
        parametrosEntrada.put("p_id_rol", empleado.getRol().getRolID());
        if(empleado.getJefeDirecto() != null)
            parametrosEntrada.put("p_id_jefe_directo", empleado.getJefeDirecto().getUsuarioID());
        else
            parametrosEntrada.put("p_id_jefe_directo", null);

        DBManager.getDBManager().ejecutarProcedimiento("pa_modificar_empleado", parametrosEntrada, null);
        return resultado;
    }
    @Override
    public int eliminar(int idEmpleado) throws SQLException {
        Map<String, Object> parametrosEntrada = new HashMap<>();
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
                empleado = new Empleado();
                empleado.setUsuarioID(rs.getInt("id_usuario"));
                empleado.setCorreoInstitucional(rs.getString("correo_institucional"));
                empleado.setNumeroCelular(rs.getString("numero_celular"));
                if(empleado.getArea() == null)
                    empleado.setArea(new Area());
                empleado.getArea().setIdArea(rs.getInt("id_area"));
                if(empleado.getRol() == null)
                    empleado.setRol(new Rol());
                empleado.getRol().setRolID(rs.getInt("id_rol"));
                if(empleado.getJefeDirecto() == null)
                    empleado.setJefeDirecto(new Empleado());
                empleado.getJefeDirecto().setUsuarioID(rs.getInt("id_jefe_directo"));
                empleado.setEstado(EstadoUsuario.Activo);
                empleado.setPassword(rs.getString("password_hash"));
                empleado.setNombres(rs.getString("nombres"));
                empleado.setApellidoPaterno(rs.getString("apellido_paterno"));
                empleado.setApellidoMaterno(rs.getString("apellido_materno"));
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar empleado por id: " + ex.getMessage());
        }finally{
            DBManager.getDBManager().cerrarConexion();
        }
        return empleado;
    }

    @Override
    public List<Empleado> listarTodas() throws SQLException {
        List<Empleado> empleados = null;
        Empleado empleado;
        rs = DBManager.getDBManager().ejecutarProcedimientoLectura("pa_listar_empleados", null);
        try{
            while(rs.next()){
                if(empleados == null) empleados = new ArrayList<>();
                empleado = new Empleado();
                empleado.setUsuarioID(rs.getInt("id_usuario"));
                empleado.setCorreoInstitucional(rs.getString("correo_institucional"));
                empleado.setNumeroCelular(rs.getString("numero_celular"));
                if(empleado.getArea() == null)
                    empleado.setArea(new Area());
                empleado.getArea().setIdArea(rs.getInt("id_area"));
                if(empleado.getRol() == null)
                    empleado.setRol(new Rol());
                empleado.getRol().setRolID(rs.getInt("id_rol"));
                if(empleado.getJefeDirecto() == null)
                    empleado.setJefeDirecto(new Empleado());
                empleado.getJefeDirecto().setUsuarioID(rs.getInt("id_jefe_directo"));
                empleado.setEstado(EstadoUsuario.Activo);
                empleado.setPassword(rs.getString("password_hash"));
                empleado.setNombres(rs.getString("nombres"));
                empleado.setApellidoPaterno(rs.getString("apellido_paterno"));
                empleado.setApellidoMaterno(rs.getString("apellido_materno"));
                empleados.add(empleado);
            }
        }catch(SQLException ex){
            System.out.println("Error al buscar empleados: " + ex.getMessage());
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
                empleado.setCorreoInstitucional(rs.getString("correo"));

                empleado.setPasswordHash(rs.getString("password"));

                empleados.add(empleado);
            }

        } catch (SQLException ex) {
            System.out.println("Error al buscar empleados: " + ex.getMessage());
        } finally {
            DBManager.getDBManager().cerrarConexion();
        }

        return empleados;
    }


}

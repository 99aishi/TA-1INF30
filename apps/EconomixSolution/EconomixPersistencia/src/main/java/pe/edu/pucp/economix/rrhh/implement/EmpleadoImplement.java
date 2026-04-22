package pe.edu.pucp.economix.rrhh.implement;

import pe.edu.pucp.economix.config.DBManager;
import pe.edu.pucp.economix.rrhh.dao.IEmpleadoDAO;
import pe.edu.pucp.economix.rrhh.model.Empleado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoImplement  implements IEmpleadoDAO{
    private Connection con;
    private ResultSet rs;
    private CallableStatement cs;

    @Override
    public int insertar(Empleado empleado){
        int id=0;
        try{
            con = DBManager.getDBManager().getConnection();
            //Insertamos al usuario
            cs= con.prepareCall("{call pa_insertar_usuario(?,?,?,?,?)}");

            cs.setString("p_nombres", empleado.getNombre());
            cs.setString("p_apellido_paterno", empleado.getApellidoPaterno());
            cs.setString("p_apellido_materno", empleado.getApellidoMaterno());
            cs.setString("p_password_hash", empleado.getPassword());
            cs.registerOutParameter("p_id_generado", java.sql.Types.INTEGER); // TODO Revisión INTEGER

            cs.executeUpdate();

            id = cs.getInt("p_id_generado");


            //COn el usuario generado, insertamos al empleado
            cs= con.prepareCall("{call pa_insertar_empleado(?,?,?,?,?,?)}");

            cs.setInt("p_id_usuario", id);
            cs.setString("p_correo_institucional", empleado.getCorreoInstitucional());
            cs.setString("p_numero_celular", empleado.getNumeroCelular());
            cs.setInt("p_id_area", empleado.getArea().getIdArea());
            cs.setInt("p_id_rol", empleado.getRol().getRolID());
            if (empleado.getJefeDirecto() != null) {
                cs.setInt("p_id_jefe_directo", empleado.getJefeDirecto().getUsuarioID());
            } else {
                cs.setNull("p_id_jefe_directo", java.sql.Types.INTEGER);
            }
            //cs.setInt("p_id_jefe_directo", empleado.getJefeDirecto().getUsuarioID());

            cs.executeUpdate();

            return id;
        }catch(Exception ex){
            System.out.println("ERROR: "+ ex.getMessage());
        }finally {
            try{
                cs.close();
            }catch (Exception ex){
                System.out.println("ERROR: "+ ex.getMessage());
            }
            try {
                con.close();
            }catch (Exception ex){
                System.out.println("ERROR: "+ ex.getMessage());
            }
        }

        return id;
    }

    @Override
    public int modificar(Empleado empleado){
        int cantidadPadre =0;
        int cantidadHijo =0;
        try{
            con = DBManager.getDBManager().getConnection();
            //Insertamos al usuario
            cs= con.prepareCall("{call pa_modificar_usuario(?,?,?,?,?)}");

            cs.setInt("p_id_usuario", empleado.getUsuarioID());
            cs.setString("p_nombres", empleado.getNombre());
            cs.setString("p_apellido_paterno", empleado.getApellidoPaterno());
            cs.setString("p_apellido_materno", empleado.getApellidoMaterno());
            cs.setString("p_password_hash", empleado.getPassword());

            cantidadPadre = cs.executeUpdate();

            //COn el usuario atualizado, actualizamos al hijo
            cs= con.prepareCall("{call pa_modificar_empleado(?,?,?,?,?,?)}");

            cs.setInt("p_id_usuario", empleado.getUsuarioID());
            cs.setString("p_correo_institucional", empleado.getCorreoInstitucional());
            cs.setString("p_numero_celular", empleado.getNumeroCelular());
            cs.setInt("p_id_area", empleado.getArea().getIdArea());
            cs.setInt("p_id_rol", empleado.getRol().getRolID());
            cs.setInt("p_id_jefe_directo", empleado.getJefeDirecto().getUsuarioID());

            cantidadHijo = cs.executeUpdate();

            return cantidadPadre + cantidadHijo;
        }catch(Exception ex){
            System.out.println("ERROR: "+ ex.getMessage());
        }finally {
            try{
                cs.close();
            }catch (Exception ex){
                System.out.println("ERROR: "+ ex.getMessage());
            }
            try {
                con.close();
            }catch (Exception ex){
                System.out.println("ERROR: "+ ex.getMessage());
            }
        }

        return cantidadPadre + cantidadHijo;
    }
    @Override
    public int eliminar(int idEmpleado){
        int cantidad=0;
        try{
            con = DBManager.getDBManager().getConnection();
            //Insertamos al usuario
            cs= con.prepareCall("{call pa_eliminar_empleado(?)}");

            cs.setInt("p_id_usuario", idEmpleado);
            cantidad = cs.executeUpdate();


        }catch(Exception ex){
            System.out.println("ERROR: "+ ex.getMessage());
        }finally {
            try{
                cs.close();
            }catch (Exception ex){
                System.out.println("ERROR: "+ ex.getMessage());
            }
            try {
                con.close();
            }catch (Exception ex){
                System.out.println("ERROR: "+ ex.getMessage());
            }
        }
        return cantidad;
    }

    @Override
    public Empleado buscarPorId(int idEmpleado){
        Empleado empleado = null;
        try{
            con = DBManager.getDBManager().getConnection();
            //Insertamos al usuario
            cs= con.prepareCall("{call pa_buscar_empleado_por_id(?)}");
            cs.setInt("p_id_usuario", idEmpleado);

            rs = cs.executeQuery();
            if(rs.next()){
                //Rescato los datose
                empleado = new Empleado();
                empleado.setUsuarioID(rs.getInt("id_usuario"));
                empleado.setCorreoInstitucional(rs.getString("correo_institucional"));
                empleado.setNumeroCelular(rs.getString("numero_celular"));
                //Para los fk, hacemos el llamado para obtener su valores desde sus tablas
                int idArea = rs.getInt("id_area");
                empleado.setArea(new AreaImplement().buscarPorId(idArea));

                int idRol = rs.getInt("id_rol");
                empleado.setRol(new RolImplement().buscarPorId(idRol));

                //Revisar relacion de recursividad al momento de obtener el jefe.
                int idJefeDirecto = rs.getInt("id_jefe_directo");
                Empleado jefe = new Empleado();
                jefe.setUsuarioID(idJefeDirecto);
                empleado.setJefeDirecto(jefe);

                //Consultamos al usuario que creamos recién
                ResultSet rsUsuario;
                CallableStatement csUsuario= con.prepareCall("{call pa_buscar_usuario_por_id(?)}");
                int id = empleado.getUsuarioID();
                csUsuario.setInt("p_id_usuario", id);
                rsUsuario = csUsuario.executeQuery();


                if(rsUsuario.next()){
                    empleado.setNombre(rsUsuario.getString("nombres"));
                    empleado.setApellidoPaterno(rsUsuario.getString("apellido_paterno"));
                    empleado.setApellidoMaterno(rsUsuario.getString("apellido_materno"));
                }

            }
        }catch(Exception ex){
            System.out.println("ERROR: "+ ex.getMessage());
        }finally {
            try{
                cs.close();
            }catch (Exception ex){
                System.out.println("ERROR: "+ ex.getMessage());
            }
            try {
                con.close();
            }catch (Exception ex){
                System.out.println("ERROR: "+ ex.getMessage());
            }
        }
        return empleado;
    }

    @Override
    public List<Empleado> listarTodas(){
        List<Empleado> empleados = null;
        try{
            con = DBManager.getDBManager().getConnection();
            //Insertamos al usuario
            cs = con.prepareCall("{call pa_listar_empleados()}");
            rs = cs.executeQuery();
            while(rs.next()){
                if(empleados==null)
                    empleados = new ArrayList<>();

                //Rescato los datos
                Empleado empleado = new Empleado();

                empleado.setUsuarioID(rs.getInt("id_usuario"));
                empleado.setCorreoInstitucional(rs.getString("correo_institucional"));
                empleado.setNumeroCelular(rs.getString("numero_celular"));
                //Para los fk, hacemos el llamado para obtener su valores desde sus tablas
                int idArea = rs.getInt("id_area");
                empleado.setArea(new AreaImplement().buscarPorId(idArea));
                int idRol = rs.getInt("id_rol");
                empleado.setRol(new RolImplement().buscarPorId(idRol));
                //Revisar relacion de recursividad al momento de obtener el jefe.
                int idJefeDirecto = rs.getInt("id_jefe_directo");
                Empleado jefe = new Empleado();
                jefe.setUsuarioID(idJefeDirecto);
                empleado.setJefeDirecto(jefe);

                //Consultamos al usuario que creamos recién
                ResultSet rsUsuario;
                CallableStatement csUsuario= con.prepareCall("{call pa_buscar_usuario_por_id(?)}");
                int id = empleado.getUsuarioID();
                csUsuario.setInt("p_id_usuario", id);
                rsUsuario = csUsuario.executeQuery();


                if(rsUsuario.next()){
                    empleado.setNombre(rsUsuario.getString("nombres"));
                    empleado.setApellidoPaterno(rsUsuario.getString("apellido_paterno"));
                    empleado.setApellidoMaterno(rsUsuario.getString("apellido_materno"));
                }
                empleados.add(empleado);
            }
        }catch(Exception ex){
            System.out.println("ERROR: "+ ex.getMessage());
        }finally {
            try{
                cs.close();
            }catch (Exception ex){
                System.out.println("ERROR: "+ ex.getMessage());
            }
            try {
                con.close();
            }catch (Exception ex){
                System.out.println("ERROR: "+ ex.getMessage());
            }
        }
        return empleados;

    }
}

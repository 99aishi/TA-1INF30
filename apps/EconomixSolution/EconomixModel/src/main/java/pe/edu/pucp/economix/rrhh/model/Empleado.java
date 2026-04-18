package pe.edu.pucp.economix.rrhh.model;

import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;

import java.util.ArrayList;
import java.util.List;

public class Empleado extends Usuario {
    private static int correlativoID = 1;
    private int empleadoID;
    private String correoInstitucional;
    private String numeroCelular;
    private Rol rol;
    private Area area;
    private List<CuentaBancaria> cuentas; //inicializado

    private List<SolicitudGasto> solicitudes; //inicializado

    //Constructror
    public Empleado(int usuarioID, String nombre, String apellido_paterno,
                    String apellido_materno, String password,EstadoUsuario estado,
                    String correoInstitucional, String numeroCelular){

        super(nombre,  apellido_paterno, apellido_materno,  password, estado);

        this.empleadoID=this.correlativoID++;
        this.correoInstitucional=correoInstitucional;
        this.numeroCelular=numeroCelular;
        this.cuentas= new ArrayList<>();
        this.solicitudes= new ArrayList<>();
    }

    //Selectores
    public void setRol(Rol rol){
        this.rol=rol;
    }
    public Rol getRol(){
        return new Rol(this.rol);
    }
    public int getEmpleadoID() {
        return empleadoID;
    }
    public String getCorreoInstitucional() {
        return correoInstitucional;
    }
    public String getNumeroCelular() {
        return numeroCelular;
    }
    public void setEmpleadoID(int empleadoID) {
        this.empleadoID = empleadoID;
    }
    public void setCorreoInstitucional(String correoInstitucional) {
        this.correoInstitucional = correoInstitucional;
    }
    public void setNumeroCelular(String numeroCelular) {
        this.numeroCelular = numeroCelular;
    }

    //Metodos
    // Métodos en Empleado.java
    public void registrarSolicitudFondo(String motivo, double monto, java.util.Date fecha) {
        // TODO: Crear solicitud con estado "pendiente" automáticamente (RF_06)
    }

    public void solicitarEfectivoCajaChica(double monto, String motivo) {
        // TODO: Validar disponibilidad de saldo en tiempo real < 500ms (RF_13, RNF_04)
    }

    public boolean puedeAprobarSolicitud(SolicitudGasto solicitud) {
        // TODO: Bloquear si el Jefe de Área intenta aprobar su propia solicitud (RF_15)
        return false;
    }

}
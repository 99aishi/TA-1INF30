package pe.edu.pucp.economix.rrhh.model;

import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;

import java.util.ArrayList;
import java.util.List;

public class Empleado extends Usuario {
    private String correoInstitucional;
    private String numeroCelular;
    private Rol rol;
    private Area area;
    private Empleado jefeDirecto;
    private List<SolicitudGasto> solicitudes; //inicializado
    private List<CuentaBancaria> cuentas; //inicializado

    //Constructror
    public Empleado(){

    }
    public Empleado(int usuarioID, String nombre, String apellido_paterno,
                    String apellido_materno, String password,EstadoUsuario estado,
                    String correoInstitucional, String numeroCelular){

        super(nombre,  apellido_paterno, apellido_materno,  password, estado);

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
    public String getCorreoInstitucional() {
        return correoInstitucional;
    }
    public String getNumeroCelular() {
        return numeroCelular;
    }
    public void setCorreoInstitucional(String correoInstitucional) {
        this.correoInstitucional = correoInstitucional;
    }
    public void setNumeroCelular(String numeroCelular) {
        this.numeroCelular = numeroCelular;
    }
    public Empleado getJefeDirecto() {
        return jefeDirecto;
    }
    public void setJefeDirecto(Empleado jefeDirecto) {
        this.jefeDirecto = jefeDirecto;
    }
    public Area getArea() {
        return area;
    }
    public void setArea(Area area) {
        this.area = area;
    }


    //Metodos
    @Override
    public String toString() {
        String cadena = "";
        cadena += super.toString(); // llama al toString de Usuario
        cadena += String.format(" - Correo: %s - Celular: %s - Rol: %s - Area: %s - Jefe: %s",
                correoInstitucional,
                numeroCelular,
                (rol != null ? rol.getTitulo() : "sin rol"),
                (area != null ? area.getNombre() : "sin area"),
                (jefeDirecto != null ? jefeDirecto.getNombre() + " " + jefeDirecto.getApellidoPaterno() : "sin jefe"));
        return cadena;
    }
    
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

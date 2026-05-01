package pe.edu.pucp.economix.rrhh.model;

import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;

import java.util.ArrayList;
import java.util.List;

public class Empleado extends Usuario {
    private String correoInstitucional;
    private String numeroCelular;

    //Relaciones
    private Rol rol;
    private Area area;
    private Empleado jefeDirecto;
    private List<SolicitudGasto> solicitudesRecibidas;
    private List<SolicitudGasto> solicitudesEnviadas;
    private List<CuentaBancaria> cuentas;

    //Constructror
    public Empleado(){

    }
    public Empleado(String nombre, String apellido_paterno,
                    String apellido_materno, String password, EstadoUsuario estado,
                    String correoInstitucional, String numeroCelular){

        super(nombre,  apellido_paterno, apellido_materno,  password, estado);

        this.correoInstitucional=correoInstitucional;
        this.numeroCelular=numeroCelular;
        this.cuentas= new ArrayList<>();
        this.solicitudesRecibidas= new ArrayList<>();
        this.solicitudesEnviadas= new ArrayList<>();
    }
    //Selectores
    public String getCorreoInstitucional() {
        return correoInstitucional;
    }
    public void setCorreoInstitucional(String correoInstitucional) {
        this.correoInstitucional = correoInstitucional;
    }
    public String getNumeroCelular() {
        return numeroCelular;
    }
    public void setNumeroCelular(String numeroCelular) {
        this.numeroCelular = numeroCelular;
    }
    public Rol getRol() {
        return rol;
    }
    public void setRol(Rol rol) {
        this.rol = rol;
    }
    public Area getArea() {
        return area;
    }
    public void setArea(Area area) {
        this.area = area;
    }
    public Empleado getJefeDirecto() {
        return jefeDirecto;
    }
    public void setJefeDirecto(Empleado jefeDirecto) {
        this.jefeDirecto = jefeDirecto;
    }
    public List<SolicitudGasto> getSolicitudesRecibidas() {
        return solicitudesRecibidas;
    }
    public void setSolicitudesRecibidas(List<SolicitudGasto> solicitudesRecibidas) {
        this.solicitudesRecibidas = solicitudesRecibidas;
    }
    public List<SolicitudGasto> getSolicitudesEnviadas() {
        return solicitudesEnviadas;
    }
    public void setSolicitudesEnviadas(List<SolicitudGasto> solicitudesEnviadas) {
        this.solicitudesEnviadas = solicitudesEnviadas;
    }
    public List<CuentaBancaria> getCuentas() {
        return cuentas;
    }
    public void setCuentas(List<CuentaBancaria> cuentas) {
        this.cuentas = cuentas;
    }

    //Metodos


    @Override
    public String toString() {
        return "Empleado{" +
                "correoInstitucional='" + correoInstitucional + '\'' +
                ", numeroCelular='" + numeroCelular + '\'' +
                ", rol=" + rol +
                ", area=" + area +
                ", jefeDirecto=" + jefeDirecto +
                ", solicitudesRecibidas=" + solicitudesRecibidas +
                ", solicitudesEnviadas=" + solicitudesEnviadas +
                ", cuentas=" + cuentas +
                '}';
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

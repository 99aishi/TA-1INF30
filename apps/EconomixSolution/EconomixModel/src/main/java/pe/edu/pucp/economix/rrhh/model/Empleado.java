package pe.edu.pucp.economix.rrhh.model;

import java.util.List;

import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;

public class Empleado extends Usuario {
    private String numeroCelular;

    private Rol rol;
    private Area area;
    private Empleado jefeDirecto;
    private List<SolicitudGasto> solicitudesRecibidas;
    private List<SolicitudGasto> solicitudesEnviadas;
    private List<CuentaBancaria> cuentas;

    public Empleado(){}

    public Empleado(int usuarioID, String nombres, String apellidoPaterno, String apellidoMaterno, 
                    String password, EstadoUsuario estado, String correo,
                    String numeroCelular, Rol rol, Area area, Empleado jefeDirecto, 
                    List<SolicitudGasto> solicitudesRecibidas, List<SolicitudGasto> solicitudesEnviadas, 
                    List<CuentaBancaria> cuentas) {
        super(usuarioID, nombres, apellidoPaterno, apellidoMaterno, password, estado, correo);
        this.numeroCelular = numeroCelular;
        this.rol = rol;
        this.area = area;
        this.jefeDirecto = jefeDirecto;
        this.solicitudesRecibidas = solicitudesRecibidas;
        this.solicitudesEnviadas = solicitudesEnviadas;
        this.cuentas = cuentas;
    }

    public Empleado(String nombres, String apellidoPaterno, String apellidoMaterno, 
                    String password, EstadoUsuario estado, String correo,
                    String numeroCelular, Rol rol, Area area, Empleado jefeDirecto, 
                    List<SolicitudGasto> solicitudesRecibidas, List<SolicitudGasto> solicitudesEnviadas, 
                    List<CuentaBancaria> cuentas) {
        super(nombres, apellidoPaterno, apellidoMaterno, password, estado, correo);
        this.numeroCelular = numeroCelular;
        this.rol = rol;
        this.area = area;
        this.jefeDirecto = jefeDirecto;
        this.solicitudesRecibidas = solicitudesRecibidas;
        this.solicitudesEnviadas = solicitudesEnviadas;
        this.cuentas = cuentas;
    }

    public Empleado(Empleado empleado){
        super(empleado);
        this.numeroCelular = empleado.numeroCelular;
        this.rol = empleado.rol;
        this.area = empleado.area;
        this.jefeDirecto = empleado.jefeDirecto;
        this.solicitudesRecibidas = empleado.solicitudesRecibidas;
        this.solicitudesEnviadas = empleado.solicitudesEnviadas;
        this.cuentas = empleado.cuentas;
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

    @Override
    public String toString() {
        return super.toString() + " Empleado{" +
                "numeroCelular='" + numeroCelular + '\'' +
                ", rol=" + rol +
                ", area=" + area +
                ", jefeDirecto=" + jefeDirecto +
                ", solicitudesRecibidas=" + solicitudesRecibidas +
                ", solicitudesEnviadas=" + solicitudesEnviadas +
                ", cuentas=" + cuentas +
                '}';
    }

    public List<SolicitudGasto> listarSolicitudesRecibidasPendientes(){
        return new java.util.ArrayList<>();
    }

    public List<SolicitudGasto> listarSolicitudesEnviadas(){
        return new java.util.ArrayList<>();
    }

    public void registrarSolicitudFondo(String motivo, double monto, java.util.Date fecha) {
    }

    public void solicitarEfectivoCajaChica(double monto, String motivo) {
    }

    public boolean puedeAprobarSolicitud(SolicitudGasto solicitud) {
        return false;
    }
}
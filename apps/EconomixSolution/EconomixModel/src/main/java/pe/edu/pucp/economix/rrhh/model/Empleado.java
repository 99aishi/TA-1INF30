package pe.edu.pucp.economix.rrhh.model;

import java.util.List;

import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoSolicitudGasto;
import pe.edu.pucp.economix.rrhh.model.RolFlujo;
import pe.edu.pucp.economix.tesoreria.model.CuentaBancaria;

public class Empleado extends Usuario {
    private String numeroCelular;
    private RolFlujo rolFlujo;

    private Rol rol;
    private Area area;
    private Empleado jefeDirecto;
    private List<SolicitudGasto> solicitudesRecibidas;
    private List<SolicitudGasto> solicitudesEnviadas;
    private List<CuentaBancaria> cuentas;

    public Empleado(){}

    public Empleado(int usuarioID, String nombres, String apellidoPaterno, String apellidoMaterno, 
                    String password, EstadoUsuario estado, String correo,
                    String numeroCelular, RolFlujo rolFlujo, Rol rol, Area area, Empleado jefeDirecto, 
                    List<SolicitudGasto> solicitudesRecibidas, List<SolicitudGasto> solicitudesEnviadas, 
                    List<CuentaBancaria> cuentas) {
        super(usuarioID, nombres, apellidoPaterno, apellidoMaterno, password, estado, correo);
        this.numeroCelular = numeroCelular;
        this.rolFlujo = rolFlujo;
        this.rol = rol;
        this.area = area;
        this.jefeDirecto = jefeDirecto;
        this.solicitudesRecibidas = solicitudesRecibidas;
        this.solicitudesEnviadas = solicitudesEnviadas;
        this.cuentas = cuentas;
    }

    public Empleado(String nombres, String apellidoPaterno, String apellidoMaterno, 
                    String password, EstadoUsuario estado, String correo,
                    String numeroCelular, RolFlujo rolFlujo, Rol rol, Area area, Empleado jefeDirecto, 
                    List<SolicitudGasto> solicitudesRecibidas, List<SolicitudGasto> solicitudesEnviadas, 
                    List<CuentaBancaria> cuentas) {
        super(nombres, apellidoPaterno, apellidoMaterno, password, estado, correo);
        this.numeroCelular = numeroCelular;
        this.rolFlujo = rolFlujo;
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
        this.rolFlujo = empleado.rolFlujo;
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
    public RolFlujo getRolFlujo() {
        return rolFlujo;
    }
    public void setRolFlujo(RolFlujo rolFlujo) {
        this.rolFlujo = rolFlujo;
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
                ", rolFlujo=" + rolFlujo +
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
        SolicitudGasto sg = new SolicitudGasto();
        sg.setMotivoSolicitud(motivo);
        sg.setMontoSolicitado(monto);
        sg.setFechaSolicitud(fecha);
        sg.setSolicitante(this);
        sg.setEstado(EstadoSolicitudGasto.PENDIENTE);
        if (this.solicitudesEnviadas == null)
            this.solicitudesEnviadas = new java.util.ArrayList<>();
        this.solicitudesEnviadas.add(sg);
    }

    public void solicitarEfectivoCajaChica(double monto, String motivo) {
        registrarSolicitudFondo(motivo, monto, new java.util.Date());
    }

    public boolean puedeAprobarSolicitud(SolicitudGasto solicitud) {
        if (solicitud == null) return false;
        if (rolFlujo == RolFlujo.JEFE_AREA &&
            solicitud.getSolicitante() != null &&
            solicitud.getSolicitante().getJefeDirecto() != null &&
            solicitud.getSolicitante().getJefeDirecto().getUsuarioID() == this.getUsuarioID()) {
            return true;
        }
        return false;
    }
}
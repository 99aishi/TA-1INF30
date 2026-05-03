package pe.edu.pucp.economix.operaciones.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Rendicion {
    private int idRendicion;
    private Date fechaPresentacion;
    private Date fechaAprobacion;
    private double totalDeclarado;
    private double totalAprobado;
    private double saldoFinal;
    private EstadoRendicion estado;
    private String comentario;
    //Relaciones
    CicloCajaChica cicloCajaChica;

    // Constructores
    public Rendicion(){}
    public Rendicion(int idRendicion, Date fechaPresentacion, Date fechaAprobacion,
                     double totalDeclarado, double totalAprobado, double saldoFinal,
                     EstadoRendicion estado, String comentario, CicloCajaChica cicloCajaChica) {
        this.idRendicion = idRendicion;
        this.fechaPresentacion = fechaPresentacion;
        this.fechaAprobacion = fechaAprobacion;
        this.totalDeclarado = totalDeclarado;
        this.totalAprobado = totalAprobado;
        this.saldoFinal = saldoFinal;
        this.estado = estado;
        this.comentario = comentario;
        this.cicloCajaChica = cicloCajaChica;
    }

    public Rendicion(Date fechaPresentacion, Date fechaAprobacion,
                     double totalDeclarado, double totalAprobado, double saldoFinal,
                     EstadoRendicion estado, String comentario, CicloCajaChica cicloCajaChica) {
        this.fechaPresentacion = fechaPresentacion;
        this.fechaAprobacion = fechaAprobacion;
        this.totalDeclarado = totalDeclarado;
        this.totalAprobado = totalAprobado;
        this.saldoFinal = saldoFinal;
        this.estado = estado;
        this.comentario = comentario;
        this.cicloCajaChica = cicloCajaChica;
    }

    //Selectores
    public int getIdRendicion() {
        return idRendicion;
    }
    public Date getFechaPresentacion() {
        return fechaPresentacion;
    }
    public Date getFechaAprobacion() {
        return fechaAprobacion;
    }
    public double getTotalDeclarado() {
        return totalDeclarado;
    }
    public double getTotalAprobado() {
        return totalAprobado;
    }
    public double getSaldoFinal() {
        return saldoFinal;
    }
    public EstadoRendicion getEstado() {
        return estado;
    }
    public String getComentario() {
        return comentario;
    }
    public void setIdRendicion(int idRendicion) {
        this.idRendicion = idRendicion;
    }
    public void setFechaPresentacion(Date fechaPresentacion) {
        this.fechaPresentacion = fechaPresentacion;
    }
    public void setFechaAprobacion(Date fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }
    public void setTotalDeclarado(double totalDeclarado) {
        this.totalDeclarado = totalDeclarado;
    }
    public void setTotalAprobado(double totalAprobado) {
        this.totalAprobado = totalAprobado;
    }
    public void setSaldoFinal(double saldoFinal) {
        this.saldoFinal = saldoFinal;
    }
    public void setEstado(EstadoRendicion estado) {
        this.estado = estado;
    }
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
    public CicloCajaChica getCicloCajaChica() {
        return cicloCajaChica;
    }
    public void setCicloCajaChica(CicloCajaChica cicloCajaChica) {
        this.cicloCajaChica = cicloCajaChica;
    }

    //Metodos

    @Override
    public String toString() {
        return "Rendicion{" +
                "idRendicion=" + idRendicion +
                ", fechaPresentacion=" + fechaPresentacion +
                ", fechaAprobacion=" + fechaAprobacion +
                ", totalDeclarado=" + totalDeclarado +
                ", totalAprobado=" + totalAprobado +
                ", saldoFinal=" + saldoFinal +
                ", estado=" + estado +
                ", comentario='" + comentario + '\'' +
                ", cicloCajaChica=" + cicloCajaChica +
                '}';
    }

    public void cargarComprobanteDigital(ComprobantePago comprobante) {
        // TODO: Aplicar validaciones automáticas de coherencia (RF_09)
    }

    public void formalizarSaldoFinal(double montoReal) {
        // TODO: Calcular reembolso o devolución para cierre (RF_10)
    }

    public List<Rendicion> consultarAuditoria(Date inicio, Date fin) {
        // TODO: Retornar detalle consolidado para Gerencia de Finanzas (RF_17)
        return new ArrayList<>();
    }
}

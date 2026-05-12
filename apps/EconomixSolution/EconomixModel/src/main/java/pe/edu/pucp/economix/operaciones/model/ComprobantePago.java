package pe.edu.pucp.economix.operaciones.model;

import java.util.Date;

import pe.edu.pucp.economix.tesoreria.model.Moneda;

public class ComprobantePago {
    private int idComprobante;
    private TipoComprobante tipoDocumento;
    private String RUCProveedor;
    private String razonSocial;
    private String numeroSerial;
    private Date fechaEmision;
    private double montoTotal;
    private double subtotal;
    private double igv;
    private double total;
    private EstadoComprobante estado;
    //Relaciones
    private SolicitudGasto solicitud;
    private Moneda moneda;

    // Constructor
    public ComprobantePago(){}
    public ComprobantePago(int idComprobante, TipoComprobante tipoDocumento, String RUCProveedor, String razonSocial, String numeroSerial,
                           Date fechaEmision, double montoTotal, double subtotal, double igv, double total, EstadoComprobante estado,
                           SolicitudGasto solicitud, Moneda moneda) {
        this.idComprobante = idComprobante;
        this.tipoDocumento = tipoDocumento;
        this.RUCProveedor = RUCProveedor;
        this.razonSocial = razonSocial;
        this.numeroSerial = numeroSerial;
        this.fechaEmision = fechaEmision;
        this.montoTotal = montoTotal;
        this.subtotal = subtotal;
        this.igv = igv;
        this.total = total;
        this.estado = estado;
        this.solicitud = solicitud;
        this.moneda = moneda;
    }

    public ComprobantePago(TipoComprobante tipoDocumento, String RUCProveedor, String razonSocial,
                           String numeroSerial, Date fechaEmision, double montoTotal, double subtotal,
                           double igv, double total, EstadoComprobante estado, SolicitudGasto solicitud, Moneda moneda) {
        this.tipoDocumento = tipoDocumento;
        this.RUCProveedor = RUCProveedor;
        this.razonSocial = razonSocial;
        this.numeroSerial = numeroSerial;
        this.fechaEmision = fechaEmision;
        this.montoTotal = montoTotal;
        this.subtotal = subtotal;
        this.igv = igv;
        this.total = total;
        this.estado = estado;
        this.solicitud = solicitud;
        this.moneda = moneda;
    }

    // Selectores
    public int getIdComprobante() {
        return idComprobante;
    }
    public TipoComprobante getTipoDocumento() {
        return tipoDocumento;
    }
    public String getRUCProveedor() {
        return RUCProveedor;
    }
    public String getRazonSocial() {
        return razonSocial;
    }
    public String getNumeroSerial() {
        return numeroSerial;
    }
    public Date getFechaEmision() {
        return fechaEmision;
    }
    public double getMontoTotal() {
        return montoTotal;
    }
    public double getSubtotal() {
        return subtotal;
    }
    public double getIgv() {
        return igv;
    }
    public double getTotal() {
        return total;
    }
    public void setIdComprobante(int idComprobante) {
        this.idComprobante = idComprobante;
    }
    public void setTipoDocumento(TipoComprobante tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
    public void setRUCProveedor(String RUCProveedor) {
        this.RUCProveedor = RUCProveedor;
    }
    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }
    public void setNumeroSerial(String numeroSerial) {
        this.numeroSerial = numeroSerial;
    }
    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }
    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    public void setIgv(double igv) {
        this.igv = igv;
    }
    public void setTotal(double total) {
        this.total = total;
    }
    public SolicitudGasto getSolicitud() {
        return solicitud;
    }
    public void setSolicitud(SolicitudGasto solicitud) {
        this.solicitud = solicitud;
    }
    public Moneda getMoneda() {
        return moneda;
    }
    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }
    public EstadoComprobante getEstado() {
        return estado;
    }
    public void setEstado(EstadoComprobante estado) {
        this.estado = estado;
    }


    //Metodos
    @Override
    public String toString() {
        return "ComprobantePago{" +
                "idComprobante=" + idComprobante +
                ", tipoDocumento=" + tipoDocumento +
                ", RUCProveedor='" + RUCProveedor + '\'' +
                ", razonSocial='" + razonSocial + '\'' +
                ", numeroSerial='" + numeroSerial + '\'' +
                ", fechaEmision=" + fechaEmision +
                ", montoTotal=" + montoTotal +
                ", subtotal=" + subtotal +
                ", igv=" + igv +
                ", total=" + total +
                ", solicitud=" + solicitud +
                ", moneda=" + moneda +
                '}';
    }
    public boolean validarConsistenciaMontos() {
        // TODO: Verificar que subtotal + igv == total (RF_09)
        return false;
    }

    public boolean esFechaValida() {
        // TODO: Validar que la fecha de emisión no sea futura ni exceda límites (RF_09)
        return false;
    }
}

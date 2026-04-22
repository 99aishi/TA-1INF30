package pe.edu.pucp.economix.operaciones.model;

import pe.edu.pucp.economix.tesoreria.model.Fondo;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

import java.util.Date;

public class ComprobantePago {
    private static int correlativoID = 1;
    private int idComprobante;
    private TipoComprobante tipoDocumento;
    private String ruc;
    private String razonSocial;
    private String numeroSerial;
    private Date fechaEmision;
    private double montoTotal;
    private double subtotal;
    private double igv;
    private double total;
    public Fondo fondoEntrega;

    private SolicitudGasto solicitud; // get set y copia
    private Moneda moneda;

    // Constructor
    public ComprobantePago(){}
    public ComprobantePago(TipoComprobante tipoDocumento, String ruc,
                           String razonSocial, String numeroSerial, Date fechaEmision,
                           double montoTotal, double subtotal, double igv, double total) {
        this.idComprobante = this.correlativoID++;
        this.tipoDocumento = tipoDocumento;
        this.ruc = ruc;
        this.razonSocial = razonSocial;
        this.numeroSerial = numeroSerial;
        this.fechaEmision = fechaEmision;
        this.montoTotal = montoTotal;
        this.subtotal = subtotal;
        this.igv = igv;
        this.total = total;
    }

    // Selectores
    public int getIdComprobante() {
        return idComprobante;
    }
    public TipoComprobante getTipoDocumento() {
        return tipoDocumento;
    }
    public String getRuc() {
        return ruc;
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
    public void setRuc(String ruc) {
        this.ruc=ruc;
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
    public Fondo getFondoEntrega() {
        return fondoEntrega;
    }
    public void setFondoEntrega(Fondo fondoEntrega) {
        this.fondoEntrega = fondoEntrega;
    }

    @Override
    public String toString() {
        String cadena = "";
        cadena += String.format("COMPROBANTE: %d - %s - RUC: %s - Razon Social: %s - Nro Serie: %s - Emision: %s - Subtotal: %.2f - IGV: %.2f - Total: %.2f",
                idComprobante, tipoDocumento, ruc, razonSocial,
                numeroSerial, fechaEmision, subtotal, igv, total);
        return cadena;
    }
    
    //Metodos
    public boolean validarConsistenciaMontos() {
        // TODO: Verificar que subtotal + igv == total (RF_09)
        return false;
    }

    public boolean esFechaValida() {
        // TODO: Validar que la fecha de emisión no sea futura ni exceda límites (RF_09)
        return false;
    }
}

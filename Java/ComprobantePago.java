import java.util.Date;

class ComprobantePago {
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

    private SolicitudGasto solicitud; // get set y copia

    // Constructor
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
}

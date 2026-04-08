import java.util.Date;

class Transaccion{
	private int idTransaccion;
	private TipoTransaccion tipoTransaccion;
	private Date fecha;
	private double monto;
	private String numeroOperacionBancaria;
	private MedioPago medioPago;
	private double tipoCambio;

    private CuentaBancaria cuentaOrigen;
    private CuentaBancaria cuentaDestino;
    private Moneda moneda;

	public Transaccion(int idTransaccion, TipoTransaccion tipo,Date fecha,double monto, 
		String numeroOperacionBancaria, MedioPago medioPago, double tipoCambio){
		this.idTransaccion=idTransaccion;
		this.tipoTransaccion=tipoTransaccion;
		this.fecha=fecha;
		this.monto=monto;
		this.numeroOperacionBancaria=numeroOperacionBancaria;
		this.medioPago=medioPago;
		this.tipoCambio=tipoCambio;	
	}

	// Getters
    public int getIdTransaccion() {
        return idTransaccion;
    }

    public TipoTransaccion getTipoTransaccion() {
        return tipoTransaccion;
    }

    public Date getFecha() {
        return fecha;
    }

    public double getMonto() {
        return monto;
    }

    public String getNumeroOperacionBancaria() {
        return numeroOperacionBancaria;
    }

    public MedioPago getMedioPago() {
        return medioPago;
    }

    public double getTipoCambio() {
        return tipoCambio;
    }

    // Setters
    public void setIdTransaccion(int idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public void setTipoTransaccion(TipoTransaccion tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public void setNumeroOperacionBancaria(String numeroOperacionBancaria) {
        this.numeroOperacionBancaria = numeroOperacionBancaria;
    }

    public void setMedioPago(MedioPago medioPago) {
        this.medioPago = medioPago;
    }

    public void setTipoCambio(double tipoCambio) {
        this.tipoCambio = tipoCambio;
    }


}
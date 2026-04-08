import java.util.ArrayList;
import java.util.Date;
import java.util.List;


class SolicitudGasto{
	private static int correlativoID = 1;
	private int idSolicitudGasto;
	private Date fechaSolicitud;
	private double montoSolicitado;
	private String motivoSolicitud;
	private EstadoSolicitudGasto estado;
	private Empleado solicitante;
	private Empleado destinatario;
	private List<ComprobantePago> comprobantes;
	private CicloCajaChica ciclo; 

	//Constructores
	public SolicitudGasto(Date fechaSolicitud, double montoSolicitado, String motivoSolicitud, 
							EstadoSolicitudGasto estado, 
							Empleado solicitante, Empleado destinatario, CicloCajaChica cicloCaja){
	    this.idSolicitudGasto=this.correlativoID++;
		this.fechaSolicitud=fechaSolicitud;
		this.montoSolicitado=montoSolicitado;
		this.motivoSolicitud=motivoSolicitud;
		this.estado=estado;
		this.solicitante = solicitante;
		this.destinatario = destinatario;
		this.ciclo = cicloCaja;
		this.comprobantes=new ArrayList<>();
	}
	
	//Selectores
	public int getIdSolicitudGasto() {
		return idSolicitudGasto;
    }
	public void setIdSolicitudGasto(int idSolicitudGasto) {
		this.idSolicitudGasto = idSolicitudGasto;
	}
	public Date getFechaSolicitud() {
		return fechaSolicitud;
	}
	public void setFechaSolicitud(Date fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}
	public double getMontoSolicitado() {
		return montoSolicitado;
	}
	public void setMontoSolicitado(double montoSolicitado) {
		this.montoSolicitado = montoSolicitado;
	}
	public String getMotivoSolicitud() {
		return motivoSolicitud;
	}
	public void setMotivoSolicitud(String motivoSolicitud) {
		this.motivoSolicitud = motivoSolicitud;
	}
	public EstadoSolicitudGasto getEstado() {
		return estado;
	}
	public void setEstado(EstadoSolicitudGasto estado) {
		this.estado = estado;
	}

	//Metodos
	public void evaluarSolicitud(Empleado jefe, boolean aprobado, String comentario) {
		// TODO: Registro obligatorio de fecha y sustento (RF_07)
	}

	public void registrarDesembolso(String nroOperacion, CuentaBancaria destino) {
		// TODO: Cambiar estado a "desembolsado" y vincular transacción (RF_08)
	}
	
}
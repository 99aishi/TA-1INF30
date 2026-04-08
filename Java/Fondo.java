import java.util.Date;

class Fondo {
    private static int correlativoID = 1;
    private int idFondo;
    private String nombre;
    private double saldoActual;
    private EstadoFondo estado;
    private Date fechaCreacion;
    private CuentaBancaria cuentaBancaria; //crear set y get
    private Moneda moneda; // crear set y get
    
    // Constructores
    public Fondo(String nombre, double saldoActual, EstadoFondo estado, Date fechaCreacion) {
        this.idFondo = this.correlativoID++;
        this.nombre = nombre;
        this.saldoActual = saldoActual;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    // Selectores
    public int getIdFondo() {
        return idFondo;
    }
    public String getNombre() {
        return nombre;
    }
    public double getSaldoActual() {
        return saldoActual;
    }
    public EstadoFondo getEstado() {
        return estado;
    }
    public Date getFechaCreacion() {
        return fechaCreacion;
    }
    public CuentaBancaria getCuentaBancaria(){
        return this.cuentaBancaria;
    }
    public Moneda getMoneda(){
        return this.moneda;
    }
    public void setIdFondo(int idFondo) {
        this.idFondo = idFondo;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setSaldoActual(double saldoActual) {
        this.saldoActual = saldoActual;
    }
    public void setEstado(EstadoFondo estado) {
        this.estado = estado;
    }
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    public void setCuentaBancaria(CuentaBancaria cuenta){
        this.cuentaBancaria = cuenta;
    }
    public void setMoneda(Moneda moneda){
        this.moneda = moneda;
    }

    //Metodos
    public void actualizarSaldo(double monto, TipoTransaccion tipo) {
        // TODO: Lógica para sumar (Reposición) o restar (Desembolso) al saldoActual
    }

    public boolean validarDisponibilidad(double montoRequerido) {
        // TODO: Verificar si el saldoActual es suficiente antes de proceder
        return false;
    }

    public void vincularCuentaBancaria(CuentaBancaria cuenta) {
        // TODO: Asignar la cuenta de origen del fondo (RF_11)
    }
}

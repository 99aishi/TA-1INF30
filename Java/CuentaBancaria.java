class CuentaBancaria {
    private static int correlativoID = 1;
    private int idCuenta;
    private String nombreBanco;
    private String numeroBancario;
    private String cci;
    //private Empleado duenho; //set y get , c copia


    //Constructores
    public CuentaBancaria(String nombreBanco, String numeroBancario, String cci) {
        this.idCuenta = this.correlativoID++;
        this.nombreBanco = nombreBanco;
        this.numeroBancario = numeroBancario;
        this.cci = cci;
        
    }
    //Selectores
    public int getIdCuenta() {
        return idCuenta;
    }
    public String getNombreBanco() {
        return nombreBanco;
    }
    public String getNumeroBancario() {
        return numeroBancario;
    }
    public String getCci() {
        return cci;
    }
    public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }
    public void setNombreBanco(String nombreBanco) {
        this.nombreBanco = nombreBanco;
    }
    public void setNumeroBancario(String numeroBancario) {
        this.numeroBancario = numeroBancario;
    }
    public void setCci(String cci) {
        this.cci = cci;
    }

    //Metodos
    
}

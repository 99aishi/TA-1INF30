class CuentaBancaria {
    private int idCuenta;
    private String nombreBanco;
    private String numeroBancario;
    private String cci;
    private Empleado duenho; //set y get

    private Fondo fondo;
    

    // Constructor vacío
    public CuentaBancaria() {
    }

    // Constructor con parámetros
    public CuentaBancaria(int idCuenta, String nombreBanco, String numeroBancario, String cci) {
        this.idCuenta = idCuenta;
        this.nombreBanco = nombreBanco;
        this.numeroBancario = numeroBancario;
        this.cci = cci;
        
    }

    //Constructor copia
    public CuentaBancaria(CuentaBancaria cb){
        this.idCuenta=cb.CuentaBancaria;
        this.nombreBanco=cb.nombreBanco;
        this.numeroBancario=cb.numeroBancario;
        this.cci=cb.cci;
        this.duenho= new Empleado(cb.duenho);
    }




    //this.duenho=new Empleado(duenho);

    // Getters
    public Empleado getDueno(){
        Empleado duenho= new Empleado(this.duenho);
        return duenho;
    }
    public void setDueno(Empleado duenho){
        this.duenho=new Empleado(duenho);
    }


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

    // Setters
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
}

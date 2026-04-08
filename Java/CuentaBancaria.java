class CuentaBancaria {
    private int idCuenta;
    private String nombreBanco;
    private String numeroBancario;
    private String cci;

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

    // Getters
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

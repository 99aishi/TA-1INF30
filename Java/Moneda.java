class Moneda{
	private int idMoneda;
	private String codigoISO;
	private String simbolo;
	
	public Moneda(int idMoneda, String codigoISO, String simbolo) {
    this.idMoneda = idMoneda;
    this.codigoISO = codigoISO;
    this.simbolo = simbolo;
	}

	public int getIdMoneda() {
		return idMoneda;
	}

	public void setIdMoneda(int idMoneda) {
		this.idMoneda = idMoneda;
	}

	public String getCodigoISO() {
		return codigoISO;
	}

	public void setCodigoISO(String codigoISO) {
		this.codigoISO = codigoISO;
	}

	public String getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}
}
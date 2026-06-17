package pe.edu.pucp.economix.operaciones.model.enums;

public enum EstadoRendicion {
    ACEPTADO,
    EN_ESPERA,
    DENEGADO,
    ANULADO;

    public boolean puedeTransicionarA(EstadoRendicion nuevo) {
        switch (this) {
            case EN_ESPERA:
                return nuevo == ACEPTADO || nuevo == DENEGADO || nuevo == ANULADO;
            case DENEGADO:
                return nuevo == EN_ESPERA;
            case ACEPTADO:
                return false;
            case ANULADO:
                return false;
            default:
                return false;
        }
    }
}

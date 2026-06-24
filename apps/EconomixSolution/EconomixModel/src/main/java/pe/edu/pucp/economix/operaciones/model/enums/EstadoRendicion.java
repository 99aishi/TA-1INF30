package pe.edu.pucp.economix.operaciones.model.enums;

public enum EstadoRendicion {
    ACEPTADO,
    EN_ESPERA,
    DENEGADO,
    OBSERVADO,
    ANULADO;

    public boolean puedeTransicionarA(EstadoRendicion nuevo) {
        switch (this) {
            case EN_ESPERA:
                return nuevo == ACEPTADO || nuevo == DENEGADO || nuevo == OBSERVADO || nuevo == ANULADO;
            case OBSERVADO:
                return nuevo == EN_ESPERA || nuevo == ACEPTADO || nuevo == DENEGADO || nuevo == ANULADO;
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

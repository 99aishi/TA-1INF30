package pe.edu.pucp.economix.operaciones.model.enums;

public enum EstadoComprobante {
    POR_REVISAR,
    ANULADO,
    APROBADO,
    OBSERVADO;

    public boolean puedeTransicionarA(EstadoComprobante nuevo) {
        switch (this) {
            case POR_REVISAR:
                return nuevo == APROBADO || nuevo == OBSERVADO || nuevo == ANULADO;
            case OBSERVADO:
                return nuevo == POR_REVISAR || nuevo == ANULADO;
            case APROBADO:
                return nuevo == ANULADO;
            case ANULADO:
                return false;
            default:
                return false;
        }
    }
}
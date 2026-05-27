package pe.edu.pucp.economix.operaciones.model.enums;

public enum EstadoComprobante {
    PorRevisar, Anulado, Aprobado, Observado;

    public boolean puedeTransicionarA(EstadoComprobante nuevo) {
        switch (this) {
            case PorRevisar:
                // Desde revisión puede ir a cualquier otro estado
                return nuevo == Aprobado || nuevo == Observado || nuevo == Anulado;
            case Observado:
                // Si fue observado, debe volver a revisión o anularse
                return nuevo == PorRevisar || nuevo == Anulado;
            case Aprobado:
                // Un comprobante aprobado normalmente ya no debería cambiar,
                // a menos que el negocio permita anularlo.
                return nuevo == Anulado;
            case Anulado:
                // Estado final, no debería cambiar a nada más
                return false;
            default:
                return false;
        }
    }
}
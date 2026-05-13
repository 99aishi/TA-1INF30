package pe.edu.pucp.economix.operaciones.model;

public enum EstadoRendicion {
    Aceptado, EnEspera, Denegado;

    public boolean puedeTransicionarA(EstadoRendicion nuevo) {
        switch (this) {
            case EnEspera:
                // Una rendición pendiente puede ser aprobada o rechazada
                return nuevo == Aceptado || nuevo == Denegado;
            case Denegado:
                // Si fue denegada, quizás se permita volver a ponerla "EnEspera" tras correcciones
                return nuevo == EnEspera;
            case Aceptado:
                // Una vez aceptada, suele ser un estado final (no debería cambiar)
                return false;
            default:
                return false;
        }
    }
}
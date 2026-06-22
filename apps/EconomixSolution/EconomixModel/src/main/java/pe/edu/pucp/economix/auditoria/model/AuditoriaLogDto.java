package pe.edu.pucp.economix.auditoria.model;

import java.time.LocalDateTime;

public class AuditoriaLogDto {
    private int idAuditoria;
    private String nombreTabla;
    private String tipoEvento;
    private String idRegistroAfectado;
    private String valoresAntiguos;
    private String valoresNuevos;
    private LocalDateTime creadoAt;
    private int idUsuarioAuditoria;
    private String usuarioNombres;
    private String usuarioApellidoPaterno;

    // Descripcion generada para usuario final
    private String descripcion;
    private String entidadNombre;
    private String accionNombre;

    public AuditoriaLogDto() {}

    public int getIdAuditoria() { return idAuditoria; }
    public void setIdAuditoria(int idAuditoria) { this.idAuditoria = idAuditoria; }

    public String getNombreTabla() { return nombreTabla; }
    public void setNombreTabla(String nombreTabla) { this.nombreTabla = nombreTabla; }

    public String getTipoEvento() { return tipoEvento; }
    public void setTipoEvento(String tipoEvento) { this.tipoEvento = tipoEvento; }

    public String getIdRegistroAfectado() { return idRegistroAfectado; }
    public void setIdRegistroAfectado(String idRegistroAfectado) { this.idRegistroAfectado = idRegistroAfectado; }

    public String getValoresAntiguos() { return valoresAntiguos; }
    public void setValoresAntiguos(String valoresAntiguos) { this.valoresAntiguos = valoresAntiguos; }

    public String getValoresNuevos() { return valoresNuevos; }
    public void setValoresNuevos(String valoresNuevos) { this.valoresNuevos = valoresNuevos; }

    public LocalDateTime getCreadoAt() { return creadoAt; }
    public void setCreadoAt(LocalDateTime creadoAt) { this.creadoAt = creadoAt; }

    public int getIdUsuarioAuditoria() { return idUsuarioAuditoria; }
    public void setIdUsuarioAuditoria(int idUsuarioAuditoria) { this.idUsuarioAuditoria = idUsuarioAuditoria; }

    public String getUsuarioNombres() { return usuarioNombres; }
    public void setUsuarioNombres(String usuarioNombres) { this.usuarioNombres = usuarioNombres; }

    public String getUsuarioApellidoPaterno() { return usuarioApellidoPaterno; }
    public void setUsuarioApellidoPaterno(String usuarioApellidoPaterno) { this.usuarioApellidoPaterno = usuarioApellidoPaterno; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEntidadNombre() { return entidadNombre; }
    public void setEntidadNombre(String entidadNombre) { this.entidadNombre = entidadNombre; }

    public String getAccionNombre() { return accionNombre; }
    public void setAccionNombre(String accionNombre) { this.accionNombre = accionNombre; }

    public String getNombreCompletoUsuario() {
        if (usuarioNombres == null) return "Sistema";
        return (usuarioNombres + " " + (usuarioApellidoPaterno != null ? usuarioApellidoPaterno : "")).trim();
    }
}

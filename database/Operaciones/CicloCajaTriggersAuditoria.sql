
DELIMITER //

-- ===============================================================================
-- 1. TABLA: ope_ciclo_caja
-- ===============================================================================
-- ===============================================================================

DROP TRIGGER IF EXISTS trg_ope_ciclo_caja_before_insert //
CREATE TRIGGER trg_ope_ciclo_caja_before_insert
BEFORE INSERT ON ope_ciclo_caja
FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
END //

DROP TRIGGER IF EXISTS trg_ope_ciclo_caja_before_update //
CREATE TRIGGER trg_ope_ciclo_caja_before_update
BEFORE UPDATE ON ope_ciclo_caja
FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
END //

DROP TRIGGER IF EXISTS trg_ope_ciclo_caja_after_insert //
CREATE TRIGGER trg_ope_ciclo_caja_after_insert
AFTER INSERT ON ope_ciclo_caja
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'ope_ciclo_caja',
        'INSERT',
        CAST(NEW.id_ciclo_caja AS CHAR),
        NULL,
        JSON_OBJECT(
            'id_ciclo_caja', NEW.id_ciclo_caja,
            'numero_semana', NEW.numero_semana,
            'fecha_apertura', NEW.fecha_apertura,
            'fecha_cierre', NEW.fecha_cierre,
            'monto_saldo_inicial', NEW.monto_saldo_inicial,
            'monto_total_gastado', NEW.monto_total_gastado,
            'estado_ciclo', NEW.estado_ciclo,
            'id_caja_chica', NEW.id_caja_chica,
            'id_rendicion', NEW.id_rendicion
        ),
        NEW.id_usuario_creacion
    );
END //

DROP TRIGGER IF EXISTS trg_ope_ciclo_caja_after_update //
CREATE TRIGGER trg_ope_ciclo_caja_after_update
AFTER UPDATE ON ope_ciclo_caja
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'ope_ciclo_caja',
        'UPDATE',
        CAST(NEW.id_ciclo_caja AS CHAR),
        JSON_OBJECT(
            'id_ciclo_caja', OLD.id_ciclo_caja,
            'numero_semana', OLD.numero_semana,
            'fecha_apertura', OLD.fecha_apertura,
            'fecha_cierre', OLD.fecha_cierre,
            'monto_saldo_inicial', OLD.monto_saldo_inicial,
            'monto_total_gastado', OLD.monto_total_gastado,
            'estado_ciclo', OLD.estado_ciclo,
            'id_caja_chica', OLD.id_caja_chica,
            'id_rendicion', OLD.id_rendicion
        ),
        JSON_OBJECT(
            'id_ciclo_caja', NEW.id_ciclo_caja,
            'numero_semana', NEW.numero_semana,
            'fecha_apertura', NEW.fecha_apertura,
            'fecha_cierre', NEW.fecha_cierre,
            'monto_saldo_inicial', NEW.monto_saldo_inicial,
            'monto_total_gastado', NEW.monto_total_gastado,
            'estado_ciclo', NEW.estado_ciclo,
            'id_caja_chica', NEW.id_caja_chica,
            'id_rendicion', NEW.id_rendicion
        ),
        NEW.id_usuario_modificacion
    );
END //

DROP TRIGGER IF EXISTS trg_ope_ciclo_caja_after_delete //
CREATE TRIGGER trg_ope_ciclo_caja_after_delete
AFTER DELETE ON ope_ciclo_caja
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'ope_ciclo_caja',
        'DELETE',
        CAST(OLD.id_ciclo_caja AS CHAR),
        JSON_OBJECT(
            'id_ciclo_caja', OLD.id_ciclo_caja,
            'numero_semana', OLD.numero_semana,
            'fecha_apertura', OLD.fecha_apertura,
            'fecha_cierre', OLD.fecha_cierre,
            'monto_saldo_inicial', OLD.monto_saldo_inicial,
            'monto_total_gastado', OLD.monto_total_gastado,
            'estado_ciclo', OLD.estado_ciclo,
            'id_caja_chica', OLD.id_caja_chica,
            'id_rendicion', OLD.id_rendicion
        ),
        NULL,
        OLD.id_usuario_modificacion
    );
END //

-- ===============================================================================
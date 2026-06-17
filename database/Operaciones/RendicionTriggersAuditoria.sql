DELIMITER //

-- ===============================================================================
-- 2. TABLA: ope_rendicion
-- ===============================================================================
-- ===============================================================================

DROP TRIGGER IF EXISTS trg_ope_rendicion_before_insert //
CREATE TRIGGER trg_ope_rendicion_before_insert
BEFORE INSERT ON ope_rendicion
FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
END //

DROP TRIGGER IF EXISTS trg_ope_rendicion_before_update //
CREATE TRIGGER trg_ope_rendicion_before_update
BEFORE UPDATE ON ope_rendicion
FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
END //

DROP TRIGGER IF EXISTS trg_ope_rendicion_after_insert //
CREATE TRIGGER trg_ope_rendicion_after_insert
AFTER INSERT ON ope_rendicion
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'ope_rendicion',
        'INSERT',
        CAST(NEW.id_rendicion AS CHAR),
        NULL,
        JSON_OBJECT(
            'id_rendicion', NEW.id_rendicion,
            'fecha_presentacion', NEW.fecha_presentacion,
            'fecha_aprobacion', NEW.fecha_aprobacion,
            'monto_total_declarado', NEW.monto_total_declarado,
            'monto_total_aprobado', NEW.monto_total_aprobado,
            'monto_saldo_final', NEW.monto_saldo_final,
            'estado_rendicion', NEW.estado_rendicion,
            'comentario', NEW.comentario,
            'id_ciclo_caja', NEW.id_ciclo_caja
        ),
        NEW.id_usuario_creacion
    );
END //

DROP TRIGGER IF EXISTS trg_ope_rendicion_after_update //
CREATE TRIGGER trg_ope_rendicion_after_update
AFTER UPDATE ON ope_rendicion
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'ope_rendicion',
        'UPDATE',
        CAST(NEW.id_rendicion AS CHAR),
        JSON_OBJECT(
            'id_rendicion', OLD.id_rendicion,
            'fecha_presentacion', OLD.fecha_presentacion,
            'fecha_aprobacion', OLD.fecha_aprobacion,
            'monto_total_declarado', OLD.monto_total_declarado,
            'monto_total_aprobado', OLD.monto_total_aprobado,
            'monto_saldo_final', OLD.monto_saldo_final,
            'estado_rendicion', OLD.estado_rendicion,
            'comentario', OLD.comentario,
            'id_ciclo_caja', OLD.id_ciclo_caja
        ),
        JSON_OBJECT(
            'id_rendicion', NEW.id_rendicion,
            'fecha_presentacion', NEW.fecha_presentacion,
            'fecha_aprobacion', NEW.fecha_aprobacion,
            'monto_total_declarado', NEW.monto_total_declarado,
            'monto_total_aprobado', NEW.monto_total_aprobado,
            'monto_saldo_final', NEW.monto_saldo_final,
            'estado_rendicion', NEW.estado_rendicion,
            'comentario', NEW.comentario,
            'id_ciclo_caja', NEW.id_ciclo_caja
        ),
        NEW.id_usuario_modificacion
    );
END //

DROP TRIGGER IF EXISTS trg_ope_rendicion_after_delete //
CREATE TRIGGER trg_ope_rendicion_after_delete
AFTER DELETE ON ope_rendicion
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'ope_rendicion',
        'DELETE',
        CAST(OLD.id_rendicion AS CHAR),
        JSON_OBJECT(
            'id_rendicion', OLD.id_rendicion,
            'fecha_presentacion', OLD.fecha_presentacion,
            'fecha_aprobacion', OLD.fecha_aprobacion,
            'monto_total_declarado', OLD.monto_total_declarado,
            'monto_total_aprobado', OLD.monto_total_aprobado,
            'monto_saldo_final', OLD.monto_saldo_final,
            'estado_rendicion', OLD.estado_rendicion,
            'comentario', OLD.comentario,
            'id_ciclo_caja', OLD.id_ciclo_caja
        ),
        NULL,
        OLD.id_usuario_modificacion
    );
END //

-- ===============================================================================
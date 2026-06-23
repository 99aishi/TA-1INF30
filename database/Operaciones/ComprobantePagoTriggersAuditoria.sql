
DELIMITER //

-- ===============================================================================
-- 4. TABLA: ope_comprobante_pago
-- ===============================================================================
-- ===============================================================================

DROP TRIGGER IF EXISTS trg_ope_comprobante_pago_before_insert //
CREATE TRIGGER trg_ope_comprobante_pago_before_insert
BEFORE INSERT ON ope_comprobante_pago
FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
END //

DROP TRIGGER IF EXISTS trg_ope_comprobante_pago_before_update //
CREATE TRIGGER trg_ope_comprobante_pago_before_update
BEFORE UPDATE ON ope_comprobante_pago
FOR EACH ROW
BEGIN
    DECLARE v_estado_ciclo VARCHAR(20) DEFAULT NULL;
    DECLARE v_permiso_id INT DEFAULT NULL;

    SET NEW.actualizado_at = NOW();

    -- Validate edit permission when cycle is closed, liquidated or in exception window
    IF NEW.id_solicitud_gasto IS NOT NULL AND NEW.id_solicitud_gasto > 0 THEN
        SELECT occ.estado_ciclo INTO v_estado_ciclo
        FROM ope_solicitud_gasto sg
        JOIN ope_ciclo_caja occ ON sg.id_ciclo_caja = occ.id_ciclo_caja
        WHERE sg.id_solicitud_gasto = NEW.id_solicitud_gasto
        LIMIT 1;

        IF v_estado_ciclo IS NOT NULL AND v_estado_ciclo != 'ABIERTO' THEN
            SELECT pe.id_permiso INTO v_permiso_id
            FROM ope_permiso_edicion pe
            WHERE pe.id_comprobante = NEW.id_comprobante
              AND pe.estado = 'ACTIVO'
              AND pe.fecha_expiracion > NOW()
              AND (pe.id_usuario_solicitante = NEW.id_usuario_modificacion
                   OR pe.id_usuario_autorizador = NEW.id_usuario_modificacion)
            LIMIT 1;

            IF v_permiso_id IS NULL THEN
                SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'El ciclo de caja chica no permite ediciones. Solicite autorizacion a Jefe o Tesorero (48h).';
            ELSE
                UPDATE ope_permiso_edicion
                   SET estado = 'USADO',
                       fecha_uso = NOW(),
                       actualizado_at = NOW(),
                       id_usuario_modificacion = NEW.id_usuario_modificacion
                 WHERE id_permiso = v_permiso_id;
            END IF;
        END IF;
    END IF;
END //

DROP TRIGGER IF EXISTS trg_ope_comprobante_pago_after_insert //
CREATE TRIGGER trg_ope_comprobante_pago_after_insert
AFTER INSERT ON ope_comprobante_pago
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'ope_comprobante_pago',
        'INSERT',
        CAST(NEW.id_comprobante AS CHAR),
        NULL,
        JSON_OBJECT(
            'id_comprobante', NEW.id_comprobante,
            'tipo_documento', NEW.tipo_documento,
            'ruc_proveedor', NEW.ruc_proveedor,
            'razon_social', NEW.razon_social,
            'numero_serie', NEW.numero_serie,
            'fecha_emision', NEW.fecha_emision,
            'monto_subtotal', NEW.monto_subtotal,
            'monto_igv', NEW.monto_igv,
            'monto_total', NEW.monto_total,
            'tipo_cambio', NEW.tipo_cambio,
            'monto_convertido', NEW.monto_convertido,
            'nombre_archivo_comprobante', NEW.nombre_archivo_comprobante,
            'estado_comprobante', NEW.estado_comprobante,
            'id_solicitud_gasto', NEW.id_solicitud_gasto,
            'id_moneda', NEW.id_moneda
        ),
        NEW.id_usuario_creacion
    );
END //

DROP TRIGGER IF EXISTS trg_ope_comprobante_pago_after_update //
CREATE TRIGGER trg_ope_comprobante_pago_after_update
AFTER UPDATE ON ope_comprobante_pago
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'ope_comprobante_pago',
        'UPDATE',
        CAST(NEW.id_comprobante AS CHAR),
        JSON_OBJECT(
            'id_comprobante', OLD.id_comprobante,
            'tipo_documento', OLD.tipo_documento,
            'ruc_proveedor', OLD.ruc_proveedor,
            'razon_social', OLD.razon_social,
            'numero_serie', OLD.numero_serie,
            'fecha_emision', OLD.fecha_emision,
            'monto_subtotal', OLD.monto_subtotal,
            'monto_igv', OLD.monto_igv,
            'monto_total', OLD.monto_total,
            'tipo_cambio', OLD.tipo_cambio,
            'monto_convertido', OLD.monto_convertido,
            'nombre_archivo_comprobante', OLD.nombre_archivo_comprobante,
            'estado_comprobante', OLD.estado_comprobante,
            'id_solicitud_gasto', OLD.id_solicitud_gasto,
            'id_moneda', OLD.id_moneda
        ),
        JSON_OBJECT(
            'id_comprobante', NEW.id_comprobante,
            'tipo_documento', NEW.tipo_documento,
            'ruc_proveedor', NEW.ruc_proveedor,
            'razon_social', NEW.razon_social,
            'numero_serie', NEW.numero_serie,
            'fecha_emision', NEW.fecha_emision,
            'monto_subtotal', NEW.monto_subtotal,
            'monto_igv', NEW.monto_igv,
            'monto_total', NEW.monto_total,
            'tipo_cambio', NEW.tipo_cambio,
            'monto_convertido', NEW.monto_convertido,
            'nombre_archivo_comprobante', NEW.nombre_archivo_comprobante,
            'estado_comprobante', NEW.estado_comprobante,
            'id_solicitud_gasto', NEW.id_solicitud_gasto,
            'id_moneda', NEW.id_moneda
        ),
        NEW.id_usuario_modificacion
    );
END //

DROP TRIGGER IF EXISTS trg_ope_comprobante_pago_after_delete //
CREATE TRIGGER trg_ope_comprobante_pago_after_delete
AFTER DELETE ON ope_comprobante_pago
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'ope_comprobante_pago',
        'DELETE',
        CAST(OLD.id_comprobante AS CHAR),
        JSON_OBJECT(
            'id_comprobante', OLD.id_comprobante,
            'tipo_documento', OLD.tipo_documento,
            'ruc_proveedor', OLD.ruc_proveedor,
            'razon_social', OLD.razon_social,
            'numero_serie', OLD.numero_serie,
            'fecha_emision', OLD.fecha_emision,
            'monto_subtotal', OLD.monto_subtotal,
            'monto_igv', OLD.monto_igv,
            'monto_total', OLD.monto_total,
            'tipo_cambio', OLD.tipo_cambio,
            'monto_convertido', OLD.monto_convertido,
            'nombre_archivo_comprobante', OLD.nombre_archivo_comprobante,
            'estado_comprobante', OLD.estado_comprobante,
            'id_solicitud_gasto', OLD.id_solicitud_gasto,
            'id_moneda', OLD.id_moneda
        ),
        NULL,
        OLD.id_usuario_modificacion
    );
END //

-- ===============================================================================
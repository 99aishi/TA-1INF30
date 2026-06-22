
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
    SET NEW.actualizado_at = NOW();
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
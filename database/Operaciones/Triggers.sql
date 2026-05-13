DELIMITER //

-- ===============================================================================
-- 1. TABLA: ope_ciclo_caja
-- ===============================================================================

CREATE TRIGGER trg_ope_ciclo_caja_before_insert
BEFORE INSERT ON ope_ciclo_caja
FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    SET NEW.id_usuario_creacion = COALESCE(NEW.id_usuario_creacion, @id_usuario_actual);
    SET NEW.id_usuario_modificacion = COALESCE(NEW.id_usuario_modificacion, @id_usuario_actual);
END //

CREATE TRIGGER trg_ope_ciclo_caja_before_update
BEFORE UPDATE ON ope_ciclo_caja
FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.id_usuario_modificacion = COALESCE(NEW.id_usuario_modificacion, @id_usuario_actual);
END //

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
        IFNULL(@id_usuario_actual, OLD.id_usuario_modificacion)
    );
END //

-- ===============================================================================
-- 2. TABLA: ope_rendicion
-- ===============================================================================

CREATE TRIGGER trg_ope_rendicion_before_insert
BEFORE INSERT ON ope_rendicion
FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    SET NEW.id_usuario_creacion = COALESCE(NEW.id_usuario_creacion, @id_usuario_actual);
    SET NEW.id_usuario_modificacion = COALESCE(NEW.id_usuario_modificacion, @id_usuario_actual);
END //

CREATE TRIGGER trg_ope_rendicion_before_update
BEFORE UPDATE ON ope_rendicion
FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.id_usuario_modificacion = COALESCE(NEW.id_usuario_modificacion, @id_usuario_actual);
END //

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
        IFNULL(@id_usuario_actual, OLD.id_usuario_modificacion)
    );
END //

-- ===============================================================================
-- 3. TABLA: ope_solicitud_gasto
-- ===============================================================================

CREATE TRIGGER trg_ope_solicitud_gasto_before_insert
BEFORE INSERT ON ope_solicitud_gasto
FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    SET NEW.id_usuario_creacion = COALESCE(NEW.id_usuario_creacion, @id_usuario_actual);
    SET NEW.id_usuario_modificacion = COALESCE(NEW.id_usuario_modificacion, @id_usuario_actual);
END //

CREATE TRIGGER trg_ope_solicitud_gasto_before_update
BEFORE UPDATE ON ope_solicitud_gasto
FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.id_usuario_modificacion = COALESCE(NEW.id_usuario_modificacion, @id_usuario_actual);
END //

CREATE TRIGGER trg_ope_solicitud_gasto_after_insert
AFTER INSERT ON ope_solicitud_gasto
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'ope_solicitud_gasto',
        'INSERT',
        CAST(NEW.id_solicitud_gasto AS CHAR),
        NULL,
        JSON_OBJECT(
            'id_solicitud_gasto', NEW.id_solicitud_gasto,
            'fecha_solicitud', NEW.fecha_solicitud,
            'monto_solicitado', NEW.monto_solicitado,
            'motivo_solicitud', NEW.motivo_solicitud,
            'estado_solicitud', NEW.estado_solicitud,
            'id_usuario_solicitante', NEW.id_usuario_solicitante,
            'id_usuario_destinatario', NEW.id_usuario_destinatario,
            'id_ciclo_caja', NEW.id_ciclo_caja
        ),
        NEW.id_usuario_creacion
    );
END //

CREATE TRIGGER trg_ope_solicitud_gasto_after_update
AFTER UPDATE ON ope_solicitud_gasto
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'ope_solicitud_gasto',
        'UPDATE',
        CAST(NEW.id_solicitud_gasto AS CHAR),
        JSON_OBJECT(
            'id_solicitud_gasto', OLD.id_solicitud_gasto,
            'fecha_solicitud', OLD.fecha_solicitud,
            'monto_solicitado', OLD.monto_solicitado,
            'motivo_solicitud', OLD.motivo_solicitud,
            'estado_solicitud', OLD.estado_solicitud,
            'id_usuario_solicitante', OLD.id_usuario_solicitante,
            'id_usuario_destinatario', OLD.id_usuario_destinatario,
            'id_ciclo_caja', OLD.id_ciclo_caja
        ),
        JSON_OBJECT(
            'id_solicitud_gasto', NEW.id_solicitud_gasto,
            'fecha_solicitud', NEW.fecha_solicitud,
            'monto_solicitado', NEW.monto_solicitado,
            'motivo_solicitud', NEW.motivo_solicitud,
            'estado_solicitud', NEW.estado_solicitud,
            'id_usuario_solicitante', NEW.id_usuario_solicitante,
            'id_usuario_destinatario', NEW.id_usuario_destinatario,
            'id_ciclo_caja', NEW.id_ciclo_caja
        ),
        NEW.id_usuario_modificacion
    );
END //

CREATE TRIGGER trg_ope_solicitud_gasto_after_delete
AFTER DELETE ON ope_solicitud_gasto
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'ope_solicitud_gasto',
        'DELETE',
        CAST(OLD.id_solicitud_gasto AS CHAR),
        JSON_OBJECT(
            'id_solicitud_gasto', OLD.id_solicitud_gasto,
            'fecha_solicitud', OLD.fecha_solicitud,
            'monto_solicitado', OLD.monto_solicitado,
            'motivo_solicitud', OLD.motivo_solicitud,
            'estado_solicitud', OLD.estado_solicitud,
            'id_usuario_solicitante', OLD.id_usuario_solicitante,
            'id_usuario_destinatario', OLD.id_usuario_destinatario,
            'id_ciclo_caja', OLD.id_ciclo_caja
        ),
        NULL,
        IFNULL(@id_usuario_actual, OLD.id_usuario_modificacion)
    );
END //

-- ===============================================================================
-- 4. TABLA: ope_comprobante_pago
-- ===============================================================================

CREATE TRIGGER trg_ope_comprobante_pago_before_insert
BEFORE INSERT ON ope_comprobante_pago
FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    SET NEW.id_usuario_creacion = COALESCE(NEW.id_usuario_creacion, @id_usuario_actual);
    SET NEW.id_usuario_modificacion = COALESCE(NEW.id_usuario_modificacion, @id_usuario_actual);
END //

CREATE TRIGGER trg_ope_comprobante_pago_before_update
BEFORE UPDATE ON ope_comprobante_pago
FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.id_usuario_modificacion = COALESCE(NEW.id_usuario_modificacion, @id_usuario_actual);
END //

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
            'estado_comprobante', NEW.estado_comprobante,
            'id_solicitud_gasto', NEW.id_solicitud_gasto,
            'id_moneda', NEW.id_moneda
        ),
        NEW.id_usuario_creacion
    );
END //

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
            'estado_comprobante', NEW.estado_comprobante,
            'id_solicitud_gasto', NEW.id_solicitud_gasto,
            'id_moneda', NEW.id_moneda
        ),
        NEW.id_usuario_modificacion
    );
END //

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
            'estado_comprobante', OLD.estado_comprobante,
            'id_solicitud_gasto', OLD.id_solicitud_gasto,
            'id_moneda', OLD.id_moneda
        ),
        NULL,
        IFNULL(@id_usuario_actual, OLD.id_usuario_modificacion)
    );
END //

-- ===============================================================================
-- 5. TABLA: ope_transaccion
-- ===============================================================================

CREATE TRIGGER trg_ope_transaccion_before_insert
BEFORE INSERT ON ope_transaccion
FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    SET NEW.id_usuario_creacion = COALESCE(NEW.id_usuario_creacion, @id_usuario_actual);
    SET NEW.id_usuario_modificacion = COALESCE(NEW.id_usuario_modificacion, @id_usuario_actual);
END //

CREATE TRIGGER trg_ope_transaccion_before_update
BEFORE UPDATE ON ope_transaccion
FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.id_usuario_modificacion = COALESCE(NEW.id_usuario_modificacion, @id_usuario_actual);
END //

CREATE TRIGGER trg_ope_transaccion_after_insert
AFTER INSERT ON ope_transaccion
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'ope_transaccion',
        'INSERT',
        CAST(NEW.id_transaccion AS CHAR),
        NULL,
        JSON_OBJECT(
            'id_transaccion', NEW.id_transaccion,
            'tipo_operacion', NEW.tipo_operacion,
            'fecha_operacion', NEW.fecha_operacion,
            'monto_transaccion', NEW.monto_transaccion,
            'numero_operacion_bancaria', NEW.numero_operacion_bancaria,
            'medio_pago', NEW.medio_pago,
            'valor_tipo_cambio', NEW.valor_tipo_cambio,
            'estado_transaccion', NEW.estado_transaccion,
            'id_cuenta_origen', NEW.id_cuenta_origen,
            'id_cuenta_destino', NEW.id_cuenta_destino,
            'id_moneda', NEW.id_moneda
        ),
        NEW.id_usuario_creacion
    );
END //

CREATE TRIGGER trg_ope_transaccion_after_update
AFTER UPDATE ON ope_transaccion
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'ope_transaccion',
        'UPDATE',
        CAST(NEW.id_transaccion AS CHAR),
        JSON_OBJECT(
            'id_transaccion', OLD.id_transaccion,
            'tipo_operacion', OLD.tipo_operacion,
            'fecha_operacion', OLD.fecha_operacion,
            'monto_transaccion', OLD.monto_transaccion,
            'numero_operacion_bancaria', OLD.numero_operacion_bancaria,
            'medio_pago', OLD.medio_pago,
            'valor_tipo_cambio', OLD.valor_tipo_cambio,
            'estado_transaccion', OLD.estado_transaccion,
            'id_cuenta_origen', OLD.id_cuenta_origen,
            'id_cuenta_destino', OLD.id_cuenta_destino,
            'id_moneda', OLD.id_moneda
        ),
        JSON_OBJECT(
            'id_transaccion', NEW.id_transaccion,
            'tipo_operacion', NEW.tipo_operacion,
            'fecha_operacion', NEW.fecha_operacion,
            'monto_transaccion', NEW.monto_transaccion,
            'numero_operacion_bancaria', NEW.numero_operacion_bancaria,
            'medio_pago', NEW.medio_pago,
            'valor_tipo_cambio', NEW.valor_tipo_cambio,
            'estado_transaccion', NEW.estado_transaccion,
            'id_cuenta_origen', NEW.id_cuenta_origen,
            'id_cuenta_destino', NEW.id_cuenta_destino,
            'id_moneda', NEW.id_moneda
        ),
        NEW.id_usuario_modificacion
    );
END //

CREATE TRIGGER trg_ope_transaccion_after_delete
AFTER DELETE ON ope_transaccion
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'ope_transaccion',
        'DELETE',
        CAST(OLD.id_transaccion AS CHAR),
        JSON_OBJECT(
            'id_transaccion', OLD.id_transaccion,
            'tipo_operacion', OLD.tipo_operacion,
            'fecha_operacion', OLD.fecha_operacion,
            'monto_transaccion', OLD.monto_transaccion,
            'numero_operacion_bancaria', OLD.numero_operacion_bancaria,
            'medio_pago', OLD.medio_pago,
            'valor_tipo_cambio', OLD.valor_tipo_cambio,
            'estado_transaccion', OLD.estado_transaccion,
            'id_cuenta_origen', OLD.id_cuenta_origen,
            'id_cuenta_destino', OLD.id_cuenta_destino,
            'id_moneda', OLD.id_moneda
        ),
        NULL,
        IFNULL(@id_usuario_actual, OLD.id_usuario_modificacion)
    );
END //

DELIMITER ;
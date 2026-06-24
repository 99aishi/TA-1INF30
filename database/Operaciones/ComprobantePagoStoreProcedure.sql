
DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_comprobante_pago $$
CREATE PROCEDURE pa_insertar_comprobante_pago(
    IN p_id_usuario_accion INT,
IN p_tipo_documento ENUM('FACTURA','BOLETA','DJ_EXCEPCIONAL'),
IN p_ruc_proveedor CHAR(11),
IN p_razon_social VARCHAR(150),
IN p_numero_serie VARCHAR(30),
IN p_fecha_emision DATE,
IN p_monto_subtotal DECIMAL(12,2),
IN p_monto_igv DECIMAL(12,2),
IN p_monto_total DECIMAL(12,2),
IN p_tipo_cambio DECIMAL(10,4),
IN p_monto_convertido DECIMAL(12,2),
IN p_nombre_archivo VARCHAR(500),
IN p_estado_comprobante ENUM('POR_REVISAR','ANULADO','APROBADO','OBSERVADO'),
IN p_id_solicitud_gasto INT,
IN p_id_moneda INT,
OUT p_id_generado INT
)
BEGIN
    INSERT INTO ope_comprobante_pago(

        tipo_documento,
        ruc_proveedor,
        razon_social,
        numero_serie,
        fecha_emision,
        monto_subtotal,
        monto_igv,
        monto_total,
        tipo_cambio,
        monto_convertido,
        nombre_archivo_comprobante,
        estado_comprobante,
        id_solicitud_gasto,
        id_moneda,
        id_usuario_creacion,
        id_usuario_modificacion    )
    VALUES(

        p_tipo_documento,
        p_ruc_proveedor,
        p_razon_social,
        p_numero_serie,
        p_fecha_emision,
        p_monto_subtotal,
        p_monto_igv,
        p_monto_total,
        p_tipo_cambio,
        p_monto_convertido,
        p_nombre_archivo,
        p_estado_comprobante,
        p_id_solicitud_gasto,
        p_id_moneda,
        p_id_usuario_accion,
        p_id_usuario_accion    );
    
    SET p_id_generado = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS pa_modificar_comprobante_pago $$
CREATE PROCEDURE pa_modificar_comprobante_pago(

        IN p_id_usuario_accion INT,
IN p_id_comprobante INT,
    IN p_tipo_documento ENUM('FACTURA','BOLETA','DJ_EXCEPCIONAL'),
    IN p_ruc_proveedor CHAR(11),
    IN p_razon_social VARCHAR(150),
    IN p_numero_serie VARCHAR(30),
    IN p_fecha_emision DATE,
    IN p_monto_subtotal DECIMAL(12,2),
    IN p_monto_igv DECIMAL(12,2),
    IN p_monto_total DECIMAL(12,2),
    IN p_tipo_cambio DECIMAL(10,4),
    IN p_monto_convertido DECIMAL(12,2),
    IN p_nombre_archivo VARCHAR(500),
IN p_estado_comprobante ENUM('POR_REVISAR','ANULADO','APROBADO','OBSERVADO'),
    IN p_id_solicitud_gasto INT,
    IN p_id_moneda INT

)
BEGIN
    IF p_id_solicitud_gasto IS NULL OR p_id_solicitud_gasto <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de comprobante de pago inválido';
    END IF;

    UPDATE ope_comprobante_pago
       SET tipo_documento = p_tipo_documento,
           ruc_proveedor = p_ruc_proveedor,
           razon_social = p_razon_social,
           numero_serie = p_numero_serie,
           fecha_emision = p_fecha_emision,
           monto_subtotal = p_monto_subtotal,
           monto_igv = p_monto_igv,
            monto_total = p_monto_total,
            tipo_cambio = p_tipo_cambio,
            monto_convertido = p_monto_convertido,
            nombre_archivo_comprobante = p_nombre_archivo,
            estado_comprobante = p_estado_comprobante,
            id_solicitud_gasto = p_id_solicitud_gasto,
            id_moneda = p_id_moneda,
            id_usuario_modificacion = p_id_usuario_accion
    WHERE id_comprobante = p_id_comprobante;
END$$


DROP PROCEDURE IF EXISTS pa_eliminar_comprobante_pago $$
CREATE PROCEDURE pa_eliminar_comprobante_pago(

        IN p_id_usuario_accion INT,
IN p_id_comprobante INT

)
BEGIN
    IF p_id_comprobante IS NULL OR p_id_comprobante <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de comprobante inválido';
    END IF;

    UPDATE ope_comprobante_pago
       SET estado_comprobante = 'ANULADO',
           id_usuario_modificacion = p_id_usuario_accion
    WHERE id_comprobante = p_id_comprobante;
    
END$$

DROP PROCEDURE IF EXISTS pa_buscar_comprobante_pago_por_id $$
CREATE PROCEDURE pa_buscar_comprobante_pago_por_id(
    IN p_id_comprobante INT
)
BEGIN
    SELECT 
        cp.id_comprobante, 
        cp.tipo_documento, 
        cp.ruc_proveedor, 
        cp.razon_social, 
        cp.numero_serie, 
        cp.fecha_emision, 
        cp.monto_subtotal, 
        cp.monto_igv, 
        cp.monto_total, 
        cp.tipo_cambio,
        cp.monto_convertido,
        cp.nombre_archivo_comprobante,
        cp.estado_comprobante,
        cp.id_solicitud_gasto, 
        cp.id_moneda,
        m.id_moneda AS mon_id_moneda,
        m.codigo_iso AS mon_codigo_iso,
        m.simbolo AS mon_simbolo,
        m.nombre_moneda AS mon_nombre,
        m.descripcion AS mon_descripcion,
        m.activa AS mon_activa,
        sg.id_solicitud_gasto AS sg_id_solicitud_gasto,
        sg.fecha_solicitud AS sg_fecha_solicitud,
        sg.monto_solicitado AS sg_monto_solicitado,
        sg.id_moneda_original AS sg_id_moneda_original,
        sg.tipo_cambio AS sg_tipo_cambio,
        sg.monto_convertido AS sg_monto_convertido,
        sg.motivo_solicitud AS sg_motivo_solicitud,
        sg.estado_solicitud AS sg_estado_solicitud,
        sg.id_transaccion AS sg_id_transaccion,
        sg.id_usuario_solicitante AS sg_id_usuario_solicitante,
        sg.id_usuario_destinatario AS sg_id_usuario_destinatario,
        sg.id_jefe_aprobador AS sg_id_jefe_aprobador,
        sg.id_tesorero_aprobador AS sg_id_tesorero_aprobador,
        sg.id_ciclo_caja AS sg_id_ciclo_caja,
        sg.comentario_decision AS sg_comentario_decision
    FROM ope_comprobante_pago cp
    LEFT JOIN tes_moneda m ON cp.id_moneda = m.id_moneda
    LEFT JOIN ope_solicitud_gasto sg ON cp.id_solicitud_gasto = sg.id_solicitud_gasto
    WHERE cp.id_comprobante = p_id_comprobante
    ORDER BY cp.estado_comprobante DESC;
END$$

DROP PROCEDURE IF EXISTS pa_listar_comprobantes_pago $$
CREATE PROCEDURE pa_listar_comprobantes_pago()
BEGIN
    SELECT 
        cp.id_comprobante, 
        cp.tipo_documento, 
        cp.ruc_proveedor, 
        cp.razon_social, 
        cp.numero_serie, 
        cp.fecha_emision, 
        cp.monto_subtotal, 
        cp.monto_igv, 
        cp.monto_total, 
        cp.tipo_cambio,
        cp.monto_convertido,
        cp.nombre_archivo_comprobante,
        cp.estado_comprobante,
        cp.id_solicitud_gasto, 
        cp.id_moneda,
        m.id_moneda AS mon_id_moneda,
        m.codigo_iso AS mon_codigo_iso,
        m.simbolo AS mon_simbolo,
        m.nombre_moneda AS mon_nombre,
        m.descripcion AS mon_descripcion,
        m.activa AS mon_activa,
        sg.id_solicitud_gasto AS sg_id_solicitud_gasto,
        sg.fecha_solicitud AS sg_fecha_solicitud,
        sg.monto_solicitado AS sg_monto_solicitado,
        sg.id_moneda_original AS sg_id_moneda_original,
        sg.tipo_cambio AS sg_tipo_cambio,
        sg.monto_convertido AS sg_monto_convertido,
        sg.motivo_solicitud AS sg_motivo_solicitud,
        sg.estado_solicitud AS sg_estado_solicitud,
        sg.id_transaccion AS sg_id_transaccion,
        sg.id_usuario_solicitante AS sg_id_usuario_solicitante,
        sg.id_usuario_destinatario AS sg_id_usuario_destinatario,
        sg.id_jefe_aprobador AS sg_id_jefe_aprobador,
        sg.id_tesorero_aprobador AS sg_id_tesorero_aprobador,
        sg.id_ciclo_caja AS sg_id_ciclo_caja,
        sg.comentario_decision AS sg_comentario_decision
    FROM ope_comprobante_pago cp
    LEFT JOIN tes_moneda m ON cp.id_moneda = m.id_moneda
    LEFT JOIN ope_solicitud_gasto sg ON cp.id_solicitud_gasto = sg.id_solicitud_gasto
    ORDER BY cp.estado_comprobante DESC;
END$$

DROP PROCEDURE IF EXISTS pa_listar_todos_comprobantes_pago $$
CREATE PROCEDURE pa_listar_todos_comprobantes_pago()
BEGIN
    SELECT 
        cp.id_comprobante, 
        cp.tipo_documento, 
        cp.ruc_proveedor, 
        cp.razon_social, 
        cp.numero_serie, 
        cp.fecha_emision, 
        cp.monto_subtotal, 
        cp.monto_igv, 
        cp.monto_total, 
        cp.tipo_cambio,
        cp.monto_convertido,
        cp.nombre_archivo_comprobante,
        cp.estado_comprobante,
        cp.id_solicitud_gasto, 
        cp.id_moneda,
        m.id_moneda AS mon_id_moneda,
        m.codigo_iso AS mon_codigo_iso,
        m.simbolo AS mon_simbolo,
        m.nombre_moneda AS mon_nombre,
        m.descripcion AS mon_descripcion,
        m.activa AS mon_activa,
        sg.id_solicitud_gasto AS sg_id_solicitud_gasto,
        sg.fecha_solicitud AS sg_fecha_solicitud,
        sg.monto_solicitado AS sg_monto_solicitado,
        sg.id_moneda_original AS sg_id_moneda_original,
        sg.tipo_cambio AS sg_tipo_cambio,
        sg.monto_convertido AS sg_monto_convertido,
        sg.motivo_solicitud AS sg_motivo_solicitud,
        sg.estado_solicitud AS sg_estado_solicitud,
        sg.id_transaccion AS sg_id_transaccion,
        sg.id_usuario_solicitante AS sg_id_usuario_solicitante,
        sg.id_usuario_destinatario AS sg_id_usuario_destinatario,
        sg.id_jefe_aprobador AS sg_id_jefe_aprobador,
        sg.id_tesorero_aprobador AS sg_id_tesorero_aprobador,
        sg.id_ciclo_caja AS sg_id_ciclo_caja,
        sg.comentario_decision AS sg_comentario_decision
    FROM ope_comprobante_pago cp
    LEFT JOIN tes_moneda m ON cp.id_moneda = m.id_moneda
    LEFT JOIN ope_solicitud_gasto sg ON cp.id_solicitud_gasto = sg.id_solicitud_gasto
    ORDER BY cp.estado_comprobante DESC;
END$$

DROP PROCEDURE IF EXISTS pa_listar_comprobantes_por_solicitud $$
CREATE PROCEDURE pa_listar_comprobantes_por_solicitud(
	IN p_id_solicitud INT
)
BEGIN
    SELECT
        cp.id_comprobante,
        cp.tipo_documento,
        cp.ruc_proveedor,
        cp.razon_social,
        cp.numero_serie,
        cp.fecha_emision,
        cp.monto_subtotal,
        cp.monto_igv,
        cp.monto_total,
        cp.tipo_cambio,
        cp.monto_convertido,
        cp.nombre_archivo_comprobante,
        cp.estado_comprobante,
        cp.id_solicitud_gasto,
        cp.id_moneda,
        m.id_moneda AS mon_id_moneda,
        m.codigo_iso AS mon_codigo_iso,
        m.simbolo AS mon_simbolo,
        m.nombre_moneda AS mon_nombre,
        m.descripcion AS mon_descripcion,
        m.activa AS mon_activa,
        sg.id_solicitud_gasto AS sg_id_solicitud_gasto,
        sg.fecha_solicitud AS sg_fecha_solicitud,
        sg.monto_solicitado AS sg_monto_solicitado,
        sg.id_moneda_original AS sg_id_moneda_original,
        sg.tipo_cambio AS sg_tipo_cambio,
        sg.monto_convertido AS sg_monto_convertido,
        sg.motivo_solicitud AS sg_motivo_solicitud,
        sg.estado_solicitud AS sg_estado_solicitud,
        sg.id_transaccion AS sg_id_transaccion,
        sg.id_usuario_solicitante AS sg_id_usuario_solicitante,
        sg.id_usuario_destinatario AS sg_id_usuario_destinatario,
        sg.id_jefe_aprobador AS sg_id_jefe_aprobador,
        sg.id_tesorero_aprobador AS sg_id_tesorero_aprobador,
        sg.id_ciclo_caja AS sg_id_ciclo_caja,
        sg.comentario_decision AS sg_comentario_decision
    FROM ope_comprobante_pago cp
    LEFT JOIN tes_moneda m ON cp.id_moneda = m.id_moneda
    LEFT JOIN ope_solicitud_gasto sg ON cp.id_solicitud_gasto = sg.id_solicitud_gasto
    WHERE cp.id_solicitud_gasto = p_id_solicitud
    ORDER BY cp.estado_comprobante DESC, cp.id_comprobante DESC;
END$$

DROP PROCEDURE IF EXISTS pa_listar_comprobantes_activos $$
CREATE PROCEDURE pa_listar_comprobantes_activos()
BEGIN
    SELECT
        cp.id_comprobante,
        cp.tipo_documento,
        cp.ruc_proveedor,
        cp.razon_social,
        cp.numero_serie,
        cp.fecha_emision,
        cp.monto_subtotal,
        cp.monto_igv,
        cp.monto_total,
        cp.tipo_cambio,
        cp.monto_convertido,
        cp.nombre_archivo_comprobante,
        cp.estado_comprobante,
        cp.id_solicitud_gasto,
        cp.id_moneda,
        m.id_moneda AS mon_id_moneda,
        m.codigo_iso AS mon_codigo_iso,
        m.simbolo AS mon_simbolo,
        m.nombre_moneda AS mon_nombre,
        m.descripcion AS mon_descripcion,
        m.activa AS mon_activa,
        sg.id_solicitud_gasto AS sg_id_solicitud_gasto,
        sg.fecha_solicitud AS sg_fecha_solicitud,
        sg.monto_solicitado AS sg_monto_solicitado,
        sg.id_moneda_original AS sg_id_moneda_original,
        sg.tipo_cambio AS sg_tipo_cambio,
        sg.monto_convertido AS sg_monto_convertido,
        sg.motivo_solicitud AS sg_motivo_solicitud,
        sg.estado_solicitud AS sg_estado_solicitud,
        sg.id_transaccion AS sg_id_transaccion,
        sg.id_usuario_solicitante AS sg_id_usuario_solicitante,
        sg.id_usuario_destinatario AS sg_id_usuario_destinatario,
        sg.id_jefe_aprobador AS sg_id_jefe_aprobador,
        sg.id_tesorero_aprobador AS sg_id_tesorero_aprobador,
        sg.id_ciclo_caja AS sg_id_ciclo_caja,
        sg.comentario_decision AS sg_comentario_decision
    FROM ope_comprobante_pago cp
    LEFT JOIN tes_moneda m ON cp.id_moneda = m.id_moneda
    LEFT JOIN ope_solicitud_gasto sg ON cp.id_solicitud_gasto = sg.id_solicitud_gasto
    WHERE cp.estado_comprobante != 'ANULADO'
    ORDER BY cp.estado_comprobante DESC, cp.id_comprobante DESC;
END$$

DELIMITER ;

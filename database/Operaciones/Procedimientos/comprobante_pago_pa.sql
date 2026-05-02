DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_comprobante_pago $$
CREATE PROCEDURE pa_insertar_comprobante_pago(
    IN p_tipo_documento VARCHAR(20),
    IN p_ruc_proveedor CHAR(11),
    IN p_razon_social VARCHAR(150),
    IN p_numero_serie VARCHAR(30),
    IN p_fecha_emision DATE,
    IN p_monto_subtotal DECIMAL(12,2),
    IN p_monto_igv DECIMAL(12,2),
    IN p_monto_total DECIMAL(12,2),
    INT p_estado_comprobante VARCHAR(20),
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
        estado_comprobante,
        id_solicitud_gasto,
        id_moneda
    )
    VALUES(
        p_tipo_documento,
        p_ruc_proveedor,
        p_razon_social,
        p_numero_serie,
        p_fecha_emision,
        p_monto_subtotal,
        p_monto_igv,
        p_monto_total,
        p_estado_comprobante,
        p_id_solicitud_gasto,
        p_id_moneda
    );
    
    SET p_id_generado = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS pa_modificar_comprobante_pago $$
CREATE PROCEDURE pa_modificar_comprobante_pago(
    IN p_id_comprobante INT,
    IN p_tipo_documento VARCHAR(20),
    IN p_ruc_proveedor CHAR(11),
    IN p_razon_social VARCHAR(150),
    IN p_numero_serie VARCHAR(30),
    IN p_fecha_emision DATE,
    IN p_monto_subtotal DECIMAL(12,2),
    IN p_monto_igv DECIMAL(12,2),
    IN p_monto_total DECIMAL(12,2),
    IN p_estado_comprobante VARCHAR(20),
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
           estado_comprobante = p_estado_comprobante,
           id_solicitud_gasto = p_id_solicitud_gasto,
           id_moneda = p_id_moneda
     WHERE id_comprobante = p_id_comprobante;
END$$


DROP PROCEDURE IF EXISTS pa_eliminar_comprobante_pago $$
CREATE PROCEDURE pa_eliminar_comprobante_pago(
    IN p_id_comprobante INT
)
BEGIN
    IF p_id_comprobante IS NULL OR p_id_comprobante <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de comprobante inválido';
    END IF;

    UPDATE ope_comprobante_pago
       SET estado_comprobante = 'Anulado'
     WHERE id_comprobante = p_id_comprobante;
    
END$$

DROP PROCEDURE IF EXISTS pa_buscar_comprobante_pago_por_id $$
CREATE PROCEDURE pa_buscar_comprobante_pago_por_id(
    IN p_id_comprobante INT
)
BEGIN
    SELECT 
        id_comprobante, 
        tipo_documento, 
        ruc_proveedor, 
        razon_social, 
        numero_serie, 
        fecha_emision, 
        monto_subtotal, 
        monto_igv, 
        monto_total, 
        estado_comprobante,
        id_solicitud_gasto, 
        id_moneda
    FROM ope_comprobante_pago
    WHERE id_comprobante = p_id_comprobante
    ORDER BY estado_comprobante DESC;
END$$

DROP PROCEDURE IF EXISTS pa_listar_comprobantes_pago $$
CREATE PROCEDURE pa_listar_comprobantes_pago()
BEGIN
    SELECT 
        id_comprobante, 
        tipo_documento, 
        ruc_proveedor, 
        razon_social, 
        numero_serie, 
        fecha_emision, 
        monto_subtotal, 
        monto_igv, 
        monto_total, 
        estado_comprobante,
        id_solicitud_gasto, 
        id_moneda
    FROM ope_comprobante_pago
    ORDER BY estado_comprobante DESC;
END$$
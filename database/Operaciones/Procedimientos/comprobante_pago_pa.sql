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
    IN p_id_solicitud_gasto INT,
    IN p_id_fondo_entrega INT,
    IN p_id_moneda INT,
    OUT p_id_generado INT
)
BEGIN
    IF p_tipo_documento IS NULL OR TRIM(p_tipo_documento) = '' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El tipo de documento es obligatorio';
    END IF;

    IF p_monto_total IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El monto total es obligatorio';
    END IF;

    -- Debe pertenecer a un flujo (Solicitud o Entrega a Rendir)
    IF (p_id_solicitud_gasto IS NULL OR p_id_solicitud_gasto <= 0) AND 
       (p_id_fondo_entrega IS NULL OR p_id_fondo_entrega <= 0) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El comprobante debe estar asociado a una solicitud de gasto o a un fondo de entrega';
    END IF;

    INSERT INTO ope_comprobante_pago(
        tipo_documento,
        ruc_proveedor,
        razon_social,
        numero_serie,
        fecha_emision,
        monto_subtotal,
        monto_igv,
        monto_total,
        id_solicitud_gasto,
        id_fondo_entrega,
        id_moneda
    )
    VALUES(
        TRIM(p_tipo_documento),
        TRIM(p_ruc_proveedor),
        TRIM(p_razon_social),
        TRIM(p_numero_serie),
        IFNULL(p_fecha_emision, CURDATE()),
        IFNULL(p_monto_subtotal, 0.00),
        IFNULL(p_monto_igv, 0.00),
        p_monto_total,
        p_id_solicitud_gasto,
        p_id_fondo_entrega,
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
    IN p_id_solicitud_gasto INT,
    IN p_id_fondo_entrega INT,
    IN p_id_moneda INT
)
BEGIN
    IF p_id_comprobante IS NULL OR p_id_comprobante <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de comprobante inválido';
    END IF;

    IF p_tipo_documento IS NULL OR TRIM(p_tipo_documento) = '' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El tipo de documento es obligatorio';
    END IF;

    IF p_monto_total IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El monto total es obligatorio';
    END IF;

    UPDATE ope_comprobante_pago
       SET tipo_documento = TRIM(p_tipo_documento),
           ruc_proveedor = TRIM(p_ruc_proveedor),
           razon_social = TRIM(p_razon_social),
           numero_serie = TRIM(p_numero_serie),
           fecha_emision = IFNULL(p_fecha_emision, fecha_emision),
           monto_subtotal = IFNULL(p_monto_subtotal, 0.00),
           monto_igv = IFNULL(p_monto_igv, 0.00),
           monto_total = p_monto_total,
           id_solicitud_gasto = p_id_solicitud_gasto,
           id_fondo_entrega = p_id_fondo_entrega,
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

    -- TODO Revisar si colocar el borrado fisico o logico o ninguno
    DELETE FROM ope_comprobante_pago
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
        id_solicitud_gasto, 
        id_fondo_entrega, 
        id_moneda
    FROM ope_comprobante_pago
    WHERE id_comprobante = p_id_comprobante;
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
        id_solicitud_gasto, 
        id_fondo_entrega, 
        id_moneda
    FROM ope_comprobante_pago
    ORDER BY id_comprobante DESC;
END$$

DROP PROCEDURE IF EXISTS pa_listar_comprobantes_por_solicitud $$
CREATE PROCEDURE pa_listar_comprobantes_por_solicitud(
    IN p_id_solicitud_gasto INT
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
        id_solicitud_gasto, 
        id_fondo_entrega, 
        id_moneda
    FROM ope_comprobante_pago
    WHERE id_solicitud_gasto = p_id_solicitud_gasto
    ORDER BY id_comprobante ASC;
END$$

DELIMITER ;
DELIMETER $$


CREATE PROCEDURE sp_insertar_tes_fondo(
    IN p_nombre_fondo VARCHAR(100),
    IN p_monto_saldo_actual DECIMAL(12,2),
    IN p_estado_fondo VARCHAR(20),
    IN p_id_moneda INT,
    IN p_id_cuenta_bancaria INT,
    IN p_id_usuario_responsable INT,
    OUT _id_moneda INT
)
BEGIN
    -- 1. Validaciones de obligatoriedad
    IF p_nombre_fondo IS NULL OR TRIM(p_nombre_fondo) = '' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error: El nombre del fondo es obligatorio.';
    END IF;

    IF p_estado_fondo IS NULL OR TRIM(p_estado_fondo) = '' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error: El estado del fondo es obligatorio.';
    END IF;

    IF p_id_moneda IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error: Debe especificar una moneda válida.';
    END IF;

    -- 2. Inserción si todo es correcto
    INSERT INTO tes_fondo (
        nombre_fondo,
        monto_saldo_actual,
        estado_fondo,
        id_moneda,
        id_cuenta_bancaria,
        id_usuario_responsable
    ) 
    VALUES (
        p_nombre_fondo,
        COALESCE(p_monto_saldo_actual, 0.00), -- Si viene null, ponemos 0
        p_estado_fondo,
        p_id_moneda,
        p_id_cuenta_bancaria,
        p_id_usuario_responsable
    );

    SET _id_moneda= @@last_insert_id;
END $$

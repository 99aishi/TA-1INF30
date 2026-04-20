DELIMITER $$


-- CRUD TES_FONDO 



CREATE PROCEDURE sp_insertar_tes_fondo(
    IN p_nombre_fondo VARCHAR(100),
    IN p_monto_saldo_actual DECIMAL(12,2),
    IN p_estado_fondo VARCHAR(20),
    IN p_id_moneda INT,
    IN p_id_cuenta_bancaria INT,
    IN p_id_usuario_responsable INT
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
        id_usuario_responsable,
        id_usuario_creacion,
        id_usuario_modificacion
    ) 
    VALUES (
        p_nombre_fondo,
        COALESCE(p_monto_saldo_actual, 0.00), -- Si viene null, ponemos 0
        p_estado_fondo,
        p_id_moneda,
        p_id_cuenta_bancaria,
        p_id_usuario_responsable,
        p_id_usuario_creacion,
        p_id_usuario_creacion
    );

    SELECT LAST_INSERT_ID() AS id_fondo_generado;
END $$

CREATE PROCEDURE sp_modifcar_tes_fondo(
    IN p_id_fondo INT,
    IN p_nombre_fondo VARCHAR(100),
    IN p_monto_saldo_actual DECIMAL(12,2),
    IN p_estado_fondo VARCHAR(20),
    IN p_id_moneda INT,
    IN p_id_cuenta_bancaria INT,
    IN p_id_usuario_responsable INT,
    IN p_id_usuario_modificacion INT
)
BEGIN
	IF NOT EXISTS (SELECT 1 FROM tes_fondo WHERE id_fondo = p_id_fondo) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error: El fondo especificado no existe.';
    END IF;


    IF p_nombre_fondo IS NULL OR TRIM(p_nombre_fondo) = '' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error: El nombre no puede estar vacío.';
    END IF;

    -- 3. Ejecutar actualización
    UPDATE tes_fondo
    SET 
        nombre_fondo = p_nombre_fondo,
        monto_saldo_actual = p_monto_saldo_actual,
        estado_fondo = p_estado_fondo,
        id_moneda = p_id_moneda,
        id_cuenta_bancaria = p_id_cuenta_bancaria,
        id_usuario_responsable = p_id_usuario_responsable
    WHERE id_fondo = p_id_fondo;
END $$

CREATE PROCEDURE sp_mostrar_fondos(
    IN p_id_fondo INT -- Pasar NULL para listar todos
)
BEGIN
    IF p_id_fondo IS NULL THEN
        SELECT * FROM tes_fondo;
    ELSE
        SELECT * FROM tes_fondo 
        WHERE id_fondo = p_id_fondo;
    END IF;
END $$

CREATE PROCEDURE sp_eliminar_fondo(
	IN p_id_fondo INT
)
BEGIN 
	UPDATE tes_fondo 
		SET estado_fondo="Inactivo"
		WHERE p_id_fondo = id_fondo


-- CRUD TES_
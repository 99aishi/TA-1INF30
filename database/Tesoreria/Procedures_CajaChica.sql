DROP PROCEDURE IF EXISTS pa_insertar_tes_caja_chica;
DELIMITER $$

$$
CREATE PROCEDURE pa_insertar_tes_caja_chica(
    OUT _id_fondo INT,
    IN p_nombre_fondo VARCHAR(100),
    IN p_monto_saldo_actual DECIMAL(12,2),
    IN p_estado_fondo VARCHAR(20),
       
    IN p_monto_techo DECIMAL(12,2),
    IN p_id_area INT
)
BEGIN
    -- 1. Validaciones de obligatoriedad
    IF p_nombre_fondo IS NULL OR TRIM(p_nombre_fondo) = '' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error: El nombre del fondo es obligatorio.';
    END IF;

    IF p_estado_fondo IS NULL OR TRIM(p_estado_fondo) = '' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error: El estado del fondo es obligatorio.';
    END IF;

    IF p_monto_techo IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error: El monto techo es obligatorio.';
    END IF;

    -- 2. Inserción si todo es correcto
    INSERT INTO tes_fondo (
        nombre_fondo,
        monto_saldo_actual,
        estado_fondo
    ) 
    VALUES (
        p_nombre_fondo,
        COALESCE(p_monto_saldo_actual, 0.00), -- Si viene null, ponemos 0
        p_estado_fondo
    );
    SET _id_fondo = LAST_INSERT_ID();

    INSERT INTO tes_caja_chica(id_fondo, monto_techo,id_area) VALUES(_id_fondo, p_monto_techo,p_id_area);



END $$



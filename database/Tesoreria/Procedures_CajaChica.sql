DROP PROCEDURE IF EXISTS pa_insertar_tes_caja_chica;
DROP PROCEDURE IF EXISTS pa_modificar_tes_caja_chica;
DROP PROCEDURE IF EXISTS pa_eliminar_caja_chica;
DROP PROCEDURE IF EXISTS pa_buscar_por_id_caja_chica;
DROP PROCEDURE IF EXISTS pa_listar_caja_chica;
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



$$
CREATE PROCEDURE pa_modificar_tes_caja_chica(
    IN p_id_fondo INT, 
    IN p_monto_saldo_actual DECIMAL(12,2), 
    IN p_monto_techo DECIMAL(12,2)
)
BEGIN
    -- Declaramos un manejador de errores para hacer ROLLBACK si algo falla
    -- DECLARE EXIT HANDLER FOR SQLEXCEPTION
    -- BEGIN
       -- ROLLBACK;
       -- SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error: No se pudo actualizar la Caja Chica.';
    -- END;

    -- START TRANSACTION;

    UPDATE tes_fondo 
    SET monto_saldo_actual = p_monto_saldo_actual,
    WHERE id_fondo = p_id_fondo;

    UPDATE tes_caja_chica 
    SET monto_techo = p_monto_techo
    WHERE id_fondo = p_id_fondo;

    -- Validar si realmente se actualizó algo (ROW_COUNT() > 0)
    -- Si no se actualizó nada, es porque el ID no existe
    -- IF ROW_COUNT() = 0 THEN
       -- SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error: No se encontró una Caja Chica con ese ID.';
    -- END IF;

    -- COMMIT;
END $$
$$


CREATE PROCEDURE pa_eliminar_caja_chica(
    IN p_id_fondo INT
)
BEGIN 
    UPDATE tes_fondo SET estado_fondo='Inactivo' WHERE id_fondo=p_id_fondo;
END

 
$$


CREATE PROCEDURE pa_buscar_por_id_caja_chica(
    IN p_id_fondo INT
)
BEGIN
    SELECT f.id_fondo, f.nombre_fondo,f.monto_saldo_actual, f.estado_fondo, c.monto_techo,c.id_area 
    FROM tes_fondo f JOIN tes_caja_chica c ON f.id_fondo=c.id_fondo AND f.id_fondo=p_id_fondo;
END 


$$



CREATE PROCEDURE pa_listar_caja_chica()
BEGIN
    SELECT f.id_fondo, f.nombre_fondo,f.monto_saldo_actual, f.estado_fondo, c.monto_techo,c.id_area
    FROM tes_fondo f JOIN tes_caja_chica c ON f.id_fondo=c.id_fondo ;
END
    $$
    
DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_tes_caja_chica $$
CREATE PROCEDURE pa_insertar_tes_caja_chica(
    IN p_id_fondo INT,
    IN p_monto_techo DECIMAL(12,2),
    IN p_id_area INT
)
BEGIN
    INSERT INTO tes_caja_chica (
        id_fondo,
        monto_techo,
        id_area
    ) 
    VALUES (
        p_id_fondo,
        p_monto_techo,
        p_id_area
    );
END$$

DROP PROCEDURE IF EXISTS pa_modificar_tes_caja_chica $$
CREATE PROCEDURE pa_modificar_tes_caja_chica(
    IN p_id_fondo INT,
    IN p_monto_techo DECIMAL(12,2),
    IN p_id_area INT
)
BEGIN
    IF p_id_fondo IS NULL OR p_id_fondo <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de caja chica no válido';
    END IF;

    UPDATE tes_caja_chica 
    SET monto_techo = p_monto_techo,
        id_area = p_id_area
    WHERE id_fondo = p_id_fondo;
END$$


DROP PROCEDURE IF EXISTS pa_eliminar_caja_chica $$
CREATE PROCEDURE pa_eliminar_caja_chica(
    IN p_id_fondo INT
)
BEGIN 
    IF p_id_fondo IS NULL OR p_id_fondo <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de caja chica no válido';
    END IF;

    pa_eliminar_fondo(p_id_fondo);

END$$

DROP PROCEDURE IF EXISTS pa_buscar_por_id_caja_chica $$
CREATE PROCEDURE pa_buscar_por_id_caja_chica(
    IN p_id_fondo INT
)
BEGIN
    IF p_id_fondo IS NULL OR p_id_fondo <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de caja chica no válido';
    END IF;

    SELECT 
        f.id_fondo, 
        f.nombre_fondo,
        c.monto_techo,
        c.id_area
    FROM tes_fondo f 
    WHERE f.id_fondo = p_id_fondo and f.estado_fondo='Activo'
    JOIN tes_caja_chica c 
        ON f.id_fondo=c.id_fondo;
END$$

DROP PROCEDURE IF EXISTS pa_listar_caja_chica $$
CREATE PROCEDURE pa_listar_caja_chica()
BEGIN
    SELECT 
        f.id_fondo, 
        f.nombre_fondo,
        c.monto_techo,
        c.id_area
    FROM tes_fondo f 
    WHERE f.estado_fondo='Activo'
    JOIN tes_caja_chica c 
        ON f.id_fondo=c.id_fondo;
END$$

DELIMITER ;
    
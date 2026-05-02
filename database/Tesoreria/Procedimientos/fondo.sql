DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_fondo $$
CREATE PROCEDURE pa_insertar_fondo(
	OUT p_id_fondo INT,
    IN p_nombre_fondo VARCHAR(100),
    IN p_estado_fondo VARCHAR(20),
)
BEGIN   
	INSERT INTO tes_fondo(nombre_fondo, estado_fondo) 
    VALUES(p_nombre_fondo, p_estado_fondo);

    SET p_id_fondo=@@last_insert_id;
END$$

DROP PROCEDURE IF EXISTS pa_modificar_fondo $$
CREATE PROCEDURE pa_modificar_fondo(
	IN p_id_fondo INT,
    IN p_nombre_fondo VARCHAR(100),
    IN p_estado_fondo VARCHAR(20)
)
BEGIN
	IF p_id_fondo IS NULL OR p_id_fondo <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de fondo no válido';
    END IF;

	UPDATE tes_fondo SET 
        nombre_fondo =p_nombre_fondo, 
	    estado_fondo=p_estado_fondo
	WHERE id_fondo=p_id_fondo;
END$$

DROP PROCEDURE IF EXISTS pa_eliminar_fondo $$
CREATE PROCEDURE pa_eliminar_fondo(
	IN p_id_fondo INT
)
BEGIN
	IF p_id_fondo IS NULL OR p_id_fondo <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de fondo no válido';
    END IF;

	UPDATE tes_fondo SET estado_fondo = 'Inactivo' 
    WHERE id_fondo = p_id_fondo;
END$$
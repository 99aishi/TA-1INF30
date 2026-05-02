DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_rol $$
CREATE PROCEDURE pa_insertar_rol(
    IN p_titulo_rol VARCHAR(50),
    IN p_descripcion_rol VARCHAR(200),
    OUT p_id_generado INT
)
BEGIN

    INSERT INTO rrhh_rol(
        titulo_rol,
        descripcion_rol, 
        esta_activo
    )
    VALUES(
        p_titulo_rol,
        p_descripcion_rol, 
        1
    );
    
    SET p_id_generado = LAST_INSERT_ID();
END;

DROP PROCEDURE IF EXISTS pa_modificar_rol $$
CREATE PROCEDURE pa_modificar_rol(
    IN p_id_rol INT,
    IN p_titulo_rol VARCHAR(50),
    IN p_descripcion_rol VARCHAR(200)
)
BEGIN
    IF p_id_rol IS NULL OR p_id_rol <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de rol inválido';
    END IF;

    UPDATE rrhh_rol
       SET titulo_rol = p_titulo_rol,
           descripcion_rol = p_descripcion_rol
     WHERE id_rol = p_id_rol;
END$$

DROP PROCEDURE IF EXISTS pa_eliminar_rol $$
CREATE PROCEDURE pa_eliminar_rol(
    IN p_id_rol INT
)
BEGIN
    IF p_id_rol IS NULL OR p_id_rol <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de rol inválido';
    END IF;
    UPDATE rrhh_rol
       SET esta_activo = 0
     WHERE id_rol = p_id_rol;
END$$

DROP PROCEDURE IF EXISTS pa_buscar_rol_por_id $$
CREATE PROCEDURE pa_buscar_rol_por_id(
    IN p_id_rol INT
)
BEGIN
    IF p_id_rol IS NULL OR p_id_rol <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de rol inválido';
    END IF;
    SELECT 
        id_rol, 
        titulo_rol, 
        descripcion_rol
    FROM rrhh_rol
    WHERE id_rol = p_id_rol and esta_activo = 1;
END$$

DROP PROCEDURE IF EXISTS pa_listar_roles $$
CREATE PROCEDURE pa_listar_roles()
BEGIN
    SELECT 
        id_rol, 
        titulo_rol, 
        descripcion_rol
    FROM rrhh_rol
    WHERE esta_activo = 1
    ORDER BY titulo_rol;
END$$

DELIMITER ;
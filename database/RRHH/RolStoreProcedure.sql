
DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_rol $$
CREATE PROCEDURE pa_insertar_rol(
    IN p_id_usuario_accion INT,
IN p_titulo_rol VARCHAR(50),
IN p_descripcion_rol VARCHAR(200),
IN p_id_area INT,
OUT p_id_generado INT
)
BEGIN

    INSERT INTO rrhh_rol(

        titulo_rol,
        descripcion_rol,
        id_area,
        esta_activo,
        id_usuario_creacion,
        id_usuario_modificacion    )
    VALUES(

        p_titulo_rol,
        p_descripcion_rol,
        p_id_area,
        1,
        p_id_usuario_accion,
        p_id_usuario_accion    );
    
    SET p_id_generado = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS pa_modificar_rol $$
CREATE PROCEDURE pa_modificar_rol(

        IN p_id_usuario_accion INT,
IN p_id_rol INT,
    IN p_titulo_rol VARCHAR(50),
    IN p_descripcion_rol VARCHAR(200),
    IN p_id_area INT
)
BEGIN
    IF p_id_rol IS NULL OR p_id_rol <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de rol inválido';
    END IF;

    UPDATE rrhh_rol
       SET titulo_rol = p_titulo_rol,
           descripcion_rol = p_descripcion_rol,
           id_area = p_id_area,
           id_usuario_modificacion = p_id_usuario_accion
    WHERE id_rol = p_id_rol;
END$$

DROP PROCEDURE IF EXISTS pa_eliminar_rol $$
CREATE PROCEDURE pa_eliminar_rol(

        IN p_id_usuario_accion INT,
IN p_id_rol INT

)
BEGIN
    IF p_id_rol IS NULL OR p_id_rol <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de rol inválido';
    END IF;
    UPDATE rrhh_rol
       SET esta_activo = 0,
           id_usuario_modificacion = p_id_usuario_accion
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
        descripcion_rol,
        id_area,
        esta_activo
    FROM rrhh_rol
    WHERE id_rol = p_id_rol;
END$$

DROP PROCEDURE IF EXISTS pa_listar_roles $$
CREATE PROCEDURE pa_listar_roles()
BEGIN
    SELECT 
        id_rol, 
        titulo_rol, 
        descripcion_rol,
        id_area,
        esta_activo
    FROM rrhh_rol
    ORDER BY esta_activo DESC, id_rol;
END$$

DROP PROCEDURE IF EXISTS pa_listar_roles_por_area $$
CREATE PROCEDURE pa_listar_roles_por_area(
    IN p_id_area INT
)
BEGIN
    IF p_id_area IS NULL OR p_id_area <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de área inválido';
    END IF;

    SELECT 
        id_rol, 
        titulo_rol, 
        descripcion_rol,
        id_area,
        esta_activo
    FROM rrhh_rol
    WHERE id_area = p_id_area
      AND esta_activo = 1
    ORDER BY id_rol;
END$$

DELIMITER ;
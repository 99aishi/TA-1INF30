DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_rol $$
CREATE PROCEDURE pa_insertar_rol(
    IN p_id_usuario_accion INT,
IN p_titulo_rol VARCHAR(50),
IN p_descripcion_rol VARCHAR(200),OUT p_id_generado INT
)
BEGIN

    INSERT INTO rrhh_rol(

        titulo_rol,
        descripcion_rol, 
        esta_activo,
        id_usuario_creacion,
        id_usuario_modificacion    )
    VALUES(

        p_titulo_rol,
        p_descripcion_rol, 
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
    IN p_descripcion_rol VARCHAR(200)
)
BEGIN
    IF p_id_rol IS NULL OR p_id_rol <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de rol inválido';
    END IF;

    UPDATE rrhh_rol
       SET titulo_rol = p_titulo_rol,
           descripcion_rol = p_descripcion_rol,
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
        esta_activo
    FROM rrhh_rol
    ORDER BY esta_activo DESC, id_rol;
END$$

DELIMITER ;
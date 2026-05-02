DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_usuario $$
CREATE PROCEDURE pa_insertar_usuario(
    IN p_nombres VARCHAR(60),
    IN p_apellido_paterno VARCHAR(40),
    IN p_apellido_materno VARCHAR(40),
    IN p_password_hash VARCHAR(255),
    OUT p_id_generado INT
)
BEGIN
    INSERT INTO rrhh_usuario(
        nombres,
        apellido_paterno,
        apellido_materno,
        password_hash,
        esta_activo
    )
    VALUES(
        p_nombres,
        p_apellido_paterno,
        p_apellido_materno,
        p_password_hash,
        1
    );
    
    SET p_id_generado = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS pa_modificar_usuario $$
CREATE PROCEDURE pa_modificar_usuario(
    IN p_id_usuario INT,
    IN p_nombres VARCHAR(60),
    IN p_apellido_paterno VARCHAR(40),
    IN p_apellido_materno VARCHAR(40),
    IN p_password_hash VARCHAR(255)
)
BEGIN
    IF p_id_usuario IS NULL OR p_id_usuario <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de usuario inválido';
    END IF;

    UPDATE rrhh_usuario
       SET nombres = p_nombres,
           apellido_paterno = p_apellido_paterno,
           apellido_materno = p_apellido_materno,
           password_hash = p_password_hash
     WHERE id_usuario = p_id_usuario;
END$$

DROP PROCEDURE IF EXISTS pa_eliminar_usuario $$
CREATE PROCEDURE pa_eliminar_usuario(
    IN p_id_usuario INT
)
BEGIN
    IF p_id_usuario IS NULL OR p_id_usuario <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de usuario inválido';
    END IF;

    UPDATE rrhh_usuario
       SET esta_activo = 0
    WHERE id_usuario = p_id_usuario;
END$$

DROP PROCEDURE IF EXISTS pa_buscar_usuario_por_id $$
CREATE PROCEDURE pa_buscar_usuario_por_id(
    IN p_id_usuario INT
)
BEGIN
    IF p_id_usuario IS NULL OR p_id_usuario <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de usuario inválido';
    END IF;

    SELECT 
        id_usuario, 
        nombres, 
        apellido_paterno, 
        apellido_materno
    FROM rrhh_usuario
    WHERE id_usuario = p_id_usuario and esta_activo = 1;
END$$

DROP PROCEDURE IF EXISTS pa_listar_usuarios  $$
CREATE PROCEDURE pa_listar_usuarios()
BEGIN
    SELECT 
        id_usuario, 
        nombres, 
        apellido_paterno, 
        apellido_materno
    FROM rrhh_usuario
    WHERE esta_activo = 1
    ORDER BY id_usuario;
END$$

DELIMITER ;
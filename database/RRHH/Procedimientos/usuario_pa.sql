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
    
    IF p_nombres IS NULL OR TRIM(p_nombres) = '' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Los nombres son obligatorios';
    END IF;

    IF p_apellido_paterno IS NULL OR TRIM(p_apellido_paterno) = '' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El apellido paterno es obligatorio';
    END IF;

    IF p_password_hash IS NULL OR TRIM(p_password_hash) = '' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La contraseña hash es obligatoria';
    END IF;

    INSERT INTO rrhh_usuario(
        nombres,
        apellido_paterno,
        apellido_materno,
        password_hash,
        esta_activo
    )
    VALUES(
        TRIM(p_nombres),
        TRIM(p_apellido_paterno),
        TRIM(p_apellido_materno),
        TRIM(p_password_hash),
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
       SET nombres = TRIM(p_nombres),
           apellido_paterno = TRIM(p_apellido_paterno),
           apellido_materno = TRIM(p_apellido_materno),
           password_hash = TRIM(p_password_hash)
     WHERE id_usuario = p_id_usuario;
END$$

DROP PROCEDURE IF EXISTS pa_eliminar_usuario $$
CREATE PROCEDURE pa_eliminar_usuario(
    IN p_id_usuario INT
)
BEGIN
    UPDATE rrhh_usuario
       SET esta_activo = 0
    WHERE id_usuario = p_id_usuario;
END$$

DROP PROCEDURE IF EXISTS pa_buscar_usuario_por_id $$
CREATE PROCEDURE pa_buscar_usuario_por_id(
    IN p_id_usuario INT
)
BEGIN
    SELECT 
        id_usuario, 
        nombres, 
        apellido_paterno, 
        apellido_materno, 
        esta_activo
    FROM rrhh_usuario
    WHERE id_usuario = p_id_usuario;
END$$

DROP PROCEDURE IF EXISTS pa_listar_usuarios  $$
CREATE PROCEDURE pa_listar_usuarios()
BEGIN
    SELECT 
        id_usuario, 
        nombres, 
        apellido_paterno, 
        apellido_materno, 
        esta_activo
    FROM rrhh_usuario
    ORDER BY id_usuario;
END$$

DELIMITER ;
DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_usuario $$
CREATE PROCEDURE pa_insertar_usuario(
    IN p_id_usuario_accion INT,
IN p_nombres VARCHAR(60),
IN p_apellido_paterno VARCHAR(40),
IN p_apellido_materno VARCHAR(40),
IN p_password_hash VARCHAR(255),
IN p_correo VARCHAR(255),OUT p_id_generado INT
)
BEGIN
    INSERT INTO rrhh_usuario(

        nombres,
        apellido_paterno,
        apellido_materno,
        password_hash,
        correo,
        esta_activo,
        id_usuario_creacion,
        id_usuario_modificacion    )
    VALUES(

        p_nombres,
        p_apellido_paterno,
        p_apellido_materno,
        p_password_hash,
        p_correo,
        1,
        p_id_usuario_accion,
        p_id_usuario_accion    );
    
    SET p_id_generado = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS pa_modificar_usuario $$
CREATE PROCEDURE pa_modificar_usuario(

        IN p_id_usuario_accion INT,
IN p_id_usuario INT,
    IN p_nombres VARCHAR(60),
    IN p_apellido_paterno VARCHAR(40),
    IN p_apellido_materno VARCHAR(40),
    IN p_password_hash VARCHAR(255),
    IN p_correo VARCHAR(255)
)
BEGIN
    IF p_id_usuario IS NULL OR p_id_usuario <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de usuario invalido';
    END IF;

    UPDATE rrhh_usuario
       SET nombres = p_nombres,
           apellido_paterno = p_apellido_paterno,
           apellido_materno = p_apellido_materno,
           password_hash = p_password_hash,
           correo = p_correo,
           id_usuario_modificacion = p_id_usuario_accion
    WHERE id_usuario = p_id_usuario;
END$$

DROP PROCEDURE IF EXISTS pa_eliminar_usuario $$
CREATE PROCEDURE pa_eliminar_usuario(
    IN p_id_usuario_accion INT,
	IN p_id_usuario INT
)
BEGIN
    IF p_id_usuario IS NULL OR p_id_usuario <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de usuario invalido';
    END IF;

    UPDATE rrhh_usuario
       SET esta_activo = 0,
           id_usuario_modificacion = p_id_usuario_accion
    WHERE id_usuario = p_id_usuario;
END$$

DROP PROCEDURE IF EXISTS pa_buscar_usuario_por_correo $$
CREATE PROCEDURE pa_buscar_usuario_por_correo(
    IN p_correo VARCHAR(255)
)
BEGIN
    SELECT 
        u.id_usuario,
        u.nombres,
        u.apellido_paterno,
        u.apellido_materno,
        u.password_hash,
        u.correo,
        CASE WHEN u.esta_activo = 1 THEN 'ACTIVO' ELSE 'INACTIVO' END AS estado,
        CASE 
            WHEN e.id_usuario IS NOT NULL THEN 'EMPLEADO'
            WHEN a.id_usuario IS NOT NULL THEN 'ADMINISTRADOR'
            ELSE 'EMPLEADO'
        END AS tipo_usuario,
        e.id_usuario AS id_empleado,
        e.id_area,
        ar.nombre_area AS nombre_area,
        e.id_rol,
        ro.titulo_rol AS titulo_rol
    FROM rrhh_usuario u
    LEFT JOIN rrhh_empleado e ON u.id_usuario = e.id_usuario
    LEFT JOIN rrhh_administrador a ON u.id_usuario = a.id_usuario
    LEFT JOIN rrhh_area ar ON e.id_area = ar.id_area
    LEFT JOIN rrhh_rol ro ON e.id_rol = ro.id_rol
    WHERE u.correo = p_correo;
END$$

DELIMITER ;
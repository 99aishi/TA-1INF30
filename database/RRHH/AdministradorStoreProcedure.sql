
DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_administrador $$
CREATE PROCEDURE pa_insertar_administrador(
    IN p_id_usuario_accion INT,
	IN p_id_usuario INT
)
BEGIN
    IF p_id_usuario IS NULL OR p_id_usuario <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El ID del administrador no es valido';
    END IF;

    INSERT INTO rrhh_administrador(

        id_usuario,
        id_usuario_creacion,
        id_usuario_modificacion    )
    VALUES(

        p_id_usuario,
        p_id_usuario_accion,
        p_id_usuario_accion    );
END$$

DROP PROCEDURE IF EXISTS pa_modificar_administrador $$
CREATE PROCEDURE pa_modificar_administrador(
    IN p_id_usuario_accion INT,
    IN p_id_usuario INT
)
BEGIN
    IF p_id_usuario IS NULL OR p_id_usuario <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de administrador no valido';
    END IF;

    UPDATE rrhh_administrador
       SET id_usuario_modificacion = p_id_usuario_accion
     WHERE id_usuario = p_id_usuario;
END$$

DROP PROCEDURE IF EXISTS pa_eliminar_administrador $$
CREATE PROCEDURE pa_eliminar_administrador(

    IN p_id_usuario_accion INT,
	IN p_id_usuario INT
)
BEGIN
    IF p_id_usuario IS NULL OR p_id_usuario <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de administrador no valido';
    END IF;

    UPDATE rrhh_usuario
       SET esta_activo = 0,
           id_usuario_modificacion = p_id_usuario_accion
    WHERE id_usuario = p_id_usuario;
END$$

DROP PROCEDURE IF EXISTS pa_buscar_administrador_por_id $$
CREATE PROCEDURE pa_buscar_administrador_por_id(
    IN p_id_usuario INT
)
BEGIN
    IF p_id_usuario IS NULL OR p_id_usuario <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de administrador no valido';
    END IF;

    SELECT
        u.id_usuario,
        u.nombres,
        u.apellido_paterno,
        u.apellido_materno,
        u.password_hash,
        u.correo,
        CASE WHEN u.esta_activo = 1 THEN 'ACTIVO' ELSE 'INACTIVO' END AS estado_usuario,
        a.id_usuario
    FROM rrhh_administrador a
    INNER JOIN rrhh_usuario u ON a.id_usuario = u.id_usuario
    WHERE a.id_usuario = p_id_usuario
      AND u.esta_activo = 1;
END$$

DROP PROCEDURE IF EXISTS pa_listar_administradores $$
CREATE PROCEDURE pa_listar_administradores()
BEGIN
    SELECT
        u.id_usuario,
        u.nombres,
        u.apellido_paterno,
        u.apellido_materno,
        u.password_hash,
        u.correo,
        CASE WHEN u.esta_activo = 1 THEN 'ACTIVO' ELSE 'INACTIVO' END AS estado_usuario,
        a.id_usuario
    FROM rrhh_administrador a
    INNER JOIN rrhh_usuario u ON a.id_usuario = u.id_usuario
    WHERE u.esta_activo = 1
    ORDER BY a.id_usuario;
END$$

DROP PROCEDURE IF EXISTS pa_listar_todos_administradores $$
CREATE PROCEDURE pa_listar_todos_administradores()
BEGIN
    SELECT
        u.id_usuario,
        u.nombres,
        u.apellido_paterno,
        u.apellido_materno,
        u.password_hash,
        u.correo,
        CASE WHEN u.esta_activo = 1 THEN 'ACTIVO' ELSE 'INACTIVO' END AS estado_usuario,
        a.id_usuario
    FROM rrhh_administrador a
    INNER JOIN rrhh_usuario u ON a.id_usuario = u.id_usuario
    ORDER BY a.id_usuario;
END$$

DROP PROCEDURE IF EXISTS pa_obtener_password_administrador_por_correo $$
CREATE PROCEDURE pa_obtener_password_administrador_por_correo(
    IN p_correo VARCHAR(255)
)
BEGIN
    SELECT
        u.password_hash
    FROM rrhh_administrador a
    INNER JOIN rrhh_usuario u ON a.id_usuario = u.id_usuario
    WHERE u.correo = p_correo
      AND u.esta_activo = 1;
END$$

DELIMITER ;
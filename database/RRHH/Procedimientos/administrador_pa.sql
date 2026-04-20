DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_administrador $$
CREATE PROCEDURE pa_insertar_administrador(
    IN p_id_usuario INT,
    IN p_correo_soporte VARCHAR(100)
)
BEGIN
    IF p_id_usuario IS NULL OR p_id_usuario <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El ID de usuario es obligatorio para el administrador';
    END IF;

    INSERT INTO rrhh_administrador(
        id_usuario,
        correo_soporte
    )
    VALUES(
        p_id_usuario,
        TRIM(p_correo_soporte)
    );
END$$

DROP PROCEDURE IF EXISTS pa_modificar_administrador $$
CREATE PROCEDURE pa_modificar_administrador(
    IN p_id_usuario INT,
    IN p_correo_soporte VARCHAR(100)
)
BEGIN
    IF p_id_usuario IS NULL OR p_id_usuario <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de administrador no válido';
    END IF;

    UPDATE rrhh_administrador
       SET correo_soporte = TRIM(p_correo_soporte)
     WHERE id_usuario = p_id_usuario;
END$$

DROP PROCEDURE IF EXISTS pa_eliminar_administrador $$
CREATE PROCEDURE pa_eliminar_administrador(
    IN p_id_usuario INT
)
BEGIN
    UPDATE rrhh_usuario
       SET esta_activo = 0
     WHERE id_usuario = p_id_usuario;
END$$

DROP PROCEDURE IF EXISTS pa_buscar_administrador_por_id $$
CREATE PROCEDURE pa_buscar_administrador_por_id(
    IN p_id_usuario INT
)
BEGIN
    SELECT 
        a.id_usuario, 
        a.correo_soporte
    FROM rrhh_administrador a
    INNER JOIN rrhh_usuario u ON a.id_usuario = u.id_usuario
    WHERE a.id_usuario = p_id_usuario
      AND u.esta_activo = 1;
END$$

DROP PROCEDURE IF EXISTS pa_listar_administradores $$
CREATE PROCEDURE pa_listar_administradores()
BEGIN
    SELECT 
        a.id_usuario, 
        a.correo_soporte
    FROM rrhh_administrador a
    INNER JOIN rrhh_usuario u ON a.id_usuario = u.id_usuario
    WHERE u.esta_activo = 1
    ORDER BY a.id_usuario;
END$$

DELIMITER ;
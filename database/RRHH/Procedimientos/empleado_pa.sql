DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_empleado $$
CREATE PROCEDURE pa_insertar_empleado(
    IN p_id_usuario INT,
    IN p_correo_institucional VARCHAR(100),
    IN p_numero_celular VARCHAR(15),
    IN p_id_area INT,
    IN p_id_rol INT,
    IN p_id_jefe_directo INT
)
BEGIN
    -- Validamos solo los campos que son obligatorios por contrato
    IF p_id_usuario IS NULL OR p_id_usuario <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El ID de usuario es obligatorio para el empleado';
    END IF;

    IF p_correo_institucional IS NULL OR TRIM(p_correo_institucional) = '' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El correo institucional es obligatorio';
    END IF;

    INSERT INTO rrhh_empleado(
        id_usuario,
        correo_institucional,
        numero_celular,
        id_area,
        id_rol,
        id_jefe_directo
    )
    VALUES(
        p_id_usuario,
        TRIM(p_correo_institucional),
        TRIM(p_numero_celular),
        p_id_area,
        p_id_rol,
        p_id_jefe_directo
    );
END$$

DROP PROCEDURE IF EXISTS pa_modificar_empleado $$
CREATE PROCEDURE pa_modificar_empleado(
    IN p_id_usuario INT,
    IN p_correo_institucional VARCHAR(100),
    IN p_numero_celular VARCHAR(15),
    IN p_id_area INT,
    IN p_id_rol INT,
    IN p_id_jefe_directo INT
)
BEGIN
    IF p_id_usuario IS NULL OR p_id_usuario <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El ID del empleado no es válido';
    END IF;

    UPDATE rrhh_empleado
       SET correo_institucional = TRIM(p_correo_institucional),
           numero_celular = TRIM(p_numero_celular),
           id_area = p_id_area,
           id_rol = p_id_rol,
           id_jefe_directo = p_id_jefe_directo
     WHERE id_usuario = p_id_usuario;
END$$

DROP PROCEDURE IF EXISTS pa_eliminar_empleado $$
CREATE PROCEDURE pa_eliminar_empleado(
    IN p_id_usuario INT
)
BEGIN
    UPDATE rrhh_usuario
       SET esta_activo = 0
     WHERE id_usuario = p_id_usuario;
END$$

DROP PROCEDURE IF EXISTS pa_buscar_empleado_por_id $$
CREATE PROCEDURE pa_buscar_empleado_por_id(
    IN p_id_usuario INT
)
BEGIN
    SELECT 
        e.id_usuario, 
        e.correo_institucional, 
        e.numero_celular, 
        e.id_area, 
        e.id_rol, 
        e.id_jefe_directo
    FROM rrhh_empleado e
    INNER JOIN rrhh_usuario u ON e.id_usuario = u.id_usuario
    WHERE e.id_usuario = p_id_usuario
      AND u.esta_activo = 1;
END$$

DROP PROCEDURE IF EXISTS pa_listar_empleados $$
CREATE PROCEDURE pa_listar_empleados()
BEGIN
    SELECT 
        e.id_usuario, 
        e.correo_institucional, 
        e.numero_celular, 
        e.id_area, 
        e.id_rol, 
        e.id_jefe_directo
    FROM rrhh_empleado e
    INNER JOIN rrhh_usuario u ON e.id_usuario = u.id_usuario
    WHERE u.esta_activo = 1
    ORDER BY e.id_usuario;
END$$

DELIMITER ;
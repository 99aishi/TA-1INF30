DELIMITER $$

CREATE PROCEDURE pa_obtener_password_administrador_por_correo(
    IN p_correo VARCHAR(100)
)
BEGIN
    SELECT 
        u.password_hash 
    FROM rrhh_usuario u
    JOIN rrhh_administrador a
        ON u.id_usuario = a.id_usuario
    WHERE a.correo_soporte = p_correo;
END $$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE pa_obtener_password_empleado_por_correo(
    IN p_correo VARCHAR(100)
)
BEGIN
    SELECT 
        u.password_hash
    FROM rrhh_usuario u
    JOIN rrhh_empleado e
        ON u.id_usuario = e.id_usuario
    WHERE e.correo_institucional = p_correo;
END $$

DELIMITER ;



DELIMITER $$

CREATE PROCEDURE pa_buscar_empleados_por_nombre_apellido(
    IN p_busqueda VARCHAR(100)
)
BEGIN
    SELECT 
        u.nombres,
        e.correo_institucional AS correo,
        u.apellido_paterno,
        u.apellido_materno,
        u.password_hash AS password
    FROM rrhh_usuario u
    JOIN rrhh_empleado e
        ON u.id_usuario = e.id_usuario
    WHERE 
        u.nombres LIKE CONCAT('%', p_busqueda, '%')
        OR u.apellido_paterno LIKE CONCAT('%', p_busqueda, '%')
        OR u.apellido_materno LIKE CONCAT('%', p_busqueda, '%')
        OR CONCAT(u.nombres, ' ', u.apellido_paterno, ' ', IFNULL(u.apellido_materno, '')) 
            LIKE CONCAT('%', p_busqueda, '%');
END $$

DELIMITER ;

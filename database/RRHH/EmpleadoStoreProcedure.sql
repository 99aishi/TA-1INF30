DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_empleado $$
CREATE PROCEDURE pa_insertar_empleado(

        IN p_id_usuario_accion INT,
IN p_id_usuario INT,
    IN p_numero_celular VARCHAR(15),
    IN p_id_area INT,
    IN p_id_rol INT,
    IN p_id_jefe_directo INT,
    IN p_rol_flujo ENUM('EMPLEADO','JEFE_AREA')
)
BEGIN
    IF p_id_usuario IS NULL OR p_id_usuario <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El ID de usuario es obligatorio para el empleado';
    END IF;

    INSERT INTO rrhh_empleado(

        id_usuario,
        numero_celular,
        rol_flujo,
        id_area,
        id_rol,
        id_jefe_directo,
        id_usuario_creacion,
        id_usuario_modificacion    )
    VALUES(

        p_id_usuario,
        p_numero_celular,
        p_rol_flujo,
        p_id_area,
        p_id_rol,
        p_id_jefe_directo,
        p_id_usuario_accion,
        p_id_usuario_accion    );
END$$

DROP PROCEDURE IF EXISTS pa_modificar_empleado $$
CREATE PROCEDURE pa_modificar_empleado(

        IN p_id_usuario_accion INT,
IN p_id_usuario INT,
    IN p_numero_celular VARCHAR(15),
    IN p_id_area INT,
    IN p_id_rol INT,
    IN p_id_jefe_directo INT,
    IN p_rol_flujo ENUM('EMPLEADO','JEFE_AREA')
)
BEGIN
    IF p_id_usuario IS NULL OR p_id_usuario <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El ID del empleado no es valido';
    END IF;

    UPDATE rrhh_empleado
       SET numero_celular = p_numero_celular,
           rol_flujo = p_rol_flujo,
           id_area = p_id_area,
           id_rol = p_id_rol,
           id_jefe_directo = p_id_jefe_directo,
           id_usuario_modificacion = p_id_usuario_accion
    WHERE id_usuario = p_id_usuario;
END$$

DROP PROCEDURE IF EXISTS pa_eliminar_empleado $$
CREATE PROCEDURE pa_eliminar_empleado(

        IN p_id_usuario_accion INT,
IN p_id_usuario INT

)
BEGIN
    IF p_id_usuario IS NULL OR p_id_usuario <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El ID del empleado no es valido';
    END IF;

    UPDATE rrhh_usuario
       SET esta_activo = 0,
           id_usuario_modificacion = p_id_usuario_accion
    WHERE id_usuario = p_id_usuario;
END$$

DROP PROCEDURE IF EXISTS pa_buscar_empleado_por_id $$
CREATE PROCEDURE pa_buscar_empleado_por_id(
    IN p_id_usuario INT
)
BEGIN
    IF p_id_usuario IS NULL OR p_id_usuario <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El ID del empleado no es valido';
    END IF;
    SELECT
        u.id_usuario,
        u.nombres,
        u.apellido_paterno,
        u.apellido_materno,
        u.password_hash,
        u.correo,
        e.numero_celular,
        e.rol_flujo,
        e.id_area,
        e.id_rol,
        e.id_jefe_directo,
        CASE WHEN u.esta_activo = 1 THEN 'ACTIVO' ELSE 'INACTIVO' END AS estado_usuario,
        a.id_area AS area_id_area,
        a.nombre_area AS area_nombre,
        a.descripcion_area AS area_descripcion,
        a.id_jefe AS area_id_jefe,
        a.esta_activo AS area_esta_activo,
        r.id_rol AS rol_id_rol,
        r.titulo_rol AS rol_titulo,
        r.descripcion_rol AS rol_descripcion,
        r.esta_activo AS rol_esta_activo,
        j.id_usuario AS jefe_id_usuario,
        uj.nombres AS jefe_nombres,
        uj.apellido_paterno AS jefe_apellido_paterno,
        uj.apellido_materno AS jefe_apellido_materno,
        uj.correo AS jefe_correo,
        j.numero_celular AS jefe_numero_celular,
        j.rol_flujo AS jefe_rol_flujo
    FROM rrhh_empleado e
    INNER JOIN rrhh_usuario u ON e.id_usuario = u.id_usuario
    LEFT JOIN rrhh_area a ON e.id_area = a.id_area
    LEFT JOIN rrhh_rol r ON e.id_rol = r.id_rol
    LEFT JOIN rrhh_empleado j ON e.id_jefe_directo = j.id_usuario
    LEFT JOIN rrhh_usuario uj ON j.id_usuario = uj.id_usuario
    WHERE e.id_usuario = p_id_usuario
      AND u.esta_activo = 1;
END$$

DROP PROCEDURE IF EXISTS pa_listar_empleados $$
CREATE PROCEDURE pa_listar_empleados()
BEGIN
    SELECT
        u.id_usuario,
        u.nombres,
        u.apellido_paterno,
        u.apellido_materno,
        u.password_hash,
        u.correo,
        e.numero_celular,
        e.rol_flujo,
        e.id_area,
        e.id_rol,
        e.id_jefe_directo,
        CASE WHEN u.esta_activo = 1 THEN 'ACTIVO' ELSE 'INACTIVO' END AS estado_usuario,
        a.id_area AS area_id_area,
        a.nombre_area AS area_nombre,
        a.descripcion_area AS area_descripcion,
        a.id_jefe AS area_id_jefe,
        a.esta_activo AS area_esta_activo,
        r.id_rol AS rol_id_rol,
        r.titulo_rol AS rol_titulo,
        r.descripcion_rol AS rol_descripcion,
        r.esta_activo AS rol_esta_activo,
        j.id_usuario AS jefe_id_usuario,
        uj.nombres AS jefe_nombres,
        uj.apellido_paterno AS jefe_apellido_paterno,
        uj.apellido_materno AS jefe_apellido_materno,
        uj.correo AS jefe_correo,
        j.numero_celular AS jefe_numero_celular,
        j.rol_flujo AS jefe_rol_flujo
    FROM rrhh_empleado e
    INNER JOIN rrhh_usuario u ON e.id_usuario = u.id_usuario
    LEFT JOIN rrhh_area a ON e.id_area = a.id_area
    LEFT JOIN rrhh_rol r ON e.id_rol = r.id_rol
    LEFT JOIN rrhh_empleado j ON e.id_jefe_directo = j.id_usuario
    LEFT JOIN rrhh_usuario uj ON j.id_usuario = uj.id_usuario
    WHERE u.esta_activo = 1
    ORDER BY e.id_usuario;
END$$

DROP PROCEDURE IF EXISTS pa_listar_todos_empleados $$
CREATE PROCEDURE pa_listar_todos_empleados()
BEGIN
    SELECT
        u.id_usuario,
        u.nombres,
        u.apellido_paterno,
        u.apellido_materno,
        u.password_hash,
        u.correo,
        e.numero_celular,
        e.rol_flujo,
        e.id_area,
        e.id_rol,
        e.id_jefe_directo,
        CASE WHEN u.esta_activo = 1 THEN 'ACTIVO' ELSE 'INACTIVO' END AS estado_usuario,
        a.id_area AS area_id_area,
        a.nombre_area AS area_nombre,
        a.descripcion_area AS area_descripcion,
        a.id_jefe AS area_id_jefe,
        a.esta_activo AS area_esta_activo,
        r.id_rol AS rol_id_rol,
        r.titulo_rol AS rol_titulo,
        r.descripcion_rol AS rol_descripcion,
        r.esta_activo AS rol_esta_activo,
        j.id_usuario AS jefe_id_usuario,
        uj.nombres AS jefe_nombres,
        uj.apellido_paterno AS jefe_apellido_paterno,
        uj.apellido_materno AS jefe_apellido_materno,
        uj.correo AS jefe_correo,
        j.numero_celular AS jefe_numero_celular,
        j.rol_flujo AS jefe_rol_flujo
    FROM rrhh_empleado e
    INNER JOIN rrhh_usuario u ON e.id_usuario = u.id_usuario
    LEFT JOIN rrhh_area a ON e.id_area = a.id_area
    LEFT JOIN rrhh_rol r ON e.id_rol = r.id_rol
    LEFT JOIN rrhh_empleado j ON e.id_jefe_directo = j.id_usuario
    LEFT JOIN rrhh_usuario uj ON j.id_usuario = uj.id_usuario
    ORDER BY u.esta_activo DESC, e.id_usuario;
END$$

DROP PROCEDURE IF EXISTS pa_obtener_password_empleado_por_correo $$
CREATE PROCEDURE pa_obtener_password_empleado_por_correo(
    IN p_correo VARCHAR(255)
)
BEGIN
    SELECT
        u.password_hash
    FROM rrhh_empleado e
    INNER JOIN rrhh_usuario u ON e.id_usuario = u.id_usuario
    WHERE u.correo = p_correo
      AND u.esta_activo = 1;
END$$

DROP PROCEDURE IF EXISTS pa_buscar_empleados_por_nombre_apellido $$
CREATE PROCEDURE pa_buscar_empleados_por_nombre_apellido(
    IN p_busqueda VARCHAR(200)
)
BEGIN
    SELECT
        u.nombres,
        u.apellido_paterno,
        u.apellido_materno,
        u.correo,
        u.password_hash
    FROM rrhh_empleado e
    INNER JOIN rrhh_usuario u ON e.id_usuario = u.id_usuario
    WHERE u.esta_activo = 1
      AND (u.nombres LIKE CONCAT('%', p_busqueda, '%')
           OR u.apellido_paterno LIKE CONCAT('%', p_busqueda, '%')
           OR u.apellido_materno LIKE CONCAT('%', p_busqueda, '%'));
END$$

DELIMITER ;
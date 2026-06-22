
DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_usuario $$
CREATE PROCEDURE pa_insertar_usuario(
	OUT p_id_generado INT, 
    IN p_id_usuario_accion INT,
    IN p_nombres VARCHAR(60),
    IN p_apellido_paterno VARCHAR(40),
    IN p_apellido_materno VARCHAR(40),
    IN p_password_hash VARCHAR(255),
    IN p_correo VARCHAR(255)
)
BEGIN
    INSERT INTO rrhh_usuario(
        nombres, apellido_paterno, apellido_materno, password_hash, correo,
        creado_at, actualizado_at, id_usuario_creacion, id_usuario_modificacion
    )
    VALUES(
        p_nombres, p_apellido_paterno, p_apellido_materno, p_password_hash, p_correo,
        NOW(), NOW(), p_id_usuario_accion, p_id_usuario_accion
    );
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
    UPDATE rrhh_usuario
       SET nombres = p_nombres,
           apellido_paterno = p_apellido_paterno,
           apellido_materno = p_apellido_materno,
           password_hash = p_password_hash,
           correo = p_correo,
           actualizado_at = NOW(),
           id_usuario_modificacion = p_id_usuario_accion
     WHERE id_usuario = p_id_usuario;
END$$

DROP PROCEDURE IF EXISTS pa_eliminar_usuario $$
CREATE PROCEDURE pa_eliminar_usuario(
    IN p_id_usuario INT
)
BEGIN
    DELETE FROM rrhh_usuario WHERE id_usuario = p_id_usuario;
END$$

DROP PROCEDURE IF EXISTS pa_listar_usuarios $$
CREATE PROCEDURE pa_listar_usuarios()
BEGIN
    SELECT id_usuario, nombres, apellido_paterno, apellido_materno, password_hash, correo
    FROM rrhh_usuario;
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
        e.numero_celular,
        e.rol_flujo,
        e.id_jefe_directo,
        e.id_area,
        ar.nombre_area AS nombre_area,
        ar.descripcion_area AS area_descripcion,
        ar.esta_activo AS area_esta_activo,
        ar.id_jefe AS area_id_jefe,
        e.id_rol,
        ro.titulo_rol AS titulo_rol,
        ro.descripcion_rol AS rol_descripcion,
        ro.esta_activo AS rol_esta_activo,
        jef.id_usuario AS jefe_id_usuario,
        uj.nombres AS jefe_nombres,
        uj.apellido_paterno AS jefe_apellido_paterno,
        uj.apellido_materno AS jefe_apellido_materno,
        uj.correo AS jefe_correo,
        jef.numero_celular AS jefe_numero_celular,
        jef.rol_flujo AS jefe_rol_flujo,
        ar_jef.id_usuario AS area_jefe_id_usuario,
        uar.nombres AS area_jefe_nombres,
        uar.apellido_paterno AS area_jefe_apellido_paterno,
        uar.apellido_materno AS area_jefe_apellido_materno,
        uar.correo AS area_jefe_correo,
        ar_jef.numero_celular AS area_jefe_numero_celular,
        ar_jef.rol_flujo AS area_jefe_rol_flujo
    FROM rrhh_usuario u
    LEFT JOIN rrhh_empleado e ON u.id_usuario = e.id_usuario
    LEFT JOIN rrhh_administrador a ON u.id_usuario = a.id_usuario
    LEFT JOIN rrhh_area ar ON e.id_area = ar.id_area
    LEFT JOIN rrhh_rol ro ON e.id_rol = ro.id_rol
    LEFT JOIN rrhh_empleado jef ON e.id_jefe_directo = jef.id_usuario
    LEFT JOIN rrhh_usuario uj ON jef.id_usuario = uj.id_usuario
    LEFT JOIN rrhh_empleado ar_jef ON ar.id_jefe = ar_jef.id_usuario
    LEFT JOIN rrhh_usuario uar ON ar_jef.id_usuario = uar.id_usuario
    WHERE u.correo = p_correo;
END$$

DELIMITER ;

DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_area  $$
CREATE PROCEDURE pa_insertar_area(
    IN p_id_usuario_accion INT,
IN p_nombre_area VARCHAR(60),
IN p_descripcion_area VARCHAR(500),
IN p_id_jefe INT,
OUT p_id_generado INT
)
BEGIN
    INSERT INTO rrhh_area(

        nombre_area,
        descripcion_area, 
        id_jefe, 
        esta_activo,
        id_usuario_creacion,
        id_usuario_modificacion    )
    VALUES(

        p_nombre_area,
        p_descripcion_area, 
        p_id_jefe, 
        1,
        p_id_usuario_accion,
        p_id_usuario_accion    );

    SET p_id_generado = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS pa_modificar_area $$
CREATE PROCEDURE pa_modificar_area(

        IN p_id_usuario_accion INT,
IN p_id_area INT,
    IN p_nombre_area VARCHAR(60),
    IN p_descripcion_area VARCHAR(500),
    IN p_id_jefe INT

)
BEGIN
    IF p_id_area IS NULL OR p_id_area <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El ID del área no es válido';
    END IF;

    UPDATE rrhh_area
       SET nombre_area = p_nombre_area,
           descripcion_area = p_descripcion_area, 
           id_jefe = p_id_jefe,
           id_usuario_modificacion = p_id_usuario_accion
    WHERE id_area = p_id_area;
END$$

DROP PROCEDURE IF EXISTS pa_asignar_jefe_area $$ 
CREATE PROCEDURE pa_asignar_jefe_area(

        IN p_id_usuario_accion INT,
IN p_id_area INT,
    IN p_id_jefe INT

)
BEGIN
    IF p_id_area IS NULL OR p_id_area <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El ID del área no es válido';
    END IF;
    IF p_id_jefe IS NULL OR p_id_jefe <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El ID del jefe no es válido';
    END IF;

    UPDATE rrhh_area
       SET id_jefe = p_id_jefe,
           id_usuario_modificacion = p_id_usuario_accion
    WHERE id_area = p_id_area;
END$$

DROP PROCEDURE IF EXISTS pa_eliminar_area  $$
CREATE PROCEDURE pa_eliminar_area(

        IN p_id_usuario_accion INT,
IN p_id_area INT

)
BEGIN
    IF p_id_area IS NULL OR p_id_area <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El ID del área no es válido';
    END IF;

    UPDATE rrhh_area
       SET esta_activo = 0,
           id_usuario_modificacion = p_id_usuario_accion
    WHERE id_area = p_id_area;

    UPDATE tes_fondo f
       INNER JOIN tes_caja_chica cc ON f.id_fondo = cc.id_fondo
       SET f.estado_fondo = 'INACTIVO',
           f.id_usuario_modificacion = p_id_usuario_accion
     WHERE cc.id_area = p_id_area;
END$$

DROP PROCEDURE IF EXISTS pa_buscar_area_por_id  $$
CREATE PROCEDURE pa_buscar_area_por_id(
    IN p_id_area INT
)
BEGIN
    IF p_id_area IS NULL OR p_id_area <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El ID del área no es válido';
    END IF;

    SELECT 
        a.id_area, 
        a.nombre_area, 
        a.descripcion_area, 
        a.id_jefe,
        a.esta_activo,
        cc.id_fondo AS id_fondo_caja_chica,
        e.id_usuario AS jefe_id_usuario,
        u.nombres AS jefe_nombres,
        u.apellido_paterno AS jefe_apellido_paterno,
        u.apellido_materno AS jefe_apellido_materno,
        u.correo AS jefe_correo,
        e.numero_celular AS jefe_numero_celular,
        e.rol_flujo AS jefe_rol_flujo,
        cc.monto_techo AS cc_monto_techo,
        cc.id_moneda AS cc_id_moneda,
        cc.id_cuenta_origen AS cc_id_cuenta_origen,
        cc.id_area AS cc_id_area,
        f.nombre_fondo AS cc_nombre_fondo,
        f.estado_fondo AS cc_estado_fondo
    FROM rrhh_area a
    LEFT JOIN rrhh_empleado e ON a.id_jefe = e.id_usuario
    LEFT JOIN rrhh_usuario u ON e.id_usuario = u.id_usuario
    LEFT JOIN tes_caja_chica cc ON a.id_area = cc.id_area
    LEFT JOIN tes_fondo f ON cc.id_fondo = f.id_fondo
    WHERE a.id_area = p_id_area;
END$$

DROP PROCEDURE IF EXISTS pa_listar_areas $$
CREATE PROCEDURE pa_listar_areas()
BEGIN
    SELECT
        a.id_area,
        a.nombre_area,
        a.descripcion_area,
        a.id_jefe,
        a.esta_activo,
        cc.id_fondo AS id_fondo_caja_chica,
        e.id_usuario AS jefe_id_usuario,
        u.nombres AS jefe_nombres,
        u.apellido_paterno AS jefe_apellido_paterno,
        u.apellido_materno AS jefe_apellido_materno,
        u.correo AS jefe_correo,
        e.numero_celular AS jefe_numero_celular,
        e.rol_flujo AS jefe_rol_flujo,
        cc.monto_techo AS cc_monto_techo,
        cc.id_moneda AS cc_id_moneda,
        cc.id_cuenta_origen AS cc_id_cuenta_origen,
        cc.id_area AS cc_id_area,
        f.nombre_fondo AS cc_nombre_fondo,
        f.estado_fondo AS cc_estado_fondo
    FROM rrhh_area a
    LEFT JOIN rrhh_empleado e ON a.id_jefe = e.id_usuario
    LEFT JOIN rrhh_usuario u ON e.id_usuario = u.id_usuario
    LEFT JOIN tes_caja_chica cc ON a.id_area = cc.id_area
    LEFT JOIN tes_fondo f ON cc.id_fondo = f.id_fondo
    ORDER BY a.esta_activo DESC, a.id_area;
END$$

DROP PROCEDURE IF EXISTS pa_listar_areas_activas $$
CREATE PROCEDURE pa_listar_areas_activas()
BEGIN
    SELECT
        a.id_area,
        a.nombre_area,
        a.descripcion_area,
        a.id_jefe,
        a.esta_activo,
        cc.id_fondo AS id_fondo_caja_chica,
        e.id_usuario AS jefe_id_usuario,
        u.nombres AS jefe_nombres,
        u.apellido_paterno AS jefe_apellido_paterno,
        u.apellido_materno AS jefe_apellido_materno,
        u.correo AS jefe_correo,
        e.numero_celular AS jefe_numero_celular,
        e.rol_flujo AS jefe_rol_flujo,
        cc.monto_techo AS cc_monto_techo,
        cc.id_moneda AS cc_id_moneda,
        cc.id_cuenta_origen AS cc_id_cuenta_origen,
        cc.id_area AS cc_id_area,
        f.nombre_fondo AS cc_nombre_fondo,
        f.estado_fondo AS cc_estado_fondo
    FROM rrhh_area a
    LEFT JOIN rrhh_empleado e ON a.id_jefe = e.id_usuario
    LEFT JOIN rrhh_usuario u ON e.id_usuario = u.id_usuario
    LEFT JOIN tes_caja_chica cc ON a.id_area = cc.id_area
    LEFT JOIN tes_fondo f ON cc.id_fondo = f.id_fondo
    WHERE a.esta_activo = 1
    ORDER BY a.id_area;
END$$

DROP PROCEDURE IF EXISTS pa_reactivar_area $$
CREATE PROCEDURE pa_reactivar_area(

        IN p_id_usuario_accion INT,
IN p_id_area INT

)
BEGIN
    IF p_id_area IS NULL OR p_id_area <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El ID del área no es válido';
    END IF;

    UPDATE rrhh_area
       SET esta_activo = 1,
           id_usuario_modificacion = p_id_usuario_accion
    WHERE id_area = p_id_area;

    UPDATE tes_fondo f
       INNER JOIN tes_caja_chica cc ON f.id_fondo = cc.id_fondo
       SET f.estado_fondo = 'ACTIVO',
           f.id_usuario_modificacion = p_id_usuario_accion
     WHERE cc.id_area = p_id_area;
END$$

DELIMITER ;
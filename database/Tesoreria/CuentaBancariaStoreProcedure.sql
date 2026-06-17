DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_cuenta_bancaria $$
CREATE PROCEDURE pa_insertar_cuenta_bancaria(
    IN p_id_usuario_accion INT,
IN p_nombre_banco VARCHAR(50),
IN p_numero_cuenta VARCHAR(30),
IN p_cci CHAR(20),
IN p_id_moneda INT,
IN p_id_usuario_titular INT,
IN p_id_area_titular INT,
OUT p_id_cuenta_bancaria INT
)
BEGIN
	INSERT INTO tes_cuenta_bancaria(

        nombre_banco, numero_cuenta, cci,
        id_moneda,
        id_usuario,
        id_area,
        id_usuario_creacion,
        id_usuario_modificacion    )
	VALUES
        (p_nombre_banco,p_numero_cuenta,p_cci,
        p_id_moneda,
        p_id_usuario_titular,
        p_id_area_titular,
        p_id_usuario_accion,
        p_id_usuario_accion);
	
	SET p_id_cuenta_bancaria=@@last_insert_id;

END$$

DROP PROCEDURE IF EXISTS pa_modificar_cuenta_bancaria $$
CREATE PROCEDURE pa_modificar_cuenta_bancaria(

        IN p_id_usuario_accion INT,
IN p_id_cuenta_bancaria INT,
    IN p_nombre_banco VARCHAR(50),
    IN p_numero_cuenta VARCHAR(30),
    IN p_cci CHAR(20),
    IN p_id_moneda INT,
    IN p_id_usuario_titular INT,
    IN p_id_area_titular INT

)
BEGIN
    IF p_id_cuenta_bancaria IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de la cuenta no válido';
    END IF;

    UPDATE tes_cuenta_bancaria
    SET nombre_banco = p_nombre_banco,
        numero_cuenta = p_numero_cuenta,
        cci = p_cci,
        id_moneda = p_id_moneda,
        id_usuario = p_id_usuario_titular,
        id_area = p_id_area_titular,
           id_usuario_modificacion = p_id_usuario_accion
    WHERE id_cuenta_bancaria = p_id_cuenta_bancaria;
END$$

DROP PROCEDURE IF EXISTS pa_eliminar_cuenta_bancaria $$
CREATE PROCEDURE pa_eliminar_cuenta_bancaria(
 
	    IN p_id_usuario_accion INT,
IN p_id_cuenta_bancaria INT

)
BEGIN 
    IF p_id_cuenta_bancaria IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de la cuenta no válido';
    END IF;
	UPDATE tes_cuenta_bancaria SET activa = 0,
           id_usuario_modificacion = p_id_usuario_accion
    WHERE id_cuenta_bancaria = p_id_cuenta_bancaria;
END$$

DROP PROCEDURE IF EXISTS pa_buscar_cuenta_bancaria_por_id $$
CREATE PROCEDURE pa_buscar_cuenta_bancaria_por_id(
    IN p_id_cuenta_bancaria INT
)
BEGIN
    IF p_id_cuenta_bancaria IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de la cuenta no válido';
    END IF;

    SELECT
        cb.id_cuenta_bancaria,
        cb.numero_cuenta,
        cb.nombre_banco,
        cb.cci,
        cb.id_moneda,
        cb.id_usuario,
        cb.id_area,
        cb.activa,
        m.id_moneda AS mon_id_moneda,
        m.codigo_iso AS mon_codigo_iso,
        m.simbolo AS mon_simbolo,
        m.nombre_moneda AS mon_nombre,
        m.descripcion AS mon_descripcion,
        m.activa AS mon_activa,
        a.id_area AS area_id_area,
        a.nombre_area AS area_nombre,
        a.descripcion_area AS area_descripcion,
        a.id_jefe AS area_id_jefe,
        a.esta_activo AS area_esta_activo,
        e.id_usuario AS emp_id_usuario,
        u.nombres AS emp_nombres,
        u.apellido_paterno AS emp_apellido_paterno,
        u.apellido_materno AS emp_apellido_materno,
        u.correo AS emp_correo,
        e.numero_celular AS emp_numero_celular,
        e.rol_flujo AS emp_rol_flujo
    FROM tes_cuenta_bancaria cb
    LEFT JOIN tes_moneda m ON cb.id_moneda = m.id_moneda
    LEFT JOIN rrhh_area a ON cb.id_area = a.id_area
    LEFT JOIN rrhh_empleado e ON cb.id_usuario = e.id_usuario
    LEFT JOIN rrhh_usuario u ON e.id_usuario = u.id_usuario
    WHERE cb.id_cuenta_bancaria = p_id_cuenta_bancaria;
END$$

DROP PROCEDURE IF EXISTS pa_listar_cuentas_bancarias $$
CREATE PROCEDURE pa_listar_cuentas_bancarias()
BEGIN
    SELECT
        cb.id_cuenta_bancaria,
        cb.numero_cuenta,
        cb.nombre_banco,
        cb.cci,
        cb.id_moneda,
        cb.id_usuario,
        cb.id_area,
        cb.activa,
        m.id_moneda AS mon_id_moneda,
        m.codigo_iso AS mon_codigo_iso,
        m.simbolo AS mon_simbolo,
        m.nombre_moneda AS mon_nombre,
        m.descripcion AS mon_descripcion,
        m.activa AS mon_activa,
        a.id_area AS area_id_area,
        a.nombre_area AS area_nombre,
        a.descripcion_area AS area_descripcion,
        a.id_jefe AS area_id_jefe,
        a.esta_activo AS area_esta_activo,
        e.id_usuario AS emp_id_usuario,
        u.nombres AS emp_nombres,
        u.apellido_paterno AS emp_apellido_paterno,
        u.apellido_materno AS emp_apellido_materno,
        u.correo AS emp_correo,
        e.numero_celular AS emp_numero_celular,
        e.rol_flujo AS emp_rol_flujo
    FROM tes_cuenta_bancaria cb
    LEFT JOIN tes_moneda m ON cb.id_moneda = m.id_moneda
    LEFT JOIN rrhh_area a ON cb.id_area = a.id_area
    LEFT JOIN rrhh_empleado e ON cb.id_usuario = e.id_usuario
    LEFT JOIN rrhh_usuario u ON e.id_usuario = u.id_usuario
    ORDER BY cb.activa DESC, cb.id_cuenta_bancaria;
END$$

DROP PROCEDURE IF EXISTS pa_listar_cuentas_bancarias_activas $$
CREATE PROCEDURE pa_listar_cuentas_bancarias_activas()
BEGIN
    SELECT
        cb.id_cuenta_bancaria,
        cb.numero_cuenta,
        cb.nombre_banco,
        cb.cci,
        cb.id_moneda,
        cb.id_usuario,
        cb.id_area,
        cb.activa,
        m.id_moneda AS mon_id_moneda,
        m.codigo_iso AS mon_codigo_iso,
        m.simbolo AS mon_simbolo,
        m.nombre_moneda AS mon_nombre,
        m.descripcion AS mon_descripcion,
        m.activa AS mon_activa,
        a.id_area AS area_id_area,
        a.nombre_area AS area_nombre,
        a.descripcion_area AS area_descripcion,
        a.id_jefe AS area_id_jefe,
        a.esta_activo AS area_esta_activo,
        e.id_usuario AS emp_id_usuario,
        u.nombres AS emp_nombres,
        u.apellido_paterno AS emp_apellido_paterno,
        u.apellido_materno AS emp_apellido_materno,
        u.correo AS emp_correo,
        e.numero_celular AS emp_numero_celular,
        e.rol_flujo AS emp_rol_flujo
    FROM tes_cuenta_bancaria cb
    LEFT JOIN tes_moneda m ON cb.id_moneda = m.id_moneda
    LEFT JOIN rrhh_area a ON cb.id_area = a.id_area
    LEFT JOIN rrhh_empleado e ON cb.id_usuario = e.id_usuario
    LEFT JOIN rrhh_usuario u ON e.id_usuario = u.id_usuario
    WHERE cb.activa = 1;
END$$

DELIMITER ;
DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_cuenta_bancaria $$
CREATE PROCEDURE pa_insertar_cuenta_bancaria(
	OUT  _id_cuenta_bancaria INT,
	IN _nombre_banco VARCHAR(50),
    IN _numero_cuenta VARCHAR(30),
    IN _cci CHAR(20),
    IN _id_moneda INT,
    IN _id_usuario_titular INT, 
    IN _id_area_titular INT
)
BEGIN

	INSERT INTO tes_cuenta_bancaria(
        nombre_banco, numero_cuenta, cci,
        id_moneda,id_usuario_titular, 
        id_area) 
	VALUES 
        (_nombre_banco,_numero_cuenta,_cci,
        _id_moneda,_id_usuario_titular,
        _id_area_titular);
	
	SET _id_cuenta_bancaria=@@last_insert_id;

END$$

DROP PROCEDURE IF EXISTS pa_modificar_cuenta_bancaria $$
CREATE PROCEDURE pa_modificar_cuenta_bancaria(
    IN _id_cuenta_bancaria INT,
    IN _nombre_banco VARCHAR(50),
    IN _numero_cuenta VARCHAR(30),
    IN _cci CHAR(20),
    IN _id_moneda INT,
    IN _id_usuario_titular INT, 
    IN _id_area_administradora INT
)
BEGIN
    UPDATE tes_cuenta_bancaria
    SET nombre_banco = _nombre_banco,
        numero_cuenta = _numero_cuenta,
        cci = _cci,
        id_moneda = _id_moneda,
        id_usuario_titular = _id_usuario_titular,
        id_area = _id_area_administradora
    WHERE id_cuenta_bancaria = _id_cuenta_bancaria;
END$$

DROP PROCEDURE IF EXISTS pa_eliminar_cuenta_bancaria $$
CREATE PROCEDURE pa_eliminar_cuenta_bancaria( 
	IN _id_cuenta_bancaria INT
)
BEGIN 
	UPDATE tes_cuenta_bancaria SET activa = 0 WHERE id_cuenta_bancaria = _id_cuenta_bancaria;

END$$

DROP PROCEDURE IF EXISTS pa_busqueda_por_id_cuenta_bancaria $$
CREATE PROCEDURE pa_busqueda_por_id_cuenta_bancaria(
    IN _id_cuenta_bancaria INT
)
BEGIN
    SELECT 
        cb.id_cuenta_bancaria AS id,
        cb.numero_cuenta,
        cb.nombre_banco,
        cb.cci,
        m.id_moneda,
        m.codigo_iso,
        u.id_usuario,
        u.nombres,
        u.apellido_paterno,
        u.apellido_materno, 
        a.id_area
    FROM tes_cuenta_bancaria cb
    INNER JOIN tes_moneda m
        ON cb.id_moneda = m.id_moneda
    INNER JOIN rrhh_usuario u
        ON cb.id_usuario_titular = u.id_usuario
    LEFT JOIN rrhh_area a 
        ON cb.id_area = a.id_area 
    WHERE cb.id_cuenta_bancaria = _id_cuenta_bancaria ;
END$$

DROP PROCEDURE IF EXISTS pa_listar_cuentas_bancarias $$
CREATE PROCEDURE pa_listar_cuentas_bancarias()
BEGIN
    SELECT 
        cb.id_cuenta_bancaria AS id,
        cb.numero_cuenta,
        cb.nombre_banco,
        cb.cci,
        m.id_moneda,
        m.codigo_iso,
        u.id_usuario, 
        u.nombres,
        u.apellido_paterno,
        u.apellido_materno, 
        a.id_area
    FROM tes_cuenta_bancaria cb
    INNER JOIN tes_moneda m
        ON cb.id_moneda = m.id_moneda
    LEFT JOIN rrhh_usuario u
        ON cb.id_usuario_titular = u.id_usuario
    LEFT JOIN rrhh_area a 
        ON cb.id_area = a.id_area;
END$$

DELIMITER ;
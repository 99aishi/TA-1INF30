-------CUENTA BANCARIA


----INSERTAR 

DELIMITER $$

CREATE PROCEDURE pa_insertar_cuenta_bancaria(
	OUT  _id_cuenta_bancaria INT,
	IN _nombre_banco VARCHAR(50),
    IN _numero_cuenta VARCHAR(30),
    IN _cci CHAR(20),
    IN _id_moneda INT,
    IN _id_usuario_titular INT
)
BEGIN

	INSERT INTO tes_cuenta_bancaria(nombre_banco,numero_cuenta,cci,id_moneda,id_usuario_titular) 
	VALUES (_nombre_banco,_numero_cuenta,_cci,_id_moneda,_id_usuario_titular);
	
	SET _id_cuenta_bancaria=@@last_insert_id;

END$$


----MODIFICAR

DELIMITER $$

CREATE PROCEDURE pa_modificar_cuenta_bancaria(
    IN _id_cuenta_bancaria INT,
    IN _nombre_banco VARCHAR(50),
    IN _numero_cuenta VARCHAR(30),
    IN _cci CHAR(20),
    IN _id_moneda INT,
    IN _id_usuario_titular INT
)
BEGIN
    UPDATE tes_cuenta_bancaria
    SET nombre_banco = _nombre_banco,
        numero_cuenta = _numero_cuenta,
        cci = _cci,
        id_moneda = _id_moneda,
        id_usuario_titular = _id_usuario_titular
    WHERE id_cuenta_bancaria = _id_cuenta_bancaria;
END$$
---ELIMINAR
CREATE PROCEDURE pa_eliminar_cuenta_bancaria( 
	IN _id_cuenta_bancaria INT
)
BEGIN 
	UPDATE tes_cuenta_bancaria SET activa = 0 WHERE id_cuenta_bancaria = _id_cuenta_bancaria;

END$$

--- BUSCAR POR ID 
DELIMITER $$

CREATE PROCEDURE pa_busqueda_por_id_cuenta_bancaria(
    IN _id_cuenta_bancaria INT
)
BEGIN
    SELECT 
        cb.id_cuenta_bancaria AS id,
        cb.numero_cuenta,
        cb.nombre_banco,
        cb.cci,
        m.codigo_iso,
        u.nombres,
        u.apellido_paterno,
        u.apellido_materno
    FROM tes_cuenta_bancaria cb
    INNER JOIN tes_moneda m
        ON cb.id_moneda = m.id_moneda
    INNER JOIN rrhh_usuario u
        ON cb.id_usuario_titular = u.id_usuario
    WHERE cb.id_cuenta_bancaria = _id_cuenta_bancaria;
END$$

---- LISTAR CUENTAS BANCARIAS

DELIMITER $$

CREATE PROCEDURE pa_listar_cuentas_bancarias()
BEGIN
    SELECT 
        cb.id_cuenta_bancaria AS id,
        cb.numero_cuenta,
        cb.nombre_banco,
        cb.cci,
        m.codigo_iso,
        u.nombres,
        u.apellido_paterno,
        u.apellido_materno
    FROM tes_cuenta_bancaria cb
    INNER JOIN tes_moneda m
        ON cb.id_moneda = m.id_moneda
    INNER JOIN rrhh_usuario u
        ON cb.id_usuario_titular = u.id_usuario;
END$$

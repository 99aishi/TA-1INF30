DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_cuenta_bancaria $$
CREATE PROCEDURE pa_insertar_cuenta_bancaria(
	OUT p_id_cuenta_bancaria INT,
	IN p_nombre_banco VARCHAR(50),
    IN p_numero_cuenta VARCHAR(30),
    IN p_cci CHAR(20),
    IN p_id_moneda INT,
    IN p_id_usuario_titular INT, 
    IN p_id_area_titular INT
)
BEGIN
	INSERT INTO tes_cuenta_bancaria(
        nombre_banco, numero_cuenta, cci,
        id_moneda,id_usuario_titular, 
        id_area) 
	VALUES 
        (p_nombre_banco,p_numero_cuenta,p_cci,
        p_id_moneda,p_id_usuario_titular,
        p_id_area_titular);
	
	SET p_id_cuenta_bancaria=@@last_insert_id;

END$$

DROP PROCEDURE IF EXISTS pa_modificar_cuenta_bancaria $$
CREATE PROCEDURE pa_modificar_cuenta_bancaria(
    IN p_id_cuenta_bancaria INT,
    IN p_nombre_banco VARCHAR(50),
    IN p_numero_cuenta VARCHAR(30),
    IN p_cci CHAR(20),
    IN p_id_moneda INT,
    IN p_id_usuario_titular INT, 
    IN p_id_area_administradora INT
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
        id_usuario_titular = p_id_usuario_titular,
        id_area = p_id_area_administradora
    WHERE id_cuenta_bancaria = p_id_cuenta_bancaria;
END$$

DROP PROCEDURE IF EXISTS pa_eliminar_cuenta_bancaria $$
CREATE PROCEDURE pa_eliminar_cuenta_bancaria( 
	IN p_id_cuenta_bancaria INT
)
BEGIN 
    IF p_id_cuenta_bancaria IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de la cuenta no válido';
    END IF;
	UPDATE tes_cuenta_bancaria SET activa = 0 WHERE id_cuenta_bancaria = p_id_cuenta_bancaria;

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
        id_cuenta_bancaria,
        numero_cuenta,
        nombre_banco,
        cci,
        id_moneda,
        id_usuario,
        id_area
    FROM tes_cuenta_bancaria 
    WHERE id_cuenta_bancaria = p_id_cuenta_bancaria and activa = 1;
END$$

DROP PROCEDURE IF EXISTS pa_listar_cuentas_bancarias $$
CREATE PROCEDURE pa_listar_cuentas_bancarias()
BEGIN
    IF p_id_cuenta_bancaria IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de la cuenta no válido';
    END IF;
    SELECT 
        id_cuenta_bancaria,
        numero_cuenta,
        nombre_banco,
        cci,
        id_moneda,
        id_usuario,
        id_area
    FROM tes_cuenta_bancaria cb
    WHERE activa = 1;
END$$

DELIMITER ;
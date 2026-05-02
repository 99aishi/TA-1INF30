DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_moneda $$
CREATE PROCEDURE pa_insertar_moneda(
	OUT p_id_moneda INT,
    IN p_codigo_iso VARCHAR(30),
	IN p_simbolo VARCHAR(5)
)
BEGIN
	INSERT INTO tes_moneda(codigo_iso,simbolo) VALUES(p_codigo_iso,p_simbolo);
	SET p_id_moneda=@@last_insert_id;
END$$

DROP PROCEDURE IF EXISTS pa_modificar_moneda $$
CREATE PROCEDURE pa_modificar_moneda(
	IN p_id_moneda INT,
	IN p_codigo_iso VARCHAR(30),
	IN p_simbolo VARCHAR(5)
)
BEGIN
	IF p_id_moneda IS NULL OR p_id_moneda <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de moneda no válido';
    END IF;

	UPDATE tes_moneda SET codigo_iso =p_codigo_iso, 
	simbolo=p_simbolo
	WHERE id_moneda=p_id_moneda;
END$$

DROP PROCEDURE IF EXISTS pa_busqueda_por_id $$
CREATE PROCEDURE pa_busqueda_por_id(
	IN p_id_moneda INT
)
BEGIN 
	IF p_id_moneda IS NULL OR p_id_moneda <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de moneda no válido';
    END IF;

	SELECT id_moneda, codigo_iso, simbolo FROM tes_moneda
	WHERE id_moneda=p_id_moneda and activa = 1;
END$$

DROP PROCEDURE IF EXISTS pa_listar_monedas $$
CREATE PROCEDURE pa_listar_monedas()
BEGIN
	SELECT id_moneda, codigo_iso,simbolo FROM tes_moneda WHERE activa = 1;
END$$

DROP PROCEDURE IF EXISTS pa_eliminar_moneda $$
CREATE PROCEDURE pa_eliminar_moneda(
	IN p_id_moneda INT
)
BEGIN
	IF p_id_moneda IS NULL OR p_id_moneda <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de moneda no válido';
    END IF;

	UPDATE tes_moneda SET activa = 0 WHERE id_moneda = p_id_moneda;
END$$
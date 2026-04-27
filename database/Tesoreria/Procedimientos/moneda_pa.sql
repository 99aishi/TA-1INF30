DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_moneda $$
CREATE PROCEDURE pa_insertar_moneda(
	OUT _id_moneda INT,
    IN _codigo_iso VARCHAR(30),
	IN _simbolo VARCHAR(5)
)
BEGIN
	INSERT INTO tes_moneda(codigo_iso,simbolo) VALUES(_codigo_iso,_simbolo);
	SET _id_moneda=@@last_insert_id;
END$$

DROP PROCEDURE IF EXISTS pa_modificar_moneda $$
CREATE PROCEDURE pa_modificar_moneda(
	IN _id_moneda INT,
	IN _codigo_iso VARCHAR(30),
	IN _simbolo VARCHAR(5)
)
BEGIN
	UPDATE tes_moneda SET codigo_iso =_codigo_iso, 
	simbolo=_simbolo
	WHERE id_moneda=_id_moneda;
END$$

DROP PROCEDURE IF EXISTS pa_busqueda_por_id $$
CREATE PROCEDURE pa_busqueda_por_id(
	IN _id_moneda INT
)
BEGIN 
	SELECT id_moneda, codigo_iso, simbolo FROM tes_moneda
	WHERE id_moneda=_id_moneda;
END$$

DROP PROCEDURE IF EXISTS pa_listar_monedas $$
CREATE PROCEDURE pa_listar_monedas()
BEGIN
	SELECT id_moneda, codigo_iso,simbolo FROM tes_moneda;
END$$
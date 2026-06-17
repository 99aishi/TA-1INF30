DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_moneda $$
CREATE PROCEDURE pa_insertar_moneda(
    IN p_id_usuario_accion INT,
	IN p_codigo_iso VARCHAR(30),
	IN p_simbolo VARCHAR(5),
	IN p_nombre VARCHAR(50),
	IN p_descripcion VARCHAR(350),
	OUT p_id_moneda INT
)
BEGIN
	INSERT INTO tes_moneda(
		codigo_iso,simbolo, 
		nombre_moneda, 
		descripcion, 
		activa,
        id_usuario_creacion,
        id_usuario_modificacion    ) 
		VALUES(
		p_codigo_iso,
		p_simbolo, 
		p_nombre, 
		p_descripcion, 
		1,
        p_id_usuario_accion,
        p_id_usuario_accion    );
	SET p_id_moneda=@@last_insert_id;
END$$

DROP PROCEDURE IF EXISTS pa_modificar_moneda $$
CREATE PROCEDURE pa_modificar_moneda(
	IN p_id_usuario_accion INT,
	IN p_id_moneda INT,
	IN p_codigo_iso VARCHAR(30),
	IN p_simbolo VARCHAR(5),
	IN p_nombre VARCHAR(50),
	IN p_descripcion VARCHAR(350)
	)
BEGIN
	IF p_id_moneda IS NULL OR p_id_moneda <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de moneda no válido';
    END IF;

	UPDATE tes_moneda SET codigo_iso = p_codigo_iso, 
	simbolo = p_simbolo,
	nombre_moneda = p_nombre,
	descripcion = p_descripcion,
	id_usuario_modificacion = p_id_usuario_accion
    WHERE id_moneda = p_id_moneda;
END$$

DROP PROCEDURE IF EXISTS pa_buscar_moneda_por_id $$
CREATE PROCEDURE pa_buscar_moneda_por_id(
	IN p_id_moneda INT
)
BEGIN
	IF p_id_moneda IS NULL OR p_id_moneda <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de moneda no válido';
    END IF;

	SELECT id_moneda, codigo_iso, simbolo, nombre_moneda, descripcion, activa FROM tes_moneda
	WHERE id_moneda=p_id_moneda AND activa=1;
END$$

DROP PROCEDURE IF EXISTS pa_listar_monedas $$
CREATE PROCEDURE pa_listar_monedas()
BEGIN
	SELECT id_moneda, codigo_iso, simbolo, nombre_moneda, descripcion, activa FROM tes_moneda ORDER BY activa DESC;
END$$


DROP PROCEDURE IF EXISTS pa_listar_monedas_por_estado $$
CREATE PROCEDURE pa_listar_monedas_por_estado(
    IN p_activa TINYINT
)
BEGIN
    SELECT 
        id_moneda,
        codigo_iso,
        simbolo,
        nombre_moneda,
        descripcion,
        activa
    FROM tes_moneda
    WHERE activa = p_activa
    ORDER BY activa DESC;
END$$

DROP PROCEDURE IF EXISTS pa_listar_monedas_X_codigoISO_nombre_simbolo $$
CREATE PROCEDURE pa_listar_monedas_X_codigoISO_nombre_simbolo (
	IN p_comentario_busqueda VARCHAR (100)
)
BEGIN 
	SELECT id_moneda, codigo_iso, simbolo, nombre_moneda, activa, descripcion
    FROM tes_moneda
    WHERE codigo_iso LIKE CONCAT('%', p_comentario_busqueda, '%')
    OR simbolo LIKE CONCAT('%', p_comentario_busqueda, '%')
    OR nombre_moneda LIKE CONCAT('%', p_comentario_busqueda, '%')
    ORDER BY activa DESC;
END $$

DROP PROCEDURE IF EXISTS pa_eliminar_moneda $$
CREATE PROCEDURE pa_eliminar_moneda(
	IN p_id_usuario_accion INT,
	IN p_id_moneda INT

)
BEGIN
	IF p_id_moneda IS NULL OR p_id_moneda <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de moneda no válido';
    END IF;

	UPDATE tes_moneda SET activa = 0,
           id_usuario_modificacion = p_id_usuario_accion
    WHERE id_moneda = p_id_moneda;
END$$

DROP PROCEDURE IF EXISTS pa_reactivar_moneda $$
CREATE PROCEDURE pa_reactivar_moneda(
	IN p_id_usuario_accion INT,
	IN p_id_moneda INT
)
BEGIN
	IF p_id_moneda IS NULL OR p_id_moneda <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de moneda no válido';
    END IF;

	UPDATE tes_moneda SET activa = 1,
           id_usuario_modificacion = p_id_usuario_accion
    WHERE id_moneda = p_id_moneda;
END$$

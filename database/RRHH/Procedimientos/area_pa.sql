DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_area  $$
CREATE PROCEDURE pa_insertar_area(
    IN p_nombre_area VARCHAR(60),
    IN p_descripcion_area VARCHAR(200),
    IN p_id_jefe INT,
    OUT p_id_generado INT
)
BEGIN
    IF p_id_area IS NULL OR p_id_area <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El ID del área no es válido';
    END IF;

    INSERT INTO rrhh_area(
        nombre_area,
        descripcion_area, 
        id_jefe, 
        esta_activo
    )
    VALUES(
        p_nombre_area,
        p_descripcion_area, 
        p_id_jefe, 
        1
    );

    SET p_id_generado = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS pa_modificar_area $$
CREATE PROCEDURE pa_modificar_area(
    IN p_id_area INT,
    IN p_nombre_area VARCHAR(60),
    IN p_descripcion_area VARCHAR(200),
    IN p_id_jefe INT
)
BEGIN
    IF p_id_area IS NULL OR p_id_area <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El ID del área no es válido';
    END IF;

    UPDATE rrhh_area
       SET nombre_area = p_nombre_area,
           descripcion_area = p_descripcion_area, 
           id_jefe = p_id_jefe
     WHERE id_area = p_id_area;
END$$

DROP PROCEDURE IF EXISTS pa_asignar_jefe_area $$ 
CREATE PROCEDURE pa_asignar_jefe_area(
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
       SET id_jefe = p_id_jefe
     WHERE id_area = p_id_area;
END$$

DROP PROCEDURE IF EXISTS pa_eliminar_area  $$
CREATE PROCEDURE pa_eliminar_area(
    IN p_id_area INT
)
BEGIN
    IF p_id_area IS NULL OR p_id_area <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El ID del área no es válido';
    END IF;

    UPDATE rrhh_area
       SET esta_activo = 0
     WHERE id_area = p_id_area;
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
        id_area, 
        nombre_area, 
        descripcion_area, 
        id_jefe
    FROM rrhh_area
    WHERE id_area = p_id_area and esta_activo = 1;
END$$

DROP PROCEDURE IF EXISTS pa_listar_areas $$
CREATE PROCEDURE pa_listar_areas()
BEGIN
    SELECT 
        id_area, 
        nombre_area, 
        descripcion_area, 
        id_jefe
    FROM rrhh_area
    WHERE esta_activo = 1
    ORDER BY nombre_area;
END$$

DELIMITER ;
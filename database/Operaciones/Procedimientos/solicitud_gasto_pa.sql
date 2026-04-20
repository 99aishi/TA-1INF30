DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_solicitud_gasto $$
CREATE PROCEDURE pa_insertar_solicitud_gasto(
    IN p_fecha_solicitud DATE,
    IN p_monto_solicitado DECIMAL(12,2),
    IN p_motivo_solicitud VARCHAR(200),
    IN p_estado_solicitud VARCHAR(20),
    IN p_id_usuario_solicitante INT,
    IN p_id_usuario_destinatario INT,
    IN p_id_ciclo_caja INT,
    OUT p_id_generado INT
)
BEGIN
    IF p_id_usuario_solicitante IS NULL OR p_id_usuario_solicitante <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El ID del usuario solicitante es obligatorio';
    END IF;

    IF p_monto_solicitado IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El monto solicitado es obligatorio';
    END IF;

    INSERT INTO ope_solicitud_gasto(
        fecha_solicitud,
        monto_solicitado,
        motivo_solicitud,
        estado_solicitud,
        id_usuario_solicitante,
        id_usuario_destinatario,
        id_ciclo_caja
    )
    VALUES(
        IFNULL(p_fecha_solicitud, CURDATE()),
        p_monto_solicitado,
        TRIM(p_motivo_solicitud),
        TRIM(p_estado_solicitud),
        p_id_usuario_solicitante,
        p_id_usuario_destinatario,
        p_id_ciclo_caja
    );
    
    SET p_id_generado = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS pa_modificar_solicitud_gasto $$
CREATE PROCEDURE pa_modificar_solicitud_gasto(
    IN p_id_solicitud_gasto INT,
    IN p_fecha_solicitud DATE,
    IN p_monto_solicitado DECIMAL(12,2),
    IN p_motivo_solicitud VARCHAR(200),
    IN p_estado_solicitud VARCHAR(20),
    IN p_id_usuario_solicitante INT,
    IN p_id_usuario_destinatario INT,
    IN p_id_ciclo_caja INT
)
BEGIN
    IF p_id_solicitud_gasto IS NULL OR p_id_solicitud_gasto <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de solicitud de gasto inválido';
    END IF;

    IF p_id_usuario_solicitante IS NULL OR p_id_usuario_solicitante <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El ID del usuario solicitante es obligatorio';
    END IF;

    IF p_monto_solicitado IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El monto solicitado es obligatorio';
    END IF;

    UPDATE ope_solicitud_gasto
       SET -- Mantiene el valor actual si el parámetro viene NULL
           fecha_solicitud = IFNULL(p_fecha_solicitud, fecha_solicitud),
           monto_solicitado = p_monto_solicitado,
           motivo_solicitud = TRIM(p_motivo_solicitud),
           estado_solicitud = TRIM(p_estado_solicitud),
           id_usuario_solicitante = p_id_usuario_solicitante,
           id_usuario_destinatario = p_id_usuario_destinatario,
           id_ciclo_caja = p_id_ciclo_caja
     WHERE id_solicitud_gasto = p_id_solicitud_gasto;
END$$

DROP PROCEDURE IF EXISTS pa_eliminar_solicitud_gasto $$
CREATE PROCEDURE pa_eliminar_solicitud_gasto(
    IN p_id_solicitud_gasto INT
)
BEGIN
    IF p_id_solicitud_gasto IS NULL OR p_id_solicitud_gasto <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de solicitud de gasto inválido';
    END IF;

    UPDATE ope_solicitud_gasto
       SET estado_solicitud = 'ANULADO'
     WHERE id_solicitud_gasto = p_id_solicitud_gasto;
END$$

DROP PROCEDURE IF EXISTS pa_buscar_solicitud_gasto_por_id $$
CREATE PROCEDURE pa_buscar_solicitud_gasto_por_id(
    IN p_id_solicitud_gasto INT
)
BEGIN
    SELECT 
        id_solicitud_gasto, 
        fecha_solicitud, 
        monto_solicitado, 
        motivo_solicitud, 
        estado_solicitud, 
        id_usuario_solicitante, 
        id_usuario_destinatario, 
        id_ciclo_caja
    FROM ope_solicitud_gasto
    WHERE id_solicitud_gasto = p_id_solicitud_gasto;
END$$

DROP PROCEDURE IF EXISTS pa_listar_solicitudes_gasto $$
CREATE PROCEDURE pa_listar_solicitudes_gasto()
BEGIN
    SELECT 
        id_solicitud_gasto, 
        fecha_solicitud, 
        monto_solicitado, 
        motivo_solicitud, 
        estado_solicitud, 
        id_usuario_solicitante, 
        id_usuario_destinatario, 
        id_ciclo_caja
    FROM ope_solicitud_gasto
    WHERE estado_solicitud IS NULL OR estado_solicitud != 'ANULADO'
    ORDER BY id_solicitud_gasto DESC;
END$$

DROP PROCEDURE IF EXISTS pa_listar_solicitudes_por_solicitante $$
CREATE PROCEDURE pa_listar_solicitudes_por_solicitante(
    IN p_id_usuario_solicitante INT
)
BEGIN
    SELECT 
        id_solicitud_gasto, 
        fecha_solicitud, 
        monto_solicitado, 
        motivo_solicitud, 
        estado_solicitud, 
        id_usuario_solicitante, 
        id_usuario_destinatario, 
        id_ciclo_caja
    FROM ope_solicitud_gasto
    WHERE id_usuario_solicitante = p_id_usuario_solicitante
      AND (estado_solicitud IS NULL OR estado_solicitud != 'ANULADO')
    ORDER BY id_solicitud_gasto DESC;
END$$

DROP PROCEDURE IF EXISTS pa_listar_solicitudes_pendientes_jefe $$
CREATE PROCEDURE pa_listar_solicitudes_pendientes_jefe(
    IN p_id_usuario_destinatario INT
)
BEGIN
    SELECT 
        id_solicitud_gasto, 
        fecha_solicitud, 
        monto_solicitado, 
        motivo_solicitud, 
        estado_solicitud, 
        id_usuario_solicitante, 
        id_usuario_destinatario, 
        id_ciclo_caja
    FROM ope_solicitud_gasto
    WHERE id_usuario_destinatario = p_id_usuario_destinatario
      AND estado_solicitud = 'PENDIENTE'
    ORDER BY fecha_solicitud ASC;
END$$

DELIMITER ;
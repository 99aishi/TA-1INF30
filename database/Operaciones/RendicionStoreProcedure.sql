DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_rendicion $$
CREATE PROCEDURE pa_insertar_rendicion(
    IN p_id_usuario_accion INT,
IN p_fecha_presentacion DATE,
IN p_fecha_aprobacion DATE,
IN p_monto_total_declarado DECIMAL(12,2),
IN p_monto_total_aprobado DECIMAL(12,2),
IN p_monto_saldo_final DECIMAL(12,2),
IN p_estado_rendicion ENUM('ACEPTADO','EN_ESPERA','DENEGADO','ANULADO'),
IN p_comentario VARCHAR(500),OUT p_id_generado INT
)
BEGIN

    INSERT INTO ope_rendicion(

        fecha_presentacion,
        fecha_aprobacion,
        monto_total_declarado,
        monto_total_aprobado,
        monto_saldo_final,
        estado_rendicion,
        comentario,
        id_usuario_creacion,
        id_usuario_modificacion    )
    VALUES(

        p_fecha_presentacion,
        p_fecha_aprobacion,
        p_monto_total_declarado,
        p_monto_total_aprobado,
        p_monto_saldo_final,
        p_estado_rendicion,
        p_comentario,
        p_id_usuario_accion,
        p_id_usuario_accion    );
    
    SET p_id_generado = LAST_INSERT_ID();
END$$


DROP PROCEDURE IF EXISTS pa_modificar_rendicion $$
CREATE PROCEDURE pa_modificar_rendicion(

    IN p_id_usuario_accion INT,
	IN p_id_rendicion INT,
    IN p_fecha_presentacion DATE,
    IN p_fecha_aprobacion DATE,
    IN p_monto_total_declarado DECIMAL(12,2),
    IN p_monto_total_aprobado DECIMAL(12,2),
    IN p_monto_saldo_final DECIMAL(12,2),
    IN p_estado_rendicion ENUM('ACEPTADO','EN_ESPERA','DENEGADO','ANULADO'),
    IN p_comentario VARCHAR(500)
)
BEGIN
    IF p_id_rendicion IS NULL OR p_id_rendicion <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de rendición inválido';
    END IF;

    UPDATE ope_rendicion
       SET fecha_presentacion = p_fecha_presentacion,
           fecha_aprobacion = p_fecha_aprobacion,
           monto_total_declarado = p_monto_total_declarado,
           monto_total_aprobado = p_monto_total_aprobado,
           monto_saldo_final = p_monto_saldo_final,
           estado_rendicion = p_estado_rendicion,
           comentario = p_comentario,
           id_usuario_modificacion = p_id_usuario_accion
    WHERE id_rendicion = p_id_rendicion;
END$$

DROP PROCEDURE IF EXISTS pa_eliminar_rendicion $$
CREATE PROCEDURE pa_eliminar_rendicion(

        IN p_id_usuario_accion INT,
IN p_id_rendicion INT

)
BEGIN
    IF p_id_rendicion IS NULL OR p_id_rendicion <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de rendición inválido';
    END IF;

    UPDATE ope_rendicion
       SET estado_rendicion = 'ANULADO',
           id_usuario_modificacion = p_id_usuario_accion
    WHERE id_rendicion = p_id_rendicion;
END$$

DROP PROCEDURE IF EXISTS pa_buscar_rendicion_por_id $$
CREATE PROCEDURE pa_buscar_rendicion_por_id(
    IN p_id_rendicion INT
)
BEGIN
    SELECT
        r.id_rendicion,
        r.fecha_presentacion,
        r.fecha_aprobacion,
        r.monto_total_declarado,
        r.monto_total_aprobado,
        r.monto_saldo_final,
        r.estado_rendicion,
        r.comentario,
        r.id_ciclo_caja,
        cc.id_ciclo_caja AS cc_id_ciclo_caja,
        cc.numero_semana AS cc_numero_semana,
        cc.fecha_apertura AS cc_fecha_apertura,
        cc.fecha_cierre AS cc_fecha_cierre,
        cc.monto_saldo_inicial AS cc_monto_saldo_inicial,
        cc.monto_total_gastado AS cc_monto_total_gastado,
        cc.estado_ciclo AS cc_estado_ciclo,
        cc.id_caja_chica AS cc_id_caja_chica,
        cc.id_rendicion AS cc_id_rendicion
    FROM ope_rendicion r
    LEFT JOIN ope_ciclo_caja cc ON r.id_ciclo_caja = cc.id_ciclo_caja
    WHERE r.id_rendicion = p_id_rendicion;
END$$

DROP PROCEDURE IF EXISTS pa_listar_rendiciones $$
CREATE PROCEDURE pa_listar_rendiciones()
BEGIN
    SELECT
        r.id_rendicion,
        r.fecha_presentacion,
        r.fecha_aprobacion,
        r.monto_total_declarado,
        r.monto_total_aprobado,
        r.monto_saldo_final,
        r.estado_rendicion,
        r.comentario,
        r.id_ciclo_caja,
        cc.id_ciclo_caja AS cc_id_ciclo_caja,
        cc.numero_semana AS cc_numero_semana,
        cc.fecha_apertura AS cc_fecha_apertura,
        cc.fecha_cierre AS cc_fecha_cierre,
        cc.monto_saldo_inicial AS cc_monto_saldo_inicial,
        cc.monto_total_gastado AS cc_monto_total_gastado,
        cc.estado_ciclo AS cc_estado_ciclo,
        cc.id_caja_chica AS cc_id_caja_chica,
        cc.id_rendicion AS cc_id_rendicion
    FROM ope_rendicion r
    LEFT JOIN ope_ciclo_caja cc ON r.id_ciclo_caja = cc.id_ciclo_caja
    ORDER BY r.estado_rendicion DESC;
END$$

DROP PROCEDURE IF EXISTS pa_listar_todas_rendiciones $$
CREATE PROCEDURE pa_listar_todas_rendiciones()
BEGIN
    SELECT
        r.id_rendicion,
        r.fecha_presentacion,
        r.fecha_aprobacion,
        r.monto_total_declarado,
        r.monto_total_aprobado,
        r.monto_saldo_final,
        r.estado_rendicion,
        r.comentario,
        r.id_ciclo_caja,
        cc.id_ciclo_caja AS cc_id_ciclo_caja,
        cc.numero_semana AS cc_numero_semana,
        cc.fecha_apertura AS cc_fecha_apertura,
        cc.fecha_cierre AS cc_fecha_cierre,
        cc.monto_saldo_inicial AS cc_monto_saldo_inicial,
        cc.monto_total_gastado AS cc_monto_total_gastado,
        cc.estado_ciclo AS cc_estado_ciclo,
        cc.id_caja_chica AS cc_id_caja_chica,
        cc.id_rendicion AS cc_id_rendicion
    FROM ope_rendicion r
    LEFT JOIN ope_ciclo_caja cc ON r.id_ciclo_caja = cc.id_ciclo_caja
    ORDER BY r.estado_rendicion DESC;
END$$

DROP PROCEDURE IF EXISTS pa_listar_rendiciones_activas $$
CREATE PROCEDURE pa_listar_rendiciones_activas()
BEGIN
    SELECT
        r.id_rendicion,
        r.fecha_presentacion,
        r.fecha_aprobacion,
        r.monto_total_declarado,
        r.monto_total_aprobado,
        r.monto_saldo_final,
        r.estado_rendicion,
        r.comentario,
        r.id_ciclo_caja,
        cc.id_ciclo_caja AS cc_id_ciclo_caja,
        cc.numero_semana AS cc_numero_semana,
        cc.fecha_apertura AS cc_fecha_apertura,
        cc.fecha_cierre AS cc_fecha_cierre,
        cc.monto_saldo_inicial AS cc_monto_saldo_inicial,
        cc.monto_total_gastado AS cc_monto_total_gastado,
        cc.estado_ciclo AS cc_estado_ciclo,
        cc.id_caja_chica AS cc_id_caja_chica,
        cc.id_rendicion AS cc_id_rendicion
    FROM ope_rendicion r
    LEFT JOIN ope_ciclo_caja cc ON r.id_ciclo_caja = cc.id_ciclo_caja
    WHERE r.estado_rendicion != 'ANULADO'
    ORDER BY r.estado_rendicion DESC, r.id_rendicion DESC;
END$$

DELIMITER ;
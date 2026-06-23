
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
    IN p_comentario VARCHAR(500),
    IN p_id_ciclo_caja INT,
    OUT p_id_generado INT
)
BEGIN
    DECLARE v_total_declarado DECIMAL(12,2) DEFAULT 0;
    DECLARE v_total_aprobado DECIMAL(12,2) DEFAULT 0;
    DECLARE v_saldo_final DECIMAL(12,2) DEFAULT 0;
    DECLARE v_saldo_inicial DECIMAL(12,2) DEFAULT 0;

    -- Auto-calcular totales si se proporciona id_ciclo_caja y los montos son 0
    IF p_id_ciclo_caja IS NOT NULL AND p_id_ciclo_caja > 0 THEN
        IF p_monto_total_declarado IS NULL OR p_monto_total_declarado = 0 THEN
            SET v_total_declarado = COALESCE((
                SELECT SUM(cp.monto_total)
                FROM ope_solicitud_gasto sg
                JOIN ope_comprobante_pago cp ON sg.id_solicitud_gasto = cp.id_solicitud_gasto
                WHERE sg.id_ciclo_caja = p_id_ciclo_caja
                  AND cp.estado_comprobante != 'ANULADO'
            ), 0);
        ELSE
            SET v_total_declarado = p_monto_total_declarado;
        END IF;

        IF p_monto_total_aprobado IS NULL OR p_monto_total_aprobado = 0 THEN
            SET v_total_aprobado = COALESCE((
                SELECT SUM(sg.monto_solicitado)
                FROM ope_solicitud_gasto sg
                WHERE sg.id_ciclo_caja = p_id_ciclo_caja
                  AND sg.estado_solicitud = 'APROBADO'
            ), 0);
        ELSE
            SET v_total_aprobado = p_monto_total_aprobado;
        END IF;

        IF p_monto_saldo_final IS NULL OR p_monto_saldo_final = 0 THEN
            SET v_saldo_inicial = COALESCE((
                SELECT occ.monto_saldo_inicial
                FROM ope_ciclo_caja occ
                WHERE occ.id_ciclo_caja = p_id_ciclo_caja
            ), 0);
            SET v_saldo_final = v_saldo_inicial - v_total_aprobado;
        ELSE
            SET v_saldo_final = p_monto_saldo_final;
        END IF;
    ELSE
        SET v_total_declarado = COALESCE(p_monto_total_declarado, 0);
        SET v_total_aprobado = COALESCE(p_monto_total_aprobado, 0);
        SET v_saldo_final = COALESCE(p_monto_saldo_final, 0);
    END IF;

    INSERT INTO ope_rendicion(
        fecha_presentacion,
        fecha_aprobacion,
        monto_total_declarado,
        monto_total_aprobado,
        monto_saldo_final,
        estado_rendicion,
        comentario,
        id_ciclo_caja,
        id_usuario_creacion,
        id_usuario_modificacion
    ) VALUES(
        p_fecha_presentacion,
        p_fecha_aprobacion,
        v_total_declarado,
        v_total_aprobado,
        v_saldo_final,
        p_estado_rendicion,
        p_comentario,
        p_id_ciclo_caja,
        p_id_usuario_accion,
        p_id_usuario_accion
    );
    
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
    IN p_comentario VARCHAR(500),
    IN p_id_ciclo_caja INT
)
BEGIN
    DECLARE v_total_declarado DECIMAL(12,2) DEFAULT 0;
    DECLARE v_total_aprobado DECIMAL(12,2) DEFAULT 0;
    DECLARE v_saldo_final DECIMAL(12,2) DEFAULT 0;
    DECLARE v_saldo_inicial DECIMAL(12,2) DEFAULT 0;
    DECLARE v_ciclo_id INT DEFAULT NULL;

    IF p_id_rendicion IS NULL OR p_id_rendicion <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de rendicion invalido';
    END IF;

    -- Obtener id_ciclo_caja si no se proporciona
    IF p_id_ciclo_caja IS NULL OR p_id_ciclo_caja <= 0 THEN
        SET v_ciclo_id = (SELECT id_ciclo_caja FROM ope_rendicion WHERE id_rendicion = p_id_rendicion);
    ELSE
        SET v_ciclo_id = p_id_ciclo_caja;
    END IF;

    -- Auto-recalcular totales si hay ciclo asociado y montos son 0
    IF v_ciclo_id IS NOT NULL AND v_ciclo_id > 0 THEN
        IF p_monto_total_declarado IS NULL OR p_monto_total_declarado = 0 THEN
            SET v_total_declarado = COALESCE((
                SELECT SUM(cp.monto_total)
                FROM ope_solicitud_gasto sg
                JOIN ope_comprobante_pago cp ON sg.id_solicitud_gasto = cp.id_solicitud_gasto
                WHERE sg.id_ciclo_caja = v_ciclo_id
                  AND cp.estado_comprobante != 'ANULADO'
            ), 0);
        ELSE
            SET v_total_declarado = p_monto_total_declarado;
        END IF;

        IF p_monto_total_aprobado IS NULL OR p_monto_total_aprobado = 0 THEN
            SET v_total_aprobado = COALESCE((
                SELECT SUM(sg.monto_solicitado)
                FROM ope_solicitud_gasto sg
                WHERE sg.id_ciclo_caja = v_ciclo_id
                  AND sg.estado_solicitud = 'APROBADO'
            ), 0);
        ELSE
            SET v_total_aprobado = p_monto_total_aprobado;
        END IF;

        IF p_monto_saldo_final IS NULL OR p_monto_saldo_final = 0 THEN
            SET v_saldo_inicial = COALESCE((
                SELECT occ.monto_saldo_inicial
                FROM ope_ciclo_caja occ
                WHERE occ.id_ciclo_caja = v_ciclo_id
            ), 0);
            SET v_saldo_final = v_saldo_inicial - v_total_aprobado;
        ELSE
            SET v_saldo_final = p_monto_saldo_final;
        END IF;
    ELSE
        SET v_total_declarado = COALESCE(p_monto_total_declarado, 0);
        SET v_total_aprobado = COALESCE(p_monto_total_aprobado, 0);
        SET v_saldo_final = COALESCE(p_monto_saldo_final, 0);
    END IF;

    UPDATE ope_rendicion
       SET fecha_presentacion = p_fecha_presentacion,
           fecha_aprobacion = p_fecha_aprobacion,
           monto_total_declarado = v_total_declarado,
           monto_total_aprobado = v_total_aprobado,
           monto_saldo_final = v_saldo_final,
           estado_rendicion = p_estado_rendicion,
           comentario = p_comentario,
           id_ciclo_caja = v_ciclo_id,
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
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de rendicion invalido';
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
        COALESCE(vtd.total_declarado, r.monto_total_declarado) AS monto_total_declarado,
        COALESCE(vta.total_aprobado, r.monto_total_aprobado) AS monto_total_aprobado,
        COALESCE(vsf.saldo_final, r.monto_saldo_final) AS monto_saldo_final,
        r.estado_rendicion,
        r.comentario,
        r.id_ciclo_caja,
        cc.id_ciclo_caja AS cc_id_ciclo_caja,
        cc.numero_semana AS cc_numero_semana,
        cc.fecha_apertura AS cc_fecha_apertura,
        cc.fecha_cierre AS cc_fecha_cierre,
        cc.monto_saldo_inicial AS cc_monto_saldo_inicial,
        COALESCE(vtg.total_gastado, cc.monto_total_gastado) AS cc_monto_total_gastado,
        cc.estado_ciclo AS cc_estado_ciclo,
        cc.id_caja_chica AS cc_id_caja_chica,
        cc.id_rendicion AS cc_id_rendicion
    FROM ope_rendicion r
    LEFT JOIN ope_ciclo_caja cc ON r.id_ciclo_caja = cc.id_ciclo_caja
    LEFT JOIN (
        SELECT sg.id_ciclo_caja, SUM(sg.monto_solicitado) AS total_gastado
        FROM ope_solicitud_gasto sg
        WHERE sg.estado_solicitud = 'APROBADO'
        GROUP BY sg.id_ciclo_caja
    ) vtg ON vtg.id_ciclo_caja = r.id_ciclo_caja
    LEFT JOIN (
        SELECT sg.id_ciclo_caja, SUM(cp.monto_total) AS total_declarado
        FROM ope_solicitud_gasto sg
        JOIN ope_comprobante_pago cp ON sg.id_solicitud_gasto = cp.id_solicitud_gasto
        WHERE cp.estado_comprobante != 'ANULADO'
        GROUP BY sg.id_ciclo_caja
    ) vtd ON vtd.id_ciclo_caja = r.id_ciclo_caja
    LEFT JOIN (
        SELECT sg.id_ciclo_caja, SUM(sg.monto_solicitado) AS total_aprobado
        FROM ope_solicitud_gasto sg
        WHERE sg.estado_solicitud = 'APROBADO'
        GROUP BY sg.id_ciclo_caja
    ) vta ON vta.id_ciclo_caja = r.id_ciclo_caja
    LEFT JOIN (
        SELECT occ.id_ciclo_caja, (occ.monto_saldo_inicial - COALESCE(vtg2.total_gastado, 0)) AS saldo_final
        FROM ope_ciclo_caja occ
        LEFT JOIN (
            SELECT sg2.id_ciclo_caja, SUM(sg2.monto_solicitado) AS total_gastado
            FROM ope_solicitud_gasto sg2
            WHERE sg2.estado_solicitud = 'APROBADO'
            GROUP BY sg2.id_ciclo_caja
        ) vtg2 ON vtg2.id_ciclo_caja = occ.id_ciclo_caja
    ) vsf ON vsf.id_ciclo_caja = r.id_ciclo_caja
    WHERE r.id_rendicion = p_id_rendicion;
END$$

DROP PROCEDURE IF EXISTS pa_listar_rendiciones $$
CREATE PROCEDURE pa_listar_rendiciones()
BEGIN
    SELECT
        r.id_rendicion,
        r.fecha_presentacion,
        r.fecha_aprobacion,
        COALESCE(vtd.total_declarado, r.monto_total_declarado) AS monto_total_declarado,
        COALESCE(vta.total_aprobado, r.monto_total_aprobado) AS monto_total_aprobado,
        COALESCE(vsf.saldo_final, r.monto_saldo_final) AS monto_saldo_final,
        r.estado_rendicion,
        r.comentario,
        r.id_ciclo_caja,
        cc.id_ciclo_caja AS cc_id_ciclo_caja,
        cc.numero_semana AS cc_numero_semana,
        cc.fecha_apertura AS cc_fecha_apertura,
        cc.fecha_cierre AS cc_fecha_cierre,
        cc.monto_saldo_inicial AS cc_monto_saldo_inicial,
        COALESCE(vtg.total_gastado, cc.monto_total_gastado) AS cc_monto_total_gastado,
        cc.estado_ciclo AS cc_estado_ciclo,
        cc.id_caja_chica AS cc_id_caja_chica,
        cc.id_rendicion AS cc_id_rendicion
    FROM ope_rendicion r
    LEFT JOIN ope_ciclo_caja cc ON r.id_ciclo_caja = cc.id_ciclo_caja
    LEFT JOIN (
        SELECT sg.id_ciclo_caja, SUM(sg.monto_solicitado) AS total_gastado
        FROM ope_solicitud_gasto sg
        WHERE sg.estado_solicitud = 'APROBADO'
        GROUP BY sg.id_ciclo_caja
    ) vtg ON vtg.id_ciclo_caja = r.id_ciclo_caja
    LEFT JOIN (
        SELECT sg.id_ciclo_caja, SUM(cp.monto_total) AS total_declarado
        FROM ope_solicitud_gasto sg
        JOIN ope_comprobante_pago cp ON sg.id_solicitud_gasto = cp.id_solicitud_gasto
        WHERE cp.estado_comprobante != 'ANULADO'
        GROUP BY sg.id_ciclo_caja
    ) vtd ON vtd.id_ciclo_caja = r.id_ciclo_caja
    LEFT JOIN (
        SELECT sg.id_ciclo_caja, SUM(sg.monto_solicitado) AS total_aprobado
        FROM ope_solicitud_gasto sg
        WHERE sg.estado_solicitud = 'APROBADO'
        GROUP BY sg.id_ciclo_caja
    ) vta ON vta.id_ciclo_caja = r.id_ciclo_caja
    LEFT JOIN (
        SELECT occ.id_ciclo_caja, (occ.monto_saldo_inicial - COALESCE(vtg2.total_gastado, 0)) AS saldo_final
        FROM ope_ciclo_caja occ
        LEFT JOIN (
            SELECT sg2.id_ciclo_caja, SUM(sg2.monto_solicitado) AS total_gastado
            FROM ope_solicitud_gasto sg2
            WHERE sg2.estado_solicitud = 'APROBADO'
            GROUP BY sg2.id_ciclo_caja
        ) vtg2 ON vtg2.id_ciclo_caja = occ.id_ciclo_caja
    ) vsf ON vsf.id_ciclo_caja = r.id_ciclo_caja
    ORDER BY r.estado_rendicion DESC;
END$$

DROP PROCEDURE IF EXISTS pa_listar_todas_rendiciones $$
CREATE PROCEDURE pa_listar_todas_rendiciones()
BEGIN
    SELECT
        r.id_rendicion,
        r.fecha_presentacion,
        r.fecha_aprobacion,
        COALESCE(vtd.total_declarado, r.monto_total_declarado) AS monto_total_declarado,
        COALESCE(vta.total_aprobado, r.monto_total_aprobado) AS monto_total_aprobado,
        COALESCE(vsf.saldo_final, r.monto_saldo_final) AS monto_saldo_final,
        r.estado_rendicion,
        r.comentario,
        r.id_ciclo_caja,
        cc.id_ciclo_caja AS cc_id_ciclo_caja,
        cc.numero_semana AS cc_numero_semana,
        cc.fecha_apertura AS cc_fecha_apertura,
        cc.fecha_cierre AS cc_fecha_cierre,
        cc.monto_saldo_inicial AS cc_monto_saldo_inicial,
        COALESCE(vtg.total_gastado, cc.monto_total_gastado) AS cc_monto_total_gastado,
        cc.estado_ciclo AS cc_estado_ciclo,
        cc.id_caja_chica AS cc_id_caja_chica,
        cc.id_rendicion AS cc_id_rendicion
    FROM ope_rendicion r
    LEFT JOIN ope_ciclo_caja cc ON r.id_ciclo_caja = cc.id_ciclo_caja
    LEFT JOIN (
        SELECT sg.id_ciclo_caja, SUM(sg.monto_solicitado) AS total_gastado
        FROM ope_solicitud_gasto sg
        WHERE sg.estado_solicitud = 'APROBADO'
        GROUP BY sg.id_ciclo_caja
    ) vtg ON vtg.id_ciclo_caja = r.id_ciclo_caja
    LEFT JOIN (
        SELECT sg.id_ciclo_caja, SUM(cp.monto_total) AS total_declarado
        FROM ope_solicitud_gasto sg
        JOIN ope_comprobante_pago cp ON sg.id_solicitud_gasto = cp.id_solicitud_gasto
        WHERE cp.estado_comprobante != 'ANULADO'
        GROUP BY sg.id_ciclo_caja
    ) vtd ON vtd.id_ciclo_caja = r.id_ciclo_caja
    LEFT JOIN (
        SELECT sg.id_ciclo_caja, SUM(sg.monto_solicitado) AS total_aprobado
        FROM ope_solicitud_gasto sg
        WHERE sg.estado_solicitud = 'APROBADO'
        GROUP BY sg.id_ciclo_caja
    ) vta ON vta.id_ciclo_caja = r.id_ciclo_caja
    LEFT JOIN (
        SELECT occ.id_ciclo_caja, (occ.monto_saldo_inicial - COALESCE(vtg2.total_gastado, 0)) AS saldo_final
        FROM ope_ciclo_caja occ
        LEFT JOIN (
            SELECT sg2.id_ciclo_caja, SUM(sg2.monto_solicitado) AS total_gastado
            FROM ope_solicitud_gasto sg2
            WHERE sg2.estado_solicitud = 'APROBADO'
            GROUP BY sg2.id_ciclo_caja
        ) vtg2 ON vtg2.id_ciclo_caja = occ.id_ciclo_caja
    ) vsf ON vsf.id_ciclo_caja = r.id_ciclo_caja
    ORDER BY r.estado_rendicion DESC;
END$$

DROP PROCEDURE IF EXISTS pa_listar_rendiciones_activas $$
CREATE PROCEDURE pa_listar_rendiciones_activas()
BEGIN
    SELECT
        r.id_rendicion,
        r.fecha_presentacion,
        r.fecha_aprobacion,
        COALESCE(vtd.total_declarado, r.monto_total_declarado) AS monto_total_declarado,
        COALESCE(vta.total_aprobado, r.monto_total_aprobado) AS monto_total_aprobado,
        COALESCE(vsf.saldo_final, r.monto_saldo_final) AS monto_saldo_final,
        r.estado_rendicion,
        r.comentario,
        r.id_ciclo_caja,
        cc.id_ciclo_caja AS cc_id_ciclo_caja,
        cc.numero_semana AS cc_numero_semana,
        cc.fecha_apertura AS cc_fecha_apertura,
        cc.fecha_cierre AS cc_fecha_cierre,
        cc.monto_saldo_inicial AS cc_monto_saldo_inicial,
        COALESCE(vtg.total_gastado, cc.monto_total_gastado) AS cc_monto_total_gastado,
        cc.estado_ciclo AS cc_estado_ciclo,
        cc.id_caja_chica AS cc_id_caja_chica,
        cc.id_rendicion AS cc_id_rendicion
    FROM ope_rendicion r
    LEFT JOIN ope_ciclo_caja cc ON r.id_ciclo_caja = cc.id_ciclo_caja
    LEFT JOIN (
        SELECT sg.id_ciclo_caja, SUM(sg.monto_solicitado) AS total_gastado
        FROM ope_solicitud_gasto sg
        WHERE sg.estado_solicitud = 'APROBADO'
        GROUP BY sg.id_ciclo_caja
    ) vtg ON vtg.id_ciclo_caja = r.id_ciclo_caja
    LEFT JOIN (
        SELECT sg.id_ciclo_caja, SUM(cp.monto_total) AS total_declarado
        FROM ope_solicitud_gasto sg
        JOIN ope_comprobante_pago cp ON sg.id_solicitud_gasto = cp.id_solicitud_gasto
        WHERE cp.estado_comprobante != 'ANULADO'
        GROUP BY sg.id_ciclo_caja
    ) vtd ON vtd.id_ciclo_caja = r.id_ciclo_caja
    LEFT JOIN (
        SELECT sg.id_ciclo_caja, SUM(sg.monto_solicitado) AS total_aprobado
        FROM ope_solicitud_gasto sg
        WHERE sg.estado_solicitud = 'APROBADO'
        GROUP BY sg.id_ciclo_caja
    ) vta ON vta.id_ciclo_caja = r.id_ciclo_caja
    LEFT JOIN (
        SELECT occ.id_ciclo_caja, (occ.monto_saldo_inicial - COALESCE(vtg2.total_gastado, 0)) AS saldo_final
        FROM ope_ciclo_caja occ
        LEFT JOIN (
            SELECT sg2.id_ciclo_caja, SUM(sg2.monto_solicitado) AS total_gastado
            FROM ope_solicitud_gasto sg2
            WHERE sg2.estado_solicitud = 'APROBADO'
            GROUP BY sg2.id_ciclo_caja
        ) vtg2 ON vtg2.id_ciclo_caja = occ.id_ciclo_caja
    ) vsf ON vsf.id_ciclo_caja = r.id_ciclo_caja
    WHERE r.estado_rendicion != 'ANULADO'
    ORDER BY r.estado_rendicion DESC, r.id_rendicion DESC;
END$$

DELIMITER ;


DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_ciclo_caja $$
CREATE PROCEDURE pa_insertar_ciclo_caja(
    IN p_id_usuario_accion INT,
	IN p_numero_semana INT,
	IN p_fecha_apertura DATE,
	IN p_fecha_cierre DATE,
	IN p_monto_saldo_inicial DECIMAL(12,2),
	IN p_monto_total_gastado DECIMAL(12,2),
	IN p_estado_ciclo ENUM('ABIERTO','CERRADO','LIQUIDADO','EN_EXCEPCION'),
	IN p_id_caja_chica INT,
	IN p_id_rendicion INT,
	OUT p_id_generado INT
)
BEGIN
    IF p_id_caja_chica IS NULL OR p_id_caja_chica <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de caja chica no valido';
    END IF;

    -- Auto-calcular numero de semana desde fecha_apertura si no se proporciona
    IF p_numero_semana IS NULL OR p_numero_semana <= 0 THEN
        SET p_numero_semana = WEEK(p_fecha_apertura, 1);
    END IF;

    INSERT INTO ope_ciclo_caja(
        numero_semana,
        fecha_apertura,
        fecha_cierre,
        monto_saldo_inicial,
        monto_total_gastado,
        estado_ciclo,
        id_caja_chica,
        id_rendicion,
        id_usuario_creacion,
        id_usuario_modificacion
    ) VALUES(
        p_numero_semana,
        p_fecha_apertura,
        p_fecha_cierre,
        p_monto_saldo_inicial,
        COALESCE(p_monto_total_gastado, 0),
        COALESCE(p_estado_ciclo, 'ABIERTO'),
        p_id_caja_chica,
        p_id_rendicion,
        p_id_usuario_accion,
        p_id_usuario_accion
    );
    
    SET p_id_generado = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS pa_modificar_ciclo_caja $$
CREATE PROCEDURE pa_modificar_ciclo_caja(
    IN p_id_usuario_accion INT,
    IN p_id_ciclo_caja INT,
    IN p_numero_semana INT,
    IN p_fecha_apertura DATE,
    IN p_fecha_cierre DATE,
    IN p_monto_saldo_inicial DECIMAL(12,2),
    IN p_monto_total_gastado DECIMAL(12,2),
    IN p_estado_ciclo VARCHAR(20),
    IN p_id_caja_chica INT,
    IN p_id_rendicion INT
)
BEGIN
    DECLARE v_total_gastado DECIMAL(12,2) DEFAULT 0;
    DECLARE v_total_declarado DECIMAL(12,2) DEFAULT 0;
    DECLARE v_total_aprobado DECIMAL(12,2) DEFAULT 0;
    DECLARE v_saldo_final DECIMAL(12,2) DEFAULT 0;
    DECLARE v_ciclo_saldo_inicial DECIMAL(12,2) DEFAULT 0;
    DECLARE v_nuevo_rendicion_id INT DEFAULT NULL;

    IF p_id_ciclo_caja IS NULL OR p_id_ciclo_caja <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de ciclo de caja no valido';
    END IF;

    -- Siempre recalcular monto_total_gastado desde solicitudes APROBADAS del ciclo
    SET v_total_gastado = COALESCE((
        SELECT SUM(sg.monto_solicitado)
        FROM ope_solicitud_gasto sg
        WHERE sg.id_ciclo_caja = p_id_ciclo_caja
          AND sg.estado_solicitud = 'APROBADO'
    ), 0);

    -- Auto-crear rendicion si se cierra/liquida y no existe
    IF p_estado_ciclo IN ('EN_EVALUACION', 'CERRADO', 'LIQUIDADO') AND (p_id_rendicion IS NULL OR p_id_rendicion <= 0) THEN
        -- Obtener saldo inicial del ciclo
        SET v_ciclo_saldo_inicial = COALESCE((
            SELECT occ.monto_saldo_inicial
            FROM ope_ciclo_caja occ
            WHERE occ.id_ciclo_caja = p_id_ciclo_caja
        ), 0);

        -- Calcular total declarado desde comprobantes de pago del ciclo
        SET v_total_declarado = COALESCE((
            SELECT SUM(cp.monto_total)
            FROM ope_solicitud_gasto sg
            JOIN ope_comprobante_pago cp ON sg.id_solicitud_gasto = cp.id_solicitud_gasto
            WHERE sg.id_ciclo_caja = p_id_ciclo_caja
              AND cp.estado_comprobante != 'ANULADO'
        ), 0);

        SET v_total_aprobado = v_total_gastado;
        SET v_saldo_final = v_ciclo_saldo_inicial - v_total_aprobado;

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
            CURDATE(),
            NULL,
            v_total_declarado,
            v_total_aprobado,
            v_saldo_final,
            'EN_ESPERA',
            'Rendicion generada automaticamente al cerrar ciclo',
            p_id_ciclo_caja,
            p_id_usuario_accion,
            p_id_usuario_accion
        );

        SET v_nuevo_rendicion_id = LAST_INSERT_ID();
        SET p_id_rendicion = v_nuevo_rendicion_id;
    END IF;

    UPDATE ope_ciclo_caja
       SET numero_semana = p_numero_semana,
           fecha_apertura = p_fecha_apertura,
           fecha_cierre = p_fecha_cierre,
           monto_saldo_inicial = p_monto_saldo_inicial,
           monto_total_gastado = v_total_gastado,
           estado_ciclo = p_estado_ciclo,
           id_caja_chica = p_id_caja_chica,
           id_rendicion = p_id_rendicion,
           id_usuario_modificacion = p_id_usuario_accion
     WHERE id_ciclo_caja = p_id_ciclo_caja;
END$$

DROP PROCEDURE IF EXISTS pa_eliminar_ciclo_caja $$
CREATE PROCEDURE pa_eliminar_ciclo_caja(
    IN p_id_usuario_accion INT,
    IN p_id_ciclo_caja INT
)
BEGIN
    IF p_id_ciclo_caja IS NULL OR p_id_ciclo_caja <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de ciclo de caja no valido';
    END IF;

    UPDATE ope_ciclo_caja
       SET estado_ciclo = 'CERRADO',
           id_usuario_modificacion = p_id_usuario_accion
     WHERE id_ciclo_caja = p_id_ciclo_caja;
END$$

DROP PROCEDURE IF EXISTS pa_buscar_ciclo_caja_por_id $$
CREATE PROCEDURE pa_buscar_ciclo_caja_por_id(
    IN p_id_ciclo_caja INT
)
BEGIN
    IF p_id_ciclo_caja IS NULL OR p_id_ciclo_caja <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de ciclo de caja no valido';
    END IF;

    SELECT 
        occ.id_ciclo_caja, 
        occ.numero_semana, 
        occ.fecha_apertura, 
        occ.fecha_cierre, 
        occ.monto_saldo_inicial, 
        COALESCE(vtg.total_gastado, occ.monto_total_gastado) AS monto_total_gastado, 
        occ.estado_ciclo, 
        occ.id_caja_chica, 
        occ.id_rendicion,
        ccj.id_fondo AS ccj_id_fondo,
        ccj.monto_techo AS ccj_monto_techo,
        ccj.id_cuenta_bancaria AS ccj_id_cuenta_bancaria,
        ccj.id_moneda AS ccj_id_moneda,
        f.nombre_fondo AS ccj_nombre_fondo,
        f.estado_fondo AS ccj_estado_fondo,
        ren.id_rendicion AS ren_id_rendicion,
        ren.fecha_presentacion AS ren_fecha_presentacion,
        ren.fecha_aprobacion AS ren_fecha_aprobacion,
        COALESCE(vtd.total_declarado, ren.monto_total_declarado) AS ren_monto_total_declarado,
        COALESCE(vta.total_aprobado, ren.monto_total_aprobado) AS ren_monto_total_aprobado,
        COALESCE(vsf.saldo_final, ren.monto_saldo_final) AS ren_monto_saldo_final,
        ren.estado_rendicion AS ren_estado_rendicion,
        ren.comentario AS ren_comentario
    FROM ope_ciclo_caja occ
    LEFT JOIN tes_caja_chica ccj ON occ.id_caja_chica = ccj.id_fondo
    LEFT JOIN tes_fondo f ON ccj.id_fondo = f.id_fondo
    LEFT JOIN ope_rendicion ren ON occ.id_rendicion = ren.id_rendicion
    LEFT JOIN (
        SELECT sg.id_ciclo_caja, SUM(sg.monto_solicitado) AS total_gastado
        FROM ope_solicitud_gasto sg
        WHERE sg.estado_solicitud = 'APROBADO'
        GROUP BY sg.id_ciclo_caja
    ) vtg ON vtg.id_ciclo_caja = occ.id_ciclo_caja
    LEFT JOIN (
        SELECT sg.id_ciclo_caja, SUM(cp.monto_total) AS total_declarado
        FROM ope_solicitud_gasto sg
        JOIN ope_comprobante_pago cp ON sg.id_solicitud_gasto = cp.id_solicitud_gasto
        WHERE cp.estado_comprobante != 'ANULADO'
        GROUP BY sg.id_ciclo_caja
    ) vtd ON vtd.id_ciclo_caja = occ.id_ciclo_caja
    LEFT JOIN (
        SELECT sg.id_ciclo_caja, SUM(sg.monto_solicitado) AS total_aprobado
        FROM ope_solicitud_gasto sg
        WHERE sg.estado_solicitud = 'APROBADO'
        GROUP BY sg.id_ciclo_caja
    ) vta ON vta.id_ciclo_caja = occ.id_ciclo_caja
    LEFT JOIN (
        SELECT occ2.id_ciclo_caja, (occ2.monto_saldo_inicial - COALESCE(vtg2.total_gastado, 0)) AS saldo_final
        FROM ope_ciclo_caja occ2
        LEFT JOIN (
            SELECT sg2.id_ciclo_caja, SUM(sg2.monto_solicitado) AS total_gastado
            FROM ope_solicitud_gasto sg2
            WHERE sg2.estado_solicitud = 'APROBADO'
            GROUP BY sg2.id_ciclo_caja
        ) vtg2 ON vtg2.id_ciclo_caja = occ2.id_ciclo_caja
    ) vsf ON vsf.id_ciclo_caja = occ.id_ciclo_caja
    WHERE occ.id_ciclo_caja = p_id_ciclo_caja
    ORDER BY occ.estado_ciclo DESC;
END$$

DROP PROCEDURE IF EXISTS pa_listar_ciclos_caja $$
CREATE PROCEDURE pa_listar_ciclos_caja()
BEGIN
    SELECT 
        occ.id_ciclo_caja, 
        occ.numero_semana, 
        occ.fecha_apertura, 
        occ.fecha_cierre, 
        occ.monto_saldo_inicial, 
        COALESCE(vtg.total_gastado, occ.monto_total_gastado) AS monto_total_gastado, 
        occ.estado_ciclo, 
        occ.id_caja_chica, 
        occ.id_rendicion,
        ccj.id_fondo AS ccj_id_fondo,
        ccj.monto_techo AS ccj_monto_techo,
        ccj.id_cuenta_bancaria AS ccj_id_cuenta_bancaria,
        ccj.id_moneda AS ccj_id_moneda,
        f.nombre_fondo AS ccj_nombre_fondo,
        f.estado_fondo AS ccj_estado_fondo,
        ren.id_rendicion AS ren_id_rendicion,
        ren.fecha_presentacion AS ren_fecha_presentacion,
        ren.fecha_aprobacion AS ren_fecha_aprobacion,
        COALESCE(vtd.total_declarado, ren.monto_total_declarado) AS ren_monto_total_declarado,
        COALESCE(vta.total_aprobado, ren.monto_total_aprobado) AS ren_monto_total_aprobado,
        COALESCE(vsf.saldo_final, ren.monto_saldo_final) AS ren_monto_saldo_final,
        ren.estado_rendicion AS ren_estado_rendicion,
        ren.comentario AS ren_comentario
    FROM ope_ciclo_caja occ
    LEFT JOIN tes_caja_chica ccj ON occ.id_caja_chica = ccj.id_fondo
    LEFT JOIN tes_fondo f ON ccj.id_fondo = f.id_fondo
    LEFT JOIN ope_rendicion ren ON occ.id_rendicion = ren.id_rendicion
    LEFT JOIN (
        SELECT sg.id_ciclo_caja, SUM(sg.monto_solicitado) AS total_gastado
        FROM ope_solicitud_gasto sg
        WHERE sg.estado_solicitud = 'APROBADO'
        GROUP BY sg.id_ciclo_caja
    ) vtg ON vtg.id_ciclo_caja = occ.id_ciclo_caja
    LEFT JOIN (
        SELECT sg.id_ciclo_caja, SUM(cp.monto_total) AS total_declarado
        FROM ope_solicitud_gasto sg
        JOIN ope_comprobante_pago cp ON sg.id_solicitud_gasto = cp.id_solicitud_gasto
        WHERE cp.estado_comprobante != 'ANULADO'
        GROUP BY sg.id_ciclo_caja
    ) vtd ON vtd.id_ciclo_caja = occ.id_ciclo_caja
    LEFT JOIN (
        SELECT sg.id_ciclo_caja, SUM(sg.monto_solicitado) AS total_aprobado
        FROM ope_solicitud_gasto sg
        WHERE sg.estado_solicitud = 'APROBADO'
        GROUP BY sg.id_ciclo_caja
    ) vta ON vta.id_ciclo_caja = occ.id_ciclo_caja
    LEFT JOIN (
        SELECT occ2.id_ciclo_caja, (occ2.monto_saldo_inicial - COALESCE(vtg2.total_gastado, 0)) AS saldo_final
        FROM ope_ciclo_caja occ2
        LEFT JOIN (
            SELECT sg2.id_ciclo_caja, SUM(sg2.monto_solicitado) AS total_gastado
            FROM ope_solicitud_gasto sg2
            WHERE sg2.estado_solicitud = 'APROBADO'
            GROUP BY sg2.id_ciclo_caja
        ) vtg2 ON vtg2.id_ciclo_caja = occ2.id_ciclo_caja
    ) vsf ON vsf.id_ciclo_caja = occ.id_ciclo_caja
    WHERE occ.estado_ciclo = 'ABIERTO'
    ORDER BY occ.id_ciclo_caja DESC;
END$$

DROP PROCEDURE IF EXISTS pa_listar_todos_ciclos_caja $$
CREATE PROCEDURE pa_listar_todos_ciclos_caja()
BEGIN
    SELECT 
        occ.id_ciclo_caja, 
        occ.numero_semana, 
        occ.fecha_apertura, 
        occ.fecha_cierre, 
        occ.monto_saldo_inicial, 
        COALESCE(vtg.total_gastado, occ.monto_total_gastado) AS monto_total_gastado, 
        occ.estado_ciclo, 
        occ.id_caja_chica, 
        occ.id_rendicion,
        ccj.id_fondo AS ccj_id_fondo,
        ccj.monto_techo AS ccj_monto_techo,
        ccj.id_cuenta_bancaria AS ccj_id_cuenta_bancaria,
        ccj.id_moneda AS ccj_id_moneda,
        f.nombre_fondo AS ccj_nombre_fondo,
        f.estado_fondo AS ccj_estado_fondo,
        ren.id_rendicion AS ren_id_rendicion,
        ren.fecha_presentacion AS ren_fecha_presentacion,
        ren.fecha_aprobacion AS ren_fecha_aprobacion,
        COALESCE(vtd.total_declarado, ren.monto_total_declarado) AS ren_monto_total_declarado,
        COALESCE(vta.total_aprobado, ren.monto_total_aprobado) AS ren_monto_total_aprobado,
        COALESCE(vsf.saldo_final, ren.monto_saldo_final) AS ren_monto_saldo_final,
        ren.estado_rendicion AS ren_estado_rendicion,
        ren.comentario AS ren_comentario
    FROM ope_ciclo_caja occ
    LEFT JOIN tes_caja_chica ccj ON occ.id_caja_chica = ccj.id_fondo
    LEFT JOIN tes_fondo f ON ccj.id_fondo = f.id_fondo
    LEFT JOIN ope_rendicion ren ON occ.id_rendicion = ren.id_rendicion
    LEFT JOIN (
        SELECT sg.id_ciclo_caja, SUM(sg.monto_solicitado) AS total_gastado
        FROM ope_solicitud_gasto sg
        WHERE sg.estado_solicitud = 'APROBADO'
        GROUP BY sg.id_ciclo_caja
    ) vtg ON vtg.id_ciclo_caja = occ.id_ciclo_caja
    LEFT JOIN (
        SELECT sg.id_ciclo_caja, SUM(cp.monto_total) AS total_declarado
        FROM ope_solicitud_gasto sg
        JOIN ope_comprobante_pago cp ON sg.id_solicitud_gasto = cp.id_solicitud_gasto
        WHERE cp.estado_comprobante != 'ANULADO'
        GROUP BY sg.id_ciclo_caja
    ) vtd ON vtd.id_ciclo_caja = occ.id_ciclo_caja
    LEFT JOIN (
        SELECT sg.id_ciclo_caja, SUM(sg.monto_solicitado) AS total_aprobado
        FROM ope_solicitud_gasto sg
        WHERE sg.estado_solicitud = 'APROBADO'
        GROUP BY sg.id_ciclo_caja
    ) vta ON vta.id_ciclo_caja = occ.id_ciclo_caja
    LEFT JOIN (
        SELECT occ2.id_ciclo_caja, (occ2.monto_saldo_inicial - COALESCE(vtg2.total_gastado, 0)) AS saldo_final
        FROM ope_ciclo_caja occ2
        LEFT JOIN (
            SELECT sg2.id_ciclo_caja, SUM(sg2.monto_solicitado) AS total_gastado
            FROM ope_solicitud_gasto sg2
            WHERE sg2.estado_solicitud = 'APROBADO'
            GROUP BY sg2.id_ciclo_caja
        ) vtg2 ON vtg2.id_ciclo_caja = occ2.id_ciclo_caja
    ) vsf ON vsf.id_ciclo_caja = occ.id_ciclo_caja
    ORDER BY FIELD(occ.estado_ciclo, 'ABIERTO', 'EN_EXCEPCION', 'CERRADO', 'LIQUIDADO'), occ.id_ciclo_caja DESC;
END$$

DROP PROCEDURE IF EXISTS pa_listar_ciclos_activos $$
CREATE PROCEDURE pa_listar_ciclos_activos()
BEGIN
    SELECT
        occ.id_ciclo_caja,
        occ.numero_semana,
        occ.fecha_apertura,
        occ.fecha_cierre,
        occ.monto_saldo_inicial,
        COALESCE(vtg.total_gastado, occ.monto_total_gastado) AS monto_total_gastado,
        occ.estado_ciclo,
        occ.id_caja_chica,
        occ.id_rendicion,
        ccj.id_fondo AS ccj_id_fondo,
        ccj.monto_techo AS ccj_monto_techo,
        ccj.id_cuenta_bancaria AS ccj_id_cuenta_bancaria,
        ccj.id_moneda AS ccj_id_moneda,
        f.nombre_fondo AS ccj_nombre_fondo,
        f.estado_fondo AS ccj_estado_fondo,
        ren.id_rendicion AS ren_id_rendicion,
        ren.fecha_presentacion AS ren_fecha_presentacion,
        ren.fecha_aprobacion AS ren_fecha_aprobacion,
        COALESCE(vtd.total_declarado, ren.monto_total_declarado) AS ren_monto_total_declarado,
        COALESCE(vta.total_aprobado, ren.monto_total_aprobado) AS ren_monto_total_aprobado,
        COALESCE(vsf.saldo_final, ren.monto_saldo_final) AS ren_monto_saldo_final,
        ren.estado_rendicion AS ren_estado_rendicion,
        ren.comentario AS ren_comentario
    FROM ope_ciclo_caja occ
    LEFT JOIN tes_caja_chica ccj ON occ.id_caja_chica = ccj.id_fondo
    LEFT JOIN tes_fondo f ON ccj.id_fondo = f.id_fondo
    LEFT JOIN ope_rendicion ren ON occ.id_rendicion = ren.id_rendicion
    LEFT JOIN (
        SELECT sg.id_ciclo_caja, SUM(sg.monto_solicitado) AS total_gastado
        FROM ope_solicitud_gasto sg
        WHERE sg.estado_solicitud = 'APROBADO'
        GROUP BY sg.id_ciclo_caja
    ) vtg ON vtg.id_ciclo_caja = occ.id_ciclo_caja
    LEFT JOIN (
        SELECT sg.id_ciclo_caja, SUM(cp.monto_total) AS total_declarado
        FROM ope_solicitud_gasto sg
        JOIN ope_comprobante_pago cp ON sg.id_solicitud_gasto = cp.id_solicitud_gasto
        WHERE cp.estado_comprobante != 'ANULADO'
        GROUP BY sg.id_ciclo_caja
    ) vtd ON vtd.id_ciclo_caja = occ.id_ciclo_caja
    LEFT JOIN (
        SELECT sg.id_ciclo_caja, SUM(sg.monto_solicitado) AS total_aprobado
        FROM ope_solicitud_gasto sg
        WHERE sg.estado_solicitud = 'APROBADO'
        GROUP BY sg.id_ciclo_caja
    ) vta ON vta.id_ciclo_caja = occ.id_ciclo_caja
    LEFT JOIN (
        SELECT occ2.id_ciclo_caja, (occ2.monto_saldo_inicial - COALESCE(vtg2.total_gastado, 0)) AS saldo_final
        FROM ope_ciclo_caja occ2
        LEFT JOIN (
            SELECT sg2.id_ciclo_caja, SUM(sg2.monto_solicitado) AS total_gastado
            FROM ope_solicitud_gasto sg2
            WHERE sg2.estado_solicitud = 'APROBADO'
            GROUP BY sg2.id_ciclo_caja
        ) vtg2 ON vtg2.id_ciclo_caja = occ2.id_ciclo_caja
    ) vsf ON vsf.id_ciclo_caja = occ.id_ciclo_caja
    WHERE occ.estado_ciclo = 'ABIERTO'
    ORDER BY occ.fecha_apertura DESC;
END$$

DROP PROCEDURE IF EXISTS pa_listar_ciclos_pasados $$
CREATE PROCEDURE pa_listar_ciclos_pasados()
BEGIN
    SELECT
        occ.id_ciclo_caja,
        occ.numero_semana,
        occ.fecha_apertura,
        occ.fecha_cierre,
        occ.monto_saldo_inicial,
        COALESCE(vtg.total_gastado, occ.monto_total_gastado) AS monto_total_gastado,
        occ.estado_ciclo,
        occ.id_caja_chica,
        occ.id_rendicion,
        ccj.id_fondo AS ccj_id_fondo,
        ccj.monto_techo AS ccj_monto_techo,
        ccj.id_cuenta_bancaria AS ccj_id_cuenta_bancaria,
        ccj.id_moneda AS ccj_id_moneda,
        f.nombre_fondo AS ccj_nombre_fondo,
        f.estado_fondo AS ccj_estado_fondo,
        ren.id_rendicion AS ren_id_rendicion,
        ren.fecha_presentacion AS ren_fecha_presentacion,
        ren.fecha_aprobacion AS ren_fecha_aprobacion,
        COALESCE(vtd.total_declarado, ren.monto_total_declarado) AS ren_monto_total_declarado,
        COALESCE(vta.total_aprobado, ren.monto_total_aprobado) AS ren_monto_total_aprobado,
        COALESCE(vsf.saldo_final, ren.monto_saldo_final) AS ren_monto_saldo_final,
        ren.estado_rendicion AS ren_estado_rendicion,
        ren.comentario AS ren_comentario
    FROM ope_ciclo_caja occ
    LEFT JOIN tes_caja_chica ccj ON occ.id_caja_chica = ccj.id_fondo
    LEFT JOIN tes_fondo f ON ccj.id_fondo = f.id_fondo
    LEFT JOIN ope_rendicion ren ON occ.id_rendicion = ren.id_rendicion
    LEFT JOIN (
        SELECT sg.id_ciclo_caja, SUM(sg.monto_solicitado) AS total_gastado
        FROM ope_solicitud_gasto sg
        WHERE sg.estado_solicitud = 'APROBADO'
        GROUP BY sg.id_ciclo_caja
    ) vtg ON vtg.id_ciclo_caja = occ.id_ciclo_caja
    LEFT JOIN (
        SELECT sg.id_ciclo_caja, SUM(cp.monto_total) AS total_declarado
        FROM ope_solicitud_gasto sg
        JOIN ope_comprobante_pago cp ON sg.id_solicitud_gasto = cp.id_solicitud_gasto
        WHERE cp.estado_comprobante != 'ANULADO'
        GROUP BY sg.id_ciclo_caja
    ) vtd ON vtd.id_ciclo_caja = occ.id_ciclo_caja
    LEFT JOIN (
        SELECT sg.id_ciclo_caja, SUM(sg.monto_solicitado) AS total_aprobado
        FROM ope_solicitud_gasto sg
        WHERE sg.estado_solicitud = 'APROBADO'
        GROUP BY sg.id_ciclo_caja
    ) vta ON vta.id_ciclo_caja = occ.id_ciclo_caja
    LEFT JOIN (
        SELECT occ2.id_ciclo_caja, (occ2.monto_saldo_inicial - COALESCE(vtg2.total_gastado, 0)) AS saldo_final
        FROM ope_ciclo_caja occ2
        LEFT JOIN (
            SELECT sg2.id_ciclo_caja, SUM(sg2.monto_solicitado) AS total_gastado
            FROM ope_solicitud_gasto sg2
            WHERE sg2.estado_solicitud = 'APROBADO'
            GROUP BY sg2.id_ciclo_caja
        ) vtg2 ON vtg2.id_ciclo_caja = occ2.id_ciclo_caja
    ) vsf ON vsf.id_ciclo_caja = occ.id_ciclo_caja
    WHERE occ.estado_ciclo IN ('CERRADO', 'LIQUIDADO', 'EN_EXCEPCION')
    ORDER BY occ.fecha_cierre DESC, occ.estado_ciclo, occ.id_ciclo_caja DESC;
END$$

DELIMITER ;

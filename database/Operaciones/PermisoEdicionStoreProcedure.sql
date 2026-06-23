
DELIMITER $$

DROP PROCEDURE IF EXISTS pa_solicitar_permiso_edicion $$
CREATE PROCEDURE pa_solicitar_permiso_edicion(
    IN p_id_usuario_accion INT,
    IN p_id_usuario_solicitante INT,
    IN p_id_comprobante INT,
    IN p_motivo_solicitud VARCHAR(500),
    OUT p_id_generado INT
)
BEGIN
    DECLARE v_permiso_existente INT DEFAULT NULL;

    -- Check existing active permission/request for same comprobante and solicitante
    SELECT pe.id_permiso INTO v_permiso_existente
    FROM ope_permiso_edicion pe
    WHERE pe.id_comprobante = p_id_comprobante
      AND pe.id_usuario_solicitante = p_id_usuario_solicitante
      AND pe.estado IN ('ACTIVO')
      AND pe.fecha_expiracion > NOW()
    LIMIT 1;

    IF v_permiso_existente IS NOT NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ya existe una solicitud o permiso activo para este comprobante';
    END IF;

    INSERT INTO ope_permiso_edicion(
        id_usuario_solicitante,
        id_usuario_autorizador,
        id_comprobante,
        estado,
        motivo_solicitud,
        fecha_creacion,
        fecha_expiracion,
        creado_at,
        actualizado_at,
        id_usuario_creacion,
        id_usuario_modificacion
    ) VALUES(
        p_id_usuario_solicitante,
        NULL,
        p_id_comprobante,
        'ACTIVO',
        p_motivo_solicitud,
        NOW(),
        DATE_ADD(NOW(), INTERVAL 48 HOUR),
        NOW(),
        NOW(),
        p_id_usuario_accion,
        p_id_usuario_accion
    );

    SET p_id_generado = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS pa_otorgar_permiso_edicion $$
CREATE PROCEDURE pa_otorgar_permiso_edicion(
    IN p_id_usuario_accion INT,
    IN p_id_permiso INT,
    IN p_id_usuario_autorizador INT,
    IN p_motivo_autorizacion VARCHAR(500)
)
BEGIN
    IF p_id_permiso IS NULL OR p_id_permiso <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de permiso invalido';
    END IF;

    UPDATE ope_permiso_edicion
       SET id_usuario_autorizador = p_id_usuario_autorizador,
           motivo_autorizacion = p_motivo_autorizacion,
           fecha_expiracion = DATE_ADD(NOW(), INTERVAL 48 HOUR),
           actualizado_at = NOW(),
           id_usuario_modificacion = p_id_usuario_accion
     WHERE id_permiso = p_id_permiso
       AND estado = 'ACTIVO'
       AND id_usuario_autorizador IS NULL;

    IF ROW_COUNT() = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El permiso no existe o ya fue otorgado/revocado';
    END IF;
END$$

DROP PROCEDURE IF EXISTS pa_revocar_permiso_edicion $$
CREATE PROCEDURE pa_revocar_permiso_edicion(
    IN p_id_usuario_accion INT,
    IN p_id_permiso INT
)
BEGIN
    IF p_id_permiso IS NULL OR p_id_permiso <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de permiso invalido';
    END IF;

    UPDATE ope_permiso_edicion
       SET estado = 'REVOCADO',
           actualizado_at = NOW(),
           id_usuario_modificacion = p_id_usuario_accion
     WHERE id_permiso = p_id_permiso
       AND estado = 'ACTIVO';
END$$

DROP PROCEDURE IF EXISTS pa_listar_permisos_pendientes $$
CREATE PROCEDURE pa_listar_permisos_pendientes(
    IN p_id_autorizador INT
)
BEGIN
    SELECT
        pe.id_permiso,
        pe.id_usuario_solicitante,
        pe.id_usuario_autorizador,
        pe.id_comprobante,
        pe.estado,
        pe.motivo_solicitud,
        pe.motivo_autorizacion,
        pe.fecha_creacion,
        pe.fecha_expiracion,
        pe.fecha_uso,
        u_sol.nombres AS sol_nombres,
        u_sol.apellido_paterno AS sol_apellido_paterno,
        u_sol.apellido_materno AS sol_apellido_materno,
        cp.tipo_documento AS cp_tipo_documento,
        cp.ruc_proveedor AS cp_ruc_proveedor,
        cp.razon_social AS cp_razon_social,
        cp.monto_total AS cp_monto_total,
        cp.estado_comprobante AS cp_estado_comprobante,
        sg.id_solicitud_gasto AS sg_id_solicitud_gasto,
        sg.monto_solicitado AS sg_monto_solicitado,
        occ.id_ciclo_caja AS ciclo_id_ciclo_caja,
        occ.estado_ciclo AS ciclo_estado_ciclo
    FROM ope_permiso_edicion pe
    JOIN rrhh_usuario u_sol ON pe.id_usuario_solicitante = u_sol.id_usuario
    JOIN ope_comprobante_pago cp ON pe.id_comprobante = cp.id_comprobante
    JOIN ope_solicitud_gasto sg ON cp.id_solicitud_gasto = sg.id_solicitud_gasto
    JOIN ope_ciclo_caja occ ON sg.id_ciclo_caja = occ.id_ciclo_caja
    WHERE pe.estado = 'ACTIVO'
      AND pe.fecha_expiracion > NOW()
    ORDER BY
      CASE WHEN pe.id_usuario_autorizador IS NULL THEN 0 ELSE 1 END,
      pe.fecha_creacion DESC;
END$$

DROP PROCEDURE IF EXISTS pa_listar_comprobantes_en_excepcion $$
CREATE PROCEDURE pa_listar_comprobantes_en_excepcion()
BEGIN
    SELECT
        cp.id_comprobante,
        cp.tipo_documento,
        cp.ruc_proveedor,
        cp.razon_social,
        cp.monto_total,
        cp.estado_comprobante,
        cp.id_solicitud_gasto,
        sg.monto_solicitado AS sg_monto_solicitado,
        sg.id_usuario_solicitante AS sg_id_usuario_solicitante,
        u_sol.nombres AS sol_nombres,
        u_sol.apellido_paterno AS sol_apellido_paterno,
        u_sol.apellido_materno AS sol_apellido_materno,
        occ.id_ciclo_caja AS ciclo_id_ciclo_caja,
        occ.numero_semana AS ciclo_numero_semana,
        occ.fecha_apertura AS ciclo_fecha_apertura,
        occ.fecha_cierre AS ciclo_fecha_cierre,
        occ.monto_saldo_inicial AS ciclo_saldo_inicial,
        ccj.id_fondo AS ccj_id_fondo,
        f.nombre_fondo AS ccj_nombre_fondo
    FROM ope_ciclo_caja occ
    JOIN tes_caja_chica ccj ON occ.id_caja_chica = ccj.id_fondo
    JOIN tes_fondo f ON ccj.id_fondo = f.id_fondo
    JOIN ope_solicitud_gasto sg ON sg.id_ciclo_caja = occ.id_ciclo_caja
    JOIN ope_comprobante_pago cp ON cp.id_solicitud_gasto = sg.id_solicitud_gasto
    JOIN rrhh_usuario u_sol ON sg.id_usuario_solicitante = u_sol.id_usuario
    WHERE occ.estado_ciclo = 'EN_EXCEPCION'
    ORDER BY occ.id_ciclo_caja DESC, cp.id_comprobante DESC;
END$$

DELIMITER ;

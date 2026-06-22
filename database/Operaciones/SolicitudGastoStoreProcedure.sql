
DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_solicitud_gasto $$
CREATE PROCEDURE pa_insertar_solicitud_gasto(
    IN p_id_usuario_accion INT,
    IN p_fecha_solicitud DATE,
    IN p_monto_solicitado DECIMAL(12,2),
    IN p_id_moneda_original INT,
    IN p_tipo_cambio DECIMAL(10,4),
    IN p_monto_convertido DECIMAL(12,2),
    IN p_motivo_solicitud VARCHAR(200),
    IN p_estado_solicitud ENUM('PENDIENTE','APROBADO','PAGADO','RENDIDO','RECHAZADO','ANULADO'),
    IN p_medio_desembolso ENUM('YAPE','PLIN','TRANSFERENCIA','EFECTIVO'),
    IN p_comentario_decision VARCHAR(500),
    IN p_id_usuario_solicitante INT,
    IN p_id_usuario_destinatario INT,
    IN p_id_jefe_aprobador INT,
    IN p_id_tesorero_aprobador INT,
    IN p_id_ciclo_caja INT,
    OUT p_id_generado INT
)
BEGIN
    INSERT INTO ope_solicitud_gasto(
        fecha_solicitud,
        monto_solicitado,
        id_moneda_original,
        tipo_cambio,
        monto_convertido,
        motivo_solicitud,
        estado_solicitud,
        medio_desembolso,
        comentario_decision,
        id_usuario_solicitante,
        id_usuario_destinatario,
        id_jefe_aprobador,
        id_tesorero_aprobador,
        id_ciclo_caja,
        id_usuario_creacion,
        id_usuario_modificacion
    )
    VALUES(
        p_fecha_solicitud,
        p_monto_solicitado,
        p_id_moneda_original,
        p_tipo_cambio,
        p_monto_convertido,
        p_motivo_solicitud,
        p_estado_solicitud,
        p_medio_desembolso,
        p_comentario_decision,
        p_id_usuario_solicitante,
        p_id_usuario_destinatario,
        p_id_jefe_aprobador,
        p_id_tesorero_aprobador,
        p_id_ciclo_caja,
        p_id_usuario_accion,
        p_id_usuario_accion
    );

    SET p_id_generado = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS pa_modificar_solicitud_gasto $$
CREATE PROCEDURE pa_modificar_solicitud_gasto(
    IN p_id_usuario_accion INT,
    IN p_id_solicitud_gasto INT,
    IN p_fecha_solicitud DATE,
    IN p_monto_solicitado DECIMAL(12,2),
    IN p_id_moneda_original INT,
    IN p_tipo_cambio DECIMAL(10,4),
    IN p_monto_convertido DECIMAL(12,2),
    IN p_motivo_solicitud VARCHAR(200),
    IN p_estado_solicitud ENUM('PENDIENTE','APROBADO','PAGADO','RENDIDO','RECHAZADO','ANULADO'),
    IN p_medio_desembolso ENUM('YAPE','PLIN','TRANSFERENCIA','EFECTIVO'),
    IN p_comentario_decision VARCHAR(500),
    IN p_id_usuario_solicitante INT,
    IN p_id_usuario_destinatario INT,
    IN p_id_jefe_aprobador INT,
    IN p_id_tesorero_aprobador INT,
    IN p_id_ciclo_caja INT
)
BEGIN
    IF p_id_solicitud_gasto IS NULL OR p_id_solicitud_gasto <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de solicitud de gasto inválido';
    END IF;

    UPDATE ope_solicitud_gasto
    SET fecha_solicitud = p_fecha_solicitud,
        monto_solicitado = p_monto_solicitado,
        id_moneda_original = p_id_moneda_original,
        tipo_cambio = p_tipo_cambio,
        monto_convertido = p_monto_convertido,
        motivo_solicitud = p_motivo_solicitud,
        estado_solicitud = p_estado_solicitud,
        medio_desembolso = p_medio_desembolso,
        comentario_decision = p_comentario_decision,
        id_usuario_solicitante = p_id_usuario_solicitante,
        id_usuario_destinatario = p_id_usuario_destinatario,
        id_jefe_aprobador = p_id_jefe_aprobador,
        id_tesorero_aprobador = p_id_tesorero_aprobador,
        id_ciclo_caja = p_id_ciclo_caja,
        id_usuario_modificacion = p_id_usuario_accion
    WHERE id_solicitud_gasto = p_id_solicitud_gasto;
END$$

DROP PROCEDURE IF EXISTS pa_eliminar_solicitud_gasto $$
CREATE PROCEDURE pa_eliminar_solicitud_gasto(
    IN p_id_usuario_accion INT,
    IN p_id_solicitud_gasto INT
)
BEGIN
    IF p_id_solicitud_gasto IS NULL OR p_id_solicitud_gasto <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de solicitud de gasto inválido';
    END IF;

    UPDATE ope_solicitud_gasto
    SET estado_solicitud = 'ANULADO',
        id_usuario_modificacion = p_id_usuario_accion
    WHERE id_solicitud_gasto = p_id_solicitud_gasto;
END$$

DROP PROCEDURE IF EXISTS pa_buscar_solicitud_gasto_por_id $$
CREATE PROCEDURE pa_buscar_solicitud_gasto_por_id(
    IN p_id_solicitud_gasto INT
)
BEGIN
    IF p_id_solicitud_gasto IS NULL OR p_id_solicitud_gasto <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de solicitud de gasto inválido';
    END IF;

    SELECT
        sg.id_solicitud_gasto,
        sg.fecha_solicitud,
        sg.monto_solicitado,
        sg.id_moneda_original,
        sg.tipo_cambio,
        sg.monto_convertido,
        sg.motivo_solicitud,
        sg.estado_solicitud,
        sg.medio_desembolso,
        sg.id_usuario_solicitante,
        sg.id_usuario_destinatario,
        sg.id_jefe_aprobador,
        sg.id_tesorero_aprobador,
        sg.id_ciclo_caja,
        sg.comentario_decision,
        mon.id_moneda AS mon_id_moneda,
        mon.codigo_iso AS mon_codigo_iso,
        mon.simbolo AS mon_simbolo,
        mon.nombre_moneda AS mon_nombre,
        mon.descripcion AS mon_descripcion,
        mon.activa AS mon_activa,
        sol.id_usuario AS sol_id_usuario,
        usol.nombres AS sol_nombres,
        usol.apellido_paterno AS sol_apellido_paterno,
        usol.apellido_materno AS sol_apellido_materno,
        usol.correo AS sol_correo,
        sol.numero_celular AS sol_numero_celular,
        sol.rol_flujo AS sol_rol_flujo,
        des.id_usuario AS des_id_usuario,
        udes.nombres AS des_nombres,
        udes.apellido_paterno AS des_apellido_paterno,
        udes.apellido_materno AS des_apellido_materno,
        udes.correo AS des_correo,
        des.numero_celular AS des_numero_celular,
        des.rol_flujo AS des_rol_flujo,
        jefe.id_usuario AS jefe_id_usuario,
        ujefe.nombres AS jefe_nombres,
        ujefe.apellido_paterno AS jefe_apellido_paterno,
        ujefe.apellido_materno AS jefe_apellido_materno,
        ujefe.correo AS jefe_correo,
        jefe.numero_celular AS jefe_numero_celular,
        jefe.rol_flujo AS jefe_rol_flujo,
        tes.id_usuario AS tes_id_usuario,
        utes.nombres AS tes_nombres,
        utes.apellido_paterno AS tes_apellido_paterno,
        utes.apellido_materno AS tes_apellido_materno,
        utes.correo AS tes_correo,
        tes.numero_celular AS tes_numero_celular,
        tes.rol_flujo AS tes_rol_flujo,
        cc.id_ciclo_caja AS cc_id_ciclo_caja,
        cc.numero_semana AS cc_numero_semana,
        cc.fecha_apertura AS cc_fecha_apertura,
        cc.fecha_cierre AS cc_fecha_cierre,
        cc.monto_saldo_inicial AS cc_monto_saldo_inicial,
        cc.monto_total_gastado AS cc_monto_total_gastado,
        cc.estado_ciclo AS cc_estado_ciclo,
        cc.id_caja_chica AS cc_id_caja_chica,
        cc.id_rendicion AS cc_id_rendicion
    FROM ope_solicitud_gasto sg
    LEFT JOIN tes_moneda mon ON sg.id_moneda_original = mon.id_moneda
    LEFT JOIN rrhh_empleado sol ON sg.id_usuario_solicitante = sol.id_usuario
    LEFT JOIN rrhh_usuario usol ON sol.id_usuario = usol.id_usuario
    LEFT JOIN rrhh_empleado des ON sg.id_usuario_destinatario = des.id_usuario
    LEFT JOIN rrhh_usuario udes ON des.id_usuario = udes.id_usuario
    LEFT JOIN rrhh_empleado jefe ON sg.id_jefe_aprobador = jefe.id_usuario
    LEFT JOIN rrhh_usuario ujefe ON jefe.id_usuario = ujefe.id_usuario
    LEFT JOIN rrhh_empleado tes ON sg.id_tesorero_aprobador = tes.id_usuario
    LEFT JOIN rrhh_usuario utes ON tes.id_usuario = utes.id_usuario
    LEFT JOIN ope_ciclo_caja cc ON sg.id_ciclo_caja = cc.id_ciclo_caja
    WHERE sg.id_solicitud_gasto = p_id_solicitud_gasto
    ORDER BY sg.estado_solicitud DESC;
END$$

DROP PROCEDURE IF EXISTS pa_listar_solicitudes_gasto $$
CREATE PROCEDURE pa_listar_solicitudes_gasto()
BEGIN
    SELECT
        sg.id_solicitud_gasto,
        sg.fecha_solicitud,
        sg.monto_solicitado,
        sg.id_moneda_original,
        sg.tipo_cambio,
        sg.monto_convertido,
        sg.motivo_solicitud,
        sg.estado_solicitud,
        sg.medio_desembolso,
        sg.id_usuario_solicitante,
        sg.id_usuario_destinatario,
        sg.id_jefe_aprobador,
        sg.id_tesorero_aprobador,
        sg.id_ciclo_caja,
        sg.comentario_decision,
        mon.id_moneda AS mon_id_moneda,
        mon.codigo_iso AS mon_codigo_iso,
        mon.simbolo AS mon_simbolo,
        mon.nombre_moneda AS mon_nombre,
        mon.descripcion AS mon_descripcion,
        mon.activa AS mon_activa,
        sol.id_usuario AS sol_id_usuario,
        usol.nombres AS sol_nombres,
        usol.apellido_paterno AS sol_apellido_paterno,
        usol.apellido_materno AS sol_apellido_materno,
        usol.correo AS sol_correo,
        sol.numero_celular AS sol_numero_celular,
        sol.rol_flujo AS sol_rol_flujo,
        des.id_usuario AS des_id_usuario,
        udes.nombres AS des_nombres,
        udes.apellido_paterno AS des_apellido_paterno,
        udes.apellido_materno AS des_apellido_materno,
        udes.correo AS des_correo,
        des.numero_celular AS des_numero_celular,
        des.rol_flujo AS des_rol_flujo,
        jefe.id_usuario AS jefe_id_usuario,
        ujefe.nombres AS jefe_nombres,
        ujefe.apellido_paterno AS jefe_apellido_paterno,
        ujefe.apellido_materno AS jefe_apellido_materno,
        ujefe.correo AS jefe_correo,
        jefe.numero_celular AS jefe_numero_celular,
        jefe.rol_flujo AS jefe_rol_flujo,
        tes.id_usuario AS tes_id_usuario,
        utes.nombres AS tes_nombres,
        utes.apellido_paterno AS tes_apellido_paterno,
        utes.apellido_materno AS tes_apellido_materno,
        utes.correo AS tes_correo,
        tes.numero_celular AS tes_numero_celular,
        tes.rol_flujo AS tes_rol_flujo,
        cc.id_ciclo_caja AS cc_id_ciclo_caja,
        cc.numero_semana AS cc_numero_semana,
        cc.fecha_apertura AS cc_fecha_apertura,
        cc.fecha_cierre AS cc_fecha_cierre,
        cc.monto_saldo_inicial AS cc_monto_saldo_inicial,
        cc.monto_total_gastado AS cc_monto_total_gastado,
        cc.estado_ciclo AS cc_estado_ciclo,
        cc.id_caja_chica AS cc_id_caja_chica,
        cc.id_rendicion AS cc_id_rendicion
    FROM ope_solicitud_gasto sg
    LEFT JOIN tes_moneda mon ON sg.id_moneda_original = mon.id_moneda
    LEFT JOIN rrhh_empleado sol ON sg.id_usuario_solicitante = sol.id_usuario
    LEFT JOIN rrhh_usuario usol ON sol.id_usuario = usol.id_usuario
    LEFT JOIN rrhh_empleado des ON sg.id_usuario_destinatario = des.id_usuario
    LEFT JOIN rrhh_usuario udes ON des.id_usuario = udes.id_usuario
    LEFT JOIN rrhh_empleado jefe ON sg.id_jefe_aprobador = jefe.id_usuario
    LEFT JOIN rrhh_usuario ujefe ON jefe.id_usuario = ujefe.id_usuario
    LEFT JOIN rrhh_empleado tes ON sg.id_tesorero_aprobador = tes.id_usuario
    LEFT JOIN rrhh_usuario utes ON tes.id_usuario = utes.id_usuario
    LEFT JOIN ope_ciclo_caja cc ON sg.id_ciclo_caja = cc.id_ciclo_caja
    ORDER BY sg.estado_solicitud DESC;
END$$

DROP PROCEDURE IF EXISTS pa_listar_todas_solicitudes_gasto $$
CREATE PROCEDURE pa_listar_todas_solicitudes_gasto()
BEGIN
    SELECT
        sg.id_solicitud_gasto,
        sg.fecha_solicitud,
        sg.monto_solicitado,
        sg.id_moneda_original,
        sg.tipo_cambio,
        sg.monto_convertido,
        sg.motivo_solicitud,
        sg.estado_solicitud,
        sg.medio_desembolso,
        sg.id_usuario_solicitante,
        sg.id_usuario_destinatario,
        sg.id_jefe_aprobador,
        sg.id_tesorero_aprobador,
        sg.id_ciclo_caja,
        sg.comentario_decision,
        mon.id_moneda AS mon_id_moneda,
        mon.codigo_iso AS mon_codigo_iso,
        mon.simbolo AS mon_simbolo,
        mon.nombre_moneda AS mon_nombre,
        mon.descripcion AS mon_descripcion,
        mon.activa AS mon_activa,
        sol.id_usuario AS sol_id_usuario,
        usol.nombres AS sol_nombres,
        usol.apellido_paterno AS sol_apellido_paterno,
        usol.apellido_materno AS sol_apellido_materno,
        usol.correo AS sol_correo,
        sol.numero_celular AS sol_numero_celular,
        sol.rol_flujo AS sol_rol_flujo,
        des.id_usuario AS des_id_usuario,
        udes.nombres AS des_nombres,
        udes.apellido_paterno AS des_apellido_paterno,
        udes.apellido_materno AS des_apellido_materno,
        udes.correo AS des_correo,
        des.numero_celular AS des_numero_celular,
        des.rol_flujo AS des_rol_flujo,
        jefe.id_usuario AS jefe_id_usuario,
        ujefe.nombres AS jefe_nombres,
        ujefe.apellido_paterno AS jefe_apellido_paterno,
        ujefe.apellido_materno AS jefe_apellido_materno,
        ujefe.correo AS jefe_correo,
        jefe.numero_celular AS jefe_numero_celular,
        jefe.rol_flujo AS jefe_rol_flujo,
        tes.id_usuario AS tes_id_usuario,
        utes.nombres AS tes_nombres,
        utes.apellido_paterno AS tes_apellido_paterno,
        utes.apellido_materno AS tes_apellido_materno,
        utes.correo AS tes_correo,
        tes.numero_celular AS tes_numero_celular,
        tes.rol_flujo AS tes_rol_flujo,
        cc.id_ciclo_caja AS cc_id_ciclo_caja,
        cc.numero_semana AS cc_numero_semana,
        cc.fecha_apertura AS cc_fecha_apertura,
        cc.fecha_cierre AS cc_fecha_cierre,
        cc.monto_saldo_inicial AS cc_monto_saldo_inicial,
        cc.monto_total_gastado AS cc_monto_total_gastado,
        cc.estado_ciclo AS cc_estado_ciclo,
        cc.id_caja_chica AS cc_id_caja_chica,
        cc.id_rendicion AS cc_id_rendicion
    FROM ope_solicitud_gasto sg
    LEFT JOIN tes_moneda mon ON sg.id_moneda_original = mon.id_moneda
    LEFT JOIN rrhh_empleado sol ON sg.id_usuario_solicitante = sol.id_usuario
    LEFT JOIN rrhh_usuario usol ON sol.id_usuario = usol.id_usuario
    LEFT JOIN rrhh_empleado des ON sg.id_usuario_destinatario = des.id_usuario
    LEFT JOIN rrhh_usuario udes ON des.id_usuario = udes.id_usuario
    LEFT JOIN rrhh_empleado jefe ON sg.id_jefe_aprobador = jefe.id_usuario
    LEFT JOIN rrhh_usuario ujefe ON jefe.id_usuario = ujefe.id_usuario
    LEFT JOIN rrhh_empleado tes ON sg.id_tesorero_aprobador = tes.id_usuario
    LEFT JOIN rrhh_usuario utes ON tes.id_usuario = utes.id_usuario
    LEFT JOIN ope_ciclo_caja cc ON sg.id_ciclo_caja = cc.id_ciclo_caja
    ORDER BY sg.estado_solicitud DESC;
END$$

DROP PROCEDURE IF EXISTS pa_listar_solicitudes_por_solicitante $$
CREATE PROCEDURE pa_listar_solicitudes_por_solicitante(
    IN p_id_usuario_solicitante INT
)
BEGIN
    IF p_id_usuario_solicitante IS NULL OR p_id_usuario_solicitante <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de usuario solicitante inválido';
    END IF;

    SELECT
        sg.id_solicitud_gasto,
        sg.fecha_solicitud,
        sg.monto_solicitado,
        sg.id_moneda_original,
        sg.tipo_cambio,
        sg.monto_convertido,
        sg.motivo_solicitud,
        sg.estado_solicitud,
        sg.medio_desembolso,
        sg.id_usuario_solicitante,
        sg.id_usuario_destinatario,
        sg.id_jefe_aprobador,
        sg.id_tesorero_aprobador,
        sg.id_ciclo_caja,
        sg.comentario_decision,
        mon.id_moneda AS mon_id_moneda,
        mon.codigo_iso AS mon_codigo_iso,
        mon.simbolo AS mon_simbolo,
        mon.nombre_moneda AS mon_nombre,
        mon.descripcion AS mon_descripcion,
        mon.activa AS mon_activa,
        sol.id_usuario AS sol_id_usuario,
        usol.nombres AS sol_nombres,
        usol.apellido_paterno AS sol_apellido_paterno,
        usol.apellido_materno AS sol_apellido_materno,
        usol.correo AS sol_correo,
        sol.numero_celular AS sol_numero_celular,
        sol.rol_flujo AS sol_rol_flujo,
        des.id_usuario AS des_id_usuario,
        udes.nombres AS des_nombres,
        udes.apellido_paterno AS des_apellido_paterno,
        udes.apellido_materno AS des_apellido_materno,
        udes.correo AS des_correo,
        des.numero_celular AS des_numero_celular,
        des.rol_flujo AS des_rol_flujo,
        jefe.id_usuario AS jefe_id_usuario,
        ujefe.nombres AS jefe_nombres,
        ujefe.apellido_paterno AS jefe_apellido_paterno,
        ujefe.apellido_materno AS jefe_apellido_materno,
        ujefe.correo AS jefe_correo,
        jefe.numero_celular AS jefe_numero_celular,
        jefe.rol_flujo AS jefe_rol_flujo,
        tes.id_usuario AS tes_id_usuario,
        utes.nombres AS tes_nombres,
        utes.apellido_paterno AS tes_apellido_paterno,
        utes.apellido_materno AS tes_apellido_materno,
        utes.correo AS tes_correo,
        tes.numero_celular AS tes_numero_celular,
        tes.rol_flujo AS tes_rol_flujo,
        cc.id_ciclo_caja AS cc_id_ciclo_caja,
        cc.numero_semana AS cc_numero_semana,
        cc.fecha_apertura AS cc_fecha_apertura,
        cc.fecha_cierre AS cc_fecha_cierre,
        cc.monto_saldo_inicial AS cc_monto_saldo_inicial,
        cc.monto_total_gastado AS cc_monto_total_gastado,
        cc.estado_ciclo AS cc_estado_ciclo,
        cc.id_caja_chica AS cc_id_caja_chica,
        cc.id_rendicion AS cc_id_rendicion
    FROM ope_solicitud_gasto sg
    LEFT JOIN tes_moneda mon ON sg.id_moneda_original = mon.id_moneda
    LEFT JOIN rrhh_empleado sol ON sg.id_usuario_solicitante = sol.id_usuario
    LEFT JOIN rrhh_usuario usol ON sol.id_usuario = usol.id_usuario
    LEFT JOIN rrhh_empleado des ON sg.id_usuario_destinatario = des.id_usuario
    LEFT JOIN rrhh_usuario udes ON des.id_usuario = udes.id_usuario
    LEFT JOIN rrhh_empleado jefe ON sg.id_jefe_aprobador = jefe.id_usuario
    LEFT JOIN rrhh_usuario ujefe ON jefe.id_usuario = ujefe.id_usuario
    LEFT JOIN rrhh_empleado tes ON sg.id_tesorero_aprobador = tes.id_usuario
    LEFT JOIN rrhh_usuario utes ON tes.id_usuario = utes.id_usuario
    LEFT JOIN ope_ciclo_caja cc ON sg.id_ciclo_caja = cc.id_ciclo_caja
    WHERE sg.id_usuario_solicitante = p_id_usuario_solicitante
      AND (sg.estado_solicitud IS NULL OR sg.estado_solicitud != 'ANULADO')
    ORDER BY sg.estado_solicitud DESC, sg.id_solicitud_gasto DESC;
END$$

DROP PROCEDURE IF EXISTS pa_listar_solicitudes_pendientes_jefe $$
CREATE PROCEDURE pa_listar_solicitudes_pendientes_jefe(
    IN p_id_usuario_destinatario INT
)
BEGIN
    IF p_id_usuario_destinatario IS NULL OR p_id_usuario_destinatario <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de usuario destinatario inválido';
    END IF;

    SELECT
        sg.id_solicitud_gasto,
        sg.fecha_solicitud,
        sg.monto_solicitado,
        sg.id_moneda_original,
        sg.tipo_cambio,
        sg.monto_convertido,
        sg.motivo_solicitud,
        sg.estado_solicitud,
        sg.medio_desembolso,
        sg.id_usuario_solicitante,
        sg.id_usuario_destinatario,
        sg.id_jefe_aprobador,
        sg.id_tesorero_aprobador,
        sg.id_ciclo_caja,
        sg.comentario_decision,
        mon.id_moneda AS mon_id_moneda,
        mon.codigo_iso AS mon_codigo_iso,
        mon.simbolo AS mon_simbolo,
        mon.nombre_moneda AS mon_nombre,
        mon.descripcion AS mon_descripcion,
        mon.activa AS mon_activa,
        sol.id_usuario AS sol_id_usuario,
        usol.nombres AS sol_nombres,
        usol.apellido_paterno AS sol_apellido_paterno,
        usol.apellido_materno AS sol_apellido_materno,
        usol.correo AS sol_correo,
        sol.numero_celular AS sol_numero_celular,
        sol.rol_flujo AS sol_rol_flujo,
        des.id_usuario AS des_id_usuario,
        udes.nombres AS des_nombres,
        udes.apellido_paterno AS des_apellido_paterno,
        udes.apellido_materno AS des_apellido_materno,
        udes.correo AS des_correo,
        des.numero_celular AS des_numero_celular,
        des.rol_flujo AS des_rol_flujo,
        jefe.id_usuario AS jefe_id_usuario,
        ujefe.nombres AS jefe_nombres,
        ujefe.apellido_paterno AS jefe_apellido_paterno,
        ujefe.apellido_materno AS jefe_apellido_materno,
        ujefe.correo AS jefe_correo,
        jefe.numero_celular AS jefe_numero_celular,
        jefe.rol_flujo AS jefe_rol_flujo,
        tes.id_usuario AS tes_id_usuario,
        utes.nombres AS tes_nombres,
        utes.apellido_paterno AS tes_apellido_paterno,
        utes.apellido_materno AS tes_apellido_materno,
        utes.correo AS tes_correo,
        tes.numero_celular AS tes_numero_celular,
        tes.rol_flujo AS tes_rol_flujo,
        cc.id_ciclo_caja AS cc_id_ciclo_caja,
        cc.numero_semana AS cc_numero_semana,
        cc.fecha_apertura AS cc_fecha_apertura,
        cc.fecha_cierre AS cc_fecha_cierre,
        cc.monto_saldo_inicial AS cc_monto_saldo_inicial,
        cc.monto_total_gastado AS cc_monto_total_gastado,
        cc.estado_ciclo AS cc_estado_ciclo,
        cc.id_caja_chica AS cc_id_caja_chica,
        cc.id_rendicion AS cc_id_rendicion
    FROM ope_solicitud_gasto sg
    LEFT JOIN tes_moneda mon ON sg.id_moneda_original = mon.id_moneda
    LEFT JOIN rrhh_empleado sol ON sg.id_usuario_solicitante = sol.id_usuario
    LEFT JOIN rrhh_usuario usol ON sol.id_usuario = usol.id_usuario
    LEFT JOIN rrhh_empleado des ON sg.id_usuario_destinatario = des.id_usuario
    LEFT JOIN rrhh_usuario udes ON des.id_usuario = udes.id_usuario
    LEFT JOIN rrhh_empleado jefe ON sg.id_jefe_aprobador = jefe.id_usuario
    LEFT JOIN rrhh_usuario ujefe ON jefe.id_usuario = ujefe.id_usuario
    LEFT JOIN rrhh_empleado tes ON sg.id_tesorero_aprobador = tes.id_usuario
    LEFT JOIN rrhh_usuario utes ON tes.id_usuario = utes.id_usuario
    LEFT JOIN ope_ciclo_caja cc ON sg.id_ciclo_caja = cc.id_ciclo_caja
    WHERE sg.id_usuario_destinatario = p_id_usuario_destinatario
      AND sg.estado_solicitud = 'PENDIENTE'
    ORDER BY sg.fecha_solicitud ASC;
END$$

DROP PROCEDURE IF EXISTS pa_listar_solicitudes_por_ciclo $$
CREATE PROCEDURE pa_listar_solicitudes_por_ciclo(
    IN p_id_ciclo_caja INT
)
BEGIN
    SELECT
        sg.id_solicitud_gasto,
        sg.fecha_solicitud,
        sg.monto_solicitado,
        sg.id_moneda_original,
        sg.tipo_cambio,
        sg.monto_convertido,
        sg.motivo_solicitud,
        sg.estado_solicitud,
        sg.medio_desembolso,
        sg.id_usuario_solicitante,
        sg.id_usuario_destinatario,
        sg.id_jefe_aprobador,
        sg.id_tesorero_aprobador,
        sg.id_ciclo_caja,
        sg.comentario_decision,
        mon.id_moneda AS mon_id_moneda,
        mon.codigo_iso AS mon_codigo_iso,
        mon.simbolo AS mon_simbolo,
        mon.nombre_moneda AS mon_nombre,
        mon.descripcion AS mon_descripcion,
        mon.activa AS mon_activa,
        sol.id_usuario AS sol_id_usuario,
        usol.nombres AS sol_nombres,
        usol.apellido_paterno AS sol_apellido_paterno,
        usol.apellido_materno AS sol_apellido_materno,
        usol.correo AS sol_correo,
        sol.numero_celular AS sol_numero_celular,
        sol.rol_flujo AS sol_rol_flujo,
        des.id_usuario AS des_id_usuario,
        udes.nombres AS des_nombres,
        udes.apellido_paterno AS des_apellido_paterno,
        udes.apellido_materno AS des_apellido_materno,
        udes.correo AS des_correo,
        des.numero_celular AS des_numero_celular,
        des.rol_flujo AS des_rol_flujo,
        jefe.id_usuario AS jefe_id_usuario,
        ujefe.nombres AS jefe_nombres,
        ujefe.apellido_paterno AS jefe_apellido_paterno,
        ujefe.apellido_materno AS jefe_apellido_materno,
        ujefe.correo AS jefe_correo,
        jefe.numero_celular AS jefe_numero_celular,
        jefe.rol_flujo AS jefe_rol_flujo,
        tes.id_usuario AS tes_id_usuario,
        utes.nombres AS tes_nombres,
        utes.apellido_paterno AS tes_apellido_paterno,
        utes.apellido_materno AS tes_apellido_materno,
        utes.correo AS tes_correo,
        tes.numero_celular AS tes_numero_celular,
        tes.rol_flujo AS tes_rol_flujo,
        cc.id_ciclo_caja AS cc_id_ciclo_caja,
        cc.numero_semana AS cc_numero_semana,
        cc.fecha_apertura AS cc_fecha_apertura,
        cc.fecha_cierre AS cc_fecha_cierre,
        cc.monto_saldo_inicial AS cc_monto_saldo_inicial,
        cc.monto_total_gastado AS cc_monto_total_gastado,
        cc.estado_ciclo AS cc_estado_ciclo,
        cc.id_caja_chica AS cc_id_caja_chica,
        cc.id_rendicion AS cc_id_rendicion
    FROM ope_solicitud_gasto sg
    LEFT JOIN tes_moneda mon ON sg.id_moneda_original = mon.id_moneda
    LEFT JOIN rrhh_empleado sol ON sg.id_usuario_solicitante = sol.id_usuario
    LEFT JOIN rrhh_usuario usol ON sol.id_usuario = usol.id_usuario
    LEFT JOIN rrhh_empleado des ON sg.id_usuario_destinatario = des.id_usuario
    LEFT JOIN rrhh_usuario udes ON des.id_usuario = udes.id_usuario
    LEFT JOIN rrhh_empleado jefe ON sg.id_jefe_aprobador = jefe.id_usuario
    LEFT JOIN rrhh_usuario ujefe ON jefe.id_usuario = ujefe.id_usuario
    LEFT JOIN rrhh_empleado tes ON sg.id_tesorero_aprobador = tes.id_usuario
    LEFT JOIN rrhh_usuario utes ON tes.id_usuario = utes.id_usuario
    LEFT JOIN ope_ciclo_caja cc ON sg.id_ciclo_caja = cc.id_ciclo_caja
    WHERE sg.id_ciclo_caja = p_id_ciclo_caja
      AND (sg.estado_solicitud IS NULL OR sg.estado_solicitud != 'ANULADO')
    ORDER BY sg.estado_solicitud DESC, sg.id_solicitud_gasto DESC;
END$$

DROP PROCEDURE IF EXISTS pa_listar_solicitudes_activas $$
CREATE PROCEDURE pa_listar_solicitudes_activas()
BEGIN
    SELECT
        sg.id_solicitud_gasto,
        sg.fecha_solicitud,
        sg.monto_solicitado,
        sg.id_moneda_original,
        sg.tipo_cambio,
        sg.monto_convertido,
        sg.motivo_solicitud,
        sg.estado_solicitud,
        sg.medio_desembolso,
        sg.id_usuario_solicitante,
        sg.id_usuario_destinatario,
        sg.id_jefe_aprobador,
        sg.id_tesorero_aprobador,
        sg.id_ciclo_caja,
        sg.comentario_decision,
        mon.id_moneda AS mon_id_moneda,
        mon.codigo_iso AS mon_codigo_iso,
        mon.simbolo AS mon_simbolo,
        mon.nombre_moneda AS mon_nombre,
        mon.descripcion AS mon_descripcion,
        mon.activa AS mon_activa,
        sol.id_usuario AS sol_id_usuario,
        usol.nombres AS sol_nombres,
        usol.apellido_paterno AS sol_apellido_paterno,
        usol.apellido_materno AS sol_apellido_materno,
        usol.correo AS sol_correo,
        sol.numero_celular AS sol_numero_celular,
        sol.rol_flujo AS sol_rol_flujo,
        des.id_usuario AS des_id_usuario,
        udes.nombres AS des_nombres,
        udes.apellido_paterno AS des_apellido_paterno,
        udes.apellido_materno AS des_apellido_materno,
        udes.correo AS des_correo,
        des.numero_celular AS des_numero_celular,
        des.rol_flujo AS des_rol_flujo,
        jefe.id_usuario AS jefe_id_usuario,
        ujefe.nombres AS jefe_nombres,
        ujefe.apellido_paterno AS jefe_apellido_paterno,
        ujefe.apellido_materno AS jefe_apellido_materno,
        ujefe.correo AS jefe_correo,
        jefe.numero_celular AS jefe_numero_celular,
        jefe.rol_flujo AS jefe_rol_flujo,
        tes.id_usuario AS tes_id_usuario,
        utes.nombres AS tes_nombres,
        utes.apellido_paterno AS tes_apellido_paterno,
        utes.apellido_materno AS tes_apellido_materno,
        utes.correo AS tes_correo,
        tes.numero_celular AS tes_numero_celular,
        tes.rol_flujo AS tes_rol_flujo,
        cc.id_ciclo_caja AS cc_id_ciclo_caja,
        cc.numero_semana AS cc_numero_semana,
        cc.fecha_apertura AS cc_fecha_apertura,
        cc.fecha_cierre AS cc_fecha_cierre,
        cc.monto_saldo_inicial AS cc_monto_saldo_inicial,
        cc.monto_total_gastado AS cc_monto_total_gastado,
        cc.estado_ciclo AS cc_estado_ciclo,
        cc.id_caja_chica AS cc_id_caja_chica,
        cc.id_rendicion AS cc_id_rendicion
    FROM ope_solicitud_gasto sg
    LEFT JOIN tes_moneda mon ON sg.id_moneda_original = mon.id_moneda
    LEFT JOIN rrhh_empleado sol ON sg.id_usuario_solicitante = sol.id_usuario
    LEFT JOIN rrhh_usuario usol ON sol.id_usuario = usol.id_usuario
    LEFT JOIN rrhh_empleado des ON sg.id_usuario_destinatario = des.id_usuario
    LEFT JOIN rrhh_usuario udes ON des.id_usuario = udes.id_usuario
    LEFT JOIN rrhh_empleado jefe ON sg.id_jefe_aprobador = jefe.id_usuario
    LEFT JOIN rrhh_usuario ujefe ON jefe.id_usuario = ujefe.id_usuario
    LEFT JOIN rrhh_empleado tes ON sg.id_tesorero_aprobador = tes.id_usuario
    LEFT JOIN rrhh_usuario utes ON tes.id_usuario = utes.id_usuario
    LEFT JOIN ope_ciclo_caja cc ON sg.id_ciclo_caja = cc.id_ciclo_caja
    WHERE sg.estado_solicitud != 'ANULADO'
    ORDER BY sg.estado_solicitud DESC, sg.id_solicitud_gasto DESC;
END$$

DELIMITER ;

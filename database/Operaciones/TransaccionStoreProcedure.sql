
DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_transaccion $$
CREATE PROCEDURE pa_insertar_transaccion(
    IN p_id_usuario_accion INT,
    IN p_tipo_operacion ENUM('DESEMBOLSO','DEVOLUCION_SOBRANTE','REEMBOLSO_DEFICIT','REPOSICION_FONDO'),
    IN p_momento_operacion DATETIME,
    IN p_monto_transaccion DECIMAL(12,2),
    IN p_numero_operacion_bancaria VARCHAR(30),
    IN p_medio_pago ENUM('YAPE','PLIN','TRANSFERENCIA','EFECTIVO'),
    IN p_id_tipo_cambio INT,
    IN p_id_cuenta_origen INT,
    IN p_id_cuenta_destino INT,
    IN p_id_moneda INT,
    IN p_id_beneficiario INT,
    IN p_estado_transaccion ENUM('REGISTRADA','COMPLETADA','ANULADA'),
    OUT p_id_generado INT
)
BEGIN
    INSERT INTO ope_transaccion(
        tipo_operacion,
        momento_operacion,
        monto_transaccion,
        numero_operacion_bancaria,
        medio_pago,
        id_tipo_cambio,
        id_cuenta_origen,
        id_cuenta_destino,
        id_moneda,
        id_beneficiario,
        estado_transaccion,
        id_usuario_creacion,
        id_usuario_modificacion
    ) VALUES(
        p_tipo_operacion,
        p_momento_operacion,
        p_monto_transaccion,
        p_numero_operacion_bancaria,
        p_medio_pago,
        p_id_tipo_cambio,
        p_id_cuenta_origen,
        p_id_cuenta_destino,
        p_id_moneda,
        p_id_beneficiario,
        p_estado_transaccion,
        p_id_usuario_accion,
        p_id_usuario_accion
    );
    SET p_id_generado = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS pa_modificar_transaccion $$
CREATE PROCEDURE pa_modificar_transaccion(
    IN p_id_usuario_accion INT,
    IN p_id_transaccion INT,
    IN p_tipo_operacion ENUM('DESEMBOLSO','DEVOLUCION_SOBRANTE','REEMBOLSO_DEFICIT','REPOSICION_FONDO'),
    IN p_momento_operacion DATETIME,
    IN p_monto_transaccion DECIMAL(12,2),
    IN p_numero_operacion_bancaria VARCHAR(30),
    IN p_medio_pago ENUM('YAPE','PLIN','TRANSFERENCIA','EFECTIVO'),
    IN p_id_tipo_cambio INT,
    IN p_id_cuenta_origen INT,
    IN p_id_cuenta_destino INT,
    IN p_id_moneda INT,
    IN p_id_beneficiario INT,
    IN p_estado_transaccion ENUM('REGISTRADA','COMPLETADA','ANULADA')
)
BEGIN
    IF p_id_transaccion IS NULL OR p_id_transaccion <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de transacción inválido';
    END IF;

    UPDATE ope_transaccion
    SET
        tipo_operacion = p_tipo_operacion,
        momento_operacion = p_momento_operacion,
        monto_transaccion = p_monto_transaccion,
        numero_operacion_bancaria = p_numero_operacion_bancaria,
        medio_pago = p_medio_pago,
        id_tipo_cambio = p_id_tipo_cambio,
        id_cuenta_origen = p_id_cuenta_origen,
        id_cuenta_destino = p_id_cuenta_destino,
        id_moneda = p_id_moneda,
        id_beneficiario = p_id_beneficiario,
        estado_transaccion = p_estado_transaccion,
        id_usuario_modificacion = p_id_usuario_accion
    WHERE id_transaccion = p_id_transaccion;
END$$

DROP PROCEDURE IF EXISTS pa_eliminar_transaccion $$
CREATE PROCEDURE pa_eliminar_transaccion(
    IN p_id_usuario_accion INT,
    IN p_id_transaccion INT
)
BEGIN
    IF p_id_transaccion IS NULL OR p_id_transaccion <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de transacción inválido';
    END IF;
    UPDATE ope_transaccion
    SET
        estado_transaccion = 'ANULADA',
        id_usuario_modificacion = p_id_usuario_accion
    WHERE id_transaccion = p_id_transaccion;
END$$

DROP PROCEDURE IF EXISTS pa_buscar_transaccion_por_id $$
CREATE PROCEDURE pa_buscar_transaccion_por_id(
    IN p_id_transaccion INT
)
BEGIN
    SELECT
        t.id_transaccion,
        t.tipo_operacion,
        t.momento_operacion,
        t.monto_transaccion,
        t.numero_operacion_bancaria,
        t.medio_pago,
        t.id_tipo_cambio,
        tc.id_moneda_origen,
        tc.id_moneda_destino,
        tc.valor_tipo_cambio,
        tc.fecha_tipo_cambio,
        mo.id_moneda AS tc_mo_id_moneda,
        mo.codigo_iso AS tc_mo_codigo_iso,
        mo.simbolo AS tc_mo_simbolo,
        mo.nombre_moneda AS tc_mo_nombre,
        mo.descripcion AS tc_mo_descripcion,
        mo.activa AS tc_mo_activa,
        md.id_moneda AS tc_md_id_moneda,
        md.codigo_iso AS tc_md_codigo_iso,
        md.simbolo AS tc_md_simbolo,
        md.nombre_moneda AS tc_md_nombre,
        md.descripcion AS tc_md_descripcion,
        md.activa AS tc_md_activa,
        t.estado_transaccion,
        t.id_cuenta_origen,
        t.id_cuenta_destino,
        t.id_moneda,
        t.id_beneficiario,
        co.id_cuenta_bancaria AS co_id_cuenta,
        co.numero_cuenta AS co_numero_cuenta,
        co.nombre_banco AS co_nombre_banco,
        co.cci AS co_cci,
        co.activa AS co_activa,
        co.id_moneda AS co_id_moneda,
        co.id_area AS co_id_area,
        co.id_usuario AS co_id_usuario,
        cd.id_cuenta_bancaria AS cd_id_cuenta,
        cd.numero_cuenta AS cd_numero_cuenta,
        cd.nombre_banco AS cd_nombre_banco,
        cd.cci AS cd_cci,
        cd.activa AS cd_activa,
        cd.id_moneda AS cd_id_moneda,
        cd.id_area AS cd_id_area,
        cd.id_usuario AS cd_id_usuario,
        m.id_moneda AS m_id_moneda,
        m.codigo_iso AS m_codigo_iso,
        m.simbolo AS m_simbolo,
        m.nombre_moneda AS m_nombre,
        m.descripcion AS m_descripcion,
        m.activa AS m_activa,
        ben.id_usuario AS ben_id_usuario,
        uben.nombres AS ben_nombres,
        uben.apellido_paterno AS ben_apellido_paterno,
        uben.apellido_materno AS ben_apellido_materno,
        uben.correo AS ben_correo,
        ben.numero_celular AS ben_numero_celular,
        ben.rol_flujo AS ben_rol_flujo
    FROM ope_transaccion t
    LEFT JOIN tes_tipo_cambio tc ON t.id_tipo_cambio = tc.id_tipo_cambio
    LEFT JOIN tes_moneda mo ON tc.id_moneda_origen = mo.id_moneda
    LEFT JOIN tes_moneda md ON tc.id_moneda_destino = md.id_moneda
    LEFT JOIN tes_cuenta_bancaria co ON t.id_cuenta_origen = co.id_cuenta_bancaria
    LEFT JOIN tes_cuenta_bancaria cd ON t.id_cuenta_destino = cd.id_cuenta_bancaria
    LEFT JOIN tes_moneda m ON t.id_moneda = m.id_moneda
    LEFT JOIN rrhh_empleado ben ON t.id_beneficiario = ben.id_usuario
    LEFT JOIN rrhh_usuario uben ON ben.id_usuario = uben.id_usuario
    WHERE t.id_transaccion = p_id_transaccion
    ORDER BY t.estado_transaccion DESC;
END$$

DROP PROCEDURE IF EXISTS pa_listar_transacciones $$
CREATE PROCEDURE pa_listar_transacciones()
BEGIN
    SELECT
        t.id_transaccion,
        t.tipo_operacion,
        t.momento_operacion,
        t.monto_transaccion,
        t.numero_operacion_bancaria,
        t.medio_pago,
        t.id_tipo_cambio,
        tc.id_moneda_origen,
        tc.id_moneda_destino,
        tc.valor_tipo_cambio,
        tc.fecha_tipo_cambio,
        mo.id_moneda AS tc_mo_id_moneda,
        mo.codigo_iso AS tc_mo_codigo_iso,
        mo.simbolo AS tc_mo_simbolo,
        mo.nombre_moneda AS tc_mo_nombre,
        mo.descripcion AS tc_mo_descripcion,
        mo.activa AS tc_mo_activa,
        md.id_moneda AS tc_md_id_moneda,
        md.codigo_iso AS tc_md_codigo_iso,
        md.simbolo AS tc_md_simbolo,
        md.nombre_moneda AS tc_md_nombre,
        md.descripcion AS tc_md_descripcion,
        md.activa AS tc_md_activa,
        t.estado_transaccion,
        t.id_cuenta_origen,
        t.id_cuenta_destino,
        t.id_moneda,
        t.id_beneficiario,
        co.id_cuenta_bancaria AS co_id_cuenta,
        co.numero_cuenta AS co_numero_cuenta,
        co.nombre_banco AS co_nombre_banco,
        co.cci AS co_cci,
        co.activa AS co_activa,
        co.id_moneda AS co_id_moneda,
        co.id_area AS co_id_area,
        co.id_usuario AS co_id_usuario,
        cd.id_cuenta_bancaria AS cd_id_cuenta,
        cd.numero_cuenta AS cd_numero_cuenta,
        cd.nombre_banco AS cd_nombre_banco,
        cd.cci AS cd_cci,
        cd.activa AS cd_activa,
        cd.id_moneda AS cd_id_moneda,
        cd.id_area AS cd_id_area,
        cd.id_usuario AS cd_id_usuario,
        m.id_moneda AS m_id_moneda,
        m.codigo_iso AS m_codigo_iso,
        m.simbolo AS m_simbolo,
        m.nombre_moneda AS m_nombre,
        m.descripcion AS m_descripcion,
        m.activa AS m_activa,
        ben.id_usuario AS ben_id_usuario,
        uben.nombres AS ben_nombres,
        uben.apellido_paterno AS ben_apellido_paterno,
        uben.apellido_materno AS ben_apellido_materno,
        uben.correo AS ben_correo,
        ben.numero_celular AS ben_numero_celular,
        ben.rol_flujo AS ben_rol_flujo
    FROM ope_transaccion t
    LEFT JOIN tes_tipo_cambio tc ON t.id_tipo_cambio = tc.id_tipo_cambio
    LEFT JOIN tes_moneda mo ON tc.id_moneda_origen = mo.id_moneda
    LEFT JOIN tes_moneda md ON tc.id_moneda_destino = md.id_moneda
    LEFT JOIN tes_cuenta_bancaria co ON t.id_cuenta_origen = co.id_cuenta_bancaria
    LEFT JOIN tes_cuenta_bancaria cd ON t.id_cuenta_destino = cd.id_cuenta_bancaria
    LEFT JOIN tes_moneda m ON t.id_moneda = m.id_moneda
    LEFT JOIN rrhh_empleado ben ON t.id_beneficiario = ben.id_usuario
    LEFT JOIN rrhh_usuario uben ON ben.id_usuario = uben.id_usuario
    ORDER BY t.estado_transaccion DESC;
END$$

DROP PROCEDURE IF EXISTS pa_listar_todas_transacciones $$
CREATE PROCEDURE pa_listar_todas_transacciones()
BEGIN
    SELECT
        t.id_transaccion,
        t.tipo_operacion,
        t.momento_operacion,
        t.monto_transaccion,
        t.numero_operacion_bancaria,
        t.medio_pago,
        t.id_tipo_cambio,
        tc.id_moneda_origen,
        tc.id_moneda_destino,
        tc.valor_tipo_cambio,
        tc.fecha_tipo_cambio,
        mo.id_moneda AS tc_mo_id_moneda,
        mo.codigo_iso AS tc_mo_codigo_iso,
        mo.simbolo AS tc_mo_simbolo,
        mo.nombre_moneda AS tc_mo_nombre,
        mo.descripcion AS tc_mo_descripcion,
        mo.activa AS tc_mo_activa,
        md.id_moneda AS tc_md_id_moneda,
        md.codigo_iso AS tc_md_codigo_iso,
        md.simbolo AS tc_md_simbolo,
        md.nombre_moneda AS tc_md_nombre,
        md.descripcion AS tc_md_descripcion,
        md.activa AS tc_md_activa,
        t.estado_transaccion,
        t.id_cuenta_origen,
        t.id_cuenta_destino,
        t.id_moneda,
        t.id_beneficiario,
        co.id_cuenta_bancaria AS co_id_cuenta,
        co.numero_cuenta AS co_numero_cuenta,
        co.nombre_banco AS co_nombre_banco,
        co.cci AS co_cci,
        co.activa AS co_activa,
        co.id_moneda AS co_id_moneda,
        co.id_area AS co_id_area,
        co.id_usuario AS co_id_usuario,
        cd.id_cuenta_bancaria AS cd_id_cuenta,
        cd.numero_cuenta AS cd_numero_cuenta,
        cd.nombre_banco AS cd_nombre_banco,
        cd.cci AS cd_cci,
        cd.activa AS cd_activa,
        cd.id_moneda AS cd_id_moneda,
        cd.id_area AS cd_id_area,
        cd.id_usuario AS cd_id_usuario,
        m.id_moneda AS m_id_moneda,
        m.codigo_iso AS m_codigo_iso,
        m.simbolo AS m_simbolo,
        m.nombre_moneda AS m_nombre,
        m.descripcion AS m_descripcion,
        m.activa AS m_activa,
        ben.id_usuario AS ben_id_usuario,
        uben.nombres AS ben_nombres,
        uben.apellido_paterno AS ben_apellido_paterno,
        uben.apellido_materno AS ben_apellido_materno,
        uben.correo AS ben_correo,
        ben.numero_celular AS ben_numero_celular,
        ben.rol_flujo AS ben_rol_flujo
    FROM ope_transaccion t
    LEFT JOIN tes_tipo_cambio tc ON t.id_tipo_cambio = tc.id_tipo_cambio
    LEFT JOIN tes_moneda mo ON tc.id_moneda_origen = mo.id_moneda
    LEFT JOIN tes_moneda md ON tc.id_moneda_destino = md.id_moneda
    LEFT JOIN tes_cuenta_bancaria co ON t.id_cuenta_origen = co.id_cuenta_bancaria
    LEFT JOIN tes_cuenta_bancaria cd ON t.id_cuenta_destino = cd.id_cuenta_bancaria
    LEFT JOIN tes_moneda m ON t.id_moneda = m.id_moneda
    LEFT JOIN rrhh_empleado ben ON t.id_beneficiario = ben.id_usuario
    LEFT JOIN rrhh_usuario uben ON ben.id_usuario = uben.id_usuario
    ORDER BY t.estado_transaccion DESC, t.id_transaccion DESC;
END$$

DROP PROCEDURE IF EXISTS pa_listar_transacciones_activas $$
CREATE PROCEDURE pa_listar_transacciones_activas()
BEGIN
    SELECT
        t.id_transaccion,
        t.tipo_operacion,
        t.momento_operacion,
        t.monto_transaccion,
        t.numero_operacion_bancaria,
        t.medio_pago,
        t.id_tipo_cambio,
        tc.id_moneda_origen,
        tc.id_moneda_destino,
        tc.valor_tipo_cambio,
        tc.fecha_tipo_cambio,
        mo.id_moneda AS tc_mo_id_moneda,
        mo.codigo_iso AS tc_mo_codigo_iso,
        mo.simbolo AS tc_mo_simbolo,
        mo.nombre_moneda AS tc_mo_nombre,
        mo.descripcion AS tc_mo_descripcion,
        mo.activa AS tc_mo_activa,
        md.id_moneda AS tc_md_id_moneda,
        md.codigo_iso AS tc_md_codigo_iso,
        md.simbolo AS tc_md_simbolo,
        md.nombre_moneda AS tc_md_nombre,
        md.descripcion AS tc_md_descripcion,
        md.activa AS tc_md_activa,
        t.estado_transaccion,
        t.id_cuenta_origen,
        t.id_cuenta_destino,
        t.id_moneda,
        t.id_beneficiario,
        co.id_cuenta_bancaria AS co_id_cuenta,
        co.numero_cuenta AS co_numero_cuenta,
        co.nombre_banco AS co_nombre_banco,
        co.cci AS co_cci,
        co.activa AS co_activa,
        co.id_moneda AS co_id_moneda,
        co.id_area AS co_id_area,
        co.id_usuario AS co_id_usuario,
        cd.id_cuenta_bancaria AS cd_id_cuenta,
        cd.numero_cuenta AS cd_numero_cuenta,
        cd.nombre_banco AS cd_nombre_banco,
        cd.cci AS cd_cci,
        cd.activa AS cd_activa,
        cd.id_moneda AS cd_id_moneda,
        cd.id_area AS cd_id_area,
        cd.id_usuario AS cd_id_usuario,
        m.id_moneda AS m_id_moneda,
        m.codigo_iso AS m_codigo_iso,
        m.simbolo AS m_simbolo,
        m.nombre_moneda AS m_nombre,
        m.descripcion AS m_descripcion,
        m.activa AS m_activa,
        ben.id_usuario AS ben_id_usuario,
        uben.nombres AS ben_nombres,
        uben.apellido_paterno AS ben_apellido_paterno,
        uben.apellido_materno AS ben_apellido_materno,
        uben.correo AS ben_correo,
        ben.numero_celular AS ben_numero_celular,
        ben.rol_flujo AS ben_rol_flujo
    FROM ope_transaccion t
    LEFT JOIN tes_tipo_cambio tc ON t.id_tipo_cambio = tc.id_tipo_cambio
    LEFT JOIN tes_moneda mo ON tc.id_moneda_origen = mo.id_moneda
    LEFT JOIN tes_moneda md ON tc.id_moneda_destino = md.id_moneda
    LEFT JOIN tes_cuenta_bancaria co ON t.id_cuenta_origen = co.id_cuenta_bancaria
    LEFT JOIN tes_cuenta_bancaria cd ON t.id_cuenta_destino = cd.id_cuenta_bancaria
    LEFT JOIN tes_moneda m ON t.id_moneda = m.id_moneda
    LEFT JOIN rrhh_empleado ben ON t.id_beneficiario = ben.id_usuario
    LEFT JOIN rrhh_usuario uben ON ben.id_usuario = uben.id_usuario
    WHERE t.estado_transaccion != 'ANULADA'
    ORDER BY t.estado_transaccion DESC, t.id_transaccion DESC;
END$$

DELIMITER ;

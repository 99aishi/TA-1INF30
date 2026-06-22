
DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_ciclo_caja $$
CREATE PROCEDURE pa_insertar_ciclo_caja(
    IN p_id_usuario_accion INT,
	IN p_numero_semana INT,
	IN p_fecha_apertura DATE,
	IN p_fecha_cierre DATE,
	IN p_monto_saldo_inicial DECIMAL(12,2),
	IN p_monto_total_gastado DECIMAL(12,2),
	IN p_estado_ciclo ENUM('ABIERTO','CERRADO','LIQUIDADO'),
	IN p_id_caja_chica INT,
	IN p_id_rendicion INT,
	OUT p_id_generado INT
)
BEGIN
    IF p_id_caja_chica IS NULL OR p_id_caja_chica <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de ciclo de caja chica no válido';
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
        id_usuario_modificacion    )
    VALUES(

        p_numero_semana,
        p_fecha_apertura,
        p_fecha_cierre,
        p_monto_saldo_inicial,
        p_monto_total_gastado,
        p_estado_ciclo,
        p_id_caja_chica,
        p_id_rendicion,
        p_id_usuario_accion,
        p_id_usuario_accion    );
    
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
    IN p_estado_ciclo ENUM('ABIERTO','CERRADO','LIQUIDADO'),
    IN p_id_caja_chica INT,
    IN p_id_rendicion INT

)
BEGIN
    IF p_id_ciclo_caja IS NULL OR p_id_ciclo_caja <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de ciclo de caja no válido';
    END IF;

    UPDATE ope_ciclo_caja
       SET numero_semana = p_numero_semana,
           fecha_apertura = p_fecha_apertura,
           fecha_cierre = p_fecha_cierre,
           monto_saldo_inicial = p_monto_saldo_inicial,
           monto_total_gastado = p_monto_total_gastado,
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
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de ciclo de caja no válido';
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
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de ciclo de caja no válido';
    END IF;

    SELECT 
        occ.id_ciclo_caja, 
        occ.numero_semana, 
        occ.fecha_apertura, 
        occ.fecha_cierre, 
        occ.monto_saldo_inicial, 
        occ.monto_total_gastado, 
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
        ren.monto_total_declarado AS ren_monto_total_declarado,
        ren.monto_total_aprobado AS ren_monto_total_aprobado,
        ren.monto_saldo_final AS ren_monto_saldo_final,
        ren.estado_rendicion AS ren_estado_rendicion,
        ren.comentario AS ren_comentario
    FROM ope_ciclo_caja occ
    LEFT JOIN tes_caja_chica ccj ON occ.id_caja_chica = ccj.id_fondo
    LEFT JOIN tes_fondo f ON ccj.id_fondo = f.id_fondo
    LEFT JOIN ope_rendicion ren ON occ.id_rendicion = ren.id_rendicion
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
        occ.monto_total_gastado, 
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
        ren.monto_total_declarado AS ren_monto_total_declarado,
        ren.monto_total_aprobado AS ren_monto_total_aprobado,
        ren.monto_saldo_final AS ren_monto_saldo_final,
        ren.estado_rendicion AS ren_estado_rendicion,
        ren.comentario AS ren_comentario
    FROM ope_ciclo_caja occ
    LEFT JOIN tes_caja_chica ccj ON occ.id_caja_chica = ccj.id_fondo
    LEFT JOIN tes_fondo f ON ccj.id_fondo = f.id_fondo
    LEFT JOIN ope_rendicion ren ON occ.id_rendicion = ren.id_rendicion
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
        occ.monto_total_gastado, 
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
        ren.monto_total_declarado AS ren_monto_total_declarado,
        ren.monto_total_aprobado AS ren_monto_total_aprobado,
        ren.monto_saldo_final AS ren_monto_saldo_final,
        ren.estado_rendicion AS ren_estado_rendicion,
        ren.comentario AS ren_comentario
    FROM ope_ciclo_caja occ
    LEFT JOIN tes_caja_chica ccj ON occ.id_caja_chica = ccj.id_fondo
    LEFT JOIN tes_fondo f ON ccj.id_fondo = f.id_fondo
    LEFT JOIN ope_rendicion ren ON occ.id_rendicion = ren.id_rendicion
    ORDER BY FIELD(occ.estado_ciclo, 'ABIERTO', 'CERRADO', 'LIQUIDADO'), occ.id_ciclo_caja DESC;
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
        occ.monto_total_gastado,
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
        ren.monto_total_declarado AS ren_monto_total_declarado,
        ren.monto_total_aprobado AS ren_monto_total_aprobado,
        ren.monto_saldo_final AS ren_monto_saldo_final,
        ren.estado_rendicion AS ren_estado_rendicion,
        ren.comentario AS ren_comentario
    FROM ope_ciclo_caja occ
    LEFT JOIN tes_caja_chica ccj ON occ.id_caja_chica = ccj.id_fondo
    LEFT JOIN tes_fondo f ON ccj.id_fondo = f.id_fondo
    LEFT JOIN ope_rendicion ren ON occ.id_rendicion = ren.id_rendicion
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
        occ.monto_total_gastado,
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
        ren.monto_total_declarado AS ren_monto_total_declarado,
        ren.monto_total_aprobado AS ren_monto_total_aprobado,
        ren.monto_saldo_final AS ren_monto_saldo_final,
        ren.estado_rendicion AS ren_estado_rendicion,
        ren.comentario AS ren_comentario
    FROM ope_ciclo_caja occ
    LEFT JOIN tes_caja_chica ccj ON occ.id_caja_chica = ccj.id_fondo
    LEFT JOIN tes_fondo f ON ccj.id_fondo = f.id_fondo
    LEFT JOIN ope_rendicion ren ON occ.id_rendicion = ren.id_rendicion
    WHERE occ.estado_ciclo IN ('CERRADO', 'LIQUIDADO')
    ORDER BY occ.fecha_cierre DESC, occ.estado_ciclo, occ.id_ciclo_caja DESC;
END$$

DELIMITER ;
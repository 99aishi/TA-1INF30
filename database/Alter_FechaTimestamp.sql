-- ============================================================================
-- MIGRACIÓN: Cambiar columnas DATE a DATETIME para preservar hora exacta
-- Ejecutar este script una sola vez. Luego eliminarlo.
-- ============================================================================

-- 1. ope_solicitud_gasto.fecha_solicitud: DATE → DATETIME
ALTER TABLE ope_solicitud_gasto
  MODIFY COLUMN fecha_solicitud DATETIME NOT NULL;

-- 2. ope_comprobante_pago.fecha_emision: DATE → DATETIME
ALTER TABLE ope_comprobante_pago
  MODIFY COLUMN fecha_emision DATETIME NULL;

-- 3. ope_ciclo_caja.fecha_apertura / fecha_cierre: DATE → DATETIME
ALTER TABLE ope_ciclo_caja
  MODIFY COLUMN fecha_apertura DATETIME NULL,
  MODIFY COLUMN fecha_cierre DATETIME NULL;

-- 4. ope_rendicion.fecha_presentacion / fecha_aprobacion: DATE → DATETIME
ALTER TABLE ope_rendicion
  MODIFY COLUMN fecha_presentacion DATETIME DEFAULT CURRENT_TIMESTAMP,
  MODIFY COLUMN fecha_aprobacion DATETIME NULL;

-- 5. tes_tipo_cambio.fecha_tipo_cambio: DATE → DATETIME
ALTER TABLE tes_tipo_cambio
  MODIFY COLUMN fecha_tipo_cambio DATETIME NOT NULL;

-- ============================================================================
-- STORED PROCEDURES: Cambiar parámetros DATE → DATETIME
-- ============================================================================

DELIMITER $$

-- ope_solicitud_gasto
DROP PROCEDURE IF EXISTS pa_insertar_solicitud_gasto $$
CREATE PROCEDURE pa_insertar_solicitud_gasto(
    IN p_id_usuario_accion INT,
    IN p_fecha_solicitud DATETIME,
    IN p_monto_solicitado DECIMAL(12,2),
    IN p_id_moneda_original INT,
    IN p_tipo_cambio DECIMAL(10,4),
    IN p_monto_convertido DECIMAL(12,2),
    IN p_motivo_solicitud VARCHAR(200),
    IN p_estado_solicitud ENUM('PENDIENTE','APROBADO','PAGADO','RENDIDO','RECHAZADO','OBSERVADO','ANULADO'),
    IN p_comentario_decision VARCHAR(500),
    IN p_id_usuario_solicitante INT,
    IN p_id_usuario_destinatario INT,
    IN p_id_jefe_aprobador INT,
    IN p_id_tesorero_aprobador INT,
    IN p_id_ciclo_caja INT,
    IN p_id_transaccion INT,
    OUT p_id_generado INT
)
BEGIN
    INSERT INTO ope_solicitud_gasto(
        fecha_solicitud, monto_solicitado, id_moneda_original, tipo_cambio,
        monto_convertido, motivo_solicitud, estado_solicitud, comentario_decision,
        id_usuario_solicitante, id_usuario_destinatario, id_jefe_aprobador,
        id_tesorero_aprobador, id_ciclo_caja, id_transaccion,
        id_usuario_creacion, id_usuario_modificacion
    ) VALUES(
        p_fecha_solicitud, p_monto_solicitado, p_id_moneda_original, p_tipo_cambio,
        p_monto_convertido, p_motivo_solicitud, p_estado_solicitud, p_comentario_decision,
        p_id_usuario_solicitante, p_id_usuario_destinatario, p_id_jefe_aprobador,
        p_id_tesorero_aprobador, p_id_ciclo_caja, p_id_transaccion,
        p_id_usuario_accion, p_id_usuario_accion
    );
    SET p_id_generado = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS pa_modificar_solicitud_gasto $$
CREATE PROCEDURE pa_modificar_solicitud_gasto(
    IN p_id_usuario_accion INT,
    IN p_id_solicitud_gasto INT,
    IN p_fecha_solicitud DATETIME,
    IN p_monto_solicitado DECIMAL(12,2),
    IN p_id_moneda_original INT,
    IN p_tipo_cambio DECIMAL(10,4),
    IN p_monto_convertido DECIMAL(12,2),
    IN p_motivo_solicitud VARCHAR(200),
    IN p_estado_solicitud ENUM('PENDIENTE','APROBADO','PAGADO','RENDIDO','RECHAZADO','ANULADO'),
    IN p_comentario_decision VARCHAR(500),
    IN p_id_usuario_solicitante INT,
    IN p_id_usuario_destinatario INT,
    IN p_id_jefe_aprobador INT,
    IN p_id_tesorero_aprobador INT,
    IN p_id_ciclo_caja INT,
    IN p_id_transaccion INT
)
BEGIN
    IF p_id_solicitud_gasto IS NULL OR p_id_solicitud_gasto <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de solicitud de gasto invalido';
    END IF;

    UPDATE ope_solicitud_gasto
    SET fecha_solicitud = p_fecha_solicitud,
        monto_solicitado = p_monto_solicitado,
        id_moneda_original = p_id_moneda_original,
        tipo_cambio = p_tipo_cambio,
        monto_convertido = p_monto_convertido,
        motivo_solicitud = p_motivo_solicitud,
        estado_solicitud = p_estado_solicitud,
        comentario_decision = p_comentario_decision,
        id_usuario_solicitante = p_id_usuario_solicitante,
        id_usuario_destinatario = p_id_usuario_destinatario,
        id_jefe_aprobador = p_id_jefe_aprobador,
        id_tesorero_aprobador = p_id_tesorero_aprobador,
        id_ciclo_caja = p_id_ciclo_caja,
        id_transaccion = p_id_transaccion,
        actualizado_at = NOW(),
        id_usuario_modificacion = p_id_usuario_accion
    WHERE id_solicitud_gasto = p_id_solicitud_gasto;
END$$

-- ope_comprobante_pago
DROP PROCEDURE IF EXISTS pa_insertar_comprobante_pago $$
CREATE PROCEDURE pa_insertar_comprobante_pago(
    IN p_id_usuario_accion INT,
    IN p_tipo_documento ENUM('FACTURA','BOLETA','DJ_EXCEPCIONAL'),
    IN p_ruc_proveedor CHAR(11),
    IN p_razon_social VARCHAR(150),
    IN p_numero_serie VARCHAR(30),
    IN p_fecha_emision DATETIME,
    IN p_monto_subtotal DECIMAL(12,2),
    IN p_monto_igv DECIMAL(12,2),
    IN p_monto_total DECIMAL(12,2),
    IN p_tipo_cambio DECIMAL(10,4),
    IN p_monto_convertido DECIMAL(12,2),
    IN p_nombre_archivo VARCHAR(500),
    IN p_estado_comprobante ENUM('POR_REVISAR','ANULADO','APROBADO','OBSERVADO'),
    IN p_id_solicitud_gasto INT,
    IN p_id_moneda INT,
    OUT p_id_generado INT
)
BEGIN
    INSERT INTO ope_comprobante_pago(
        tipo_documento, ruc_proveedor, razon_social, numero_serie, fecha_emision,
        monto_subtotal, monto_igv, monto_total, tipo_cambio, monto_convertido,
        nombre_archivo_comprobante, estado_comprobante, id_solicitud_gasto, id_moneda,
        id_usuario_creacion, id_usuario_modificacion
    ) VALUES(
        p_tipo_documento, p_ruc_proveedor, p_razon_social, p_numero_serie, p_fecha_emision,
        p_monto_subtotal, p_monto_igv, p_monto_total, p_tipo_cambio, p_monto_convertido,
        p_nombre_archivo, p_estado_comprobante, p_id_solicitud_gasto, p_id_moneda,
        p_id_usuario_accion, p_id_usuario_accion
    );
    SET p_id_generado = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS pa_modificar_comprobante_pago $$
CREATE PROCEDURE pa_modificar_comprobante_pago(
    IN p_id_usuario_accion INT,
    IN p_id_comprobante INT,
    IN p_tipo_documento ENUM('FACTURA','BOLETA','DJ_EXCEPCIONAL'),
    IN p_ruc_proveedor CHAR(11),
    IN p_razon_social VARCHAR(150),
    IN p_numero_serie VARCHAR(30),
    IN p_fecha_emision DATETIME,
    IN p_monto_subtotal DECIMAL(12,2),
    IN p_monto_igv DECIMAL(12,2),
    IN p_monto_total DECIMAL(12,2),
    IN p_tipo_cambio DECIMAL(10,4),
    IN p_monto_convertido DECIMAL(12,2),
    IN p_nombre_archivo VARCHAR(500),
    IN p_estado_comprobante ENUM('POR_REVISAR','ANULADO','APROBADO','OBSERVADO'),
    IN p_id_solicitud_gasto INT,
    IN p_id_moneda INT
)
BEGIN
    IF p_id_comprobante IS NULL OR p_id_comprobante <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de comprobante invalido';
    END IF;

    UPDATE ope_comprobante_pago
    SET tipo_documento = p_tipo_documento,
        ruc_proveedor = p_ruc_proveedor,
        razon_social = p_razon_social,
        numero_serie = p_numero_serie,
        fecha_emision = p_fecha_emision,
        monto_subtotal = p_monto_subtotal,
        monto_igv = p_monto_igv,
        monto_total = p_monto_total,
        tipo_cambio = p_tipo_cambio,
        monto_convertido = p_monto_convertido,
        nombre_archivo_comprobante = p_nombre_archivo,
        estado_comprobante = p_estado_comprobante,
        id_solicitud_gasto = p_id_solicitud_gasto,
        id_moneda = p_id_moneda,
        actualizado_at = NOW(),
        id_usuario_modificacion = p_id_usuario_accion
    WHERE id_comprobante = p_id_comprobante;
END$$

-- ope_ciclo_caja
DROP PROCEDURE IF EXISTS pa_insertar_ciclo_caja $$
CREATE PROCEDURE pa_insertar_ciclo_caja(
    IN p_id_usuario_accion INT,
    IN p_numero_semana INT,
    IN p_fecha_apertura DATETIME,
    IN p_fecha_cierre DATETIME,
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

    IF p_numero_semana IS NULL OR p_numero_semana <= 0 THEN
        SET p_numero_semana = WEEK(p_fecha_apertura, 1);
    END IF;

    INSERT INTO ope_ciclo_caja(
        numero_semana, fecha_apertura, fecha_cierre, monto_saldo_inicial,
        monto_total_gastado, estado_ciclo, id_caja_chica, id_rendicion,
        id_usuario_creacion, id_usuario_modificacion
    ) VALUES(
        p_numero_semana, p_fecha_apertura, p_fecha_cierre,
        p_monto_saldo_inicial, COALESCE(p_monto_total_gastado, 0),
        COALESCE(p_estado_ciclo, 'ABIERTO'), p_id_caja_chica, p_id_rendicion,
        p_id_usuario_accion, p_id_usuario_accion
    );
    SET p_id_generado = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS pa_modificar_ciclo_caja $$
CREATE PROCEDURE pa_modificar_ciclo_caja(
    IN p_id_usuario_accion INT,
    IN p_id_ciclo_caja INT,
    IN p_numero_semana INT,
    IN p_fecha_apertura DATETIME,
    IN p_fecha_cierre DATETIME,
    IN p_monto_saldo_inicial DECIMAL(12,2),
    IN p_monto_total_gastado DECIMAL(12,2),
    IN p_estado_ciclo ENUM('ABIERTO','CERRADO','LIQUIDADO','EN_EXCEPCION'),
    IN p_id_caja_chica INT,
    IN p_id_rendicion INT
)
BEGIN
    IF p_id_ciclo_caja IS NULL OR p_id_ciclo_caja <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de ciclo de caja invalido';
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
        actualizado_at = NOW(),
        id_usuario_modificacion = p_id_usuario_accion
    WHERE id_ciclo_caja = p_id_ciclo_caja;
END$$

-- ope_rendicion
DROP PROCEDURE IF EXISTS pa_insertar_rendicion $$
CREATE PROCEDURE pa_insertar_rendicion(
    IN p_id_usuario_accion INT,
    IN p_fecha_presentacion DATETIME,
    IN p_fecha_aprobacion DATETIME,
    IN p_monto_total_declarado DECIMAL(12,2),
    IN p_monto_total_aprobado DECIMAL(12,2),
    IN p_monto_saldo_final DECIMAL(12,2),
    IN p_estado_rendicion ENUM('ACEPTADO','EN_ESPERA','DENEGADO','OBSERVADO','ANULADO'),
    IN p_comentario VARCHAR(500),
    IN p_id_ciclo_caja INT,
    OUT p_id_generado INT
)
BEGIN
    DECLARE v_total_declarado DECIMAL(12,2) DEFAULT 0;
    DECLARE v_total_aprobado DECIMAL(12,2) DEFAULT 0;
    DECLARE v_saldo_final DECIMAL(12,2) DEFAULT 0;
    DECLARE v_saldo_inicial DECIMAL(12,2) DEFAULT 0;

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
        fecha_presentacion, fecha_aprobacion, monto_total_declarado,
        monto_total_aprobado, monto_saldo_final, estado_rendicion,
        comentario, id_ciclo_caja, id_usuario_creacion, id_usuario_modificacion
    ) VALUES(
        p_fecha_presentacion, p_fecha_aprobacion, v_total_declarado,
        v_total_aprobado, v_saldo_final, p_estado_rendicion,
        p_comentario, p_id_ciclo_caja, p_id_usuario_accion, p_id_usuario_accion
    );
    SET p_id_generado = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS pa_modificar_rendicion $$
CREATE PROCEDURE pa_modificar_rendicion(
    IN p_id_usuario_accion INT,
    IN p_id_rendicion INT,
    IN p_fecha_presentacion DATETIME,
    IN p_fecha_aprobacion DATETIME,
    IN p_monto_total_declarado DECIMAL(12,2),
    IN p_monto_total_aprobado DECIMAL(12,2),
    IN p_monto_saldo_final DECIMAL(12,2),
    IN p_estado_rendicion ENUM('ACEPTADO','EN_ESPERA','DENEGADO','OBSERVADO','ANULADO'),
    IN p_comentario VARCHAR(500),
    IN p_id_ciclo_caja INT
)
BEGIN
    DECLARE v_total_declarado DECIMAL(12,2) DEFAULT 0;
    DECLARE v_total_aprobado DECIMAL(12,2) DEFAULT 0;
    DECLARE v_saldo_final DECIMAL(12,2) DEFAULT 0;
    DECLARE v_saldo_inicial DECIMAL(12,2) DEFAULT 0;
    DECLARE v_ciclo_id INT DEFAULT NULL;

    SET v_ciclo_id = COALESCE(p_id_ciclo_caja, (SELECT id_ciclo_caja FROM ope_rendicion WHERE id_rendicion = p_id_rendicion));

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
        actualizado_at = NOW(),
        id_usuario_modificacion = p_id_usuario_accion
    WHERE id_rendicion = p_id_rendicion;
END$$

-- tes_tipo_cambio
DROP PROCEDURE IF EXISTS pa_insertar_tipo_cambio $$
CREATE PROCEDURE pa_insertar_tipo_cambio(
    IN p_id_usuario_accion INT,
    IN p_id_moneda_origen INT,
    IN p_id_moneda_destino INT,
    IN p_valor_tipo_cambio DECIMAL(10,4),
    IN p_fecha_tipo_cambio DATETIME,
    OUT p_id_generado INT
)
BEGIN
    INSERT INTO tes_tipo_cambio(
        id_moneda_origen, id_moneda_destino, valor_tipo_cambio, fecha_tipo_cambio,
        id_usuario_creacion, id_usuario_modificacion
    ) VALUES(
        p_id_moneda_origen, p_id_moneda_destino, p_valor_tipo_cambio, p_fecha_tipo_cambio,
        p_id_usuario_accion, p_id_usuario_accion
    );
    SET p_id_generado = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS pa_modificar_tipo_cambio $$
CREATE PROCEDURE pa_modificar_tipo_cambio(
    IN p_id_usuario_accion INT,
    IN p_id_tipo_cambio INT,
    IN p_id_moneda_origen INT,
    IN p_id_moneda_destino INT,
    IN p_valor_tipo_cambio DECIMAL(10,4),
    IN p_fecha_tipo_cambio DATETIME
)
BEGIN
    IF p_id_tipo_cambio IS NULL OR p_id_tipo_cambio <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de tipo de cambio invalido';
    END IF;

    UPDATE tes_tipo_cambio
    SET id_moneda_origen = p_id_moneda_origen,
        id_moneda_destino = p_id_moneda_destino,
        valor_tipo_cambio = p_valor_tipo_cambio,
        fecha_tipo_cambio = p_fecha_tipo_cambio,
        id_usuario_modificacion = p_id_usuario_accion
    WHERE id_tipo_cambio = p_id_tipo_cambio;
END$$

DELIMITER ;

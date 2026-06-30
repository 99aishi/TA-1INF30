-- 1. Alter the table column ENUM definition
ALTER TABLE ope_solicitud_gasto 
MODIFY COLUMN estado_solicitud ENUM('PENDIENTE', 'APROBADO', 'PAGADO', 'RENDIDO', 'RECHAZADO', 'OBSERVADO', 'ANULADO') DEFAULT 'PENDIENTE';

-- 2. Drop and recreate the stored procedure pa_modificar_solicitud_gasto to accept 'OBSERVADO'
DELIMITER $$

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
    IN p_estado_solicitud ENUM('PENDIENTE','APROBADO','PAGADO','RENDIDO','RECHAZADO','OBSERVADO','ANULADO'),
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

DELIMITER ;

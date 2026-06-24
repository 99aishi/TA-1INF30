-- ===============================================================================
-- EVENTOS AUTOMÁTICOS
-- ===============================================================================
-- Activa el planificador de eventos de MySQL
SET GLOBAL event_scheduler = ON;

DELIMITER $$

DROP EVENT IF EXISTS ev_cierre_semanal_caja_chica $$
CREATE EVENT ev_cierre_semanal_caja_chica
ON SCHEDULE EVERY 1 WEEK
STARTS TIMESTAMP(CURRENT_DATE) + INTERVAL (4 - WEEKDAY(CURRENT_DATE)) DAY + INTERVAL 23 HOUR
COMMENT 'Cada viernes a las 23:00 cierra los ciclos abiertos, genera su rendición y crea el ciclo de la siguiente semana.'
DO
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE v_id_ciclo INT DEFAULT NULL;
    DECLARE v_id_caja INT DEFAULT NULL;
    DECLARE v_monto_techo DECIMAL(12,2) DEFAULT 0;
    DECLARE v_fecha_apertura_sig DATE;
    DECLARE v_fecha_cierre_sig DATE;
    DECLARE v_numero_semana_sig INT;

    REPEAT
        SET v_id_ciclo = NULL;

        SELECT occ.id_ciclo_caja,
               occ.id_caja_chica,
               cc.monto_techo
          INTO v_id_ciclo, v_id_caja, v_monto_techo
          FROM ope_ciclo_caja occ
          JOIN tes_caja_chica cc ON occ.id_caja_chica = cc.id_fondo
         WHERE occ.estado_ciclo = 'ABIERTO'
           AND (occ.fecha_cierre IS NULL OR occ.fecha_cierre <= CURDATE())
         LIMIT 1;

        IF v_id_ciclo IS NOT NULL THEN
            -- Generar la rendición del ciclo; este SP también cambia el ciclo a EN_EXCEPCION
            CALL pa_generar_rendicion_de_ciclo(1, v_id_ciclo);

            -- Crear el ciclo de la siguiente semana (lunes a domingo)
            SET v_fecha_apertura_sig = DATE_ADD(CURDATE(), INTERVAL (7 - WEEKDAY(CURDATE())) DAY);
            SET v_fecha_cierre_sig   = DATE_ADD(v_fecha_apertura_sig, INTERVAL 6 DAY);
            SET v_numero_semana_sig  = WEEK(v_fecha_apertura_sig, 1);

            CALL pa_insertar_ciclo_caja(1, v_numero_semana_sig, v_fecha_apertura_sig,
                                        v_fecha_cierre_sig, v_monto_techo, 0,
                                        'ABIERTO', v_id_caja, NULL, @nuevo_id_ciclo);
        ELSE
            SET done = TRUE;
        END IF;
    UNTIL done END REPEAT;
END$$

DELIMITER ;

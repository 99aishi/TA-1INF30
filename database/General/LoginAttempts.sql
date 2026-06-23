
-- ===============================================================================
-- TABLA Y PROCEDIMIENTOS PARA CONTROL DE INTENTOS DE LOGIN
-- Reglas: maximo 5 intentos fallidos, bloqueo de 15 minutos
-- ===============================================================================

CREATE TABLE IF NOT EXISTS rrhh_intentos_login (
    id_intento INT NOT NULL AUTO_INCREMENT,
    id_usuario INT NULL,
    correo VARCHAR(255) NOT NULL,
    intentos_fallidos INT DEFAULT 0,
    bloqueado_hasta DATETIME NULL,
    ultimo_intento DATETIME DEFAULT CURRENT_TIMESTAMP,
    creado_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    actualizado_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_rrhh_intentos_login PRIMARY KEY (id_intento),
    CONSTRAINT fk_intentos_usuario FOREIGN KEY (id_usuario)
        REFERENCES rrhh_usuario(id_usuario)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    CONSTRAINT uk_intentos_correo UNIQUE (correo)
) ENGINE=InnoDB;

CREATE INDEX idx_intentos_bloqueado ON rrhh_intentos_login(bloqueado_hasta);
CREATE INDEX idx_intentos_correo ON rrhh_intentos_login(correo);
CREATE INDEX idx_intentos_ultimo ON rrhh_intentos_login(ultimo_intento);

DELIMITER $$

DROP PROCEDURE IF EXISTS pa_verificar_bloqueo $$
CREATE PROCEDURE pa_verificar_bloqueo(
    IN p_correo VARCHAR(255),
    OUT p_bloqueado TINYINT,
    OUT p_intentos_restantes INT,
    OUT p_minutos_restantes INT,
    OUT p_id_usuario INT
)
BEGIN
    DECLARE v_bloqueado_hasta DATETIME;
    DECLARE v_intentos INT DEFAULT 0;
    DECLARE v_id_usuario INT DEFAULT NULL;
    DECLARE v_max_intentos INT DEFAULT 5;
    DECLARE v_minutos_bloqueo INT DEFAULT 15;

    SET p_bloqueado = 0;
    SET p_intentos_restantes = v_max_intentos;
    SET p_minutos_restantes = 0;
    SET p_id_usuario = NULL;

    -- Buscar usuario por correo para obtener id_usuario
    SELECT u.id_usuario INTO v_id_usuario
    FROM rrhh_usuario u
    WHERE u.correo = p_correo;

    SET p_id_usuario = v_id_usuario;

    -- Obtener registro de intentos si existe
    SELECT intentos_fallidos, bloqueado_hasta
      INTO v_intentos, v_bloqueado_hasta
    FROM rrhh_intentos_login
    WHERE correo = p_correo;

    -- Si no hay registro, el usuario puede intentar con todos los intentos disponibles
    IF v_intentos IS NULL THEN
        SET p_intentos_restantes = v_max_intentos;
    ELSE
        -- Verificar si ya paso el tiempo de bloqueo
        IF v_bloqueado_hasta IS NOT NULL AND v_bloqueado_hasta > NOW() THEN
            SET p_bloqueado = 1;
            SET p_minutos_restantes = TIMESTAMPDIFF(MINUTE, NOW(), v_bloqueado_hasta);
            SET p_intentos_restantes = 0;
        ELSE
            -- Bloqueo expiro o no estaba bloqueado, calcular intentos restantes
            SET p_intentos_restantes = GREATEST(v_max_intentos - v_intentos, 0);
        END IF;
    END IF;
END$$

DROP PROCEDURE IF EXISTS pa_registrar_intento_fallido $$
CREATE PROCEDURE pa_registrar_intento_fallido(
    IN p_correo VARCHAR(255),
    IN p_id_usuario INT
)
BEGIN
    DECLARE v_intentos INT DEFAULT 0;
    DECLARE v_max_intentos INT DEFAULT 5;
    DECLARE v_minutos_bloqueo INT DEFAULT 15;

    -- Intentar insertar si no existe
    INSERT INTO rrhh_intentos_login (id_usuario, correo, intentos_fallidos, ultimo_intento)
    VALUES (p_id_usuario, p_correo, 1, NOW())
    ON DUPLICATE KEY UPDATE
        id_usuario = COALESCE(p_id_usuario, id_usuario),
        intentos_fallidos = intentos_fallidos + 1,
        ultimo_intento = NOW();

    -- Obtener contador actualizado
    SELECT intentos_fallidos INTO v_intentos
    FROM rrhh_intentos_login
    WHERE correo = p_correo;

    -- Si se supera el limite, establecer bloqueo de 15 minutos
    IF v_intentos >= v_max_intentos THEN
        UPDATE rrhh_intentos_login
           SET bloqueado_hasta = DATE_ADD(NOW(), INTERVAL v_minutos_bloqueo MINUTE)
         WHERE correo = p_correo;
    END IF;
END$$

DROP PROCEDURE IF EXISTS pa_resetear_intentos $$
CREATE PROCEDURE pa_resetear_intentos(
    IN p_correo VARCHAR(255)
)
BEGIN
    DELETE FROM rrhh_intentos_login
    WHERE correo = p_correo;
END$$

DELIMITER ;


CREATE TABLE IF NOT EXISTS log_auditoria (
    id_auditoria INT NOT NULL AUTO_INCREMENT,
    nombre_tabla VARCHAR(100) NOT NULL,
    tipo_evento VARCHAR(20) NOT NULL, -- Valores: 'INSERT', 'UPDATE', 'DELETE', 'LOGIN_SUCCESS', 'LOGIN_FAILED'
    id_registro_afectado VARCHAR(50) NOT NULL,
    valores_antiguos JSON, -- Estado previo
    valores_nuevos JSON,   -- Estado final
    
    creado_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_usuario_auditoria INT NULL,
    
    CONSTRAINT pk_log_auditoria PRIMARY KEY (id_auditoria)
) ENGINE=InnoDB;

CREATE INDEX idx_log_auditoria_tabla ON log_auditoria(nombre_tabla);
CREATE INDEX idx_log_auditoria_evento ON log_auditoria(tipo_evento);
CREATE INDEX idx_log_auditoria_creado ON log_auditoria(creado_at);
CREATE INDEX idx_log_auditoria_usuario ON log_auditoria(id_usuario_auditoria);

DELIMITER //


DROP PROCEDURE IF EXISTS pa_insertar_auditoria//
CREATE PROCEDURE pa_insertar_auditoria (
    IN p_nombre_tabla VARCHAR(100),
    IN p_tipo_evento VARCHAR(10),
    IN p_id_registro VARCHAR(50),
    IN p_valores_antiguos JSON,
    IN p_valores_nuevos JSON,
    IN p_id_usuario_accion INT
)
BEGIN
    INSERT INTO log_auditoria (
        nombre_tabla,
        tipo_evento,
        id_registro_afectado,
        valores_antiguos,
        valores_nuevos,
        id_usuario_auditoria
    ) VALUES (
        p_nombre_tabla,
        p_tipo_evento,
        p_id_registro,
        p_valores_antiguos,
        p_valores_nuevos,
        p_id_usuario_accion
    );
END //

DROP PROCEDURE IF EXISTS pa_listar_auditoria_recientes//
CREATE PROCEDURE pa_listar_auditoria_recientes(
    IN p_limite INT
)
BEGIN
    IF p_limite IS NULL OR p_limite <= 0 THEN
        SET p_limite = 50;
    END IF;

    SELECT 
        a.id_auditoria,
        a.nombre_tabla,
        a.tipo_evento,
        a.id_registro_afectado,
        a.valores_antiguos,
        a.valores_nuevos,
        a.creado_at,
        a.id_usuario_auditoria,
        u.nombres AS usuario_nombres,
        u.apellido_paterno AS usuario_apellido_paterno
    FROM log_auditoria a
    LEFT JOIN rrhh_usuario u ON a.id_usuario_auditoria = u.id_usuario
    ORDER BY a.creado_at DESC
    LIMIT p_limite;
END //

DELIMITER ;
DROP PROCEDURE IF EXISTS pa_insertar_tes_entrega_rendir;
$$
DELIMITER $$


CREATE PROCEDURE pa_insertar_tes_entrega_rendir(           
    
    OUT _id_fondo INT,
    IN p_nombre_fondo VARCHAR(100), -- not null
    IN p_monto_saldo_actual DECIMAL(12,2),
    IN p_estado_fondo VARCHAR(20), -- not nul
    

    IN p_motivo_entrega VARCHAR(200),
    IN p_monto_solicitado DECIMAL(12,2), -- not null
    IN p_fecha_solicitud DATE,
    IN p_fecha_apertura DATE,
    IN p_fecha_cierre DATE,
    IN p_estado_entrega VARCHAR(20),
    IN p_id_usuario_solicitante INT, -- not null
    IN p_id_usuario_aprobador INT -- not null
    
)
BEGIN
	DECLARE v_id_titular INT;
    -- 1. Validaciones de obligatoriedad
    IF p_nombre_fondo IS NULL OR TRIM(p_nombre_fondo) = '' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error: El nombre del fondo es obligatorio.';
    END IF;

    IF p_estado_fondo IS NULL OR TRIM(p_estado_fondo) = '' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error: El estado del fondo es obligatorio.';
    END IF;
    
    IF p_monto_solicitado IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error: El monto solicitado es obligatorio.';
    END IF;
	
	IF p_id_usuario_solicitante IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error: El usuario solicitante es obligatorio.';
    END IF;

	IF p_id_usuario_aprobador IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error: El usuario aprobador es obligatorio.';
    END IF;




    -- 2. Inserción si todo es correcto
    INSERT INTO tes_fondo (
        nombre_fondo,
        monto_saldo_actual,
        estado_fondo
    ) 
    VALUES (
        p_nombre_fondo,
        COALESCE(p_monto_saldo_actual, 0.00), -- Si viene null, ponemos 0
        p_estado_fondo
    );
    SET _id_fondo = LAST_INSERT_ID();

    INSERT INTO tes_entrega_rendir(
    	id_fondo,
    	motivo_entrega,
    	monto_solicitado, 
    	fecha_solicitud, 
    	fecha_apertura, 
    	fecha_cierre, 
    	estado_entrega, 
    	id_usuario_solicitante,
    	id_usuario_aprobador
    	)
    VALUES(
    	_id_fondo,
		p_motivo_entrega,
		p_monto_solicitado,
		p_fecha_solicitud, 
		p_fecha_apertura,
		p_fecha_cierre, 
		p_estado_entrega,
		p_id_usuario_solicitante, 
		p_id_usuario_aprobador 
    	);



END $$

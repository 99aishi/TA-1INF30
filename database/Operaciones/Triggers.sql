DELIMITER //

-- -------------------------------------------------------------------------------
-- 1. TABLA: ope_rendicion
-- -------------------------------------------------------------------------------
CREATE TRIGGER trg_ope_rendicion_before_insert BEFORE INSERT ON ope_rendicion FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    IF NEW.id_usuario_creacion IS NULL THEN 
        SET NEW.id_usuario_creacion = @id_usuario_sesion; 
    END IF;
    SET NEW.id_usuario_modificacion = NEW.id_usuario_creacion;
END //

CREATE TRIGGER trg_ope_rendicion_before_update BEFORE UPDATE ON ope_rendicion FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.creado_at = OLD.creado_at; -- Inmutable
    SET NEW.id_usuario_creacion = OLD.id_usuario_creacion; -- Inmutable
    IF NEW.id_usuario_modificacion IS NULL OR NEW.id_usuario_modificacion = OLD.id_usuario_modificacion THEN 
        SET NEW.id_usuario_modificacion = @id_usuario_sesion; 
    END IF;
END //

-- -------------------------------------------------------------------------------
-- 2. TABLA: ope_ciclo_caja
-- -------------------------------------------------------------------------------
CREATE TRIGGER trg_ope_ciclo_caja_before_insert BEFORE INSERT ON ope_ciclo_caja FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    IF NEW.id_usuario_creacion IS NULL THEN 
        SET NEW.id_usuario_creacion = @id_usuario_sesion; 
    END IF;
    SET NEW.id_usuario_modificacion = NEW.id_usuario_creacion;
END //

CREATE TRIGGER trg_ope_ciclo_caja_before_update BEFORE UPDATE ON ope_ciclo_caja FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.creado_at = OLD.creado_at; 
    SET NEW.id_usuario_creacion = OLD.id_usuario_creacion; 
    IF NEW.id_usuario_modificacion IS NULL OR NEW.id_usuario_modificacion = OLD.id_usuario_modificacion THEN 
        SET NEW.id_usuario_modificacion = @id_usuario_sesion; 
    END IF;
END //

-- -------------------------------------------------------------------------------
-- 3. TABLA: ope_solicitud_gasto
-- -------------------------------------------------------------------------------
CREATE TRIGGER trg_ope_solic_gasto_before_insert BEFORE INSERT ON ope_solicitud_gasto FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    IF NEW.id_usuario_creacion IS NULL THEN 
        SET NEW.id_usuario_creacion = @id_usuario_sesion; 
    END IF;
    SET NEW.id_usuario_modificacion = NEW.id_usuario_creacion;
END //

CREATE TRIGGER trg_ope_solic_gasto_before_update BEFORE UPDATE ON ope_solicitud_gasto FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.creado_at = OLD.creado_at; 
    SET NEW.id_usuario_creacion = OLD.id_usuario_creacion; 
    IF NEW.id_usuario_modificacion IS NULL OR NEW.id_usuario_modificacion = OLD.id_usuario_modificacion THEN 
        SET NEW.id_usuario_modificacion = @id_usuario_sesion; 
    END IF;
END //

-- -------------------------------------------------------------------------------
-- 4. TABLA: ope_comprobante_pago
-- -------------------------------------------------------------------------------
CREATE TRIGGER trg_ope_comp_pago_before_insert BEFORE INSERT ON ope_comprobante_pago FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    IF NEW.id_usuario_creacion IS NULL THEN 
        SET NEW.id_usuario_creacion = @id_usuario_sesion; 
    END IF;
    SET NEW.id_usuario_modificacion = NEW.id_usuario_creacion;
END //

CREATE TRIGGER trg_ope_comp_pago_before_update BEFORE UPDATE ON ope_comprobante_pago FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.creado_at = OLD.creado_at; 
    SET NEW.id_usuario_creacion = OLD.id_usuario_creacion; 
    IF NEW.id_usuario_modificacion IS NULL OR NEW.id_usuario_modificacion = OLD.id_usuario_modificacion THEN 
        SET NEW.id_usuario_modificacion = @id_usuario_sesion; 
    END IF;
END //

-- -------------------------------------------------------------------------------
-- 5. TABLA: ope_transaccion
-- -------------------------------------------------------------------------------
CREATE TRIGGER trg_ope_transaccion_before_insert BEFORE INSERT ON ope_transaccion FOR EACH ROW
BEGIN
    -- Establecer la hora de la operación como se indicó en la estructura de la tabla
    SET NEW.momento_operacion = NOW();

    -- Campos para la Auditoría
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    IF NEW.id_usuario_creacion IS NULL THEN 
        SET NEW.id_usuario_creacion = @id_usuario_sesion; 
    END IF;
    SET NEW.id_usuario_modificacion = NEW.id_usuario_creacion;
END //

CREATE TRIGGER trg_ope_transaccion_before_update BEFORE UPDATE ON ope_transaccion FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.creado_at = OLD.creado_at; 
    SET NEW.id_usuario_creacion = OLD.id_usuario_creacion; 
    IF NEW.id_usuario_modificacion IS NULL OR NEW.id_usuario_modificacion = OLD.id_usuario_modificacion THEN 
        SET NEW.id_usuario_modificacion = @id_usuario_sesion; 
    END IF;
END //

-- Restauramos el delimitador por defecto (punto y coma)
DELIMITER ;
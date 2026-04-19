DELIMITER //

-- ===============================================================================
-- MÓDULO: ope (Operaciones)
-- ===============================================================================

-- TABLA: ope_rendicion
CREATE TRIGGER trg_ope_rendicion_before_insert BEFORE INSERT ON ope_rendicion FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    IF NEW.id_usuario_creacion IS NULL THEN SET NEW.id_usuario_creacion = @id_usuario_sesion; END IF;
    SET NEW.id_usuario_modificacion = NEW.id_usuario_creacion;
END //

CREATE TRIGGER trg_ope_rendicion_before_update BEFORE UPDATE ON ope_rendicion FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.creado_at = OLD.creado_at;
    SET NEW.id_usuario_creacion = OLD.id_usuario_creacion;
    IF NEW.id_usuario_modificacion IS NULL OR NEW.id_usuario_modificacion = OLD.id_usuario_modificacion THEN SET NEW.id_usuario_modificacion = @id_usuario_sesion; END IF;
END //

-- TABLA: ope_ciclo_caja
CREATE TRIGGER trg_ope_ciclo_caja_before_insert BEFORE INSERT ON ope_ciclo_caja FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    IF NEW.id_usuario_creacion IS NULL THEN SET NEW.id_usuario_creacion = @id_usuario_sesion; END IF;
    SET NEW.id_usuario_modificacion = NEW.id_usuario_creacion;
END //

CREATE TRIGGER trg_ope_ciclo_caja_before_update BEFORE UPDATE ON ope_ciclo_caja FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.creado_at = OLD.creado_at;
    SET NEW.id_usuario_creacion = OLD.id_usuario_creacion;
    IF NEW.id_usuario_modificacion IS NULL OR NEW.id_usuario_modificacion = OLD.id_usuario_modificacion THEN SET NEW.id_usuario_modificacion = @id_usuario_sesion; END IF;
END //

-- TABLA: ope_solicitud_gasto
CREATE TRIGGER trg_ope_solic_gasto_before_insert BEFORE INSERT ON ope_solicitud_gasto FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    IF NEW.id_usuario_creacion IS NULL THEN SET NEW.id_usuario_creacion = @id_usuario_sesion; END IF;
    SET NEW.id_usuario_modificacion = NEW.id_usuario_creacion;
END //

CREATE TRIGGER trg_ope_solic_gasto_before_update BEFORE UPDATE ON ope_solicitud_gasto FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.creado_at = OLD.creado_at;
    SET NEW.id_usuario_creacion = OLD.id_usuario_creacion;
    IF NEW.id_usuario_modificacion IS NULL OR NEW.id_usuario_modificacion = OLD.id_usuario_modificacion THEN SET NEW.id_usuario_modificacion = @id_usuario_sesion; END IF;
END //

-- TABLA: ope_comprobante_pago
CREATE TRIGGER trg_ope_comp_pago_before_insert BEFORE INSERT ON ope_comprobante_pago FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    IF NEW.id_usuario_creacion IS NULL THEN SET NEW.id_usuario_creacion = @id_usuario_sesion; END IF;
    SET NEW.id_usuario_modificacion = NEW.id_usuario_creacion;
END //

CREATE TRIGGER trg_ope_comp_pago_before_update BEFORE UPDATE ON ope_comprobante_pago FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.creado_at = OLD.creado_at;
    SET NEW.id_usuario_creacion = OLD.id_usuario_creacion;
    IF NEW.id_usuario_modificacion IS NULL OR NEW.id_usuario_modificacion = OLD.id_usuario_modificacion THEN SET NEW.id_usuario_modificacion = @id_usuario_sesion; END IF;
END //

-- TABLA: ope_transaccion
CREATE TRIGGER trg_ope_transaccion_before_insert BEFORE INSERT ON ope_transaccion FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    IF NEW.id_usuario_creacion IS NULL THEN SET NEW.id_usuario_creacion = @id_usuario_sesion; END IF;
    SET NEW.id_usuario_modificacion = NEW.id_usuario_creacion;
END //

CREATE TRIGGER trg_ope_transaccion_before_update BEFORE UPDATE ON ope_transaccion FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.creado_at = OLD.creado_at;
    SET NEW.id_usuario_creacion = OLD.id_usuario_creacion;
    IF NEW.id_usuario_modificacion IS NULL OR NEW.id_usuario_modificacion = OLD.id_usuario_modificacion THEN SET NEW.id_usuario_modificacion = @id_usuario_sesion; END IF;
END //

DELIMITER ;
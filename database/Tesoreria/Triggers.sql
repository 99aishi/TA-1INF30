DELIMITER //

-- -------------------------------------------------------------------------------
-- 1. TABLA: tes_moneda
-- -------------------------------------------------------------------------------
CREATE TRIGGER trg_tes_moneda_before_insert BEFORE INSERT ON tes_moneda FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    IF NEW.id_usuario_creacion IS NULL THEN SET NEW.id_usuario_creacion = @id_usuario_sesion; END IF;
    SET NEW.id_usuario_modificacion = NEW.id_usuario_creacion;
END //

CREATE TRIGGER trg_tes_moneda_before_update BEFORE UPDATE ON tes_moneda FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.creado_at = OLD.creado_at;
    SET NEW.id_usuario_creacion = OLD.id_usuario_creacion;
    IF NEW.id_usuario_modificacion IS NULL OR NEW.id_usuario_modificacion = OLD.id_usuario_modificacion THEN SET NEW.id_usuario_modificacion = @id_usuario_sesion; END IF;
END //

-- -------------------------------------------------------------------------------
-- 2. TABLA: tes_cuenta_bancaria
-- -------------------------------------------------------------------------------
CREATE TRIGGER trg_tes_cuenta_bancaria_before_insert BEFORE INSERT ON tes_cuenta_bancaria FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    IF NEW.id_usuario_creacion IS NULL THEN SET NEW.id_usuario_creacion = @id_usuario_sesion; END IF;
    SET NEW.id_usuario_modificacion = NEW.id_usuario_creacion;
END //

CREATE TRIGGER trg_tes_cuenta_bancaria_before_update BEFORE UPDATE ON tes_cuenta_bancaria FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.creado_at = OLD.creado_at;
    SET NEW.id_usuario_creacion = OLD.id_usuario_creacion;
    IF NEW.id_usuario_modificacion IS NULL OR NEW.id_usuario_modificacion = OLD.id_usuario_modificacion THEN SET NEW.id_usuario_modificacion = @id_usuario_sesion; END IF;
END //

-- -------------------------------------------------------------------------------
-- 3. TABLA: tes_fondo
-- -------------------------------------------------------------------------------
CREATE TRIGGER trg_tes_fondo_before_insert BEFORE INSERT ON tes_fondo FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    IF NEW.id_usuario_creacion IS NULL THEN SET NEW.id_usuario_creacion = @id_usuario_sesion; END IF;
    SET NEW.id_usuario_modificacion = NEW.id_usuario_creacion;
END //

CREATE TRIGGER trg_tes_fondo_before_update BEFORE UPDATE ON tes_fondo FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.creado_at = OLD.creado_at;
    SET NEW.id_usuario_creacion = OLD.id_usuario_creacion;
    IF NEW.id_usuario_modificacion IS NULL OR NEW.id_usuario_modificacion = OLD.id_usuario_modificacion THEN SET NEW.id_usuario_modificacion = @id_usuario_sesion; END IF;
END //

-- -------------------------------------------------------------------------------
-- 4. TABLA: tes_caja_chica
-- -------------------------------------------------------------------------------
CREATE TRIGGER trg_tes_caja_chica_before_insert BEFORE INSERT ON tes_caja_chica FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    IF NEW.id_usuario_creacion IS NULL THEN SET NEW.id_usuario_creacion = @id_usuario_sesion; END IF;
    SET NEW.id_usuario_modificacion = NEW.id_usuario_creacion;
END //

CREATE TRIGGER trg_tes_caja_chica_before_update BEFORE UPDATE ON tes_caja_chica FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.creado_at = OLD.creado_at;
    SET NEW.id_usuario_creacion = OLD.id_usuario_creacion;
    IF NEW.id_usuario_modificacion IS NULL OR NEW.id_usuario_modificacion = OLD.id_usuario_modificacion THEN SET NEW.id_usuario_modificacion = @id_usuario_sesion; END IF;
END //

-- -------------------------------------------------------------------------------
-- 5. TABLA: tes_entrega_rendir
-- -------------------------------------------------------------------------------
CREATE TRIGGER trg_tes_entrega_rendir_before_insert BEFORE INSERT ON tes_entrega_rendir FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    IF NEW.id_usuario_creacion IS NULL THEN SET NEW.id_usuario_creacion = @id_usuario_sesion; END IF;
    SET NEW.id_usuario_modificacion = NEW.id_usuario_creacion;
END //

CREATE TRIGGER trg_tes_entrega_rendir_before_update BEFORE UPDATE ON tes_entrega_rendir FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.creado_at = OLD.creado_at;
    SET NEW.id_usuario_creacion = OLD.id_usuario_creacion;
    IF NEW.id_usuario_modificacion IS NULL OR NEW.id_usuario_modificacion = OLD.id_usuario_modificacion THEN SET NEW.id_usuario_modificacion = @id_usuario_sesion; END IF;
END //

DELIMITER ;
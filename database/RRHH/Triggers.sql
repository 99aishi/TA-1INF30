ELIMITER //

-- ===============================================================================
-- MÓDULO: rrhh (Recursos Humanos y Accesos)
-- ===============================================================================

-- TABLA: rrhh_area
CREATE TRIGGER trg_rrhh_area_before_insert BEFORE INSERT ON rrhh_area FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    IF NEW.id_usuario_creacion IS NULL THEN SET NEW.id_usuario_creacion = @id_usuario_sesion; END IF;
    SET NEW.id_usuario_modificacion = NEW.id_usuario_creacion;
END //

CREATE TRIGGER trg_rrhh_area_before_update BEFORE UPDATE ON rrhh_area FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.creado_at = OLD.creado_at; -- Inmutable
    SET NEW.id_usuario_creacion = OLD.id_usuario_creacion; -- Inmutable
    IF NEW.id_usuario_modificacion IS NULL OR NEW.id_usuario_modificacion = OLD.id_usuario_modificacion THEN SET NEW.id_usuario_modificacion = @id_usuario_sesion; END IF;
END //

-- TABLA: rrhh_rol
CREATE TRIGGER trg_rrhh_rol_before_insert BEFORE INSERT ON rrhh_rol FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    IF NEW.id_usuario_creacion IS NULL THEN SET NEW.id_usuario_creacion = @id_usuario_sesion; END IF;
    SET NEW.id_usuario_modificacion = NEW.id_usuario_creacion;
END //

CREATE TRIGGER trg_rrhh_rol_before_update BEFORE UPDATE ON rrhh_rol FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.creado_at = OLD.creado_at;
    SET NEW.id_usuario_creacion = OLD.id_usuario_creacion;
    IF NEW.id_usuario_modificacion IS NULL OR NEW.id_usuario_modificacion = OLD.id_usuario_modificacion THEN SET NEW.id_usuario_modificacion = @id_usuario_sesion; END IF;
END //

-- TABLA: rrhh_usuario
CREATE TRIGGER trg_rrhh_usuario_before_insert BEFORE INSERT ON rrhh_usuario FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    IF NEW.id_usuario_creacion IS NULL THEN SET NEW.id_usuario_creacion = @id_usuario_sesion; END IF;
    SET NEW.id_usuario_modificacion = NEW.id_usuario_creacion;
END //

CREATE TRIGGER trg_rrhh_usuario_before_update BEFORE UPDATE ON rrhh_usuario FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.creado_at = OLD.creado_at;
    SET NEW.id_usuario_creacion = OLD.id_usuario_creacion;
    IF NEW.id_usuario_modificacion IS NULL OR NEW.id_usuario_modificacion = OLD.id_usuario_modificacion THEN SET NEW.id_usuario_modificacion = @id_usuario_sesion; END IF;
END //

-- TABLA: rrhh_empleado
CREATE TRIGGER trg_rrhh_empleado_before_insert BEFORE INSERT ON rrhh_empleado FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    IF NEW.id_usuario_creacion IS NULL THEN SET NEW.id_usuario_creacion = @id_usuario_sesion; END IF;
    SET NEW.id_usuario_modificacion = NEW.id_usuario_creacion;
END //

CREATE TRIGGER trg_rrhh_empleado_before_update BEFORE UPDATE ON rrhh_empleado FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.creado_at = OLD.creado_at;
    SET NEW.id_usuario_creacion = OLD.id_usuario_creacion;
    IF NEW.id_usuario_modificacion IS NULL OR NEW.id_usuario_modificacion = OLD.id_usuario_modificacion THEN SET NEW.id_usuario_modificacion = @id_usuario_sesion; END IF;
END //

-- TABLA: rrhh_administrador
CREATE TRIGGER trg_rrhh_administrador_before_insert BEFORE INSERT ON rrhh_administrador FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    IF NEW.id_usuario_creacion IS NULL THEN SET NEW.id_usuario_creacion = @id_usuario_sesion; END IF;
    SET NEW.id_usuario_modificacion = NEW.id_usuario_creacion;
END //

CREATE TRIGGER trg_rrhh_administrador_before_update BEFORE UPDATE ON rrhh_administrador FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.creado_at = OLD.creado_at;
    SET NEW.id_usuario_creacion = OLD.id_usuario_creacion;
    IF NEW.id_usuario_modificacion IS NULL OR NEW.id_usuario_modificacion = OLD.id_usuario_modificacion THEN SET NEW.id_usuario_modificacion = @id_usuario_sesion; END IF;
END //

-- TABLA: rrhh_historial_jefatura
CREATE TRIGGER trg_rrhh_hist_jefatura_before_insert BEFORE INSERT ON rrhh_historial_jefatura FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    IF NEW.id_usuario_creacion IS NULL THEN SET NEW.id_usuario_creacion = @id_usuario_sesion; END IF;
    SET NEW.id_usuario_modificacion = NEW.id_usuario_creacion;
END //

CREATE TRIGGER trg_rrhh_hist_jefatura_before_update BEFORE UPDATE ON rrhh_historial_jefatura FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.creado_at = OLD.creado_at;
    SET NEW.id_usuario_creacion = OLD.id_usuario_creacion;
    IF NEW.id_usuario_modificacion IS NULL OR NEW.id_usuario_modificacion = OLD.id_usuario_modificacion THEN SET NEW.id_usuario_modificacion = @id_usuario_sesion; END IF;
END //

DELIMITER ;
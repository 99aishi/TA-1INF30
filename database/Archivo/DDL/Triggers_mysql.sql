-- ===============================================================================
-- PROYECTO: Collab DB Scripts - Sistema de Gestión Financiera
-- ESTÁNDAR: v2.0 (Triggers de Auditoría)
-- MOTOR: MySQL 8.0+
-- NOTA: Utiliza la variable de sesión @id_usuario_sesion como fallback
--       si Java no envía explícitamente el id_usuario en el INSERT/UPDATE.
-- ===============================================================================

DELIMITER //

-- ===============================================================================
-- 1. MÓDULO: rrhh (Recursos Humanos y Accesos)
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


-- ===============================================================================
-- 2. MÓDULO: tes (Tesorería)
-- ===============================================================================

-- TABLA: tes_moneda
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

-- TABLA: tes_cuenta_bancaria
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

-- TABLA: tes_fondo
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

-- TABLA: tes_caja_chica
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

-- TABLA: tes_entrega_rendir
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


-- ===============================================================================
-- 3. MÓDULO: ope (Operaciones)
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
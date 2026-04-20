SET FOREIGN_KEY_CHECKS = 0;

-- ---------------------------------------------------------
-- MÓDULO 3: Operaciones (ope)
-- ---------------------------------------------------------
DROP TABLE IF EXISTS ope_transaccion;
DROP TABLE IF EXISTS ope_comprobante_pago;
DROP TABLE IF EXISTS ope_solicitud_gasto;
DROP TABLE IF EXISTS ope_ciclo_caja;
DROP TABLE IF EXISTS ope_rendicion;

-- ---------------------------------------------------------
-- MÓDULO 2: Tesorería (tes)
-- ---------------------------------------------------------
DROP TABLE IF EXISTS tes_entrega_rendir;
DROP TABLE IF EXISTS tes_caja_chica;
DROP TABLE IF EXISTS tes_fondo;
DROP TABLE IF EXISTS tes_cuenta_bancaria;
DROP TABLE IF EXISTS tes_moneda;

-- ---------------------------------------------------------
-- MÓDULO 1: Recursos Humanos (rrhh)
-- ---------------------------------------------------------
DROP TABLE IF EXISTS rrhh_administrador;
DROP TABLE IF EXISTS rrhh_empleado;
DROP TABLE IF EXISTS rrhh_area;
DROP TABLE IF EXISTS rrhh_usuario;
DROP TABLE IF EXISTS rrhh_rol;

SET FOREIGN_KEY_CHECKS = 1;
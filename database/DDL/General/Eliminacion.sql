-- Desactivar temporalmente la verificación de llaves foráneas
SET FOREIGN_KEY_CHECKS = 0;

-- 1. Eliminar Módulo de Operaciones (ope)
DROP TABLE IF EXISTS ope_transaccion;
DROP TABLE IF EXISTS ope_comprobante_pago;
DROP TABLE IF EXISTS ope_solicitud_gasto;
DROP TABLE IF EXISTS ope_ciclo_caja;
DROP TABLE IF EXISTS ope_rendicion;

-- 2. Eliminar Módulo de Tesorería (tes)
DROP TABLE IF EXISTS tes_entrega_rendir;
DROP TABLE IF EXISTS tes_caja_chica;
DROP TABLE IF EXISTS tes_fondo;
DROP TABLE IF EXISTS tes_cuenta_bancaria;
DROP TABLE IF EXISTS tes_moneda;

-- 3. Eliminar Módulo de Recursos Humanos y Accesos (rrhh)
DROP TABLE IF EXISTS rrhh_historial_jefatura;
DROP TABLE IF EXISTS rrhh_administrador;
DROP TABLE IF EXISTS rrhh_empleado;
DROP TABLE IF EXISTS rrhh_usuario;
DROP TABLE IF EXISTS rrhh_rol;
DROP TABLE IF EXISTS rrhh_area;

-- Reactivar la verificación de llaves foráneas
SET FOREIGN_KEY_CHECKS = 1;
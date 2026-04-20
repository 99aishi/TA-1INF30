-- =============================================
-- SCRIPT DE ELIMINACIÓN (DROP)
-- Ordenado de hijos a padres para evitar conflictos de Foreign Keys
-- =============================================

-- 1. Tablas Operativas y de Historial
DROP TABLE CC_HISTORIAL_JEFATURA;
DROP TABLE CC_TRANSACCION;
DROP TABLE CC_COMPROBANTE_PAGO;
DROP TABLE CC_SOLICITUD_GASTO;
DROP TABLE CC_CICLO_CAJA;

-- 2. Especializaciones de Fondo
DROP TABLE CC_ENTREGA_RENDIR;
DROP TABLE CC_CAJA_CHICA;

-- 3. Entidades de Soporte y Fondos
DROP TABLE CC_FONDO;
DROP TABLE CC_RENDICION;
DROP TABLE CC_CUENTA_BANCARIA;

-- 4. Jerarquía de Usuarios
DROP TABLE CC_ADMINISTRADOR;
DROP TABLE CC_EMPLEADO;
DROP TABLE CC_USUARIO;

-- 5. Tablas Maestras (Catálogos)
DROP TABLE CC_ROL;
DROP TABLE CC_AREA;
DROP TABLE CC_RUBRO;
DROP TABLE CC_MONEDA;
SET FOREIGN_KEY_CHECKS = 0;

--- ===============================================================================
-- 1. MÓDULO: rrhh (Recursos Humanos y Accesos)
-- ===============================================================================

CREATE TABLE IF NOT EXISTS rrhh_rol (
    id_rol INT NOT NULL AUTO_INCREMENT,
    titulo_rol VARCHAR(50) NOT NULL,
    descripcion_rol VARCHAR(200),
    
    -- Auditoría
    creado_at DATETIME,
    actualizado_at DATETIME,
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    
    CONSTRAINT pk_rrhh_rol PRIMARY KEY (id_rol)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS rrhh_usuario (
    id_usuario INT NOT NULL AUTO_INCREMENT,
    nombres VARCHAR(60) NOT NULL,
    apellido_paterno VARCHAR(40) NOT NULL,
    apellido_materno VARCHAR(40),
    password_hash VARCHAR(255) NOT NULL,
    esta_activo TINYINT(1) DEFAULT 1,
    
    -- Auditoría
    creado_at DATETIME,
    actualizado_at DATETIME,
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    
    CONSTRAINT pk_rrhh_usuario PRIMARY KEY (id_usuario)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS rrhh_area (
    id_area INT NOT NULL AUTO_INCREMENT,
    nombre_area VARCHAR(60) NOT NULL UNIQUE,
    descripcion_area VARCHAR(200),
    id_jefe INT,
    
    -- Auditoría
    creado_at DATETIME,
    actualizado_at DATETIME,
    id_usuario_creacion INT,
    id_usuario_modificacion INT,

    CONSTRAINT pk_rrhh_area PRIMARY KEY (id_area),
    CONSTRAINT fk_rrhh_area_rrhh_empleado FOREIGN KEY (id_jefe) 
        REFERENCES rrhh_empleado(id_usuario)
        ON DELETE SET NULL 
        ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS rrhh_empleado (
    id_usuario INT NOT NULL,
    correo_institucional VARCHAR(100) NOT NULL,
    numero_celular VARCHAR(15),
    id_area INT,
    id_rol INT,
    id_jefe_directo INT NULL,
    
    -- Auditoría
    creado_at DATETIME,
    actualizado_at DATETIME,
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    
    CONSTRAINT pk_rrhh_empleado PRIMARY KEY (id_usuario),
    CONSTRAINT fk_rrhh_empleado_rrhh_usuario FOREIGN KEY (id_usuario) 
        REFERENCES rrhh_usuario(id_usuario),
    CONSTRAINT fk_rrhh_empleado_rrhh_area FOREIGN KEY (id_area) 
        REFERENCES rrhh_area(id_area),
    CONSTRAINT fk_rrhh_empleado_rrhh_rol FOREIGN KEY (id_rol) 
        REFERENCES rrhh_rol(id_rol),
    CONSTRAINT fk_rrhh_empleado_rrhh_empleado_jefe FOREIGN KEY (id_jefe_directo) 
        REFERENCES rrhh_empleado(id_usuario),
    CONSTRAINT uk_rrhh_empleado_correo UNIQUE (correo_institucional)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS rrhh_administrador (
    id_usuario INT NOT NULL,
    correo_soporte VARCHAR(100),
    
    -- Auditoría
    creado_at DATETIME,
    actualizado_at DATETIME,
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    
    CONSTRAINT pk_rrhh_administrador PRIMARY KEY (id_usuario),
    CONSTRAINT fk_rrhh_administrador_rrhh_usuario FOREIGN KEY (id_usuario) 
        REFERENCES rrhh_usuario(id_usuario)
) ENGINE=InnoDB;

-- ===============================================================================
-- 2. MÓDULO: tes (Tesorería)
-- ===============================================================================

CREATE TABLE IF NOT EXISTS tes_moneda (
    id_moneda INT NOT NULL AUTO_INCREMENT,
    codigo_iso CHAR(3) NOT NULL,
    simbolo VARCHAR(5) NOT NULL,
    
    -- Auditoría
    creado_at DATETIME,
    actualizado_at DATETIME,
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    
    CONSTRAINT pk_tes_moneda PRIMARY KEY (id_moneda)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS tes_cuenta_bancaria (
    id_cuenta_bancaria INT NOT NULL AUTO_INCREMENT,
    nombre_banco VARCHAR(50) NOT NULL,
    numero_cuenta VARCHAR(30) NOT NULL,
    cci CHAR(20),
    id_moneda INT NOT NULL,
    es_principal TINYINT(1) DEFAULT 0,
    id_usuario_titular INT,
    
    -- Auditoría
    creado_at DATETIME,
    actualizado_at DATETIME,
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    
    CONSTRAINT pk_tes_cuenta_bancaria PRIMARY KEY (id_cuenta_bancaria),
    CONSTRAINT fk_tes_cuenta_bancaria_tes_moneda FOREIGN KEY (id_moneda) 
        REFERENCES tes_moneda(id_moneda),
    CONSTRAINT fk_tes_cuenta_bancaria_rrhh_empleado FOREIGN KEY (id_usuario_titular) 
        REFERENCES rrhh_empleado(id_usuario)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS tes_fondo (
    id_fondo INT NOT NULL AUTO_INCREMENT,
    nombre_fondo VARCHAR(100) NOT NULL,
    monto_saldo_actual DECIMAL(12,2) DEFAULT 0.00,
    estado_fondo VARCHAR(20) NOT NULL,
    id_moneda INT NOT NULL,
    id_cuenta_bancaria INT,
    id_usuario_responsable INT,
    
    -- Auditoría
    creado_at DATETIME,
    actualizado_at DATETIME,
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    
    CONSTRAINT pk_tes_fondo PRIMARY KEY (id_fondo),
    CONSTRAINT fk_tes_fondo_tes_moneda FOREIGN KEY (id_moneda) 
        REFERENCES tes_moneda(id_moneda),
    CONSTRAINT fk_tes_fondo_tes_cuenta_bancaria FOREIGN KEY (id_cuenta_bancaria) 
        REFERENCES tes_cuenta_bancaria(id_cuenta_bancaria),
    CONSTRAINT fk_tes_fondo_rrhh_empleado FOREIGN KEY (id_usuario_responsable) 
        REFERENCES rrhh_empleado(id_usuario)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS tes_caja_chica (
    id_fondo INT NOT NULL,
    monto_techo DECIMAL(12,2) NOT NULL,
    id_area INT,
    
    -- Auditoría
    creado_at DATETIME,
    actualizado_at DATETIME,
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    
    CONSTRAINT pk_tes_caja_chica PRIMARY KEY (id_fondo),
    CONSTRAINT fk_tes_caja_chica_tes_fondo FOREIGN KEY (id_fondo) 
        REFERENCES tes_fondo(id_fondo),
    CONSTRAINT fk_tes_caja_chica_rrhh_area FOREIGN KEY (id_area) 
        REFERENCES rrhh_area(id_area)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS tes_entrega_rendir (
    id_fondo INT NOT NULL,
    motivo_entrega VARCHAR(200),
    monto_solicitado DECIMAL(12,2) NOT NULL,
    fecha_solicitud DATE,
    fecha_apertura DATE,
    fecha_cierre DATE,
    estado_entrega VARCHAR(20),
    id_usuario_solicitante INT,
    id_usuario_aprobador INT,
    
    -- Auditoría
    creado_at DATETIME,
    actualizado_at DATETIME,
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    
    CONSTRAINT pk_tes_entrega_rendir PRIMARY KEY (id_fondo),
    CONSTRAINT fk_tes_entrega_rendir_tes_fondo FOREIGN KEY (id_fondo) 
        REFERENCES tes_fondo(id_fondo),
    CONSTRAINT fk_tes_entrega_rendir_rrhh_empleado_sol FOREIGN KEY (id_usuario_solicitante) 
        REFERENCES rrhh_empleado(id_usuario),
    CONSTRAINT fk_tes_entrega_rendir_rrhh_empleado_apr FOREIGN KEY (id_usuario_aprobador) 
        REFERENCES rrhh_empleado(id_usuario)
) ENGINE=InnoDB;

-- ===============================================================================
-- 3. MÓDULO: ope (Operaciones)
-- ===============================================================================

CREATE TABLE IF NOT EXISTS ope_rendicion (
    id_rendicion INT NOT NULL AUTO_INCREMENT,
    fecha_presentacion DATE DEFAULT (CURRENT_DATE),
    fecha_aprobacion DATE NULL,
    monto_total_declarado DECIMAL(12,2) DEFAULT 0.00,
    monto_total_aprobado DECIMAL(12,2) DEFAULT 0.00,
    monto_saldo_final DECIMAL(12,2) DEFAULT 0.00,
    estado_rendicion VARCHAR(20) NOT NULL,
    comentario VARCHAR(500),
    
    -- Auditoría
    creado_at DATETIME,
    actualizado_at DATETIME,
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    
    CONSTRAINT pk_ope_rendicion PRIMARY KEY (id_rendicion)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS ope_ciclo_caja (
    id_ciclo_caja INT NOT NULL AUTO_INCREMENT,
    numero_semana INT,
    fecha_apertura DATE NOT NULL,
    fecha_cierre DATE NULL,
    monto_saldo_inicial DECIMAL(12,2) DEFAULT 0.00,
    monto_total_gastado DECIMAL(12,2) DEFAULT 0.00,
    estado_ciclo VARCHAR(20),
    id_fondo_caja_chica INT NOT NULL,
    id_rendicion INT,
    
    -- Auditoría
    creado_at DATETIME,
    actualizado_at DATETIME,
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    
    CONSTRAINT pk_ope_ciclo_caja PRIMARY KEY (id_ciclo_caja),
    CONSTRAINT fk_ope_ciclo_caja_tes_caja_chica FOREIGN KEY (id_fondo_caja_chica) 
        REFERENCES tes_caja_chica(id_fondo),
    CONSTRAINT fk_ope_ciclo_caja_ope_rendicion FOREIGN KEY (id_rendicion) 
        REFERENCES ope_rendicion(id_rendicion)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS ope_solicitud_gasto (
    id_solicitud_gasto INT NOT NULL AUTO_INCREMENT,
    fecha_solicitud DATE NOT NULL,
    monto_solicitado DECIMAL(12,2) NOT NULL,
    motivo_solicitud VARCHAR(200),
    estado_solicitud VARCHAR(20),
    id_usuario_solicitante INT NOT NULL,
    id_usuario_destinatario INT,
    id_ciclo_caja INT,
    
    -- Auditoría
    creado_at DATETIME,
    actualizado_at DATETIME,
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    
    CONSTRAINT pk_ope_solicitud_gasto PRIMARY KEY (id_solicitud_gasto),
    CONSTRAINT fk_ope_solicitud_gasto_rrhh_empleado_sol FOREIGN KEY (id_usuario_solicitante) 
        REFERENCES rrhh_empleado(id_usuario),
    CONSTRAINT fk_ope_solicitud_gasto_rrhh_empleado_des FOREIGN KEY (id_usuario_destinatario) 
        REFERENCES rrhh_empleado(id_usuario),
    CONSTRAINT fk_ope_solicitud_gasto_ope_ciclo_caja FOREIGN KEY (id_ciclo_caja) 
        REFERENCES ope_ciclo_caja(id_ciclo_caja)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS ope_comprobante_pago (
    id_comprobante INT NOT NULL AUTO_INCREMENT,
    tipo_documento VARCHAR(20) NOT NULL,
    ruc_proveedor CHAR(11),
    razon_social VARCHAR(150),
    numero_serie VARCHAR(30),
    fecha_emision DATE,
    monto_subtotal DECIMAL(12,2),
    monto_igv DECIMAL(12,2),
    monto_total DECIMAL(12,2) NOT NULL,
    id_solicitud_gasto INT,
    id_fondo_entrega INT,
    id_moneda INT,
    
    -- Auditoría
    creado_at DATETIME,
    actualizado_at DATETIME,
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    
    CONSTRAINT pk_ope_comprobante_pago PRIMARY KEY (id_comprobante),
    CONSTRAINT fk_ope_comprobante_pago_ope_solicitud_gasto FOREIGN KEY (id_solicitud_gasto) 
        REFERENCES ope_solicitud_gasto(id_solicitud_gasto),
    CONSTRAINT fk_ope_comprobante_pago_tes_entrega_rendir FOREIGN KEY (id_fondo_entrega) 
        REFERENCES tes_entrega_rendir(id_fondo),
    CONSTRAINT fk_ope_comprobante_pago_tes_moneda FOREIGN KEY (id_moneda) 
        REFERENCES tes_moneda(id_moneda)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS ope_transaccion (
    id_transaccion INT NOT NULL AUTO_INCREMENT,
    tipo_operacion VARCHAR(30) NOT NULL,
    momento_operacion DATETIME, -- Gestionado por el trigger -- Lol
    monto_transaccion DECIMAL(12,2) NOT NULL,
    numero_operacion_bancaria VARCHAR(30),
    medio_pago VARCHAR(30),
    valor_tipo_cambio DECIMAL(10,4),
    id_cuenta_origen INT,
    id_cuenta_destino INT,
    id_moneda INT,
    
    -- Auditoría
    creado_at DATETIME,
    actualizado_at DATETIME,
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    
    CONSTRAINT pk_ope_transaccion PRIMARY KEY (id_transaccion),
    CONSTRAINT fk_ope_transaccion_tes_cuenta_bancaria_ori FOREIGN KEY (id_cuenta_origen) 
        REFERENCES tes_cuenta_bancaria(id_cuenta_bancaria),
    CONSTRAINT fk_ope_transaccion_tes_cuenta_bancaria_des FOREIGN KEY (id_cuenta_destino) 
        REFERENCES tes_cuenta_bancaria(id_cuenta_bancaria),
    CONSTRAINT fk_ope_transaccion_tes_moneda FOREIGN KEY (id_moneda) 
        REFERENCES tes_moneda(id_moneda)
) ENGINE=InnoDB;

SET FOREIGN_KEY_CHECKS = 1;
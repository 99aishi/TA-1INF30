DELIMITER $$

-- =========================================================
-- RRHH_AREA
-- =========================================================

CREATE PROCEDURE sp_insertar_area(
    IN p_nombre_area VARCHAR(60),
    IN p_descripcion_area VARCHAR(200)
)
BEGIN
    IF p_nombre_area IS NULL OR TRIM(p_nombre_area) = '' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El nombre del área es obligatorio';
    END IF;

    INSERT INTO rrhh_area(
        nombre_area,
        descripcion_area
    )
    VALUES(
        TRIM(p_nombre_area),
        TRIM(p_descripcion_area)
    );
END$$

CREATE PROCEDURE sp_modificar_area(
    IN p_id_area INT,
    IN p_nombre_area VARCHAR(60),
    IN p_descripcion_area VARCHAR(200)
)
BEGIN
    IF p_id_area IS NULL OR p_id_area <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ID de área inválido';
    END IF;

    UPDATE rrhh_area
       SET nombre_area = TRIM(p_nombre_area),
           descripcion_area = TRIM(p_descripcion_area),
           id_usuario_modificacion = NULL
     WHERE id_area = p_id_area;
END$$

CREATE PROCEDURE sp_eliminar_area(
    IN p_id_area INT
)
BEGIN
    DELETE FROM rrhh_area
     WHERE id_area = p_id_area;
END$$

CREATE PROCEDURE sp_buscar_area_por_id(
    IN p_id_area INT
)
BEGIN
    SELECT *
      FROM rrhh_area
     WHERE id_area = p_id_area;
END$$

CREATE PROCEDURE sp_listar_areas()
BEGIN
    SELECT *
      FROM rrhh_area
     ORDER BY nombre_area;
END$$

-- =========================================================
-- RRHH_ROL
-- =========================================================

CREATE PROCEDURE sp_insertar_rol(
    IN p_titulo_rol VARCHAR(50),
    IN p_descripcion_rol VARCHAR(200)
)
BEGIN
    IF p_titulo_rol IS NULL OR TRIM(p_titulo_rol) = '' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El título del rol es obligatorio';
    END IF;

    INSERT INTO rrhh_rol(
        titulo_rol,
        descripcion_rol
    )
    VALUES(
        TRIM(p_titulo_rol),
        TRIM(p_descripcion_rol)
    );
END$$

CREATE PROCEDURE sp_modificar_rol(
    IN p_id_rol INT,
    IN p_titulo_rol VARCHAR(50),
    IN p_descripcion_rol VARCHAR(200)
)
BEGIN
    IF p_id_rol IS NULL OR p_id_rol <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ID de rol inválido';
    END IF;

    UPDATE rrhh_rol
       SET titulo_rol = TRIM(p_titulo_rol),
           descripcion_rol = TRIM(p_descripcion_rol),
           id_usuario_modificacion = NULL
     WHERE id_rol = p_id_rol;
END$$

CREATE PROCEDURE sp_eliminar_rol(
    IN p_id_rol INT
)
BEGIN
    DELETE FROM rrhh_rol
     WHERE id_rol = p_id_rol;
END$$

CREATE PROCEDURE sp_buscar_rol_por_id(
    IN p_id_rol INT
)
BEGIN
    SELECT *
      FROM rrhh_rol
     WHERE id_rol = p_id_rol;
END$$

CREATE PROCEDURE sp_listar_roles()
BEGIN
    SELECT *
      FROM rrhh_rol
     ORDER BY titulo_rol;
END$$

-- =========================================================
-- RRHH_USUARIO
-- =========================================================

CREATE PROCEDURE sp_insertar_usuario(
    IN p_nombres VARCHAR(60),
    IN p_apellido_paterno VARCHAR(40),
    IN p_apellido_materno VARCHAR(40),
    IN p_password_hash VARCHAR(255)
)
BEGIN
    IF p_nombres IS NULL OR TRIM(p_nombres) = '' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Los nombres son obligatorios';
    END IF;

    IF p_apellido_paterno IS NULL OR TRIM(p_apellido_paterno) = '' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El apellido paterno es obligatorio';
    END IF;

    IF p_password_hash IS NULL OR TRIM(p_password_hash) = '' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La contraseña hash es obligatoria';
    END IF;

    INSERT INTO rrhh_usuario(
        nombres,
        apellido_paterno,
        apellido_materno,
        password_hash,
        esta_activo
    )
    VALUES(
        TRIM(p_nombres),
        TRIM(p_apellido_paterno),
        TRIM(p_apellido_materno),
        TRIM(p_password_hash),
        1
    );
END$$

CREATE PROCEDURE sp_modificar_usuario(
    IN p_id_usuario INT,
    IN p_nombres VARCHAR(60),
    IN p_apellido_paterno VARCHAR(40),
    IN p_apellido_materno VARCHAR(40),
    IN p_password_hash VARCHAR(255),
    IN p_esta_activo TINYINT
)
BEGIN
    IF p_id_usuario IS NULL OR p_id_usuario <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ID de usuario inválido';
    END IF;

    UPDATE rrhh_usuario
       SET nombres = TRIM(p_nombres),
           apellido_paterno = TRIM(p_apellido_paterno),
           apellido_materno = TRIM(p_apellido_materno),
           password_hash = TRIM(p_password_hash),
           esta_activo = p_esta_activo,
           id_usuario_modificacion = NULL
     WHERE id_usuario = p_id_usuario;
END$$

CREATE PROCEDURE sp_eliminar_usuario(
    IN p_id_usuario INT
)
BEGIN
    UPDATE rrhh_usuario
       SET esta_activo = 0
	WHERE id_usuario = p_id_usuario;
END$$

CREATE PROCEDURE sp_buscar_usuario_por_id(
    IN p_id_usuario INT
)
BEGIN
    SELECT *
      FROM rrhh_usuario
     WHERE id_usuario = p_id_usuario;
END$$

CREATE PROCEDURE sp_listar_usuarios()
BEGIN
    SELECT *
      FROM rrhh_usuario
     ORDER BY id_usuario;
END$$

-- =========================================================
-- RRHH_EMPLEADO
-- =========================================================

CREATE PROCEDURE sp_insertar_empleado(
    IN p_id_usuario INT,
    IN p_correo_institucional VARCHAR(100),
    IN p_numero_celular VARCHAR(15),
    IN p_id_area INT,
    IN p_id_rol INT,
    IN p_id_jefe_directo INT
)
BEGIN
    IF p_id_usuario IS NULL OR p_id_usuario <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ID de usuario inválido para empleado';
    END IF;

    IF p_correo_institucional IS NULL OR TRIM(p_correo_institucional) = '' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El correo institucional es obligatorio';
    END IF;

    INSERT INTO rrhh_empleado(
        id_usuario,
        correo_institucional,
        numero_celular,
        id_area,
        id_rol,
        id_jefe_directo
    )
    VALUES(
        p_id_usuario,
        TRIM(p_correo_institucional),
        TRIM(p_numero_celular),
        p_id_area,
        p_id_rol,
        p_id_jefe_directo
    );
END$$

CREATE PROCEDURE sp_modificar_empleado(
    IN p_id_usuario INT,
    IN p_correo_institucional VARCHAR(100),
    IN p_numero_celular VARCHAR(15),
    IN p_id_area INT,
    IN p_id_rol INT,
    IN p_id_jefe_directo INT
)
BEGIN
    IF p_id_usuario IS NULL OR p_id_usuario <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ID de empleado inválido';
    END IF;

    UPDATE rrhh_empleado
       SET correo_institucional = TRIM(p_correo_institucional),
           numero_celular = TRIM(p_numero_celular),
           id_area = p_id_area,
           id_rol = p_id_rol,
           id_jefe_directo = p_id_jefe_directo,
           id_usuario_modificacion = NULL
     WHERE id_usuario = p_id_usuario;
END$$

CREATE PROCEDURE sp_eliminar_empleado(
    IN p_id_usuario INT
)
BEGIN
    DELETE FROM rrhh_empleado
     WHERE id_usuario = p_id_usuario;
END$$

CREATE PROCEDURE sp_buscar_empleado_por_id(
    IN p_id_usuario INT
)
BEGIN
    SELECT
        u.id_usuario,
        u.nombres,
        u.apellido_paterno,
        u.apellido_materno,
        u.esta_activo,
        e.correo_institucional,
        e.numero_celular,
        e.id_area,
        a.nombre_area,
        e.id_rol,
        r.titulo_rol,
        e.id_jefe_directo,
        CONCAT(ju.nombres, ' ', ju.apellido_paterno, ' ', IFNULL(ju.apellido_materno, '')) AS jefe_directo,
        e.creado_at,
        e.actualizado_at,
        e.id_usuario_creacion,
        e.id_usuario_modificacion
    FROM rrhh_empleado e
    INNER JOIN rrhh_usuario u ON e.id_usuario = u.id_usuario
    LEFT JOIN rrhh_area a ON e.id_area = a.id_area
    LEFT JOIN rrhh_rol r ON e.id_rol = r.id_rol
    LEFT JOIN rrhh_usuario ju ON e.id_jefe_directo = ju.id_usuario
    WHERE e.id_usuario = p_id_usuario;
END$$

CREATE PROCEDURE sp_listar_empleados()
BEGIN
    SELECT
        u.id_usuario,
        u.nombres,
        u.apellido_paterno,
        u.apellido_materno,
        u.esta_activo,
        e.correo_institucional,
        e.numero_celular,
        e.id_area,
        a.nombre_area,
        e.id_rol,
        r.titulo_rol,
        e.id_jefe_directo,
        CONCAT(ju.nombres, ' ', ju.apellido_paterno, ' ', IFNULL(ju.apellido_materno, '')) AS jefe_directo
    FROM rrhh_empleado e
    INNER JOIN rrhh_usuario u ON e.id_usuario = u.id_usuario
    LEFT JOIN rrhh_area a ON e.id_area = a.id_area
    LEFT JOIN rrhh_rol r ON e.id_rol = r.id_rol
    LEFT JOIN rrhh_usuario ju ON e.id_jefe_directo = ju.id_usuario
    ORDER BY u.apellido_paterno, u.apellido_materno, u.nombres;
END$$

-- =========================================================
-- RRHH_ADMINISTRADOR
-- =========================================================

CREATE PROCEDURE sp_insertar_administrador(
    IN p_id_usuario INT,
    IN p_correo_soporte VARCHAR(100)
)
BEGIN
    IF p_id_usuario IS NULL OR p_id_usuario <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ID de usuario inválido para administrador';
    END IF;

    INSERT INTO rrhh_administrador(
        id_usuario,
        correo_soporte
    )
    VALUES(
        p_id_usuario,
        TRIM(p_correo_soporte)
    );
END$$

CREATE PROCEDURE sp_modificar_administrador(
    IN p_id_usuario INT,
    IN p_correo_soporte VARCHAR(100)
)
BEGIN
    UPDATE rrhh_administrador
       SET correo_soporte = TRIM(p_correo_soporte),
           id_usuario_modificacion = NULL
     WHERE id_usuario = p_id_usuario;
END$$

CREATE PROCEDURE sp_eliminar_administrador(
    IN p_id_usuario INT
)
BEGIN
    DELETE FROM rrhh_administrador
     WHERE id_usuario = p_id_usuario;
END$$

CREATE PROCEDURE sp_buscar_administrador_por_id(
    IN p_id_usuario INT
)
BEGIN
    SELECT
        a.id_usuario,
        u.nombres,
        u.apellido_paterno,
        u.apellido_materno,
        u.esta_activo,
        a.correo_soporte
    FROM rrhh_administrador a
    INNER JOIN rrhh_usuario u ON a.id_usuario = u.id_usuario
    WHERE a.id_usuario = p_id_usuario;
END$$

CREATE PROCEDURE sp_listar_administradores()
BEGIN
    SELECT
        a.id_usuario,
        u.nombres,
        u.apellido_paterno,
        u.apellido_materno,
        u.esta_activo,
        a.correo_soporte
    FROM rrhh_administrador a
    INNER JOIN rrhh_usuario u ON a.id_usuario = u.id_usuario
    ORDER BY u.apellido_paterno, u.apellido_materno, u.nombres;
END$$

-- =========================================================
-- RRHH_HISTORIAL_JEFATURA
-- =========================================================

CREATE PROCEDURE sp_insertar_historial_jefatura(
    IN p_id_empleado INT,
    IN p_id_jefe INT,
    IN p_fecha_inicio DATE,
    IN p_fecha_fin DATE
)
BEGIN
    INSERT INTO rrhh_historial_jefatura(
        id_empleado,
        id_jefe,
        fecha_inicio,
        fecha_fin
    )
    VALUES(
        p_id_empleado,
        p_id_jefe,
        p_fecha_inicio,
        p_fecha_fin
    );
END$$

CREATE PROCEDURE sp_modificar_historial_jefatura(
    IN p_id_historial INT,
    IN p_id_empleado INT,
    IN p_id_jefe INT,
    IN p_fecha_inicio DATE,
    IN p_fecha_fin DATE
)
BEGIN
    UPDATE rrhh_historial_jefatura
       SET id_empleado = p_id_empleado,
           id_jefe = p_id_jefe,
           fecha_inicio = p_fecha_inicio,
           fecha_fin = p_fecha_fin,
           id_usuario_modificacion = NULL
     WHERE id_historial = p_id_historial;
END$$

CREATE PROCEDURE sp_eliminar_historial_jefatura(
    IN p_id_historial INT
)
BEGIN
    DELETE FROM rrhh_historial_jefatura
     WHERE id_historial = p_id_historial;
END$$

CREATE PROCEDURE sp_buscar_historial_jefatura_por_id(
    IN p_id_historial INT
)
BEGIN
    SELECT
        h.id_historial,
        h.id_empleado,
        CONCAT(ue.nombres, ' ', ue.apellido_paterno, ' ', IFNULL(ue.apellido_materno, '')) AS empleado,
        h.id_jefe,
        CONCAT(uj.nombres, ' ', uj.apellido_paterno, ' ', IFNULL(uj.apellido_materno, '')) AS jefe,
        h.fecha_inicio,
        h.fecha_fin,
        h.creado_at,
        h.actualizado_at
    FROM rrhh_historial_jefatura h
    INNER JOIN rrhh_usuario ue ON h.id_empleado = ue.id_usuario
    LEFT JOIN rrhh_usuario uj ON h.id_jefe = uj.id_usuario
    WHERE h.id_historial = p_id_historial;
END$$

CREATE PROCEDURE sp_listar_historial_jefatura()
BEGIN
    SELECT
        h.id_historial,
        h.id_empleado,
        CONCAT(ue.nombres, ' ', ue.apellido_paterno, ' ', IFNULL(ue.apellido_materno, '')) AS empleado,
        h.id_jefe,
        CONCAT(uj.nombres, ' ', uj.apellido_paterno, ' ', IFNULL(uj.apellido_materno, '')) AS jefe,
        h.fecha_inicio,
        h.fecha_fin
    FROM rrhh_historial_jefatura h
    INNER JOIN rrhh_usuario ue ON h.id_empleado = ue.id_usuario
    LEFT JOIN rrhh_usuario uj ON h.id_jefe = uj.id_usuario
    ORDER BY h.id_historial;
END$$

DELIMITER ;

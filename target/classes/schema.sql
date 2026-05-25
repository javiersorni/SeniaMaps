DROP DATABASE IF EXISTS Test;
CREATE DATABASE IF NOT EXISTS Test;
USE Test;

-- =====================
-- USUARIO
-- =====================
CREATE TABLE usuario (
    idUsuario BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    rol VARCHAR(255) NOT NULL
);

-- =====================
-- BUSQUEDA
-- =====================
CREATE TABLE busqueda (
    idBusqueda BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    fechaBusqueda DATETIME DEFAULT CURRENT_TIMESTAMP,
    query VARCHAR(255) NOT NULL,
    longitud DOUBLE,
    latitud DOUBLE,

    idUsuario BIGINT UNSIGNED,

    CONSTRAINT fk_busqueda_usuario
        FOREIGN KEY (idUsuario)
        REFERENCES usuario(idUsuario)
        ON DELETE CASCADE
);

-- =====================
-- RESULTADO
-- =====================
CREATE TABLE resultado (
    idResultado BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    idLugar VARCHAR(255),
    nombre VARCHAR(255),
    direccion VARCHAR(255),
    rating DOUBLE,
    latitud DOUBLE,
    longitud DOUBLE
);

-- =====================
-- BUSQUEDA - RESULTADO (CORREGIDO)
-- =====================
CREATE TABLE resultado_busqueda (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,

    idBusqueda BIGINT UNSIGNED NOT NULL,
    idResultado BIGINT UNSIGNED NOT NULL,
    fechaConsulta DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_rb_busqueda
        FOREIGN KEY (idBusqueda)
        REFERENCES busqueda(idBusqueda)
        ON DELETE CASCADE,

    CONSTRAINT fk_rb_resultado
        FOREIGN KEY (idResultado)
        REFERENCES resultado(idResultado)
        ON DELETE CASCADE
);

-- =====================
-- CATEGORIA
-- =====================
CREATE TABLE categoria (
    idCategoria BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nombreCategoria VARCHAR(255) NOT NULL UNIQUE
);

-- =====================
-- RESULTADO - CATEGORIA
-- =====================
CREATE TABLE resultado_categoria (
    idResultado BIGINT UNSIGNED NOT NULL,
    idCategoria BIGINT UNSIGNED NOT NULL,

    PRIMARY KEY (idResultado, idCategoria),

    FOREIGN KEY (idResultado)
        REFERENCES resultado(idResultado)
        ON DELETE CASCADE,

    FOREIGN KEY (idCategoria)
        REFERENCES categoria(idCategoria)
        ON DELETE CASCADE
);

-- =====================
-- FAVORITOS
-- =====================
CREATE TABLE favoritos (
    idFavorito BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,

    idUsuario BIGINT UNSIGNED NOT NULL,
    idResultado BIGINT UNSIGNED NOT NULL,

    FOREIGN KEY (idUsuario)
        REFERENCES usuario(idUsuario)
        ON DELETE CASCADE,

    FOREIGN KEY (idResultado)
        REFERENCES resultado(idResultado)
        ON DELETE CASCADE
);

-- =====================
-- ETIQUETAS
-- =====================
CREATE TABLE etiqueta (
    idEtiqueta BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,

    nombreEtiqueta VARCHAR(255) NOT NULL,

    idUsuario BIGINT UNSIGNED NOT NULL,
    idResultado BIGINT UNSIGNED NOT NULL,

    FOREIGN KEY (idUsuario)
        REFERENCES usuario(idUsuario)
        ON DELETE CASCADE,

    FOREIGN KEY (idResultado)
        REFERENCES resultado(idResultado)
        ON DELETE CASCADE,
    CONSTRAINT uq_usuario_resultado_etiqueta UNIQUE (idUsuario, idResultado, nombreEtiqueta)
);

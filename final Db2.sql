drop database if exists finalproject;
create database finalproject;
use FinalProject;

drop table if exists fabricante;
CREATE TABLE fabricante (
    id_fabricante INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(30) NOT NULL,
    PRIMARY KEY (id_fabricante)
);

drop table if exists producto;
CREATE TABLE producto (
    id_producto INT NOT NULL AUTO_INCREMENT,
    precio DOUBLE NOT NULL,
    nombre VARCHAR(20) NOT NULL,
    id_fabricante INT UNIQUE,
    PRIMARY KEY (id_producto),
    FOREIGN KEY (id_fabricante)
        REFERENCES fabricante (id_fabricante)
);

drop table if exists empresa;
CREATE TABLE empresa (
    id_empresa INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(20) NOT NULL,
    id_producto INT,
    PRIMARY KEY (id_empresa),
    CONSTRAINT fk_producto FOREIGN KEY (id_producto)
        REFERENCES producto (id_producto)
);

drop table if exists cliente;
CREATE TABLE cliente (
    id_cliente INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(30) NOT NULL,
    tel VARCHAR(9),
    PRIMARY KEY (id_cliente)
);
CREATE TABLE cliente_empresa (
    id_cliente INT,
    id_empresa INT,
    FOREIGN KEY (id_cliente)
        REFERENCES cliente (id_cliente),
    FOREIGN KEY (id_empresa)
        REFERENCES empresa (id_empresa),
    PRIMARY KEY (id_cliente , id_empresa)
);



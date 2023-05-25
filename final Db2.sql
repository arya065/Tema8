drop database if exists `FinalProject`;
create database `FinalProject`;
use FinalProject;

drop table if exists `Fabricante`;
CREATE TABLE Fabricante (
    `idFabricante` INT NOT NULL AUTO_INCREMENT,
    `nombre` VARCHAR(30) NOT NULL,
    PRIMARY KEY (`idFabricante`)
);

drop table if exists `Producto`;
CREATE TABLE `Producto` (
    `idProducto` INT NOT NULL AUTO_INCREMENT,
    `precio` DOUBLE NOT NULL,
    `nombre` VARCHAR(20) NOT NULL,
    `idFabricante` INT NOT NULL,
    PRIMARY KEY (`idProducto`),
    CONSTRAINT `fkFabricante` FOREIGN KEY (`idFabricante`)
        REFERENCES `Fabricante` (`idFabricante`)
);

drop table if exists `Empresa`;
CREATE TABLE `Empresa` (
    `idEmpresa` INT NOT NULL AUTO_INCREMENT,
    `nombre` VARCHAR(20) NOT NULL,
    -- `idCliente` INT NOT NULL,
    `idProducto` INT NOT NULL,
    PRIMARY KEY (`idEmpresa`),
    CONSTRAINT `fkProducto` FOREIGN KEY (`idProducto`)
        REFERENCES `Producto` (`idProducto`)
    -- CONSTRAINT `fkCliente` FOREIGN KEY (`idCliente`)
--         REFERENCES `Cliente` (`idCliente`)
);

drop table if exists `Cliente`;
CREATE TABLE `Cliente` (
    `idCliente` INT NOT NULL AUTO_INCREMENT,
    `nombre` VARCHAR(30) NOT NULL,
    `tel` VARCHAR(9),
    `idEmpresa` INT NOT NULL,
    PRIMARY KEY (`idCliente`)
);
CREATE TABLE `Cliente_Empresa` (
    `idCliente` INT,
    `idEmpresa` INT,
    FOREIGN KEY (`idCliente`)
        REFERENCES Cliente (`idCliente`),
    FOREIGN KEY (`idEmpresa`)
        REFERENCES Empresa (`idEmpresa`),
    PRIMARY KEY (`idCliente` , `idEmpresa`)
);
DROP TABLE IF EXISTS Preinscripcions;
DROP TABLE IF EXISTS Centre_Estudis;
DROP TABLE IF EXISTS Alumnes;
DROP TABLE IF EXISTS Centres;
DROP TABLE IF EXISTS Estudis;


CREATE TABLE Centres (
    codi_centre VARCHAR(10) PRIMARY KEY,
    nom_centre VARCHAR(100) NOT NULL
);


CREATE TABLE Estudis (
    codi_estudi VARCHAR(10) PRIMARY KEY,
    nom_estudi VARCHAR(100) NOT NULL
);


CREATE TABLE Centre_Estudis (
    codi_centre VARCHAR(10),
    codi_estudi VARCHAR(10),
    PRIMARY KEY (codi_centre, codi_estudi),
    FOREIGN KEY (codi_centre) REFERENCES Centres(codi_centre),
    FOREIGN KEY (codi_estudi) REFERENCES Estudis(codi_estudi)
);


CREATE TABLE Alumnes (
    dni VARCHAR(20) PRIMARY KEY,
    nom_alumne VARCHAR(100) NOT NULL
);


CREATE TABLE Preinscripcions (
    codi_estudi VARCHAR(10),
    codi_centre VARCHAR(10),
    dni VARCHAR(20),
    curs VARCHAR(20) NOT NULL,
    prioritat INT NOT NULL CHECK (prioritat BETWEEN 1 AND 3),
    PRIMARY KEY (codi_estudi, codi_centre, dni),
    FOREIGN KEY (codi_estudi) REFERENCES Estudis(codi_estudi),
    FOREIGN KEY (codi_centre) REFERENCES Centres(codi_centre),
    FOREIGN KEY (dni) REFERENCES Alumnes(dni),
    FOREIGN KEY (codi_centre, codi_estudi) REFERENCES Centre_Estudis(codi_centre, codi_estudi)
);


CREATE UNIQUE INDEX idx_preinscripcio_unica 
ON Preinscripcions(dni, codi_centre, codi_estudi);
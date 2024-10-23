-- Reconstruction de la base de données
DROP DATABASE IF EXISTS Business;

CREATE DATABASE Business;

USE Business;
-- Encodage de caractères
SET NAMES 'utf8mb4'; 

-- Construction de la table des catégories
CREATE TABLE T_Categories (
    IdCategory     INT PRIMARY KEY AUTO_INCREMENT,
    CategoryName   VARCHAR(30) NOT NULL
) ENGINE = InnoDB; -- utilise le moteur de stockage InnoDB 

-- Construction de la table des formations
CREATE TABLE T_Formations (
    IdFormation    INT PRIMARY KEY AUTO_INCREMENT,
    Name            VARCHAR(100) NOT NULL,
    Description    TEXT NOT NULL,
    DurationDays     INT NOT NULL,
    FaceToFace     BOOLEAN NOT NULL, -- Indique si la formation est P = (TRUE) ou D = (FALSE)
    Price          DECIMAL(10, 2) NOT NULL, -- DECIMAL 10 chiffres et 2 chiffres apres la virgule
    IdCategory     INT,
    FOREIGN KEY (IdCategory) REFERENCES T_Categories(IdCategory)
) ENGINE = InnoDB;

-- Insertion des catégories dans la table T_Categories
INSERT INTO T_Categories (CategoryName) VALUES ('Développement Web');
INSERT INTO T_Categories (CategoryName) VALUES ('Data Science');
INSERT INTO T_Categories (CategoryName) VALUES ('Marketing Digital');
INSERT INTO T_Categories (CategoryName) VALUES ('Design UX/UI');
INSERT INTO T_Categories (CategoryName) VALUES ('Design Web');

-- Insertion des formations dans la table T_Formations
INSERT INTO T_Formations (Name, Description, DurationDays,  FaceToFace , Price, IdCategory) 
VALUES ('Formation Java', 'Java SE 8 : Syntaxe & Poo', 5, TRUE, 20, 1); 
INSERT INTO T_Formations (Name, Description, DurationDays,  FaceToFace, Price, IdCategory) 
VALUES ('Formation Java avancé', 'Exceptions, fichiers, Jdbc, thread...', 10, FALSE, 20, 2); 
INSERT INTO T_Formations (Name, Description, DurationDays,  FaceToFace , Price, IdCategory) 
VALUES ('Formation Spring', 'Spring Core/Mvc/Security', 3, TRUE, 20, 3); 
INSERT INTO T_Formations (Name, Description, DurationDays,  FaceToFace , Price, IdCategory) 
VALUES ('Formation Php frameworks', 'Symphony', 7, FALSE, 15, 4); 
INSERT INTO T_Formations (Name, Description, DurationDays,  FaceToFace , Price, IdCategory) 
VALUES ('Formation C#', 'DotNet Core', 7, TRUE, 20, 5);

-- Sélection des formations par catégorie
SELECT F.IdFormation, F.Name, F.Description, F.DurationDays, F.FaceToFace, F.Price, C.CategoryName 
FROM T_Formations F 
JOIN T_Categories C 
ON F.IdCategory = C.IdCategory; -- jointure entre T_Formations et T_Categories 


-- Sélection formations contenant un mot clé dans la description (Spring)
SELECT * FROM T_Formations WHERE Description LIKE '%Spring%';

-- Sélection des formations en présentiel ou distanciel
SELECT * FROM T_Formations WHERE FaceToFace = TRUE; -- Présentiel
SELECT * FROM T_Formations WHERE FaceToFace = FALSE; -- Distanciel

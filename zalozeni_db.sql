# DROP DATABASE pexeso;

CREATE DATABASE pexeso
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_czech_ci;

USE pexeso;

CREATE TABLE gameboards (
  id INT PRIMARY KEY AUTO_INCREMENT,
  status VARCHAR(250),
  lastTurnStamp TIMESTAMP DEFAULT now() NOT NULL
);

CREATE TABLE cards (
  id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  gameboardId INT NOT NULL,
  cardNumber INT DEFAULT 0 NOT NULL,
  status VARCHAR(250) ,
  cardOrder INT NOT NULL DEFAULT 0,
  CONSTRAINT gameboard_FK FOREIGN KEY (gameboardId) REFERENCES gameboards(id)
);

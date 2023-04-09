CREATE DATABASE
    IF NOT EXISTS `db_test` CHARACTER SET utf8 COLLATE utf8_general_ci;

USE db_test;

CREATE TABLE
    IF NOT EXISTS `user` (
        id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
        firstname VARCHAR(255),
        lastname VARCHAR(100),
        username VARCHAR(255) UNIQUE,
        email VARCHAR(255) UNIQUE,
        phone INT UNIQUE,
        password VARCHAR(255),
        biography VARCHAR(255),
        address VARCHAR(255),
        additional_address VARCHAR(255),
        city VARCHAR(255),
        zip_code INT(5),
        role VARCHAR(255),
        avatar VARCHAR(255),
        status TINYINT,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    ) ENGINE = InnoDB;

CREATE TABLE
    IF NOT EXISTS `ad` (
        id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
        title VARCHAR(255),
        content VARCHAR(100),
        price FLOAT,
        status TINYINT,
        id_user INT,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        FOREIGN KEY (id_user) REFERENCES user(id) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE = InnoDB;

CREATE TABLE
    IF NOT EXISTS `image` (
        id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
        url VARCHAR(255) UNIQUE,
        id_ad INT,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        FOREIGN KEY (id_ad) REFERENCES ad(id) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE = InnoDB;

CREATE TABLE
    IF NOT EXISTS `offer` (
        id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
        amount FLOAT,
        status TINYINT,
        id_user INT,
        id_ad INT,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        FOREIGN KEY (id_user) REFERENCES user(id) ON DELETE CASCADE ON UPDATE CASCADE,
        FOREIGN KEY (id_ad) REFERENCES ad(id) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE = InnoDB;

CREATE TABLE
    IF NOT EXISTS `rate` (
        id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
        rate_value INT(1),
        comment VARCHAR(255),
        status TINYINT,
        id_user INT,
        id_ad INT,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        FOREIGN KEY (id_user) REFERENCES user(id) ON DELETE CASCADE ON UPDATE CASCADE,
        FOREIGN KEY (id_ad) REFERENCES ad(id) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE = InnoDB;

CREATE TABLE
    IF NOT EXISTS `category` (
        id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
        title VARCHAR(255) UNIQUE,
        id_parent INT,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    ) ENGINE = InnoDB;

CREATE TABLE
    IF NOT EXISTS `ad_category` (
        id_ad INT,
        id_category INT,
        PRIMARY KEY (id_ad, id_category),
        FOREIGN KEY (id_category) REFERENCES category(id) ON DELETE CASCADE ON UPDATE CASCADE,
        FOREIGN KEY (id_ad) REFERENCES ad(id) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE = InnoDB;

CREATE TABLE
    IF NOT EXISTS `orders` (
        id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
        delivery_address VARCHAR(255),
        additional_delivery_address VARCHAR(255),
        city VARCHAR(255),
        zip_code INT(5),
        amount FLOAT,
        status TINYINT,
        id_user INT,
        id_ad INT,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        FOREIGN KEY (id_user) REFERENCES user(id),
        FOREIGN KEY (id_ad) REFERENCES ad(id)
    ) ENGINE = InnoDB;
-- Create Database
CREATE DATABASE IF NOT EXISTS game_system;
USE game_system;

-- =========================
-- Table: game_rounds
-- =========================
CREATE TABLE game_rounds (
    round_id INT NOT NULL AUTO_INCREMENT,
    game_type VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (round_id)
);

INSERT INTO game_rounds VALUES
(1,'Snake','2026-04-18 07:55:11'),
(2,'Snake','2026-04-18 07:56:30'),
(3,'Snake','2026-04-18 07:57:06'),
(4,'Snake','2026-04-18 07:57:46'),
(5,'Snake','2026-04-18 08:04:34'),
(6,'Snake','2026-04-18 08:09:57'),
(7,'Snake','2026-04-18 08:11:01'),
(8,'Snake','2026-04-18 08:11:21'),
(9,'Snake','2026-04-18 08:11:29'),
(10,'Snake','2026-04-18 08:15:20');

-- =========================
-- Table: players
-- =========================
CREATE TABLE players (
    player_id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    PRIMARY KEY (player_id)
);

INSERT INTO players VALUES
(1,'Dilshan'),
(2,'Shehan Oerera'),
(3,'Dilshan Gunawardena'),
(4,'Sajith'),
(5,'Shehan'),
(6,'Tharindu'),
(7,'Anura'),
(8,'Dislahan'),
(9,'Senura');

-- =========================
-- Table: algorithm_times
-- =========================
CREATE TABLE algorithm_times (
    time_id INT NOT NULL AUTO_INCREMENT,
    round_id INT,
    algorithm_name VARCHAR(100),
    time_taken BIGINT,
    PRIMARY KEY (time_id),
    KEY (round_id),
    CONSTRAINT algorithm_times_ibfk_1 
        FOREIGN KEY (round_id) REFERENCES game_rounds(round_id)
);

INSERT INTO algorithm_times VALUES
(1,1,'BFS',220700),
(2,1,'Dijkstra',1664000),
(3,2,'BFS',268000),
(4,2,'Dijkstra',1660100),
(5,3,'BFS',317000),
(6,3,'Dijkstra',1586400);

-- =========================
-- Table: player_answers
-- =========================
CREATE TABLE player_answers (
    answer_id INT NOT NULL AUTO_INCREMENT,
    player_id INT,
    round_id INT,
    answer INT,
    is_correct TINYINT(1),
    PRIMARY KEY (answer_id),
    KEY (player_id),
    KEY (round_id),
    CONSTRAINT player_answers_ibfk_1 
        FOREIGN KEY (player_id) REFERENCES players(player_id),
    CONSTRAINT player_answers_ibfk_2 
        FOREIGN KEY (round_id) REFERENCES game_rounds(round_id)
);

INSERT INTO player_answers VALUES
(1,1,3,12,0),
(2,1,3,15,0),
(3,2,7,5,0),
(4,2,8,6,0),
(5,3,10,11,1);

-- =========================
-- Table: solutions
-- =========================
CREATE TABLE solutions (
    solution_id INT NOT NULL AUTO_INCREMENT,
    round_id INT,
    correct_answer INT,
    PRIMARY KEY (solution_id),
    KEY (round_id),
    CONSTRAINT solutions_ibfk_1 
        FOREIGN KEY (round_id) REFERENCES game_rounds(round_id)
);

INSERT INTO solutions VALUES
(1,1,3),
(2,2,3),
(3,3,5),
(4,4,9),
(5,5,3);

-- =========================
-- Table: unique_solutions
-- =========================
CREATE TABLE unique_solutions (
    id INT NOT NULL AUTO_INCREMENT,
    game_type VARCHAR(50),
    solution_text TEXT,
    is_found TINYINT(1) DEFAULT 1,
    PRIMARY KEY (id)
);
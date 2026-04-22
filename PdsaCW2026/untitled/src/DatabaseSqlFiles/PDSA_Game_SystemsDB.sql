CREATE DATABASE game_system;
USE game_system;

CREATE TABLE players (
    player_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE game_rounds (
    round_id INT AUTO_INCREMENT PRIMARY KEY,
    game_type VARCHAR(50), 
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE algorithm_times (
    time_id INT AUTO_INCREMENT PRIMARY KEY,
    round_id INT,
    algorithm_name VARCHAR(100),
    time_taken BIGINT,
    FOREIGN KEY (round_id) REFERENCES game_rounds(round_id)
);

CREATE TABLE solutions (
    solution_id INT AUTO_INCREMENT PRIMARY KEY,
    round_id INT,
    correct_answer INT,
    FOREIGN KEY (round_id) REFERENCES game_rounds(round_id)
);

CREATE TABLE player_answers (
    answer_id INT AUTO_INCREMENT PRIMARY KEY,
    player_id INT,
    round_id INT,
    answer INT,
    is_correct BOOLEAN,
    FOREIGN KEY (player_id) REFERENCES players(player_id),
    FOREIGN KEY (round_id) REFERENCES game_rounds(round_id)
);

CREATE TABLE unique_solutions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    game_type VARCHAR(50),
    solution_text TEXT,
    is_found BOOLEAN DEFAULT TRUE
);
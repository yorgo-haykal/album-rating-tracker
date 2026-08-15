CREATE TABLE app_user (
                          id BIGSERIAL PRIMARY KEY,
                          username VARCHAR(50) NOT NULL UNIQUE,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          password_hash VARCHAR(255) NOT NULL,
                          created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE scoring_weights (
                                 id BIGSERIAL PRIMARY KEY,
                                 user_id BIGINT NOT NULL UNIQUE REFERENCES app_user(id),
                                 songwriting_weight DECIMAL(4,2) NOT NULL,
                                 production_weight DECIMAL(4,2) NOT NULL,
                                 cohesion_weight DECIMAL(4,2) NOT NULL,
                                 tracklist_weight DECIMAL(4,2) NOT NULL,
                                 replay_value_weight DECIMAL(4,2) NOT NULL,
                                 emotional_impact_weight DECIMAL(4,2) NOT NULL
);

CREATE TABLE album (
                       id BIGSERIAL PRIMARY KEY,
                       user_id BIGINT NOT NULL REFERENCES app_user(id),
                       title VARCHAR(255) NOT NULL,
                       artist VARCHAR(255) NOT NULL,
                       genre VARCHAR(100),
                       release_year INT,
                       date_added TIMESTAMP NOT NULL DEFAULT now(),
                       songwriting_score DECIMAL(3,1) NOT NULL,
                       production_score DECIMAL(3,1) NOT NULL,
                       cohesion_score DECIMAL(3,1) NOT NULL,
                       tracklist_score DECIMAL(3,1) NOT NULL,
                       replay_value_score DECIMAL(3,1) NOT NULL,
                       emotional_impact_score DECIMAL(3,1) NOT NULL
);
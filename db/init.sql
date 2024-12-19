-- Active: 1734357443174@@127.0.0.1@3306
-- Create the database if it doesn't exist
CREATE DATABASE IF NOT EXISTS lunaperiodtracker;

-- Switch to the database
USE lunaperiodtracker;

-- Drop existing tables if they exist
DROP TABLE IF EXISTS PregnancyChances;
DROP TABLE IF EXISTS Predictions;
DROP TABLE IF EXISTS PeriodEntries;

-- Create Users table
CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    cycle_length INT CHECK (cycle_length > 0), 
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Table for storing period entries
CREATE TABLE PeriodEntries (
    entry_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL, -- Foreign key to Users
    period_date DATE NOT NULL, -- Date of the period
    mood VARCHAR(50), -- User's mood
    symptoms VARCHAR(255), -- Symptoms logged
    digestion VARCHAR(50), -- Digestion status
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Table for storing predictions
-- Create Predictions table
CREATE TABLE Predictions (
    prediction_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    prediction_date DATE NOT NULL,
    predicted_period DATE NOT NULL,
    predicted_fertile_window_start DATE,
    predicted_fertile_window_end DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- Create PregnancyChances table
CREATE TABLE PregnancyChances (
    chance_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    checked_date DATE NOT NULL,
    prediction_date DATE NOT NULL,
    chance_level ENUM('Low', 'High') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- Insert sample data into Users table
INSERT INTO Users (user_name, cycle_length)
VALUES
    ('Renismae', 28),
    ('Chelsie', 30);

-- Insert sample data into PeriodEntries table
INSERT INTO PeriodEntries (user_id, period_date, mood, symptoms, digestion)
VALUES
    (1, '2024-11-01', 'happy', 'everything is fine', 'everything is fine'),
    (1, '2024-11-29', 'low energy', 'cramps, headache', 'bloating'),
    (2, '2024-11-05', 'mood swings', 'fatigue, acne', 'nausea');

-- Insert sample data into Predictions table
INSERT INTO Predictions (user_id, prediction_date, predicted_period, predicted_fertile_window_start, predicted_fertile_window_end) 
VALUES 
    (1, '2024-12-01', '2024-12-27', '2024-12-13', '2024-12-20'),
    (3, '2024-12-02', '2024-12-25', '2024-12-11', '2024-12-18');

-- Sample data for PregnancyChances
INSERT INTO PregnancyChances (user_id, checked_date, prediction_date, chance_level) 
VALUES 
    (1, '2024-12-15', '2024-12-01', 'High'),
    (2, '2024-12-16', '2024-12-02', 'Low');

-- Verify data
SELECT * FROM Users;
SELECT * FROM PeriodEntries;
SELECT * FROM Predictions;
SELECT * FROM PregnancyChances;

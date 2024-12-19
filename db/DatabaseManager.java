import java.sql.*;
import java.time.LocalDate;

class DatabaseManager {
    private Connection connection;

    public DatabaseManager(String url, String user, String password) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver not found: " + e.getMessage());
            throw new SQLException("Failed to load MySQL driver", e);
        }
    }

    public Connection getConnection() {
        return connection;
    }    

    public void createTables() throws SQLException {
        String createUserTable = "CREATE TABLE IF NOT EXISTS users (" +
                                 "id INT AUTO_INCREMENT PRIMARY KEY, " +
                                 "name VARCHAR(50), " +
                                 "cycle_length INT)";
        String createEntryTable = "CREATE TABLE IF NOT EXISTS period_entries (" +
                                  "id INT AUTO_INCREMENT PRIMARY KEY, " +
                                  "user_id INT, " +
                                  "period_date DATE, " +
                                  "mood VARCHAR(50), " +
                                  "symptoms VARCHAR(255), " +
                                  "digestion VARCHAR(50), " +
                                  "FOREIGN KEY (user_id) REFERENCES users(user_id))";
        try (Statement statement = connection.createStatement()) {
            statement.execute(createUserTable);
            statement.execute(createEntryTable);
        }
    }

    public int saveUser(String name, int cycleLength) throws SQLException {
        String query = "INSERT INTO users (user_name, cycle_length) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.setInt(2, cycleLength);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public void savePeriodEntry(int userId, LocalDate date, String mood, String symptoms, String digestion) throws SQLException {
        String query = "INSERT INTO period_entries (user_id, period_date, mood, symptoms, digestion) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setDate(2, Date.valueOf(date));
            statement.setString(3, mood);
            statement.setString(4, symptoms);
            statement.setString(5, digestion);
            statement.executeUpdate();
        }
    }

    public LocalDate getLastPeriodDate(int userId) throws SQLException {
        String query = "SELECT MAX(period_date) AS last_date FROM period_entries WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Date lastDate = resultSet.getDate("last_date");
                    if (lastDate != null) {
                        return lastDate.toLocalDate(); 
                    }
                }
            }
        }
        return null;
    }    
    
    public void displayPeriodEntries(int userId) throws SQLException {
        String query = "SELECT period_date, mood, symptoms, digestion FROM period_entries WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    System.out.println("Date: " + resultSet.getDate("period_date"));
                    System.out.println("Mood: " + resultSet.getString("mood"));
                    System.out.println("Symptoms: " + resultSet.getString("symptoms"));
                    System.out.println("Digestion: " + resultSet.getString("digestion"));
                    System.out.println("----------------------------");
                }
            }
        }
    }

    public void savePrediction(int userId, LocalDate predictionDate, LocalDate predictedPeriod, 
                           LocalDate fertileWindowStart, LocalDate fertileWindowEnd) throws SQLException {
    String query = "INSERT INTO Predictions (user_id, prediction_date, predicted_period, " +
                   "predicted_fertile_window_start, predicted_fertile_window_end) " +
                   "VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, userId);
        statement.setDate(2, Date.valueOf(predictionDate));
        statement.setDate(3, Date.valueOf(predictedPeriod));
        statement.setDate(4, (fertileWindowStart != null ? Date.valueOf(fertileWindowStart) : null));
        statement.setDate(5, (fertileWindowEnd != null ? Date.valueOf(fertileWindowEnd) : null));
        statement.executeUpdate();
    }
}


    public void savePregnancyChance(int userId, LocalDate checkedDate, LocalDate predictionDate, 
                                String chanceLevel) throws SQLException {
    String query = "INSERT INTO PregnancyChances (user_id, checked_date, prediction_date, chance_level) " +
                   "VALUES (?, ?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, userId);
        statement.setDate(2, Date.valueOf(checkedDate));
        statement.setDate(3, Date.valueOf(predictionDate));
        statement.setString(4, chanceLevel);
        statement.executeUpdate();
    }
}


    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

class Luna {
    private static final Scanner scanner = new Scanner(System.in);

    static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/lunaperiodtracker";
    static final String USER = "root";
    static final String PASS = "Treasure23!!!";

    public static void main(String[] args) {
        DatabaseManager dbManager;
        int userId;

        try {
            dbManager = new DatabaseManager(DB_URL, USER, PASS);
            dbManager.createTables();

            System.out.println("Welcome to Luna Period Tracker!");
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();

            System.out.print("Enter your cycle length (in days): ");
            int cycleLength = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            userId = dbManager.saveUser(name, cycleLength);

            while (true) {
                System.out.println("\n1. Log a period entry");
                System.out.println("2. Display period entries");
                System.out.println("3. Predict future periods");
                System.out.println("4. Check pregnancy chance");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        logPeriod(dbManager, userId);
                        break;
                    case 2:
                        dbManager.displayPeriodEntries(userId);
                        break;
                    case 3:
                        predictNextPeriods(dbManager, userId);
                        break;
                    case 4:
                        checkPregnancyChance(dbManager, userId);
                        break;
                    case 5:
                        System.out.println("Goodbye!");
                        dbManager.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private static void logPeriod(DatabaseManager dbManager, int userId) {
        System.out.print("Enter the date of the period (yyyy-mm-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());

        System.out.print("Enter your mood: ");
        String mood = scanner.nextLine();

        System.out.print("Enter your symptoms: ");
        String symptoms = scanner.nextLine();

        System.out.print("Enter your digestion: ");
        String digestion = scanner.nextLine();

        try {
            dbManager.savePeriodEntry(userId, date, mood, symptoms, digestion);
            System.out.println("Period entry saved.");
        } catch (SQLException e) {
            System.out.println("Error saving entry: " + e.getMessage());
        }
    }

    private static void predictNextPeriods(DatabaseManager dbManager, int userId) {
        try {
            LocalDate lastPeriodDate = dbManager.getLastPeriodDate(userId);
            if (lastPeriodDate == null) {
                System.out.println("No period entries found. Log a period first.");
                return;
            }

            System.out.println("How many months do you want predictions for?");
            int months = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            int cycleLength = getUserCycleLength(dbManager, userId);
            if (cycleLength <= 0) {
                System.out.println("Invalid cycle length. Please set your cycle length correctly.");
                return;
            }

            System.out.println("\nPredicted periods for the next " + months + " months:");
            LocalDate nextPeriod = lastPeriodDate;

            for (int i = 1; i <= months; i++) {
                nextPeriod = nextPeriod.plusDays(cycleLength);
                System.out.println("Prediction " + i + ": " + nextPeriod);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching predictions: " + e.getMessage());
        }
    }

    private static void checkPregnancyChance(DatabaseManager dbManager, int userId) {
        try {
            LocalDate lastPeriodDate = dbManager.getLastPeriodDate(userId);
            if (lastPeriodDate == null) {
                System.out.println("No period entries found. Log a period first.");
                return;
            }

            System.out.print("Enter a date to check pregnancy chance (yyyy-mm-dd): ");
            LocalDate chosenDate = LocalDate.parse(scanner.nextLine());

            int cycleLength = getUserCycleLength(dbManager, userId);
            if (cycleLength <= 0) {
                System.out.println("Invalid cycle length. Please set your cycle length correctly.");
                return;
            }

            LocalDate ovulationDate = lastPeriodDate.plusDays(cycleLength - 14);
            LocalDate fertileStart = ovulationDate.minusDays(5);
            LocalDate fertileEnd = ovulationDate.plusDays(2);

            if (chosenDate.isBefore(lastPeriodDate)) {
                System.out.println("The chosen date is before the last logged period. No data available.");
            } else if (chosenDate.isEqual(ovulationDate)) {
                System.out.println("Chance of pregnancy on " + chosenDate + ": High (ovulation day).");
            } else if (!chosenDate.isBefore(fertileStart) && !chosenDate.isAfter(fertileEnd)) {
                System.out.println("Chance of pregnancy on " + chosenDate + ": High (fertile window).");
            } else {
                System.out.println("Chance of pregnancy on " + chosenDate + ": Low.");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching data: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a valid date in yyyy-mm-dd format.");
        }
    }

    private static int getUserCycleLength(DatabaseManager dbManager, int userId) throws SQLException {
        String query = "SELECT cycle_length FROM users WHERE id = ?";
        try (PreparedStatement statement = dbManager.getConnection().prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("cycle_length");
                }
            }
        }
        return 0;
    }
}

# 🌙Luna: Your Personal Period Tracker🌙

## I. Project Overview: *Embrace Your Cycle, Empower Your Health*
**Luna** is a period tracking application designed to help users track their menstrual cycles, predict upcoming periods, and estimate their chances of pregnancy based on their cycle data. The app allows 
users to log period details, including mood, symptoms, and digestion, as well as calculate and predict their next menstrual cycle. 

### Key Features:
* **Cycle Tracking**: Record your period dates, mood, symptoms, and digestion.
* **Predict Your Period**: Get accurate predictions for the next 5 months based on your cycle length.
* **Pregnancy Chances**: Estimate your chances of pregnancy based on your fertility window.
* **Health Insights**: Track changes in mood, energy, and digestion with thoughtful feedback.

## II. How Luna Embraces MySQL🌟
### 1. User Management:
* Stores user details such as name and cycle length in a Users table.
* Enforces data validity using constraints (e.g., positive cycle length).

### 2. Period Tracking:
* Logs period entries in the PeriodEntries table, including date, mood, symptoms, and digestion status.
* Uses foreign key constraints to associate period entries with specific users.

### 3. Prediction Storage:
* Optionally stores predictions of future periods and fertile windows in the Predictions table.
* Enables historical analysis of prediction accuracy.

### 4. Pregnancy Chances:
* Records and calculates the probability of pregnancy for specific dates using the PregnancyChances table.
* Ensures accurate tracking by linking queries to users and predictions.

## III. Integration of  the Sustainable Development Goal (SDG): Good Health & Well-Being 🌏
**Luna** is built with **Sustainable Development Goal 3** in mind—promoting **Good Health and Well-Being** for all. Here’s how Luna supports your health:
* **Physical Health**: Track your menstrual cycle and get predictions to help you manage your health better.
* **Mental Health**: By tracking your mood, symptoms, and digestion, Luna helps you understand the connection between your cycle and your mental well-being.
* **Reproductive Health**: Luna estimates your chances of pregnancy, helping you understand your fertility window and empowering you to make informed choices.

By helping users manage and understand their cycles, Luna plays a small but meaningful role in 

## IV. Instructions for running the program 💻
Ready to start tracking your cycle with Luna? 

### 📖 User Manual
**1. Getting Started**: When you run Luna for the first time, it will prompt you to provide your name. This name will personalize your experience.
**2. Main Menu Options**: After starting the program, you'll see the following menu:

**Option 1**: *Log a New Period Entry*
- You’ll be prompted to enter your cycle length or calculate it based on dates.
- Record additional details such as your mood, symptoms, and digestion.

**Option 2**: *Predict Next Period*
- Luna predicts your next periods for up to 5 months based on your logged data.
- If no data is available, Luna will ask you to log your cycle length first.

**Option 3**: *Check Pregnancy Chances*
- Enter a specific date to check your likelihood of pregnancy based on your cycle and fertile window.

**Option 4**: *View Cycle Insights*
- Displays your logged period entries, including cycle length, moods, symptoms, and digestion data

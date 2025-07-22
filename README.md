# 💼 Labor Attendance Management System

A Java-based desktop application designed to manage the attendance and wages of casual laborers in a company. Built using Java Swing for the GUI and MySQL for the backend, the system enables staff to track daily attendance and calculate monthly wages automatically.

---

## 📋 Features

- 🔐 **User Login System**  
  Secure authentication to restrict access to authorized users only.

- 👷 **Laborer Management**  
  - Add, update, delete laborers  
  - Store personal details including name, position, wage per day, and national ID

- 📆 **Attendance Tracking**  
  - Mark laborers as Present or Absent for each day  
  - View attendance records in a table format

- 💰 **Wage Calculation**  
  - Auto-calculates wages based on attendance records  
  - Supports monthly and custom date range reports

- 📊 **Excel Exporting**  
  - Generate Excel reports of monthly wages using Apache POI

- 🖥️ **Modern User Interface**  
  - Clean and responsive interface built with Java Swing

---

## 🛠 Technology Stack

| Component     | Description                         |
|---------------|-------------------------------------|
| Java          | Backend logic and desktop GUI       |
| Java Swing    | Desktop user interface              |
| MySQL         | Relational database storage         |
| Apache POI    | Export reports to Excel             |
| NetBeans IDE  | Development environment             |

---

## 🗃️ Database Schema

- `users`:  
  Stores system users with username and password for login

- `laborers`:  
  Stores laborer information (name, position, wage, national ID)

- `attendance`:  
  Stores daily attendance marked as `Present` or `Absent`

> **Note**: SQL script is included under the `/db/` folder to recreate the lost database.

---


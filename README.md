# 🎵 Music Player App (Java Swing + Oracle DB)

A Java Swing-based desktop application for playing `.mp3` audio files with Oracle Database integration for song management.

---

## 📌 Overview
The **Music Player App** allows you to add `.mp3` songs to a database, play them directly from the UI, and manage your collection.  
Built with Java Swing for the interface, JLayer for audio playback, and Oracle Database for storing song details.

---

## ✨ Features
- 🎶 Play, pause, and stop MP3 audio files
- 📂 Add and store songs with name and file path in Oracle Database
- 🗑 Delete songs from the database
- ⚠ Error handling for unsupported formats (supports only `.mp3`)
- 🖥 User-friendly Java Swing interface

---

## 🛠️ Technologies Used
- **Java** (JDK 8+)
- **Java Swing** (GUI)
- **Oracle Database XE** (Data storage)
- **JDBC** with `ojdbc8.jar`
- **JLayer** (`jl1.0.1.jar`) for MP3 playback

---

## 🚀 What I Did
- Created a **Java Swing** application for MP3 playback
- Integrated **JLayer** for audio handling
- Configured **Oracle Database** to store song metadata
- Implemented **Add**, **Play**, and **Delete** functionalities with JDBC
- Organized project files:
  - `src/` — Java source code
  - `lib/` — required JAR libraries
  - `sql/` — database setup scripts
- Wrote and tested **SQL scripts** to create necessary table and sequence
- Documented setup and usage for easy deployment

---
## 📂 Project Structure
MusicPlayer/
├── src/ # Java source files
├── lib/ # JAR dependencies (jl1.0.1.jar, ojdbc8.jar)
├── sql/ # Database scripts (songs_table.sql)
└── README.md # Project documentation└── README.md # Project documentation

---

# 🖼️ Screenshot below!
<img width="1187" height="926" alt="image" src="https://github.com/user-attachments/assets/2aa99346-f68c-4530-a63e-ef9e88cc2fec" />
<img width="572" height="240" alt="image" src="https://github.com/user-attachments/assets/8c8ae235-bec7-4cf3-8c23-1498500fe721" />
<img width="626" height="478" alt="image" src="https://github.com/user-attachments/assets/9edc9c72-cfde-449b-b615-b0225c35dfae" />

---
## ⚙️ Setup Instructions

### 1️⃣ Prerequisites
Before running the project, make sure you have:
- **Java JDK 8+** installed
- **Eclipse IDE** (or any Java IDE of your choice)
- **Oracle Database XE** installed and running

### 2️⃣ Installation & Configuration

#### Step 1 — Import the Project into Eclipse
1. Open **Eclipse IDE**
2. Go to `File → Import → Existing Projects into Workspace`
3. Click **Browse…** and select the `MusicPlayer` project folder
4. Ensure the project is checked, then click **Finish**

---

#### Step 2 — Set Up the Database
1. Open **SQL*Plus** or any Oracle SQL client
2. Run the SQL script to create the required table and sequence:
   ```sql
   @path/to/songs_table.sql
Verify the table is created:

SELECT * FROM songs;

**### Step 3 — Add Required Libraries**

In Eclipse, right-click the project → Build Path → Configure Build Path

Go to the Libraries tab and click Add JARs…

Add the following JARs from the lib folder:

jl1.0.1.jar (for MP3 playback)

ojdbc8.jar (for Oracle DB connection)

**Step 4 — Run the Application**
In Eclipse, navigate to the src/ folder and open MP3MusicPlayer.java

Right-click the file → Run As → Java Application

The Music Player window should now appear

🎯 Usage

Launch the app

Click Add Song to select and store an MP3 file in the database

Select a song from the list and click Play

Use Stop to halt playback or Delete to remove the song from the database



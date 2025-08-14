🎵 MP3 Music Player (Java Swing + Oracle DB)
📌 Overview

A Java Swing application for playing .mp3 files.

Stores song details in an Oracle Database.

Uses JLayer for audio playback and JDBC for database connectivity.

Ideal for learning GUI + Multimedia + Database integration in Java.

🚀 Features

Add MP3 songs and store name/path in the database.

Delete songs from the database.

Play and stop MP3 files.

Supports .mp3 format only.

🛠 Tech Stack

Language: Java (JDK 8+)

GUI: Java Swing

Database: Oracle Database XE

Libraries:

jl1.0.1.jar → MP3 playback

ojdbc8.jar → Oracle JDBC driver

📂 Project Structure

src/ → Java source code.

lib/ → Required JAR files.

sql/ → SQL scripts for database setup.

⚙️ Setup Instructions

Install JDK 8+, Eclipse IDE, and Oracle XE.

Set JAVA_HOME and update system Path.

Open Eclipse → File → Import → Existing Projects into Workspace.

Add jl1.0.1.jar and ojdbc8.jar from lib to Build Path.

In Oracle DB, run sql/songs_table.sql to create the table and sequence.

Run MP3MusicPlayer.java in Eclipse.

👤 Author

Name: Wadkar Deepak Vishwas

GitHub: wadkarkalpesh

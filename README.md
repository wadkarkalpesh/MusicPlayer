# 🎵 MP3 Music Player (Java + Swing + Oracle DB)

A simple **Java Swing** application to play MP3 files, store them in an **Oracle database**, and manage a playlist.  
Built with **JLayer** for MP3 playback and **JDBC** for database integration.


## 📌 Features
- **Add Song** → Choose an MP3 file and save its path & name to the database.
- **Delete Song** → Remove a song entry from the database.
- **Play Song** → Play MP3 files using the JLayer library.
- **Stop Playback** → Stop the currently playing song.
- **Database Integration** → Stores song names and file paths in Oracle DB.

  
## 🛠️ Tech Stack
- **Language:** Java (JDK 8+)
- **GUI:** Java Swing
- **Database:** Oracle Database XE
- **Libraries:**
  - [JLayer (javazoom)](http://www.javazoom.net/javalayer/javalayer.html) – MP3 playback
  - Oracle JDBC Driver (`ojdbc8.jar`)

---

## 📂 Project Structure
MP3MusicPlayer/
│
├── src/
│   └── MP3MusicPlayer.java          # Main Java source file
│
├── lib/
│   ├── jl1.0.1.jar                  # JLayer library (MP3 playback)
│   └── ojdbc8.jar                   # Oracle JDBC driver
│
├── sql/
│   └── songs_table.sql              # SQL script to create table & sequence
│
├── README.md                        # Project documentation
└── .gitignore                       # Ignore build and IDE files


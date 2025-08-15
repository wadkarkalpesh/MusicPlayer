import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javazoom.jl.player.Player;

public class MP3MusicPlayer extends JFrame implements ActionListener {

    private JButton playButton, stopButton, addToDBButton, searchButton;
    private JLabel songLabel;
    private JFileChooser fileChooser;
    private JTextField searchField;
    private Player player;
    private Thread playerThread;
    private File currentSong;
    private Connection conn;
    private List<String> songs;
    private int currentSongIndex = -1;

    public MP3MusicPlayer() {
        setTitle("MP3 Music Player with Database");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 250);
        setLayout(new FlowLayout());

        connectToDatabase();

        // UI Components
        songLabel = new JLabel("No song selected");
        playButton = new JButton("Play");
        stopButton = new JButton("Stop");
        addToDBButton = new JButton("Add to Database");
        searchField = new JTextField(20);
        searchButton = new JButton("Search");

        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".mp3");
            }

            public String getDescription() {
                return "MP3 Files (*.mp3)";
            }
        });

        // Listeners
        playButton.addActionListener(this);
        stopButton.addActionListener(this);
        addToDBButton.addActionListener(this);
        searchButton.addActionListener(this);

        // Add components
        add(songLabel);
        add(playButton);
        add(stopButton);
        add(addToDBButton);
        add(searchField);
        add(searchButton);

        // Menu
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem loadFromDBItem = new JMenuItem("Load from Database");
        openItem.addActionListener(e -> openFile());
        loadFromDBItem.addActionListener(e -> loadSongsFromDB());
        fileMenu.add(openItem);
        fileMenu.add(loadFromDBItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        songs = new ArrayList<>();

        // Shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (player != null) player.close();
                if (conn != null && !conn.isClosed()) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }

    private void connectToDatabase() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:xe",
                    "DEEPAK_38",
                    "DEEPAK_38"
            );
            createSongsTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database connection error: " + e.getMessage());
        }
    }

    private void createSongsTable() throws SQLException {
        String createSQL =
                "CREATE TABLE songs (" +
                        "id NUMBER PRIMARY KEY, " +
                        "song_path VARCHAR2(500), " +
                        "song_name VARCHAR2(255))";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createSQL);
        } catch (SQLException e) {
            // Table may already exist
        }
    }

    private void loadSongsFromDB() {
        try {
            String query = "SELECT song_path FROM songs";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            songs.clear();
            while (rs.next()) {
                songs.add(rs.getString("song_path"));
            }

            rs.close();
            stmt.close();

            if (!songs.isEmpty()) {
                currentSongIndex = 0;
                playSong(new File(songs.get(0)));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading songs: " + e.getMessage());
        }
    }

    private void addSongToDB(File file) {
        try {
            String query = "INSERT INTO songs (id, song_path, song_name) VALUES (songs_seq.NEXTVAL, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, file.getAbsolutePath());
            pstmt.setString(2, file.getName());
            pstmt.executeUpdate();
            pstmt.close();

            JOptionPane.showMessageDialog(this, "Song added to DB!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding song: " + e.getMessage());
        }
    }

    private void openFile() {
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            currentSong = fileChooser.getSelectedFile();
            songLabel.setText("Selected: " + currentSong.getName());
            playSong(currentSong);
        }
    }

    private void playSong(File file) {
        stopPlayback();

        songLabel.setText("Now Playing: " + file.getName());

        playerThread = new Thread(() -> {
            try {
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                player = new Player(bis);
                player.play();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Playback error: " + e.getMessage());
            }
        });
        playerThread.start();
    }

    private void stopPlayback() {
        if (player != null) {
            player.close();
        }
    }

    private void searchAndPlaySong(String keyword) {
        try {
            String query = "SELECT song_path FROM songs WHERE LOWER(song_name) LIKE ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, "%" + keyword.toLowerCase() + "%");
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                File songFile = new File(rs.getString("song_path"));
                if (songFile.exists()) {
                    playSong(songFile);
                } else {
                    JOptionPane.showMessageDialog(this, "File not found: " + songFile.getAbsolutePath());
                }
            } else {
                JOptionPane.showMessageDialog(this, "No matching song found.");
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Search error: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playButton) {
            if (currentSong != null) {
                playSong(currentSong);
            } else {
                JOptionPane.showMessageDialog(this, "Select a song first!");
            }
        } else if (e.getSource() == stopButton) {
            stopPlayback();
        } else if (e.getSource() == addToDBButton) {
            if (currentSong != null) {
                addSongToDB(currentSong);
            } else {
                JOptionPane.showMessageDialog(this, "Select a song first!");
            }
        } else if (e.getSource() == searchButton) {
            String keyword = searchField.getText().trim();
            if (!keyword.isEmpty()) {
                searchAndPlaySong(keyword);
            } else {
                JOptionPane.showMessageDialog(this, "Enter a song name to search.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MP3MusicPlayer player = new MP3MusicPlayer();
            player.setVisible(true);
        });
    }
}
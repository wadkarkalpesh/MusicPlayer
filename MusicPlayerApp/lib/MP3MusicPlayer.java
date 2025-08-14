import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import javazoom.jl.player.Player;

public class MP3MusicPlayer extends JFrame implements ActionListener {

    private JButton playButton, stopButton, addToDBButton;
    private JLabel songLabel;
    private JFileChooser fileChooser;
    private Player player;
    private Thread playerThread;
    private File currentSong;
    private Connection conn;
    private List<String> songs;
    private int currentSongIndex = -1;

    public MP3MusicPlayer() {
        setTitle("MP3 Music Player with Database");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 200);
        setLayout(new FlowLayout());

        // Initialize DB
        connectToDatabase();

        // Components
        songLabel = new JLabel("No song selected");
        playButton = new JButton("Play");
        stopButton = new JButton("Stop");
        addToDBButton = new JButton("Add to Database");

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

        add(songLabel);
        add(playButton);
        add(stopButton);
        add(addToDBButton);

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

        // Close resources on exit
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
            // Table may already exist, ignore
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
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MP3MusicPlayer player = new MP3MusicPlayer();
            player.setVisible(true);
        });
    }
}
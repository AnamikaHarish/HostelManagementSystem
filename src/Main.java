import database.DatabaseManager;
import ui.MainFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Ensure Database and Tables exist
        DatabaseManager.initializeDatabase();

        // Launch Swing Application on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
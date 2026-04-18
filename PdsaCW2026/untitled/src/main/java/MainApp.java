import ui.MainMenu;

import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainMenu();
        });
    }
}
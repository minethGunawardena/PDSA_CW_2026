package ui;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {

    public MainMenu() {

        setTitle("Algorithm Game System");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center screen
        setResizable(false);

        // Main container
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        // ================= TITLE =================
        JLabel title = new JLabel("Algorithm Game System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        title.setFont(loadPixelFont(25f));

        mainPanel.add(title, BorderLayout.NORTH);

        // ================= BUTTON PANEL =================
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 1, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));
        buttonPanel.setBackground(new Color(245, 245, 245));

        buttonPanel.add(createButton("Minimum Cost", new Color(52, 152, 219)));
        buttonPanel.add(createButton("Snake & Ladder", new Color(46, 204, 113)));
        buttonPanel.add(createButton("Traffic Simulation", new Color(155, 89, 182)));
        buttonPanel.add(createButton("Knight's Tour", new Color(241, 196, 15)));
        buttonPanel.add(createButton("16 Queens", new Color(231, 76, 60)));

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);

        setVisible(true);
    }

    // ================= BUTTON DESIGN =================
    private JButton createButton(String title, Color color) {

        JButton btn = new JButton(title);

        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // hover effect
        Color original = color;
        Color hover = color.brighter();

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(hover);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(original);
            }
        });

        btn.addActionListener(e -> new GameFrame(title));
        btn.setFont(loadPixelFont(18f));

        return btn;
    }

    private Font loadPixelFont(float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT,
                    new java.io.File("src/fonts/Minecraft.ttf"));
            return font.deriveFont(size);
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("Monospaced", Font.BOLD, (int) size);
        }
    }
}
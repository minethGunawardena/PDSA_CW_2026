package ui;

import ui.panels.*;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    public GameFrame(String gameName) {
        setTitle(gameName);
        setSize(800, 600);
        setLayout(new BorderLayout());

        switch (gameName) {
            case "Minimum Cost":
                add(new MinimumCostPanel());
                break;
            case "Snake & Ladder":
                add(new SnakeLadderPanel());
                break;
            case "Traffic Simulation":
                add(new TrafficSimulationPanel());
                break;
            case "Knight's Tour":
                //add(new KnightsTourPanel());
                break;
            case "16 Queens":
                //add(new QueensPanel());
                break;
        }

        setVisible(true);
    }
}

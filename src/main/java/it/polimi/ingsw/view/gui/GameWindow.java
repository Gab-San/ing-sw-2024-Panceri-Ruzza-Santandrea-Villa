package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.Scene;
import it.polimi.ingsw.view.SceneID;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    public static final int SCREEN_WIDTH = 1280;
    public static final int HEIGHT_WIDTH = 720;
    public GameWindow(){
        setSize(SCREEN_WIDTH,HEIGHT_WIDTH);
        //TODO: Maybe implement full screen
        Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        center.translate(-SCREEN_WIDTH/2, -HEIGHT_WIDTH/2);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(center);
        setVisible(true);
    }

    public void displayScene(Scene scene){
        SwingUtilities.invokeLater(
                () -> {
                    add((Component) scene);
                    scene.display();
                    setVisible(true);
                }
        );

    }

    public void closeCurrentScene(Scene scene) {
        SwingUtilities.invokeLater(
                () -> remove((Component) scene)
        );
    }

    public void hideScene(Scene scene) {
        SwingUtilities.invokeLater(
                () -> ((Component) scene).setVisible(false)
        );
    }
}

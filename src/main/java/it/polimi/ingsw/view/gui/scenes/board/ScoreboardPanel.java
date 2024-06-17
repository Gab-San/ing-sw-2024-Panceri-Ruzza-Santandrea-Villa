package it.polimi.ingsw.view.gui.scenes.board;

import it.polimi.ingsw.view.GameColor;
import it.polimi.ingsw.view.gui.GameWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ScoreboardPanel extends JPanel {
    public static final int SCOREBOARD_WIDTH = 300;
    public static final int SCOREBOARD_HEIGHT = 900;
    private BufferedImage scoreboardImage;
    public ScoreboardPanel(){
        setPreferredSize(new Dimension(SCOREBOARD_WIDTH, SCOREBOARD_HEIGHT));
        setBackground(GameColor.BOARD_COLOUR.getColor());
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;
        if(scoreboardImage == null){
            try(InputStream is = this.getClass().getClassLoader().getResourceAsStream("icons/plateau_score.jpg")){
                assert is != null;
                scoreboardImage = ImageIO.read(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        int xOffset = (int) (SCOREBOARD_WIDTH / 10 * GameWindow.SCALE_FACTOR);
        int yOffset = (int) (SCOREBOARD_HEIGHT/6 * GameWindow.SCALE_FACTOR);
        System.out.println(xOffset + " " + yOffset + " " +
                scoreboardImage.getWidth()/2 + " " + scoreboardImage.getHeight()/2 + " " + GameWindow.SCALE_FACTOR);
        graphics2D.drawImage(scoreboardImage, xOffset, yOffset,
                scoreboardImage.getWidth()/2, scoreboardImage.getHeight()/2, null);
    }
}

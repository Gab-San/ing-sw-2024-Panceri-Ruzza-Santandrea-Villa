package it.polimi.ingsw.view.gui.scenes.board;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.view.GameColor;
import it.polimi.ingsw.view.gui.GameWindow;
import it.polimi.ingsw.view.model.ViewBoard;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.view.GameColor.PLAYER_RED;

public class ScoreboardPanel extends JPanel {
    private final int PANEL_WIDTH = (int) (350 * GameWindow.SCALE_FACTOR);
    private final int PANEL_HEIGHT = (int) (1100 * GameWindow.SCALE_FACTOR);
    public static final int SCOREBOARD_WIDTH = (int) (484/1.5f * GameWindow.SCALE_FACTOR);
    public static final int SCOREBOARD_HEIGHT = (int) (948/1.5f * GameWindow.SCALE_FACTOR);
    int xOffset = (getWidth() - SCOREBOARD_WIDTH)/2;
    int yOffset = (getHeight() - SCOREBOARD_HEIGHT)/4;
    private final int componentWidth = 60;
    private final int componentHeight = 58;
    private final int hgap = 15;
    private final int vgap = 10;
    private BufferedImage scoreboardImage;
    private final ViewBoard board;
    private final Map<Integer, JPanel> scorePosition;
    public ScoreboardPanel(ViewBoard board){
        if(scoreboardImage == null){
            try(InputStream is = this.getClass().getClassLoader().getResourceAsStream("icons/plateau_score.jpg")){
                assert is != null;
                scoreboardImage = ImageIO.read(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        this.board = board;
        this.scorePosition = initializeScorePosition();
        initializePanels();
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(GameColor.SCOREBOARD_COLOUR.getColor());
        setLayout(null);

        JPanel panel = scorePosition.get(10);
        System.out.println(panel);
        panel.add(new PlayerPawn(PLAYER_RED.getColor()));
        panel.revalidate();

        panel = scorePosition.get(3);
        System.out.println(panel);
        panel.add(new PlayerPawn(PLAYER_RED.getColor()));
        panel.revalidate();

        panel = scorePosition.get(11);
        System.out.println(panel);
        panel.add(new PlayerPawn(PLAYER_RED.getColor()));
        panel.revalidate();

        panel = scorePosition.get(18);
        System.out.println(panel);
        panel.add(new PlayerPawn(PLAYER_RED.getColor()));
        panel.revalidate();

    }

    private void initializePanels() {
        for(JPanel panel : scorePosition.values()){
            panel.setSize(60,60);
            panel.setPreferredSize(new Dimension(60,60));
            panel.setMaximumSize(new Dimension(60,60));
            panel.setLayout(new GridLayout(2,2,20,24));
            panel.setOpaque(false);
            add(panel);
        }
    }

    private Map<Integer, JPanel> initializeScorePosition() {
        Map<Integer, JPanel> mapScorePanel = new HashMap<>();
        int i = 0;
        while(i < 3){
            // 58 is the offset from the side of the scoreboard
            int x = xOffset + 58 + (componentWidth+hgap) * i;
            // 20 is the offset from the south border of the scoreboard
            int y = yOffset + SCOREBOARD_HEIGHT - (20 + componentHeight);
            JPanel panel = new JPanel();
            panel.setBounds(x,y,componentWidth, componentHeight);
            mapScorePanel.put(i, panel);
            i++;
        }

        // Starting from right to left
        int dirX = -1;
        // Here i is equal to 3
        while (i < 20){
            if((i-3)%4 == 0 && i != 3){
                dirX *= -1;
                System.out.println("Changing dir: " + i);
            }
            int x = xOffset + (dirX == -1 ? SCOREBOARD_WIDTH : 0)
                    + dirX * (23 + (dirX == -1 ? componentWidth : 0) + (hgap + componentWidth) * ((i - 3) % 4));

            int y = yOffset + SCOREBOARD_HEIGHT - (20 + componentHeight + 8
                    + componentHeight + (vgap + componentHeight) * ((int) (i-3)/4));
            if(i == 10){
                System.out.println(xOffset + " " + dirX + " " + (i - 3) %4);
            }
            System.out.println("SCORE POINT: " + i + " " + "X: " + x + " Y: " + y);
            JPanel panel = new JPanel();
            panel.setBounds(x,y,componentWidth, componentHeight);
            mapScorePanel.put(i, panel);
            i++;
        }

        //Number 20 has a particular position
        i = 21;
        while(i < 24){
            int x = xOffset + 23;
            int y = yOffset + SCOREBOARD_HEIGHT - (20 + componentHeight + 8
                    + componentHeight + (vgap + componentHeight) * (4 + i%21));
            JPanel panel = new JPanel();
            panel.setBounds(x,y,componentWidth, componentHeight);
            mapScorePanel.put(i, panel);
            i++;
        }

        //After 23 there's it is simpler positioning by hand
        int x;
        int y;
        JPanel panel;

        i = 20;

        i = 24;

        i = 25;

        i = 26;

        i = 27;
        x = xOffset + SCOREBOARD_WIDTH - (23 + componentWidth);
        y = yOffset + SCOREBOARD_HEIGHT - (20 + componentHeight + 8 + componentHeight + (vgap + componentHeight) * 6);
        panel = new JPanel();
        panel.setBounds(x,y, componentWidth, componentHeight);
        mapScorePanel.put(i, panel);

        i = 28;
        y = yOffset + SCOREBOARD_HEIGHT - (20 + componentHeight + 8 + componentHeight + (vgap + componentHeight) * 5);
        panel = new JPanel();
        panel.setBounds(x,y, componentWidth, componentHeight);
        mapScorePanel.put(i, panel);

        i = 29;

        return mapScorePanel;
    }

    @Override
    public int getWidth() {
        return PANEL_WIDTH;
    }

    @Override
    public int getHeight() {
        return PANEL_HEIGHT;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;


        graphics2D.drawImage(scoreboardImage, xOffset, yOffset,
                SCOREBOARD_WIDTH, SCOREBOARD_HEIGHT, null);
    }
}

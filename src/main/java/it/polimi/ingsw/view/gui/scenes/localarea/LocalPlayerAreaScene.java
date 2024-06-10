package it.polimi.ingsw.view.gui.scenes.localarea;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.view.gui.GUI_Scene;
import it.polimi.ingsw.view.gui.GameWindow;

import javax.swing.*;
import java.util.List;

public class LocalPlayerAreaScene extends JPanel implements GUI_Scene {

    public LocalPlayerAreaScene(){
        setSize(GameWindow.SCREEN_WIDTH, GameWindow.SCREEN_HEIGHT);

    }
    @Override
    public void display() {

    }

    @Override
    public void displayError(String error) {

    }

    @Override
    public void displayNotification(List<String> backlog) {

    }

    @Override
    public void moveView(List<CornerDirection> cornerDirections) {

    }

    @Override
    public void setCenter(int row, int col) {

    }

    @Override
    public void setCenter(GamePoint center) {

    }

    @Override
    public void close() {

    }
}

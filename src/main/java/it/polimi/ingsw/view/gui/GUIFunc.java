package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.UIFunctions;

import javax.swing.*;
import java.awt.*;

public final class GUIFunc extends UIFunctions {
    public static JLabel createNotificationLabel(){
        JLabel label =  new JLabel("");
        //Error is not shown until it occurs
        label.setVisible(false);
        return label;
    }

    public static JLabel createNotificationLabel(int prefWidth, int prefHeight,
                                                 int maxWidth, int maxHeight){
        JLabel label =  new JLabel("");
        //Align the label to the center of its display area
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setPreferredSize(new Dimension(prefWidth, prefHeight));
        label.setMaximumSize(new Dimension(maxWidth,maxHeight));
        //Notification is not shown until it occurs
        label.setVisible(false);
        return label;
    }


    public static int setupDisplayTimer(float displayTimeSeconds, Timer displayTimer){
        int displayTime = (int) (displayTimeSeconds * 1000);
        // Stops any error timer, the label will be changed
        // so there's no need to wait for the display timer to end
        if(displayTimer != null){
            displayTimer.stop();
        }
        return displayTime;
    }
    public static String correctToLabelFormat(String message) {
        // Labels don't parse newlines
        return "<html>" + message.replaceAll("\n", "<br>") + "</html>";
    }

    public static void setupFrame(JFrame frame, String title, int width, int height){
        frame.setTitle(title);
        frame.setSize(width, height);
        Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        center.translate(-width/2, -height/2);
        frame.setLocation(center);
    }

    public static void setupPanel(JPanel panel){

    }
}

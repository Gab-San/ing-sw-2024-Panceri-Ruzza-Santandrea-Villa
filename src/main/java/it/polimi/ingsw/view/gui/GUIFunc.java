package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.UIFunctions;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static java.awt.Component.CENTER_ALIGNMENT;

/**
 * This class extends UIFunctions class. It acts as a library for GUI methods that work
 * for most setups across UI elements.
 */
public final class GUIFunc extends UIFunctions {

    /**
     * Returns the path to the root of all graphic resources (icons, images, fonts).
     * @return the path to the root of all graphic resources (icons, images, fonts)
     */
    public static String getGraphicsResourcesRootPath(){
        return "graphics/";
    }

    /**
     * Creates a simple notification label.
     * @return notification label
     */
    public static JLabel createNotificationLabel(){
        JLabel label =  new JLabel("");
        //Error is not shown until it occurs
        label.setVisible(false);
        label.setAlignmentX(CENTER_ALIGNMENT);
        return label;
    }


    /**
     * Creates a notification label with given sizes.
     * @param prefWidth preferred width
     * @param prefHeight preferred height
     * @param maxWidth maximum width
     * @param maxHeight maximum height
     * @return notification label
     */
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

    /**
     * Sets up a display timer.
     * <p>
     *     A display timer is a timer on which to display
     *      notifications for a given amount of seconds.
     * </p>
     * @param displayTimeSeconds amount of time to display notification
     * @param displayTimer timer instance on which to display notification
     * @return amount of displaying time
     */
    public static int setupDisplayTimer(float displayTimeSeconds, Timer displayTimer){
        int displayTime = (int) (displayTimeSeconds * 1000);
        // Stops any error timer, the label will be changed
        // so there's no need to wait for the display timer to end
        if(displayTimer != null){
            displayTimer.stop();
        }
        return displayTime;
    }

    /**
     * Corrects string to be used in a label.
     * @param message string to be displayed on a label
     * @return string to be displayed on a label correctly formatted
     */
    public static String correctToLabelFormat(String message) {
        // Labels don't parse newlines
        return "<html>" + message.replaceAll("\n", "<br>") + "</html>";
    }

    /**
     * Imports the desired image found at the specified path relative to resource folder.
     * @param cl class loader
     * @param imagePath relative path to the desired image
     * @return desired buffered image
     */
    public static BufferedImage importIMG(ClassLoader cl, String imagePath){
        InputStream url = null;
        try {
            url = cl.getResourceAsStream(imagePath);
        } catch(NullPointerException e){
            e.printStackTrace(System.err);
        }

        BufferedImage img = null;
        try{
            assert url != null;
            img = ImageIO.read(url);
        } catch(IOException e){
            e.printStackTrace(System.err);
        }
        return img;
    }
    /**
     * Sets up a frame with given sizes.
     * @param frame frame instance to set up
     * @param title title of the frame
     * @param width width of the frame
     * @param height height of the frame
     * @param windowAdapter window listener
     */
    public static void setupFrame(JFrame frame, String title, int width, int height, WindowAdapter windowAdapter){
        // Sets title frame
        frame.setTitle(title);
        frame.setSize(width, height);
        // Centers screen
        Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        center.translate(-width/2, -height/2);
        frame.addWindowListener(windowAdapter);
        frame.setLocation(center);
    }

    /**
     * Sets up a dialog frame
     * @param dialog dialog frame instance
     * @param width width of the dialog
     * @param height height of the dialog
     * @param windowAdapter window listener
     */
    public static void setupDialog(JDialog dialog, int width, int height, WindowAdapter windowAdapter){
        dialog.setSize(width, height);
        dialog.setPreferredSize(new Dimension(width, height));
        Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        center.translate(-width/2, -height/2);
        dialog.setLocation(center);
        dialog.addWindowListener(windowAdapter);
    }
}

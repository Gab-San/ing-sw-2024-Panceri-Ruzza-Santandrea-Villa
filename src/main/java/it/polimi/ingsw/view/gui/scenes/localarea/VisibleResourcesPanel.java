package it.polimi.ingsw.view.gui.scenes.localarea;

import it.polimi.ingsw.GameResource;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class VisibleResourcesPanel extends JPanel {
    private final Map<GameResource, GameResourceLabel> resourceLabelMap;
    public VisibleResourcesPanel(){
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setPreferredSize(new Dimension(400, PlayerInfoPanel.HEIGHT));
        setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        this.resourceLabelMap = new HashMap<>();
        //LEAF
        try(InputStream is = this.getClass().getClassLoader().getResourceAsStream("icons/LEAF_TEST.png")){
            assert is != null;
            ImageIcon icon = new ImageIcon(ImageIO.read(is));
            GameResourceLabel resLabel = new GameResourceLabel(icon, 0);
            resourceLabelMap.put(GameResource.LEAF, resLabel);
            add(resLabel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //MUSHROOM
        try(InputStream is = this.getClass().getClassLoader().getResourceAsStream("icons/MUSH_TEST.png")){
            assert is != null;
            ImageIcon icon = new ImageIcon(ImageIO.read(is));
            GameResourceLabel resLabel = new GameResourceLabel(icon, 0);
            resourceLabelMap.put(GameResource.MUSHROOM, resLabel);
            add(resLabel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //BUTTERFLY
        try(InputStream is = this.getClass().getClassLoader().getResourceAsStream("icons/BUTTERFLY_TEST.png")){
            assert is != null;
            ImageIcon icon = new ImageIcon(ImageIO.read(is));
            GameResourceLabel resLabel = new GameResourceLabel(icon, 0);
            resourceLabelMap.put(GameResource.BUTTERFLY, resLabel);
            add(resLabel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //WOLF
        try(InputStream is = this.getClass().getClassLoader().getResourceAsStream("icons/WOLF_TEST.png")){
            assert is != null;
            ImageIcon icon = new ImageIcon(ImageIO.read(is));
            GameResourceLabel resLabel = new GameResourceLabel(icon, 0);
            resourceLabelMap.put(GameResource.WOLF, resLabel);
            add(resLabel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //POTION
        try(InputStream is = this.getClass().getClassLoader().getResourceAsStream("icons/POTION_TEST.png")){
            assert is != null;
            ImageIcon icon = new ImageIcon(ImageIO.read(is));
            GameResourceLabel resLabel = new GameResourceLabel(icon, 0);
            resourceLabelMap.put(GameResource.POTION, resLabel);
            add(resLabel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //SCROLL
        try(InputStream is = this.getClass().getClassLoader().getResourceAsStream("icons/SCROLL_TEST.png")){
            assert is != null;
            ImageIcon icon = new ImageIcon(ImageIO.read(is));
            GameResourceLabel resLabel = new GameResourceLabel(icon, 0);
            resourceLabelMap.put(GameResource.SCROLL, resLabel);
            add(resLabel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //QUILL
        try(InputStream is = this.getClass().getClassLoader().getResourceAsStream("icons/QUILL_TEST.png")){
            assert is != null;
            ImageIcon icon = new ImageIcon(ImageIO.read(is));
            GameResourceLabel resLabel = new GameResourceLabel(icon, 0);
            resourceLabelMap.put(GameResource.QUILL, resLabel);
            add(resLabel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void adjournResourceCount(GameResource res, Integer value){
        synchronized (resourceLabelMap){
            GameResourceLabel label = resourceLabelMap.get(res);
            label.setQuantity(value);
        }
    }
}

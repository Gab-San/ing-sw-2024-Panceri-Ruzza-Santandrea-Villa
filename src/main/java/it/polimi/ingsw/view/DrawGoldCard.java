package it.polimi.ingsw.view;

import it.polimi.ingsw.model.enums.GameResource;
import it.polimi.ingsw.view.GoldCardJSONView;
import it.polimi.ingsw.model.json.deserializers.CornerJ;
import it.polimi.ingsw.model.json.deserializers.PointOnPlace;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class DrawGoldCard {
    private static final int CARD_WIDTH = 200;
    private static final int CARD_HEIGHT = 300;
    private static final Font FONT = new Font("Arial", Font.PLAIN, 12);
    private static final Color TEXT_COLOR = Color.BLACK;

    public void drawCard(GoldCardJSONView card, Graphics2D g2d, boolean isFront) {
        // Disegna una carta alle coordinate (x, y) con le dimensioni standard
        Rectangle cardBounds = new Rectangle(0, 0, CARD_WIDTH, CARD_HEIGHT);

        String cardId = card.getCardId();
        GameResource backResource = card.getBackResource();
        Map<String, Integer> placementCost = card.getPlacementCost();
        PointOnPlace pointsOnPlace = card.getPointsOnPlace();
        List<CornerJ> cornersJS = card.getCornersJS();

        if (isFront) {
            // Draw cardId
            drawText(cardId, CARD_WIDTH - 20, CARD_HEIGHT / 2, g2d);

            // Draw frontResource (assuming it's in the middle at the top)
            drawText("FrontResource: " + card.getColour(), CARD_WIDTH / 2, 20, g2d);

            // Draw pointsOnPlace (assuming it's in the middle at the top)
            drawText("PointsOnPlace: " + pointsOnPlace.getType() + " " + pointsOnPlace.getAmount(), CARD_WIDTH / 2, 40, g2d);

            // Draw placementCost (assuming it's in the middle at the bottom)
            int placementCostY = CARD_HEIGHT - 20;
            for (Map.Entry<String, Integer> entry : placementCost.entrySet()) {
                String costText = entry.getKey() + " = " + entry.getValue();
                drawText(costText, CARD_WIDTH / 2, placementCostY, g2d);
                placementCostY -= 15;
            }

            // Draw corners on the front
            drawFrontCorners(cornersJS, g2d);
        } else {
            // Draw cardId
            drawText(cardId, CARD_WIDTH - 20, CARD_HEIGHT / 2, g2d);

            // Draw backResource (assuming it's in the middle)
            drawText("BackResource: " + backResource.toString(), CARD_WIDTH / 2, CARD_HEIGHT / 2, g2d);

            // Draw corners on the back
            drawBackCorners(cornersJS, g2d);
        }
    }

    private void drawText(String text, int x, int y, Graphics2D g2d) {
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(FONT);
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        g2d.drawString(text, x - textWidth / 2, y);
    }

    private void drawFrontCorners(List<CornerJ> cornersJS, Graphics2D g2d) {
        for (CornerJ corner : cornersJS) {
            // Draw corners on the front
            // Assuming corner positions and resources are drawn as per requirements
            // You may need to adjust this based on your actual corner representation
            int cornerX = 0;
            int cornerY = 0;
            String cornerResource = "";

            switch (corner.getDirection()) {
                case "TL":
                    cornerX = 0;
                    cornerY = 0;
                    cornerResource = corner.getFrontResource().toString();
                    break;
                case "TR":
                    cornerX = CARD_WIDTH;
                    cornerY = 0;
                    cornerResource = corner.getFrontResource().toString();
                    break;
                case "BL":
                    cornerX = 0;
                    cornerY = CARD_HEIGHT;
                    cornerResource = corner.getFrontResource().toString();
                    break;
                case "BR":
                    cornerX = CARD_WIDTH;
                    cornerY = CARD_HEIGHT;
                    cornerResource = corner.getFrontResource().toString();
                    break;
            }
            drawText(cornerResource, cornerX, cornerY, g2d);
        }
    }

    private void drawBackCorners(List<CornerJ> cornersJS, Graphics2D g2d) {
        for (CornerJ corner : cornersJS) {
            // Draw corners on the front
            // Assuming corner positions and resources are drawn as per requirements
            // You may need to adjust this based on your actual corner representation
            int cornerX = 0;
            int cornerY = 0;
            String cornerResource = "";

            switch (corner.getDirection()) {
                case "TL":
                    cornerX = 0;
                    cornerY = 0;
                    cornerResource = corner.getBackResource().toString();
                    break;
                case "TR":
                    cornerX = CARD_WIDTH;
                    cornerY = 0;
                    cornerResource = corner.getBackResource().toString();
                    break;
                case "BL":
                    cornerX = 0;
                    cornerY = CARD_HEIGHT;
                    cornerResource = corner.getBackResource().toString();
                    break;
                case "BR":
                    cornerX = CARD_WIDTH;
                    cornerY = CARD_HEIGHT;
                    cornerResource = corner.getBackResource().toString();
                    break;
            }
            drawText(cornerResource, cornerX, cornerY, g2d);
        }
    }
}

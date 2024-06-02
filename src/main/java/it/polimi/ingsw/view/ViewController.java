package it.polimi.ingsw.view;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.view.model.ViewBoard;
import it.polimi.ingsw.view.model.ViewHand;
import it.polimi.ingsw.view.model.ViewOpponentHand;
import it.polimi.ingsw.view.model.ViewPlayArea;
import it.polimi.ingsw.view.model.cards.ViewPlayCard;
import it.polimi.ingsw.view.model.cards.ViewStartCard;

public class ViewController {
    private final ViewBoard board;
    private final ViewPlayArea selfPlayArea;

    public ViewController(ViewBoard board){
        this.board = board;
        selfPlayArea = board.getPlayerArea(board.getPlayerHand().getNickname());
    }

    public void validatePlaceStartCard() throws IllegalStateException, IllegalArgumentException{
        validatePhase(GamePhase.PLACESTARTING);
        if(selfPlayArea.getCardAt(0,0) != null)
            throw new IllegalStateException("Starting card was already placed!");
        if(board.getPlayerHand().getStartCard() == null)
            throw new IllegalArgumentException("You do not have a starting card in hand!");
    }

    public void validateChooseColor() throws IllegalStateException, IllegalArgumentException{
        validatePhase(GamePhase.CHOOSECOLOR);
        if(board.getPlayerHand().getColor() != null){
            throw new IllegalStateException("Color was already chosen!");
        }
    }

    public void validateChooseObjective() throws IllegalStateException{
        validatePhase(GamePhase.CHOOSEOBJECTIVE);
        if(board.getPlayerHand().getSecretObjectives().size() == 1) {
            throw new IllegalStateException("Cannot choose objective. The secret objective was already chosen!");
        }
        if(board.getPlayerHand().getSecretObjectives().isEmpty()){
            throw new IllegalStateException("Cannot choose objective. There are none in hand!");
        }
    }

    private void validateTurn() throws IllegalStateException{
        if(board.getCurrentTurn() != board.getPlayerHand().getTurn())
            throw new IllegalStateException("It's not your turn!");
    }
    private void validatePhase(GamePhase phase) throws IllegalStateException{
        if(board.getGamePhase() != board.getGamePhase())
            throw new IllegalStateException("It's not the correct phase for that action!");
    }

    public void validatePlaceCard(String cardID, Point placePos, String cornerDir) throws IllegalStateException, IllegalArgumentException{
        validateTurn();
        validatePhase(GamePhase.PLACECARD);
        Point correctPos = placePos.move(CornerDirection.getDirectionFromString(cornerDir));
        ViewPlayCard card = board.getPlayerHand().getCardByID(cardID);
        if(!selfPlayArea.validatePlacement(correctPos, card))
            throw new IllegalStateException(cardID + " can't be placed there.");
    }

    public void validateDraw(char deck, int card) throws IllegalStateException, IllegalArgumentException{
        validateTurn();
        validatePhase(GamePhase.DRAWCARD);
        boolean isDrawInvalid =
        switch (Character.toUpperCase(deck)) {
            case ViewBoard.RESOURCE_DECK ->
                switch (card) {
                    case 0 -> board.getResourceCardDeck().getTopCard() == null;
                    case 1 -> board.getResourceCardDeck().getFirstRevealed() == null;
                    case 2 -> board.getResourceCardDeck().getSecondRevealed() == null;
                    default -> throw new IllegalArgumentException("Invalid card position!");
                };
            case ViewBoard.GOLD_DECK ->
                switch (card) {
                    case 0 -> board.getGoldCardDeck().getTopCard() == null;
                    case 1 -> board.getGoldCardDeck().getFirstRevealed() == null;
                    case 2 -> board.getGoldCardDeck().getSecondRevealed() == null;
                    default -> throw new IllegalArgumentException("Invalid card position!");
                };
            default -> throw new IllegalArgumentException("Invalid deck!");
        };
        if(isDrawInvalid){
            throw new IllegalArgumentException("Can't draw from empty position!");
        }
    }

    public void validateMsg(String addressee) throws IllegalArgumentException {
        if(addressee.equalsIgnoreCase("all")) return;
        ViewHand hand = board.getAllPlayerHands().stream()
                .filter(h -> h.getNickname().equals(addressee))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException("Can't message a player not in game!")
                );

        if(hand instanceof ViewOpponentHand opponentHand){
            if(!opponentHand.isConnected())
                throw new IllegalArgumentException("Can't message a disconnected player!");
        }
    }

    public ViewPlayArea getSelfPlayArea(){
        return selfPlayArea;
    }

    public ViewPlayCard getSelfCardById(String id){
        return board.getPlayerHand().getCardByID(id);
    }

    public ViewStartCard getSelfStartingCard(){
        return board.getPlayerHand().getStartCard();
    }
}

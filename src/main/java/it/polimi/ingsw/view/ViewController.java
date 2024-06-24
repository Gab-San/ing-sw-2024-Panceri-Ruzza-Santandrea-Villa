package it.polimi.ingsw.view;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.view.model.*;
import it.polimi.ingsw.view.model.cards.ViewObjectiveCard;
import it.polimi.ingsw.view.model.cards.ViewPlayCard;
import it.polimi.ingsw.view.model.cards.ViewStartCard;

import java.util.List;
import java.util.ListIterator;

/**
 * This class acts as the controller communicating with the board
 * to perform checks and modify it.
 */
public class ViewController {
    private final ViewBoard board;
    private ViewPlayArea selfPlayArea;

    /**
     * Constructs the ViewController and sets the ViewBoard reference
     * @param board the game's ViewBoard to be used by this ViewController
     */
    public ViewController(ViewBoard board){
        this.board = board;
    }

    /**
     * Validates whether the place starting card command can be run. <br>
     * Successfully returning from this method means passing the check.
     * @throws IllegalStateException if the starting card can't be placed at the time of invocation.
     *                              The exception message explains the reason in greater detail.
     * @throws IllegalArgumentException if the player has no starting card in hand.
     */
    public void validatePlaceStartCard() throws IllegalStateException, IllegalArgumentException{
        validatePhase(GamePhase.PLACESTARTING);
        if(selfPlayArea.getCardAt(0,0) != null)
            throw new IllegalStateException("Starting card was already placed!");
        if(board.getPlayerHand().getStartCard() == null)
            throw new IllegalArgumentException("You do not have a starting card in hand!");
    }

    /**
     * Validates whether the choose color command can be run. <br>
     * Successfully returning from this method means passing the check.
     * @throws IllegalStateException if the color can't be chosen at the time of invocation.
     *                              The exception message explains the reason in greater detail.
     */
    public void validateChooseColor() throws IllegalStateException{
        validatePhase(GamePhase.CHOOSECOLOR);
        if(board.getPlayerHand().getColor() != null){
            throw new IllegalStateException("Color was already chosen!");
        }
    }

    /**
     * Validates whether the choose objective command can be run. <br>
     * Successfully returning from this method means passing the check.
     * @throws IllegalStateException if the objective can't be chosen at the time of invocation.
     *                              The exception message explains the reason in greater detail.
     */
    public void validateChooseObjective() throws IllegalStateException{
        validatePhase(GamePhase.CHOOSEOBJECTIVE);
        if(board.getPlayerHand().getSecretObjectives().size() == 1) {
            throw new IllegalStateException("Cannot choose objective. The secret objective was already chosen!");
        }
        if(board.getPlayerHand().getSecretObjectives().isEmpty()){
            throw new IllegalStateException("Cannot choose objective. There are none in hand!");
        }
    }

    /**
     * Validates whether the current turn is the local player's turn. <br>
     * Successfully returning from this method means passing the check.
     * @throws IllegalStateException if the turn is not the local player's turn.
     */
    private void validateTurn() throws IllegalStateException{
        if(board.getCurrentTurn() != board.getPlayerHand().getTurn())
            throw new IllegalStateException("It's not your turn!");
    }
    /**
     * Validates whether the current GamePhase is the same as phase. <br>
     * Successfully returning from this method means passing the check.
     * @param phase the desired GamePhase
     * @throws IllegalStateException if the current GamePhase and phase don't match.
     */
    public void validatePhase(GamePhase phase) throws IllegalStateException{
        if(board.getGamePhase() != phase)
            throw new IllegalStateException("It's not the correct phase for that action!");
    }

    /**
     * Validates whether the place card command can be run. <br>
     * Successfully returning from this method means passing the check.
     * @param cardID ID of the card to be placed
     * @param placePos position of the card on which to place the card
     * @param cornerDir corner direction on which to place the card
     * @throws IllegalStateException if the card can't be placed at the time of invocation.
     *                              The exception message explains the reason in greater detail.
     * @throws IllegalArgumentException if the player does not have the requested card in hand.
     */
    public void validatePlaceCard(String cardID, GamePoint placePos, String cornerDir) throws IllegalStateException, IllegalArgumentException{
        validateTurn();
        validatePhase(GamePhase.PLACECARD);
        GamePoint correctPos = placePos.move(CornerDirection.getDirectionFromString(cornerDir));
        ViewPlayCard card = board.getPlayerHand().getCardByID(cardID);
        if(!selfPlayArea.validatePlacement(correctPos, card))
            throw new IllegalStateException(cardID + " can't be placed there.");
    }
    /**
     * Validates whether the place card command can be run. <br>
     * Successfully returning from this method means passing the check.
     * @param deck char indicating the deck from which to draw (R/G)
     * @param card position of the card to draw <br> (0=top, 1,2=first,second revealed)
     * @throws IllegalStateException if the player can't draw the requested deck-card combination.
     *                              The exception message explains the reason in greater detail.
     * @throws IllegalArgumentException if the requested deck-card combination is invalid.
     *                              The exception message explains the reason in greater detail.
     */
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

    /**
     * Validates whether the restart game command can be run. <br>
     * Successfully returning from this method means passing the check.
     * @param numOfPlayers number of players required to start the new game
     * @throws IllegalStateException if the player can't currently restart the game.
     *                              The exception message explains the reason in greater detail.
     * @throws IllegalArgumentException if the numOfPlayers argument is invalid (<2 or >4).
     *                              The exception message explains the reason in greater detail.
     */
    public void validateRestart(int numOfPlayers)throws IllegalStateException, IllegalArgumentException{
        validatePhase(GamePhase.SHOWWIN);
        int numActivePlayers = board.getOpponents().size() + 1 ; //add 1 for the local player
        if(numOfPlayers < numActivePlayers || numOfPlayers > 4){
            throw new IllegalArgumentException("Number of Players must be ("+numActivePlayers+"-4)");
        }
    }

    /**
     * Validates whether a chat message can be sent to addressee
     * Successfully returning from this method means passing the check.
     * @param addressee the nickname of the addressee of the chat message,
     *                  or "all" if broadcasting the message.
     * @throws IllegalArgumentException if the addressee nickname is invalid.
     *                              The exception message explains the reason in greater detail.
     */
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

    /**
     * @return local player's ViewPlayArea
     */
    public ViewPlayArea getSelfPlayArea(){
        return selfPlayArea;
    }
    /**
     * @param id card ID to return
     * @return local player's card in hand with given ID
     */
    public ViewPlayCard getSelfCardById(String id){
        return board.getPlayerHand().getCardByID(id);
    }
    /**
     * @return local player's starting card (could be null)
     */
    public ViewStartCard getSelfStartingCard(){
        return board.getPlayerHand().getStartCard();
    }

    /**
     * Sets the local player's ViewPlayArea reference in the ViewController
     */
    public void setSelfPlayerArea() {
        selfPlayArea = board.getPlayerArea(board.getPlayerHand().getNickname());
    }
    /**
     * Creates the local player in the ViewBoard
     */
    public void addLocalPlayer(String nickname) {
        board.addLocalPlayer(nickname);
    }

    /**
     * @param nickname a player nickname
     * @return the ViewHand of the player with given nickname. <br>
     *          Due to ViewBoard implementation, it also runs addOpponent()
     *          if the given nickname doesn't correspond to the local player or to any opponent in game.
     */
    public ViewHand getPlayer(String nickname) {
        if(board.getPlayerHand().getNickname().equals(nickname)){
            return board.getPlayerHand();
        } else {
            return board.getOpponentHand(nickname);
        }
    }

    /**
     * @param nickname a player nickname
     * @return true if the nickname corresponds to the local player's nickname.
     */
    public boolean isLocalPlayer(String nickname) {
        return board.getPlayerHand().getNickname().equals(nickname);
    }

    public ViewPlayerHand getLocalPlayer() {
        return board.getPlayerHand();
    }

    public int getObjectiveCardIndex(ViewObjectiveCard comparedCard) {
        List<ViewObjectiveCard> secretCards = board.getPlayerHand().getSecretObjectives();
        ListIterator<ViewObjectiveCard> iterator = secretCards.listIterator();
        while(iterator.hasNext()){
            if(iterator.next().equals(comparedCard)){
                return iterator.nextIndex();
            }
        }
        throw new IllegalStateException("Card not in hand");
    }
}

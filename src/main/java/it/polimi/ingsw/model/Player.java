package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.enums.PlayerColor;
import org.jetbrains.annotations.Range;

public class Player {
    private final String nickname;
    @Range(from=1,to=4)
    private int turn;
    private boolean isConnected;
    private final PlayerHand hand;
    private final PlayerColor color;

    public static class PlayerHand{
        final PlayCard[] cards;
        ObjectiveCard secretObjective;
        StartingCard startingCard;

        PlayerHand(){
            cards = new PlayCard[3];
            secretObjective = null;
            startingCard = null;
        }

        public boolean containsCard(PlayCard card) {
            for(PlayCard c : cards){
                if(c.equals(card)) return true;
            }
            return false;
        }
        public PlayCard getCard(int pos){
            if (pos < 0 || pos >= cards.length) return null;
            else return cards[pos];
        }

        public ObjectiveCard getSecretObjective() {
            return secretObjective;
        }
        public void setSecretObjective(ObjectiveCard secretObjective) {
            this.secretObjective = secretObjective;
        }

        public StartingCard getStartingCard() {
            return startingCard;
        }
        public void setStartingCard(StartingCard startingCard) {
            this.startingCard = startingCard;
        }
    }

    public Player(String nickname, int turn, PlayerColor color){
        this.nickname = nickname;
        this.turn = turn;
        isConnected = true;
        hand = new PlayerHand();
        this.color = color;
    }

    public String getNickname(){
        return nickname;
    }
    public int getTurn(){
        return turn;
    }
    void setTurn(int turn) {
        this.turn = turn;
    }
    public boolean isConnected() {
        return isConnected;
    }
    public void setConnected(boolean connected) {
        isConnected = connected;
    }
    public PlayerHand getHand() {
        return hand;
    }
    public PlayerColor getColor() {
        return color;
    }

}

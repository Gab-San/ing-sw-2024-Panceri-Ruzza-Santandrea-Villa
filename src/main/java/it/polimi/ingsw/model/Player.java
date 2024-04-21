package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.enums.PlayerColor;
import org.jetbrains.annotations.Range;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Player {
    private final String nickname;
    @Range(from=1,to=4)
    private int turn;
    private boolean isConnected;
    private final PlayerHand hand;
    private final PlayerColor color;

    public static class PlayerHand{
        static final int MAX_CARDS = 3;
        final List<PlayCard> cards;
        ObjectiveCard secretObjective;
        StartingCard startingCard;

        PlayerHand(){
            cards = new LinkedList<>();
            secretObjective = null;
            startingCard = null;
        }

        @Override
        public boolean equals(Object other) {
            if(other == this) return true;
            if(other instanceof PlayerHand hand){
                return cards.equals(hand.cards) &&
                        secretObjective.equals(hand.secretObjective) &&
                        startingCard.equals(hand.startingCard);
            }
            else return false;
        }

        public boolean containsCard(PlayCard card) {
            for(PlayCard c : cards){
                if(c.equals(card)) return true;
            }
            return false;
        }
        public PlayCard getCard(int pos){
            if (pos < 0 || pos >= cards.size()) return null;
            else return cards.get(pos);
        }
        public void addCard(PlayCard card) throws RuntimeException{
            if(cards.size() > MAX_CARDS) throw new RuntimeException("Too many cards in hand!");
            cards.add(card);
        }
        public void removeCard(PlayCard card) throws RuntimeException{
            if(!cards.contains(card)) throw new RuntimeException("Card wasn't in hand!");
            cards.remove(card);
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

    @Override
    public boolean equals(Object other) {
        if(other == this) return true;
        if(other instanceof Player player){
            return nickname.equals(player.nickname) &&
                    isConnected == player.isConnected &&
                    //turn == player.turn &&
                    //FIXME: compare turn?? Or maybe only compare nickname as they would be unique
                    hand.equals(player.hand) &&
                    color.equals(player.color);
        }
        else return false;
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

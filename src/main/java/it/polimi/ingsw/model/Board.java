package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartingCard;

import java.security.InvalidParameterException;
import java.util.*;

public class Board {
    //TODO: add decks
    private final Map<Player, Integer> scoreboard;
    private final Map<Player, PlayArea> playerAreas;

    private int currentTurn;

    protected Board(){
        currentTurn = 0;
        scoreboard = new Hashtable<>();
        playerAreas = new Hashtable<>();
    }

    /**
     * Constructs the Board (as in initializing the game)
     * @param players all (2-4) players that are joining this game
     * @throws InvalidParameterException if the parameter players has duplicates or an illegal player count
     */
    public Board(Player ...players) throws InvalidParameterException {
        this();
        if(players.length > 4) throw new InvalidParameterException("Illegal number of players! Too high.");
        int playerNum = 0;
        for(Player p : players) {
            if(playerAreas.containsKey(p)) throw new InvalidParameterException("Duplicate player found.");
            scoreboard.put(p, 0);
            playerAreas.put(p, new PlayArea());
            p.setTurn(++playerNum); // turns range from 1 to 4
        }
    }

    /**
     * @return list of players of this game ordered by their turn
     */
    public List<Player> getPlayers(){
        return playerAreas.keySet().stream()
                .sorted(Comparator.comparingInt(Player::getTurn))
                .toList();
    }
    public Map<Player, PlayArea> getPlayerAreas(){
        return Collections.unmodifiableMap(playerAreas);
    }
    public Map<Player, Integer> getScoreboard(){
        return Collections.unmodifiableMap(scoreboard);
    }
    public void addScore(Player p, int amount){
        scoreboard.replace(
            p,
            scoreboard.get(p) + amount
        );
    }
    public void setScore(Player p, int score){
        scoreboard.replace(
                p,
                score
        );
    }

    //TODO: review Board.placeCard() args after making client/view
    public void placeCard(Player player, PlayCard card, Corner corner) throws InvalidParameterException, RuntimeException{
        //checks
        if(!playerAreas.containsKey(player)) throw new InvalidParameterException("Player not in this game!");
        if(!player.getHand().containsCard(card)) throw new InvalidParameterException("Card not in player's hand!");
        // corner invalid if it's not attached to a card or if it's not in player's playArea's freeCorners
        if(corner.getCardRef()==null || !playerAreas.get(player).getFreeCorners().contains(corner)) throw new InvalidParameterException("Corner isn't free!");

        //placement
        PlayArea playArea = playerAreas.get(player);
        PlayCard placedCard = playArea.placeCard(card, corner);

        //scoreboard update
        addScore(player, placedCard.calculatePointsOnPlace(playArea));
    }
    public void placeStartingCard(Player player, boolean placeOnFront){
        StartingCard startingCard = player.getHand().getStartingCard();
        if(startingCard == null) throw new RuntimeException("Player doesn't have a starting card yet!");
        if(placeOnFront)
            startingCard.turnFaceUp();
        else
            startingCard.turnFaceDown();

        playerAreas.get(player).placeStartingCard(startingCard);
    }
    // TODO: methods to give a startingCard and a secretObjective to the player (through the decks)
    // TODO: methods to draw cards (through the decks)
}

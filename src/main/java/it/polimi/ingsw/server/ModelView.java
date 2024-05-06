package it.polimi.ingsw.server;

import it.polimi.ingsw.Point;

import java.util.Random;

// TODO Delete
public class ModelView {
   public Point getPosition(String cardID){
       return new Point(new Random().nextInt()%100, new Random().nextInt()%100);
   }
   public ModelView getPlayerStartingCard(){
       return this;
   }
   public boolean isFaceUp(){
       return true;
   }

   public ModelView getPlayerHand(){ return this;
   }
   public boolean isFaceUp(String card) { return true; }
    public int getPlayerCount() { return new Random().nextInt(3) + 2; } //upper bound exclusive
}

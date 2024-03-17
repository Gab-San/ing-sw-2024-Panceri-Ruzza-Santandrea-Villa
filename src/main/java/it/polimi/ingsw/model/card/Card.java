package it.polimi.ingsw.model.card;

public abstract class Card {
    private boolean flipped; // false == front face up

    public Card(){
        flipped=false;
    }

    public boolean isFlipped(){
        return flipped;
    }
    public void flip(){
        if(flipped) turnFaceUp();
        else turnFaceDown();
    }
    public void turnFaceUp(){
        // maybe add guard clause
        flipped = true;
        // insert here other effects
    }
    public void turnFaceDown(){
        // maybe add guard clause
        flipped = false;
        // insert here other effects
    }
}

package it.polimi.ingsw.view.model.cards;

public abstract class ViewCard {
    private final String cardID;
    protected boolean isFaceUp;

    public ViewCard(String cardID){
        this.cardID = cardID;
        this.isFaceUp = false;
    }

    public String getCardID(){
        return cardID;
    }
    public boolean isFaceUp(){
        return isFaceUp;
    }
    public void flip(){
        isFaceUp = !isFaceUp;
        // eventual GUI code here
    }
    public void turnFaceUp(){
        if(!isFaceUp) flip();
    }
    public void turnFaceDown(){
        if(isFaceUp) flip();
    }
}

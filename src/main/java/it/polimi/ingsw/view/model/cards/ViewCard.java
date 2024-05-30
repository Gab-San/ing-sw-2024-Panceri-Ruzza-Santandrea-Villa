package it.polimi.ingsw.view.model.cards;

public abstract class ViewCard {
    private final String cardID;
    private final String imageFrontName;
    private final String imageBackName;
    protected boolean isFaceUp;

    public ViewCard(String cardID, String imageFrontName, String imageBackName){
        this.cardID = cardID;
        this.imageFrontName = imageFrontName;
        this.imageBackName = imageBackName;
        this.isFaceUp = false;
    }
    public ViewCard(ViewCard other){
        this(other.cardID, other.imageFrontName, other.imageBackName);
        this.isFaceUp = other.isFaceUp;
    }

    @Override
    public boolean equals(Object other){
        if(other == this) return true;
        else if(other instanceof ViewCard otherCard){
            return this.cardID.equals(otherCard.cardID);
        }else return false;
    }
    @Override
    public int hashCode(){
        return cardID.hashCode();
    }

    public String getCardID(){
        return cardID;
    }
    public synchronized boolean isFaceUp(){
        return isFaceUp;
    }
    public synchronized void flip(){
        isFaceUp = !isFaceUp;
        // eventual GUI code here
    }
    public synchronized void turnFaceUp(){
        if(!isFaceUp) flip();
    }
    public synchronized void turnFaceDown(){
        if(isFaceUp) flip();
    }

    public String getImageFrontName() {
        return imageFrontName;
    }
    public String getImageBackName() {
        return imageBackName;
    }
}

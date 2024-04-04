package it.polimi.ingsw.model.cards.objective;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.GameResource;

public enum PatternObjective {
    L_RED_RED_GREEN("*R*" +
                    "*R*" +
                    "**G"),
    DIAG_BLUE(  "**B" +
                "*B*" +
                "B**")
    ;

    final DoubleMap<GameResource> pattern;
    PatternObjective(String pattern){
        this.pattern = new DoubleMap<>();
        for (int i = 0; i < pattern.length(); i++) {
            GameResource r = charToResource(pattern.charAt(i));
            if(r!=null)
                this.pattern.put(i/3, i%3, r);
        }
    }

    public DoubleMapRO<GameResource> getPattern(){
        return pattern;
    }
    private GameResource charToResource(char c){
        switch (c){
            case 'R': return GameResource.MUSHROOM; // red
            case 'B': return GameResource.WOLF; // blue
            case 'G': return GameResource.LEAF; // green
            case 'P': return GameResource.BUTTERFLY; // purple
            default : return null;
        }
    }


}

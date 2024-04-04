package it.polimi.ingsw.model.cards.objective;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.Resource;

public enum PatternObjective {
    L_RED_RED_GREEN("*R*" +
                    "*R*" +
                    "**G"),
    DIAG_BLUE(  "**B" +
                "*B*" +
                "B**")
    ;

    final DoubleMap<Resource> pattern;
    PatternObjective(String pattern){
        this.pattern = new DoubleMap<>();
        for (int i = 0; i < pattern.length(); i++) {
            Resource r = charToResource(pattern.charAt(i));
            if(r!=null)
                this.pattern.put(i/3, i%3, r);
        }
    }

    public DoubleMapRO<Resource> getPattern(){
        return pattern;
    }
    private Resource charToResource(char c){
        switch (c){
            case 'R': return Resource.MUSHROOM; // red
            case 'B': return Resource.WOLF; // blue
            case 'G': return Resource.LEAF; // green
            case 'P': return Resource.BUTTERFLY; // purple
            default : return null;
        }
    }


}

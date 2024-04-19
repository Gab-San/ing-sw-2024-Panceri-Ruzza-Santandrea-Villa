package it.polimi.ingsw.model.cards.objective;

import it.polimi.ingsw.model.PlayArea;
import org.jetbrains.annotations.NotNull;

public interface ObjectiveStrategy {
    //TODO write documentation for the objective strategies
    public int calculateSolves(@NotNull PlayArea p);
}

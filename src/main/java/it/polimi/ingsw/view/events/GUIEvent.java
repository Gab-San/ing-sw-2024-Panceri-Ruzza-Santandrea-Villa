package it.polimi.ingsw.view.events;

import it.polimi.ingsw.view.gui.GUI;

public interface GUIEvent extends DisplayEvent {
    void displayEvent(GUI gui);
}

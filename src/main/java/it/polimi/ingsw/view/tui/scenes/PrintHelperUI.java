package it.polimi.ingsw.view.tui.scenes;

import it.polimi.ingsw.view.tui.TUI_Scene;

public class PrintHelperUI extends TUI_Scene {
    @Override
    protected void print() {
        //TODO Complete helper
        System.out.println("""
                Each command has a different format, but they follow the same pattern:
                commandId <optional> <command_arguments>
                The available commands are:
                "place|play" to play a card
                "draw" to draw a card
                "choose" color|objective to either choose color or objective
                "disconnect" to leave the game
                "send" to send a message
                "set|players|start" to set the number of players and effectively start the match
                "quit" to close the application
                """);

        out.println("\n" + "-".repeat(30) + "\n");
    }
}

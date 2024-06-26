package it.polimi.ingsw.view.tui.scenes;

import it.polimi.ingsw.view.tui.TUI_Scene;

/**
 * The command helper (legend) UI scene printer for the TUI
 */
public class PrintHelperUI extends TUI_Scene {

    /**
     * Default constructor.
     */
    public PrintHelperUI(){}

    @Override
    protected void print() {
        System.out.println("""
                Each command has a different format, but they follow the same pattern:
                
                commandId [identifier] <command_arguments>
                
                The available commands are:
                
                HELP: to show this screen
                help
                legend
                
                MOVE: to move onto the play area
                move <direction>|<cardinal_direction>|"center"
                
                VIEW: to view differents areas of the table
                view "me"|"board"|<player_id>
                goto "me"|"board"|<player_id>
                
                FLIP: flips the specified card
                flip <card_index>|<card_id>
                turn <card_index>|<card_id>
                turnover <card_index>|<card_id>
                
                DISCONNECT: to leave the game or lobby
                disconnect
                quit
                N.B. to quit the application type quit in the welcoming screen
                
                SET: to set the number of players of the match
                set <2-4>
                players <2-4>
                
                SEND: to send a message to another player
                send "all"|<player_id> <message>
                
                CHOOSE: to choose objective or player color
                choose objective <card_index>
                choose color Red|Blue|Yellow|Green
                
                DRAW: to draw a card
                draw <r | g><0, 1, 2>
                r to choose the Resource deck
                g to choose the Gold deck
                0 to draw the top card of the chosen deck
                1 to draw the first revealed card of the chosen deck
                2 to draw the second revealed card of the chosen deck
                N.B. do not put spaces between the letter and the number
                
                PLACE: to place a card
                place starting
                place <placing_card_id> <id_card_on_which_to_place> TL|TR|BL|BR
                play <placing_card_id> <id_card_on_which_to_place> TL|TR|BL|BR
                
                RESTART: to restart the game after it is finished
                restart <2-4>
                """);

        out.println("\n" + "-".repeat(30) + " Scroll up to see all commands" + "\n");
    }
}

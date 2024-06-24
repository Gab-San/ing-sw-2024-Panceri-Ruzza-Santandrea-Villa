# Implemented features
Complete rules + TUI + GUI + RMI + Socket + 2FA  (30)

### Advanced functionalities
Chat and Disconnection resilience

# How to launch the Server
1. Navigate to the folder containing the Server.jar and the "src" folder
2. Run the command `java -jar Server.jar <RMI port> <TCP port> [Server IP]`  (the **order** of the arguments is **important**)
      - The IP can be omitted: the app will automatically use the local machine's IP (excluding localhost)
      - If the local machine has multiple IPs, then it will prompt the user to select one of them (other than localhost)
3. If using the FatJar.jar instead run: <br> `java -cp Server.jar it.polimi.ingsw.network.Server <RMI port> <TCP port> [Server IP]`


# How to launch the Client
1. Navigate to the folder containing the Server.jar and the "src" folder
2. Run the command `java -jar Client.jar <Server port> <RMI | TCP> [Server IP] [myIP=<Client IP>]` (arguments can be in any order) 
      - The IPs are optional: if omitted, localhost will be used. 
      - The `myIP=<IP>` argument is also optional, as it is only used by RMI. It must be an IP reachable by the server.
3. If using the FatJar you only need to replace `Client.jar` with `FatJar.jar` in the command above.

### TUI notes
You can list all available commands with their syntax explanation by typing the "help" command.
Help syntax: (e.g. `view "me"|"board"|<player_id>`)
- view is the command keyword (it is always the first word)
- `<description>` indicates a command argument 
- `"value"` indicates an argument that must be the given value (**without** the "")
- `"value"|<description>|"other value"` indicates multiple alternatives for that argument (only use one)

### GUI notes
Flip cards in your hand by hovering over the card and pressing F.
Move in the playArea by pressing on the area background and using W A S D to move up/left/down/right.
Press the player names in the sidebar to view their playArea.
Select a card in your hand or on the board by clicking on it.
You can use the dropdown menu in the chat to send messages to a specific player (that player must be connected)


# Notes (on both Client and Server)
The IP arguments can be hostnames instead of IP addresses (a.b.c.d)
All arguments other than IPs/Hostnames are validated and the app will quit if an argument is invalid.
If the IP/Hostname argument is invalid, the app will instead list all available IPs on the local machine and prompt the user to input a valid IP address.
If a machine only has one IP (other than localhost) then that IP will be used automatically.

Please also note that compiling the project using Maven creates a FatJar.


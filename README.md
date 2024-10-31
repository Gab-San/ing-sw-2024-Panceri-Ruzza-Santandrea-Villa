# Implemented features
Complete rules + TUI + GUI + RMI + Socket + 2FA  (30)

### Advanced functionalities
Chat and Disconnection resilience

# How to launch the Server
1. Navigate to the folder containing the Server.jar
2. Run the command `java -jar Server.jar <RMI port> <TCP port> [Server IP]`  (the **order** of the arguments is **important**)
      - The IP can be omitted: the app will list the local machine IPs and prompt the user to select one of them (other than `localhost`)
3. If using the FatJar instead run: <br> `java -cp Server.jar it.polimi.ingsw.network.Server <RMI port> <TCP port> [Server IP]`

# How to launch the Client
1. Navigate to the folder containing the Client.jar
2. Run the command `java -jar Client.jar <Server port> <RMI | TCP> [Server IP] [myIP=<Client IP>]` (arguments can be in any order) 
      - The IPs are optional: if omitted, localhost will be used. 
      - The `myIP=<IP>` argument is also optional, as it is only used by RMI. It must be an IP reachable by the server.
3. If using the FatJar you only need to replace `Client.jar` with `FatJar.jar` in the command above.

### TUI notes
The TUI uses ANSI color codes. They should show correctly on Mac/Linux consoles, but they are disabled on Windows CMD by default. <br>
You can enable them by adding a DWORD with value `1` in the register: <br>
1. Press `Win+R`, then type `regedit` and navigate to `HKEY_CURRENT_USER\Console`
2. If the `VirtualTerminalLevel` value is not present then right-click on the `Console` directory and press `New -> DWORD` to add it with value `1`
3. Otherwise change its value to `1` 

You can list all available commands with their syntax explanation by typing the "help" command and pressing enter. <br>
Command syntax: (e.g. `view "me"|"board"|<player_id>`)
- view is the command keyword (it is always the first word)
- `<description>` indicates a command argument 
- `"value"` indicates an argument that must be the given value (**without** the "")
- `"value"|<description>|"other value"` indicates multiple alternatives for that argument (only use one)

### GUI notes
- Press enter in the nickname selection to attempt connecting to the server.
- Flip cards in your hand by hovering over the card and pressing F.
- Move in the playArea by pressing on the area background and using W A S D to move up/left/down/right.
- Press the player names in the sidebar to view their playArea.
- Select a card in your hand or on the board by clicking on it.
- You can use the dropdown menu in the chat to send messages to a specific player (that player must be connected)

# Notes (on both Client and Server)
Please note that compiling the project using Maven creates a FatJar.
- The IP arguments can be hostnames instead of IP addresses (a.b.c.d)
- All arguments other than IPs/Hostnames are validated on launch and the app will quit if an argument is invalid.
- If the IP/Hostname argument is invalid, the app will instead list all available IPs on the local machine and prompt the user to input a valid IP address.
- If a machine only has one IP (other than localhost) then that IP will be used automatically.

-----
NOTE: Codex Naturalis is a board game developed and published by Cranio Creations Srl. The graphic content of this project related to the board game is used with the prior approval of Cranio Creations Srl solely for educational purposes. Distribution, copying, or reproduction of the content and images in any form outside the project is prohibited, as is the redistribution and publication of the content and images for purposes other than those mentioned above. The commercial use of the aforementioned content is also prohibited.

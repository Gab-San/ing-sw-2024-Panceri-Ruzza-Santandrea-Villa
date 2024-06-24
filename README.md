# Implemented features
Complete rules + TUI + GUI + RMI + Socket + 2FA (30)

### Advanced functionalities
Chat and Disconnection resilience

# How to install the jars
1. Download the jar and the associated "src" folder
2. Place them in the same directory (the "src" folder contains the JSON files with the card information)

The JSON files can be modified by the user, but the changes made in the Server JSON files should be reflected in the Client JSON files for a correct display of the cards.
Please note, however, that the game should function even if the JSON files differ slightly between server and client.

# How to launch the Server
1. Navigate to the folder containing the Server.jar and the "src" folder
2. Run the command "`java -jar Server.jar <RMI port> <TCP port> [Server IP]`"  (the **order** of the arguments is **important**)
      - The IP can be omitted: the app will automatically use the local machine's IP (excluding localhost)
      - If the local machine has multiple IPs, then it will prompt the user to select one of them (other than localhost)
3. If using the FatJar.jar instead run "`java -cp Server.jar it.polimi.ingsw.network.Server <RMI port> <TCP port> [Server IP]`"


# How to launch the Client
1. Navigate to the folder containing the Server.jar and the "src" folder
2. Run the command "`java -jar Client.jar <Server port> <RMI | TCP> [Server IP] [myIP=<Client IP>]`" (arguments can be in any order) 
      - The IPs are optional: if omitted, localhost will be used. 
      - The "`myIP=<IP>`" argument is also optional, as it is only used by RMI. It must be an IP reachable by the server.
3. If using the FatJar.jar you only need to replace "Client.jar" with "FatJar.jar" in the command above.

# Notes (on both Client and Server)
The IP arguments can be hostnames instead of IP addresses (a.b.c.d)
All arguments other than IPs/Hostnames are validated and the app will quit if an argument is invalid.
If the IP/Hostname argument is invalid, the app will instead list all available IPs on the local machine and prompt the user to input a valid IP address.
If a machine only has one IP (other than localhost) then that IP will be used automatically.

# TUI notes
You can list all available commands with their syntax explanation by typing the "help" command.
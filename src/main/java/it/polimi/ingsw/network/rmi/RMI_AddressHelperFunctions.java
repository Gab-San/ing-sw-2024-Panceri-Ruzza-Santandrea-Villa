package it.polimi.ingsw.network.rmi;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Collection of static functions related to local IP address retrieval and handling.
 */
public interface RMI_AddressHelperFunctions {
    /**
     * Returns list of distinct IPs and hostnames on the local machine. <br>
     * Reads the IPs using java.net.NetworkInterface
     * @return list of distinct IPs and hostnames on the local machine.
     */
    static List<String> getListOfValidLocalIPs(){
        List<String> localIPs = new LinkedList<>();
        try {
            for (Iterator<NetworkInterface> it = NetworkInterface.getNetworkInterfaces().asIterator(); it.hasNext(); ) {
                NetworkInterface net = it.next();
                for (Iterator<InetAddress> iter = net.getInetAddresses().asIterator(); iter.hasNext(); ) {
                    InetAddress address = iter.next();
                    //we're discarding IPv6 addresses as well as the loopback address
                    boolean isRelevantAddress = !address.isLoopbackAddress() && address instanceof Inet4Address;
                    String hostName = isRelevantAddress ? address.getHostName() : "";
                    boolean is_WSL_Address = hostName.contains("mshome.net");
                    if(isRelevantAddress && !is_WSL_Address) {
                        localIPs.add(address.getHostAddress());
                        localIPs.add(hostName);
                    }
                }
            }
        }catch (SocketException ignored){}
        return localIPs.stream().distinct().toList(); //not using a Set to keep the IP-hostname pairings
    }

    /**
     * Returns true if address matches the loopback address (127.0.0.1) or "localhost".
     * @param address and IP address or a hostname
     * @return true if address matches the loopback address (127.0.0.1) or "localhost"
     */
    static boolean isLoopbackAddress(String address){
        return address.matches("127.0.0.1|localhost");
    }
}

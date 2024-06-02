package it.polimi.ingsw.network.rmi;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public interface RMI_AddressHelperFunctions {
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
        return localIPs.stream().distinct().toList();
    }

    static boolean isLoopbackAddress(String address){
        return address.matches("127.0.0.1|localhost");
    }
}

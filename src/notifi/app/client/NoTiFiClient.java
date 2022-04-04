package notifi.app.client;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class NoTiFiClient {


    public static void main(String[] args) throws IOException {

        // Validate the Arguments
            // Throw an Illegal Argument Exception if Invalid Arguments
        if((args.length != 3)){
            throw new IllegalArgumentException("Parameter(s): <Server IP/Name> <Server Port> <Local Client IP>");
        }


        // Save Arguments
            // Server IP or Name, Server Port, & Local Client
        InetAddress serverAddress = Inet4Address.getByName(args[0]);
        int servPort = Integer.parseInt(args[1]);
        InetAddress localClientAddress = InetAddress.getByAddress(args[2].getBytes(StandardCharsets.UTF_8));



        // Send a NoTiFi Register Message
            // w/ Random ID + Local Client + Local UDP Port


        // Repeatedly (While The User Doesn't Quit or

            // Receive Message

            // Decode the Message into its proper type

            // Print the Messages to the console until the user quits

        //





    }


}

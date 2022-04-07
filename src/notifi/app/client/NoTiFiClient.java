package notifi.app.client;

import addatude.serialization.ValidationException;
import notifi.serialization.NoTiFiACK;
import notifi.serialization.NoTiFiMessage;
import notifi.serialization.NoTiFiRegister;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class NoTiFiClient {

    private static final int TIMEOUT = 3000;
    private static final int MAXTRIES = 2;

    public static void main(String[] args) throws IOException {

        // Validate the Arguments
            // Throw an Illegal Argument Exception if Invalid Arguments
        if((args.length != 3)){
            throw new IllegalArgumentException("Parameter(s): <Server IP/Name> <Server Port> <Local Client IP>");
        }


        // Save Arguments
            // Server IP or Name, Server Port, & Local Client
        Inet4Address serverAddress = (Inet4Address) Inet4Address.getByName(args[0]);
        int servPort = Integer.parseInt(args[1]);
        Inet4Address localClientAddress = (Inet4Address) Inet4Address.getByAddress(args[2].getBytes(StandardCharsets.UTF_8));
        // TODO is type casting allowed here? ^


        // Establishing Datagram Socket
        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(TIMEOUT);

        // To help keep track of tries
        int tries = 0;
        boolean receivedResponse = false;


        // Random Message ID
        int msgId = 234;


        // Send a NoTiFi Register Message
            // w/ Random ID + Local Client + Local UDP Port
        NoTiFiRegister noTiFiRegister = new NoTiFiRegister(msgId, localClientAddress, servPort);
        byte[] bytesToSend = noTiFiRegister.encode();

        // Datagram Packet to Send
        DatagramPacket sentPacket = new DatagramPacket(bytesToSend, bytesToSend.length, serverAddress, servPort);

        // Datagram Packet to Receive
        // Todo This may need modification. 100 bytes is random
        DatagramPacket receivedPacket = new DatagramPacket(new byte[100], 100);

        // Repeatedly (While The User Doesn't Quit or
        do {
            // Receive Message
            socket.send(sentPacket);

            try {
                socket.receive(receivedPacket);

                if(!receivedPacket.getAddress().equals(serverAddress)){
                    throw new IOException("Received a packet from an unknown source");
                }

                NoTiFiACK noTiFiMessage = (NoTiFiACK) NoTiFiMessage.decode(receivedPacket.getData());


                //TODO we have to do something here??


                receivedResponse = true;

            } catch(InterruptedIOException e){
                tries += 1;
            }


        }while((!receivedResponse) && (tries < MAXTRIES));


        if(receivedResponse){

            // Decode the Message into its proper type


            // Print the Messages to the console until the user quits

        }
        else{
            System.out.println("Unable to register");
        }








    }


}

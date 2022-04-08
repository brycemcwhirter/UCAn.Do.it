package notifi.app.client;

import addatude.serialization.ValidationException;
import notifi.serialization.*;

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
        Inet4Address localClientAddress = (Inet4Address) Inet4Address.getByName(args[2]);
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
        // TODO UDP SIZE??
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


                try {

                    // Decode the Message You Received
                    NoTiFiMessage newMessage = NoTiFiMessage.decode(receivedPacket.getData());

                    // If you received an ACK
                    if(newMessage.getCode() == NoTiFiACK.ACK_CODE){

                        // test if the received ACK is the same as the sent ACK
                        // If they are not the same, print error message to console
                        // & terminate

                        if(newMessage.getMsgId() != noTiFiRegister.getMsgId()){
                            System.out.println("Unexpected MSG ID");
                            return;
                        }

                    }



                    // If you received a Location Addition
                    // Print the location addition following specification
                    else if(newMessage.getCode() == NoTiFiLocationAddition.LOCATION_ADDITION_CODE) {

                        NoTiFiLocationAddition LocationAdditionMessage = (NoTiFiLocationAddition) newMessage;

                        System.out.println("Addition");
                        System.out.println("Latitude & Longitude");
                        System.out.println(LocationAdditionMessage.getLatitude() + " " + LocationAdditionMessage.getLongitude());
                        System.out.println("Name & Description");
                        System.out.println(LocationAdditionMessage.getLocationName() + " " + LocationAdditionMessage.getLocationDescription());

                    }




                    // If you received an Error
                    // Print the error message to the console
                    else if(newMessage.getCode() == NoTiFiError.ERROR_CODE) {

                        NoTiFiError error = (NoTiFiError) newMessage;
                        System.out.println(error.getErrorMessage());

                    }




                    // If you received any other NoTiFi type
                    // print Unexpected Message type
                    else {

                        System.out.println("Unexpected Message Type");

                    }




                }
                // CATCH the parsing/validating error caused by decode
                    // print "Unexpected message type"
                // TODO catch validation exception as well?
                catch(IllegalArgumentException e){
                    System.out.println("Unable to parse message: "+ e.getMessage());

                }



                receivedResponse = true;

            } catch(InterruptedIOException e){
                tries += 1;
            }


        }while((!receivedResponse) && (tries < MAXTRIES));



        if(!receivedResponse){
            System.out.println("Unable to register");
        }


    }


}

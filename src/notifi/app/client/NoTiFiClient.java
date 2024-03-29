/*

 Author: Bryce McWhirter
 Assignment: Program 5
 Class: Data Communications

 */

package notifi.app.client;


import notifi.serialization.*;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.util.Arrays;


/**
 * The NoTiFi Client sends Notification
 * of location additions from the Addatude
 * Protocol
 */
public class NoTiFiClient {




    // Describes the Timeout Length
    private static final int TIMEOUT = 3000;

    // Describes the Maximum Number of Tries
    private static final int MAXTRIES = 2;

    // Describes the Largest Valid MSG ID Value
    private static final int MAX_MSG_ID_VALUE = 255;


    // Describes the Largest Number of bytes to be read
    private static final int MAX_READ_SIZE = 65507;







    public static void main(String[] args) throws IOException {

        // Validate the Arguments
            // Throw an Illegal Argument Exception if Invalid Arguments
        if((args.length != 3)){
            throw new IllegalArgumentException("Parameter(s): <Server IP/Name> <Server Port> <Local Client IP>");
        }


        // Establishing Datagram Socket
        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(TIMEOUT);


        // Save Arguments
            // Server IP or Name, Server Port, & Local Client
        Inet4Address serverAddress = (Inet4Address) Inet4Address.getByName(args[0]);
        Inet4Address localClientAddress = (Inet4Address) Inet4Address.getByName(args[2]);

        // Establish Port Values
        int localPort = socket.getLocalPort();
        int servPort = Integer.parseInt(args[1]);

        // To help keep track of tries
        int tries = 0;
        boolean receivedResponse = false;


        // Random Message ID
        int msgId = (int) (Math.random() * (MAX_MSG_ID_VALUE + 1));


        // Send a NoTiFi Register Message
            // w/ Random ID + Local Client + Local UDP Port
        NoTiFiRegister noTiFiRegister = new NoTiFiRegister(msgId, localClientAddress, localPort);
        byte[] bytesToSend = noTiFiRegister.encode();

        // Datagram Packet to Send
        DatagramPacket sentPacket = new DatagramPacket(bytesToSend, bytesToSend.length, serverAddress, servPort);

        // Datagram Packet to Receive
        DatagramPacket receivedAck = new DatagramPacket(new byte[NoTiFiACK.ACK_SIZE], NoTiFiACK.ACK_SIZE);

        // Repeatedly (While The User Doesn't Quit or
        do {


            // Receive Message
            socket.send(sentPacket);

            try {

                socket.receive(receivedAck);

                // Decode the Message You Received
                NoTiFiMessage newMessage = NoTiFiMessage.decode(receivedAck.getData());


                // If you received an ACK
                if(newMessage.getCode() == NoTiFiACK.ACK_CODE){

                    // test if the received ACK is the same as the sent ACK
                    // If they are not the same, print error message to console
                    // & terminate

                    if(newMessage.getMsgId() != noTiFiRegister.getMsgId()){
                        System.err.println("Unexpected MSG ID");
                    }


                    receivedResponse = true;

                }

                System.out.println("Connection Occurred");



            } catch(InterruptedIOException e){
                tries += 1;
            }


        }while((!receivedResponse) && (tries < MAXTRIES));


        if(!receivedResponse){
            System.err.println("Unable to register");
            return;
        }

        // Reset the timeout to infinite
        socket.setSoTimeout(0);



        while(true){

            DatagramPacket receivedPacket = new DatagramPacket(new byte[MAX_READ_SIZE], MAX_READ_SIZE);
            socket.receive(receivedPacket);
            byte[] receivedBuf = Arrays.copyOf(receivedPacket.getData(), receivedPacket.getLength());

            try {

                NoTiFiMessage newMessage = NoTiFiMessage.decode(receivedBuf);


                // If you received a Location Addition
                // Print the location addition following specification
                if (newMessage.getCode() == NoTiFiLocationAddition.LOCATION_ADDITION_CODE) {
                    NoTiFiLocationAddition LocationAdditionMessage = (NoTiFiLocationAddition) newMessage;
                    System.out.println(LocationAdditionMessage);
                }


                // If you received an Error
                // Print the error message to the console
                else if (newMessage.getCode() == NoTiFiError.ERROR_CODE) {
                    NoTiFiError error = (NoTiFiError) newMessage;
                    System.err.println(error.getErrorMessage());
                }


                // If you received any other NoTiFi type
                // print Unexpected Message type
                else if(newMessage.getCode() == NoTiFiRegister.REGISTER_CODE){
                    System.err.println("Unexpected Message Type");
                }


            }catch(IllegalArgumentException e){
                System.err.println("Unable to parse message: " + e.getMessage());
            }

        }







    }


}

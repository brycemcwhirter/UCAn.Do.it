package notifi.app.server;


import addatude.serialization.ValidationException;
import notifi.serialization.NoTiFiLocationAddition;
import notifi.serialization.NoTiFiMessage;
import notifi.serialization.NoTiFiRegister;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class NoTiFiServer {


    private static final int MAX_READ_SIZE = 65507;



    public static void main(String[] args) throws IOException {

        // Get the Port & Create the Socket
        int serverPort = Integer.parseInt(args[0]);
        DatagramSocket sock = new DatagramSocket(serverPort);



        byte[] inBuffer = new byte[MAX_READ_SIZE];


       while(true){

           // Receive the packet from the client
           DatagramPacket packet = new DatagramPacket(inBuffer, inBuffer.length);
           sock.receive(packet);
           byte[] encodedMsg = Arrays.copyOfRange(packet.getData(), 0, packet.getLength());



           // Decode the Message
           NoTiFiMessage msg = NoTiFiMessage.decode(encodedMsg);


           if(msg.getCode() == NoTiFiRegister.REGISTER_CODE){


               // Get the Address involved with the NoTiFi Register message


               // If the address is multicast
                // Send Error Message to Client "Bad Address"


               // If the port is incorrect
                // Send Error Message to Client "Incorrect Port"


               // If the register address & port is already registered
                // Send error message to Client "Already Registered"


               // else

                // Add the specified Register Address & Port to client List


                // Send the ACK with MSG ID from the received message to the client


                // If register & source address do not match
                    // log warning entry on specification



           }


           // If a location has been added in the Addatude protocol

            // Send a NoTiFi location addition message to all addresses registered.

           // TODO how to see if new location has been added to Addatude.






           else{

               // Send "Unexpected Message type: <rcvd code>" to client

           }



           }

       }


    }



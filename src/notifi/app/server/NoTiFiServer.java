package notifi.app.server;


import notifi.serialization.NoTiFiACK;
import notifi.serialization.NoTiFiError;
import notifi.serialization.NoTiFiMessage;
import notifi.serialization.NoTiFiRegister;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;





public class NoTiFiServer {



    private static final int VALID_PORT_NOTIFI = 5000;

    private static ArrayList registeredAddresses = new ArrayList();



    public static void handleClientConnection(DatagramSocket socket, byte[] encodedMsg, Logger logger, int serverPort) throws IOException {

        // Decode the Message
        NoTiFiMessage msg = NoTiFiMessage.decode(encodedMsg);




        if(msg.getCode() != NoTiFiRegister.REGISTER_CODE) {
            NoTiFiError errorMsg = new NoTiFiError(msg.getMsgId(), "Unexpected Message Type: "+ msg.getCode());
            socket.send(new DatagramPacket(errorMsg.encode(), errorMsg.encode().length, socket.getInetAddress(), serverPort));
        }


        // Get the Address involved with the NoTiFi Register message
        NoTiFiRegister regMsg = (NoTiFiRegister) msg;



        // If the address is Multicast Address
        if(regMsg.getAddress().isMulticastAddress()) {
            // Send Error Message to Client "Bad Address"
            NoTiFiError errorMsg = new NoTiFiError(msg.getMsgId(), "Bad Address");
            socket.send(new DatagramPacket(errorMsg.encode(), errorMsg.encode().length, regMsg.getAddress(), serverPort));


        }




        // If the port is incorrect
        else if(regMsg.getPort() != VALID_PORT_NOTIFI){
            // Send Error Message to Client "Incorrect Port"
            NoTiFiError errorMsg = new NoTiFiError(msg.getMsgId(), "Incorrect Port");
            socket.send(new DatagramPacket(errorMsg.encode(), errorMsg.encode().length, regMsg.getAddress(), serverPort));


        }





        // If the register address & port is already registered
        else if(registeredAddresses.contains(regMsg.getAddress())){

            // Send error message to Client "Already Registered"
            NoTiFiError errorMsg = new NoTiFiError(msg.getMsgId(), "Already Registered");
            socket.send(new DatagramPacket(errorMsg.encode(), errorMsg.encode().length, regMsg.getAddress(), serverPort));



        }



        // else
        else {

            // Add the specified Register Address & Port to client List
            registeredAddresses.add(regMsg.getAddress());


            // Send the ACK with MSG ID from the received message to the client
            DatagramPacket ackResponse = new DatagramPacket(new NoTiFiACK(msg.getMsgId()).encode(), NoTiFiACK.ACK_SIZE, regMsg.getAddress(), serverPort);
            socket.send(ackResponse);



            // If register & source address do not match
            if(socket.getInetAddress() != regMsg.getAddress()){
                // log warning entry on specification
                logger.warning("Register and Source Address mismatch: "+regMsg.getAddress()+" "+socket.getInetAddress());

            }

        }


    }



    public static void handleNewLocationAddition(){

        // If a location has been added in the Addatude protocol

        // Send a NoTiFi location addition message to all addresses registered.

        // TODO how to see if new location has been added to Addatude.

    }


}













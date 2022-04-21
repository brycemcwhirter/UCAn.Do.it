/*

 Author: Bryce McWhirter
 Assignment: Program 6
 Class: Data Communications

 */


package notifi.app.server;


import addatude.serialization.LocationRecord;
import notifi.serialization.*;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.Logger;


/**
 * The NoTiFi Server handles the connection
 * from the NoTiFi Client
 */
public class NoTiFiServer {

    // A List of Registered Internet Addresses
    private static final ArrayList<Inet4Address> registeredAddresses = new ArrayList<>();


    // The Datagram Socket Connection
    private static DatagramSocket datagramSocket;


    // The received register message
    public static NoTiFiRegister regMsg;


    /**
     * The Constructor sets up the Datagram Socket &
     * the register message
     * @param datagramSocket the socket
     * @param encodedMsg the encoded message
     */
    public NoTiFiServer(DatagramSocket datagramSocket, byte[] encodedMsg) {
        NoTiFiServer.datagramSocket = datagramSocket;
        regMsg = (NoTiFiRegister) NoTiFiMessage.decode(encodedMsg);
    }






    /**
     * Handles the Initial Client Connection made to the server
     * @param logger the logger for logging messages
     * @param datagramPacket
     * @throws IOException
     *      if an I/O error occurs
     */
    public void handleClientConnection(Logger logger, DatagramPacket datagramPacket) throws IOException {


        // If the address is Multicast Address
        // Send Error Message to Client "Bad Address"

        if(regMsg.getAddress().isMulticastAddress()) {
            NoTiFiError errorMsg = new NoTiFiError(regMsg.getMsgId(), "Bad Address");
            datagramSocket.send(new DatagramPacket(errorMsg.encode(), errorMsg.encode().length, regMsg.getAddress(), regMsg.getPort()));
        }




        // If the port is incorrect
        // Send Error Message to Client "Incorrect Port"
        else if(regMsg.getPort() != datagramPacket.getPort()){
            NoTiFiError errorMsg = new NoTiFiError(regMsg.getMsgId(), "Incorrect Port");
            datagramSocket.send(new DatagramPacket(errorMsg.encode(), errorMsg.encode().length, regMsg.getAddress(), regMsg.getPort()));
        }




        // If the register address & port is already registered
        // Send error message to Client "Already Registered"
        else if(registeredAddresses.contains(regMsg.getAddress())){
            NoTiFiError errorMsg = new NoTiFiError(regMsg.getMsgId(), "Already Registered");
            datagramSocket.send(new DatagramPacket(errorMsg.encode(), errorMsg.encode().length, regMsg.getAddress(), regMsg.getPort()));
        }



        // else
        else {

            // Add the specified Register Address & Port to client List
            registeredAddresses.add(regMsg.getAddress());


            // Send the ACK with MSG ID from the received message to the client
            DatagramPacket ackResponse = new DatagramPacket(new NoTiFiACK(regMsg.getMsgId()).encode(), NoTiFiACK.ACK_SIZE, regMsg.getAddress(), regMsg.getPort());
            datagramSocket.send(ackResponse);



            // If register & source address do not match
            // log warning entry on specification
            if(datagramPacket.getAddress() != regMsg.getAddress()){
                logger.warning("Register and Source Address mismatch: "+regMsg.getAddress()+" "+datagramPacket.getAddress());

            }

        }


    }


    /**
     * This method sends a notification of a new
     * location addition to the NoTiFi Client
     * @param newLocationRecord the new location record added
     * @throws IOException
     *      if an I/O Error Occurs
     */
    public static void handleNewLocationAddition(LocationRecord newLocationRecord) throws IOException {

        byte[] msgToSend = new NoTiFiLocationAddition(regMsg.getMsgId(), (int) newLocationRecord.getUserId(),
                Double.parseDouble(newLocationRecord.getLongitude()), Double.parseDouble(newLocationRecord.getLatitude()),
                newLocationRecord.getLocationName(), newLocationRecord.getLocationDescription()).encode();


        datagramSocket.send(new DatagramPacket(msgToSend, msgToSend.length, regMsg.getAddress(), regMsg.getPort()));
    }


}













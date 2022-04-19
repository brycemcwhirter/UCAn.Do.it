package notifi.app.server;


import notifi.serialization.NoTiFiACK;
import notifi.serialization.NoTiFiError;
import notifi.serialization.NoTiFiMessage;
import notifi.serialization.NoTiFiRegister;

import java.io.IOException;
import java.net.*;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Arrays;
import java.util.logging.Logger;

public class NoTiFiServer {


    private static final int MAX_READ_SIZE = 65507;

    private static final int VALID_PORT = 5000;

    private static final int TIMEOUT = 3000;


    public static void handleClient(DatagramSocket socket, Logger logger, int serverPort) throws IOException {


        byte[] inBuffer = new byte[MAX_READ_SIZE];





            // Receive the packet from the client
            DatagramPacket packet = new DatagramPacket(inBuffer, inBuffer.length);
            socket.receive(packet);
            byte[] encodedMsg = Arrays.copyOfRange(packet.getData(), 0, packet.getLength());

            // Decode the Message
            NoTiFiMessage msg = NoTiFiMessage.decode(encodedMsg);


            if(msg.getCode() == NoTiFiRegister.REGISTER_CODE){


                // Get the Address involved with the NoTiFi Register message
                NoTiFiRegister regMsg = (NoTiFiRegister) msg;

                // Prep a Datagram Channel
                DatagramChannel datagramChannel = DatagramChannel.open();
                datagramChannel.configureBlocking(false);


                // If the address is multicast
                if(regMsg.getAddress().isMulticastAddress()) {
                    // Send Error Message to Client "Bad Address"
                    NoTiFiError errorMsg = new NoTiFiError(msg.getMsgId(), "Bad Address");
                    socket.send(new DatagramPacket(errorMsg.encode(), errorMsg.encode().length, regMsg.getAddress(), serverPort));


                }

                // If the port is incorrect
                else if(regMsg.getPort() != VALID_PORT){
                    // Send Error Message to Client "Incorrect Port"
                    NoTiFiError errorMsg = new NoTiFiError(msg.getMsgId(), "Incorrect Port");
                    socket.send(new DatagramPacket(errorMsg.encode(), errorMsg.encode().length, regMsg.getAddress(), serverPort));


                }


                // If the register address & port is already registered
                else if(datagramChannel.isRegistered()){

                    // Send error message to Client "Already Registered"
                    NoTiFiError errorMsg = new NoTiFiError(msg.getMsgId(), "Already Registered");
                    socket.send(new DatagramPacket(errorMsg.encode(), errorMsg.encode().length, regMsg.getAddress(), serverPort));



                }


                // else
                else {

                    // Add the specified Register Address & Port to client List


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


            // If a location has been added in the Addatude protocol

            // Send a NoTiFi location addition message to all addresses registered.

            // TODO how to see if new location has been added to Addatude.






            else{

                // Send "Unexpected Message type: <rcvd code>" to client
                NoTiFiError errorMsg = new NoTiFiError(msg.getMsgId(), "Unexpected Message Type: "+ msg.getCode());
                //socket.send(new DatagramPacket(errorMsg.encode(), errorMsg.encode().length, regMsg.getAddress(), serverPort));
                // todo how to return back to the address

            }



        }



    }










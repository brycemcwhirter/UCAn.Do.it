package notifi.app.client;

import notifi.serialization.NoTiFiError;
import notifi.serialization.NoTiFiLocationAddition;
import notifi.serialization.NoTiFiMessage;
import notifi.serialization.NoTiFiRegister;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class NoTiFiMulticastClient {


    // Describes the Largest Number of bytes to be read
    private static final int MAX_READ_SIZE = 65507;


    public static void main(String[] args) throws IOException {

        // Validate Argument Length
        if(args.length != 2){
            throw new IllegalArgumentException("Parameters: <MulticastAddress> <Port>");
        }


        // Save Arguments
        InetAddress multicastAddress = InetAddress.getByName(args[0]);
        int destPort = Integer.parseInt(args[1]);


        // Validate Arguments
        if(!multicastAddress.isMulticastAddress()){
            throw new IllegalArgumentException("Attach a Multicast Address");
        }



        // Establish the Multicast Socket
        InetSocketAddress socketAddress = new InetSocketAddress(multicastAddress, destPort);
        MulticastSocket socket = new MulticastSocket(socketAddress);



        while(true){

            // Receive a Packet
            DatagramPacket receivedPacket = new DatagramPacket(new byte[MAX_READ_SIZE], MAX_READ_SIZE);
            socket.receive(receivedPacket);
            byte[] receivedBuf = Arrays.copyOf(receivedPacket.getData(), receivedPacket.getLength());



            try {

                // Decode the NoTiFi Message
                NoTiFiMessage newMessage = NoTiFiMessage.decode(receivedBuf);



                // Protocol for a Location Addition
                if (newMessage.getCode() == NoTiFiLocationAddition.LOCATION_ADDITION_CODE) {
                    NoTiFiLocationAddition LocationAdditionMessage = (NoTiFiLocationAddition) newMessage;
                    System.out.println(LocationAdditionMessage);
                }


                // Protocol for an Error
                else if (newMessage.getCode() == NoTiFiError.ERROR_CODE) {
                    NoTiFiError error = (NoTiFiError) newMessage;
                    System.err.println(error.getErrorMessage());
                }


                // Protocol for any other message type
                else if(newMessage.getCode() == NoTiFiRegister.REGISTER_CODE){
                    System.err.println("Unexpected Message Type");
                }


            }catch(IllegalArgumentException e){
                System.err.println("Unable to parse message: " + e.getMessage());
            }


        }

    }


}

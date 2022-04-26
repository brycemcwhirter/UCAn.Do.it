/*
 *
 * Author: Bryce McWhirter
 * Assignment: Program 7
 * Class: Data Communications
 *
 */

package notifi.app.client;

import notifi.serialization.NoTiFiError;
import notifi.serialization.NoTiFiLocationAddition;
import notifi.serialization.NoTiFiMessage;
import notifi.serialization.NoTiFiRegister;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Arrays;


/**
 * Handles the Initialization of variables
 * and pre necessary steps for the NoTiFi Connection
 */
public class NoTiFiMulticastClient {



    public static void main(String[] args) {

        // Validate Argument Length
        if (args.length != 2) {
            throw new IllegalArgumentException("Parameters: <MulticastAddress> <Port>");
        }


        // Save Arguments
        InetSocketAddress multicastAddress = new InetSocketAddress(args[0], 0);
        int destPort = Integer.parseInt(args[1]);


        // Validate Arguments
        if (!multicastAddress.getAddress().isMulticastAddress()) {
            throw new IllegalArgumentException("Address is not multicast");
        }

        // The Console Reader for reading the statement "quit"
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));



        // Generating two threads

        // One for handling NoTiFi Messages
        new Thread(new handleNoTiFiMessages(multicastAddress, destPort)).start();

        // The other for handling user input
        new Thread(new handleUserInput(consoleReader)).start();


    }

}


/**
 * This method handles the NoTiFi Messages
 * being received.
 */
class handleNoTiFiMessages implements Runnable {

    // Describes the Largest Number of bytes to be read
    private static final int MAX_READ_SIZE = 65507;

    // The destination Port
    int destPort;

    // The socket with the multicast address
    InetSocketAddress multicastAddress;


    /**
     * Constructs an instance of the class
     * @param multicastAddress the multicast socket address
     * @param destPort the destination port
     */
    handleNoTiFiMessages(InetSocketAddress multicastAddress, int destPort){
        this.multicastAddress = multicastAddress;
        this.destPort = destPort;
    }




    @Override
    public void run() {

        while(true) {

            try (MulticastSocket sock = new MulticastSocket(destPort)) {
                sock.joinGroup(multicastAddress, null);

                // Receive a Packet
                DatagramPacket receivedPacket = new DatagramPacket(new byte[MAX_READ_SIZE], MAX_READ_SIZE);
                sock.receive(receivedPacket);
                byte[] receivedBuf = Arrays.copyOf(receivedPacket.getData(), receivedPacket.getLength());

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
                else if (newMessage.getCode() == NoTiFiRegister.REGISTER_CODE) {
                    System.err.println("Unexpected Message Type");
                }


            } catch (IllegalArgumentException | IOException e) {
                System.err.println("Unable to parse message: " + e.getMessage());
            }
        }


    }
}


/**
 * Handles User Input
 */
class handleUserInput implements Runnable{

    // The Quit Option
    private static final String QUIT_OPTION = "quit";

    // The Console Reader
    BufferedReader consoleReader;


    handleUserInput(BufferedReader consoleReader){
        this.consoleReader = consoleReader;
    }


    @Override
    public void run() {

        try {
            // Retrieve a line
            String line = consoleReader.readLine();

            // If the user wishes to quit
            // exit the program
            if(line.equals(QUIT_OPTION)){
                System.exit(0);
            }

        } catch (IOException ignored) {
        }

    }
}
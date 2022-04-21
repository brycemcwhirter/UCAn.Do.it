/*
 * Author: Bryce McWhirter
 * Assignment: Program 2
 * Class: Data Communications
 *
 */

/*
 * Programming Compadre: John Harrison
 */

package addatude.app.client;

import addatude.serialization.*;
import addatude.serialization.Error;

import java.io.*;
import java.net.Socket;
import java.util.Objects;


/**
 * The Client handles the location records being sent
 * to a server. The User has the ability to see all
 * the locations input to a server or add a new location
 * to the server.
 */
public class Client {

    /**
     * Describes the YES option
     */
    private static final char YES = 'y';


    /**
     * Describes ALL Operation command
     */
    private static final String ALL_OPERATION = "ALL";


    /**
     * Describes NEW Operation command
     */
    private static final String NEW_OPERATION = "NEW";











    /**
     * Returns the MapId read in by the user
     * @param consoleReader the console reader accepting user input.
     * @return
     *      the mapID read by the user or -1 if a Validation Exception
     *      occurs
     */
    static int getMapID(BufferedReader consoleReader) {
        // Input Appropriate Attribute Values &
        // Ask for the MAP ID
        System.out.print("Map ID > ");
        int mapID = 0;

        try {
            String candidate = consoleReader.readLine();
            AddatudeValidator.validUnsignedInteger("Map ID", candidate);
            mapID = Integer.parseInt(candidate);
        }

        catch (ValidationException e){
            System.err.println("Invalid User Input: " + e.getMessage());
            System.err.flush();
            return -1;
        }

        catch(IOException e) {
            e.printStackTrace();
        }


        // Restart the cycle if map id isn't VALID_MAP_ID
        return mapID;
    }





    /**
     * This method is executed if the user decides to run the NEW operation.
     * The user inputs all the necessary parameters for the new location record
     * and a validation exception is thrown if any of these parameters are invalid.
     *
     *
     * @param mapId The MAP ID associated with the NewLocation message
     * @param consoleReader the console reader that reads input from the user
     * @return
     *      The encoded new operation message or NULL if a validation exception occurs.
     */
    private static byte[] newOperation(int mapId, BufferedReader consoleReader) throws IOException {

        // Generating Output Variables
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MessageOutput out = new MessageOutput(byteArrayOutputStream);

        try {

            // Reading the User ID
            System.out.print("User ID > ");
            String userID;
            userID = consoleReader.readLine();
            AddatudeValidator.validUnsignedInteger("User ID", userID);


            // Reading Longitude
            System.out.print("Longitude > ");
            String readLongitude = consoleReader.readLine();
            AddatudeValidator.validLongitude(readLongitude);

            // Reading Latitude
            System.out.print("Latitude > ");
            String readLatitude = consoleReader.readLine();
            AddatudeValidator.validLongitude(readLatitude);

            // Reading the Location Name
            System.out.print("Name > ");
            String readLocationName = consoleReader.readLine();
            AddatudeValidator.validString("Location Name", readLocationName);
            AddatudeValidator.validUnsignedInteger("Location Name Size", String.valueOf(readLocationName.length()));


            // reading the location description
            System.out.print("Desc > ");
            String readLocationDesc = consoleReader.readLine();
            AddatudeValidator.validString("Location Description", readLocationDesc);
            AddatudeValidator.validUnsignedInteger("Location Description Size", String.valueOf(readLocationDesc.length()));

            // Generating the Location Record & the New Location.
            LocationRecord loc = new LocationRecord(Long.parseLong(userID), readLongitude, readLatitude, readLocationName, readLocationDesc);
            NewLocation newLocation = new NewLocation(mapId, loc);

            // Encoding the new location to the output stream.
            newLocation.encode(out);
        }

        catch (ValidationException e){
            // If a Validation Error Occurs on the client side
            System.err.println("Invalid User Input: " + e.getMessage());
            System.err.flush();
            return null;
        }



        return byteArrayOutputStream.toByteArray();

    }




    /**
     * This method is called if the user decides to run ALL operation.
     * A location request is created with the mapID and is encoded to a
     * MessageOutput stream. That message is then returned to the user
     * to be encoded for the user.
     *
     * @param mapId The mapID to be sent with the message
     * @return a set of bytes describing an ALL message or null
     */
    private static byte[] allOperation(int mapId)  {

        // The Location Request
        LocationRequest req;
        try {
            req = new LocationRequest(mapId);
        } catch (ValidationException e) {
            // If a Validation Error Occurs on the client side
            System.err.println("Invalid User Input: " + e.getMessage());
            System.err.flush();
            return null;
        }


        // The Message Output Stream.
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MessageOutput out = new MessageOutput(byteArrayOutputStream);

        try {
            req.encode(out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }









    /**
     * Returns the operation inputted by the user. This method repeats until the
     * user enters a valid operation. The Valid operations are ALL & NEW.
     *
     * @param consoleReader The Console Reader that reads the input
     * @return
     *      a boolean describing if the operation read is the new operation.
     */
    private static boolean getOperation(BufferedReader consoleReader) {

        // Describes if the input is valid operation
        boolean validOperation;

        // Describes if the user selects the new operation.
        boolean isNewOperation = false;

        // Continue to ask the user for the operation
        // until a valid operation is given.
        do {

            //prompt user for type of message (ALL OR NEW)
            System.out.print("Operation > ");
            String operation = null;
            try {
                operation = consoleReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }


            switch (Objects.requireNonNull(operation)) {
                case ALL_OPERATION -> validOperation = true;

                case NEW_OPERATION -> {
                    isNewOperation = true;
                    validOperation = true;
                }
                default -> {
                    System.err.println("Invalid User Input: " + "Unknown Operation \""+ operation+"\"");
                    System.err.flush();
                    validOperation = false;
                }
            }
        } while (!validOperation);

        return isNewOperation;
    }





    /**
     * The Main Method describes the process between
     * the user adding & viewing location records from a specified
     * server.
     *
     * @param args The arguments to the program (server identity) (server port)
     */
    public static void main(String[] args) throws IOException {

        // The Input Reader from the console
        BufferedReader consoleReader;


        // Describes if the user would like to execute the new operation
        boolean isNewOperation;

        // The Message to be sent to the Server
        byte[] msg;

        // The Map ID inputted by the user
        int mapId;

        // Option to continue execution
        char option = 'n';




        // Test for Valid Arguments (Server Identity & Server Port)
        if(args.length != 2){
            System.out.println("Run As: java Client.java <ServerIdentity> <ServerPort>");
            return;
        }

        // The Server & Server Port
        String server = args[0];
        int serverPort = Integer.parseInt(args[1]);



        do {
            consoleReader = new BufferedReader(new InputStreamReader(System.in));

            // Get the Operation & MapID
            isNewOperation = getOperation(consoleReader);
            mapId = getMapID(consoleReader);



            if(mapId != -1) {


                // Call the correct operation and create the message
                // to be encoded to the server
                if (isNewOperation) {
                    msg = newOperation(mapId, consoleReader);
                } else {
                    msg = allOperation(mapId);
                }


                // Don't do anything if the message received back is null.
                // This is possible due to the new operation & all operation
                // methods returning a null byte array if there is a validation
                // exception. We simply just restart the process of user input
                // and continue executing until we get valid input from the user


                if (Objects.nonNull(msg)) {

                    try {
                        // Establishing the connection
                        Socket socket = new Socket(server, serverPort);
                        InputStream in = socket.getInputStream();
                        OutputStream out = socket.getOutputStream();


                        // Sending the request
                        out.write(msg);


                        // Handle The Message Received from the Server
                        // Handles the Message with the given input stream
                        MessageInput messageInput = new MessageInput(in);

                        try {
                            // The received message from the input stream.
                            Message receivedMessage = Message.decode(messageInput);


                            // If you received a Location Response,
                            // Output each location record
                            if (receivedMessage.getOperation().equals(LocationResponse.OPERATION)) {
                                for(LocationRecord loc : ((LocationResponse) receivedMessage).getLocationRecordList()){
                                    System.out.println(loc);
                                }
                            }

                            // If you received an Error Message from the server
                            // Handle the error message
                            else if (receivedMessage.getOperation().equals(Error.OPERATION)) {
                                Error error = (Error) receivedMessage;
                                System.out.println("Error: " + error.getErrorMessage());
                            }
                        }

                        // If you received an unexpected message,
                        // catch the validation exception thrown and handle
                        // the unexpected message
                        catch (ValidationException e){
                            System.err.println("Invalid User Input: " + messageInput);
                            System.err.flush();
                        }


                    } catch (IOException e) {
                        System.err.println("Unable to Communicate: "+ e.getMessage());
                        System.err.flush();
                    }

                }


            }


        // TO DO Figure out Why system error aand print ln are not printing in order

        try {
            System.out.print("Would you like to continue? > ");
            option = (char) consoleReader.read();
        } catch (IOException e) {
            e.printStackTrace();
        }



        // Continue execution if the user
            // inputs an affirmative option
        }while(option == YES);


    }




}









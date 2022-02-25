/************************************************
 *
 * Author: Bryce McWhirter
 * Assignment: Program 2
 * Class: Data Communications
 *
 ************************************************/

package addatude.app.client;

import addatude.serialization.*;

import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.util.Objects;
import java.util.Scanner;




public class Client {

    private static final char YES = 'y';
    private static final int VALID_MAP_ID = 345;
    private static final int VALID_USER_ID = 143; //653 % 255

    private static final String ALL_OPERATION = "ALL";
    private static final String NEW_OPERATION = "NEW";


    static class ClientErrorMessageHandler{

        static void localClientValidation(String errorMessage){
            System.out.println("Invalid User Input: " + errorMessage);
        }

        static void handleErrorMessage(String errorMessage){
            System.out.println("Error: " + errorMessage);
        }

    }


    static int getMapID(BufferedReader consoleReader) throws IOException {
        // Input Appropriate Attribute Values &
        // Ask for the MAP ID
        System.out.print("Map ID > ");
        int mapID = Integer.parseInt(consoleReader.readLine());

        // Restart the cycle if map id isn't VALID_MAP_ID
        return mapID;
    }







    private static byte[] newOperation(int mapId, BufferedReader consoleReader) throws IOException {

        // Generating Output Variables
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MessageOutput out = new MessageOutput(byteArrayOutputStream);

        try {

            System.out.print("User ID > ");
            String userID = consoleReader.readLine();
            if(Integer.parseInt(userID) != VALID_USER_ID){
                ClientErrorMessageHandler.localClientValidation("Invalid User ID");
                return null;
            }


            // Reading Longitude
            System.out.print("Longitude > ");
            String readLongitude = consoleReader.readLine();
            Validator.validLongitude(readLongitude);

            // Reading Latitude
            System.out.print("Latitude > ");
            String readLatitude = consoleReader.readLine();
            Validator.validLongitude(readLatitude);

            // Reading the Location Name
            System.out.print("Name > ");
            String readLocationName = consoleReader.readLine();
            Validator.validString("Location Name", readLocationName);
            Validator.validUnsignedInteger("Location Name Size", String.valueOf(readLocationName.length()));


            // reading the location description
            System.out.print("Desc > ");
            String readLocationDesc = consoleReader.readLine();
            Validator.validString("Location Description", readLocationDesc);
            Validator.validUnsignedInteger("Location Description Size", String.valueOf(readLocationDesc.length()));

            LocationRecord loc = new LocationRecord(VALID_USER_ID, readLongitude, readLatitude, readLocationName, readLocationDesc);
            NewLocation newLocation = new NewLocation(mapId, loc);

            newLocation.encode(out);
        }
        catch (ValidationException e){
            ClientErrorMessageHandler.localClientValidation(e.getMessage());
            return null;
        }

        return byteArrayOutputStream.toByteArray();

    }







    private static byte[] allOperation(int mapId) throws ValidationException, IOException {
        LocationRequest req = new LocationRequest(mapId);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MessageOutput out = new MessageOutput(byteArrayOutputStream);

        req.encode(out);

        return byteArrayOutputStream.toByteArray();
    }







    private static boolean wouldLikeToContinue(BufferedReader consoleReader) throws IOException {
        System.out.print("Continue (y/n) > ");
        char option = (char) consoleReader.read();
        return (option == YES);
    }



    private static boolean getOperation(BufferedReader consoleReader) throws IOException {

        boolean validOperation;
        boolean isNewOperation = false;

        // Continue to ask the user for the operation
        // until a valid operation is given.
        do {

            //prompt user for type of message (ALL OR NEW)
            System.out.print("Operation > ");
            String operation = consoleReader.readLine();


            switch (operation) {
                case ALL_OPERATION -> {
                    validOperation = true;
                    isNewOperation = false;
                }
                case NEW_OPERATION -> {
                    isNewOperation = true;
                    validOperation = true;
                }
                default -> {
                    ClientErrorMessageHandler.localClientValidation("Unknown Operation \""+ operation+"\"");
                    validOperation = false;
                }
            }
        } while (!validOperation);

        return isNewOperation;
    }










    public static void main(String[] args) throws IOException, ValidationException {

        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        boolean isNewOperation = false;
        byte[] msg;
        String server;
        int serverPort, mapId;



        // Test for Valid Arguments (Server Identity & Server Port)
        if(args.length != 2){
            System.out.println("Run As: java Client.java <ServerIdentity> <ServerPort>");
            return;
        }

        server = args[0];
        serverPort = Integer.parseInt(args[1]);



        do {


            isNewOperation = getOperation(consoleReader);



            // Test for Valid Map ID
            if ((mapId = getMapID(consoleReader)) == VALID_MAP_ID) {

                // Call New Operation
                if (isNewOperation){
                    msg = newOperation(mapId, consoleReader);
                }

                // Call All operation
                else {
                    msg = allOperation(mapId);
                }



                if(Objects.isNull(msg)){
                    // Don't do anything if the message received back is null.
                    // This is possible due to the new operation & all operation
                    // methods returning a null byte array if there is a validation
                    // exception.
                }

                else {

                    // Creating Connection
                    Socket socket = new Socket(server, serverPort);
                    InputStream in = socket.getInputStream();
                    OutputStream out = socket.getOutputStream();


                    // Sending the request
                    out.write(msg);


                    // print the response / error message to the console
                    BufferedReader socketReader = new BufferedReader(new InputStreamReader(in));
                    String line = socketReader.readLine();    // reads a line of text
                    System.out.println(line);
                }


            }

            // Throw an exception if not a valid map ID.
            else{
                ClientErrorMessageHandler.localClientValidation("Map ID is Invalid");
            }


        }while(wouldLikeToContinue(consoleReader));


    }




}









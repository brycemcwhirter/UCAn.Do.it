package addatude.app.client;

import addatude.serialization.*;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Client {

    private static final char YES = 'y';
    private static final char NO = 'n';
    private static final int VALID_MAP_ID = 345;
    private static final int VALID_USER_ID = 143; //653 % 255

    private static final String ALL_OPERATION = "ALL";
    private static final String NEW_OPERATION = "NEW";
    private static final String PROTOCOL = "ADDATUDEv1";


    static int validMapID(Scanner scanner){
        // Input Appropriate Attribute Values &
        // Ask for the MAP ID
        System.out.print("Map ID > ");
        int mapID = Integer.parseInt(scanner.nextLine());

        // Restart the cycle if map id isn't VALID_MAP_ID
        return mapID;
    }







    //Todo Tests for valid longitude & latitude values according to program 2 specification
    //TODO tests that all values read in are valid. (return msg that says invalid or catch Validation Exception in main?)
    private static String newOperation(int mapId, Scanner scanner) throws ValidationException {



        // Reading Longitude
        String readLongitude = scanner.nextLine();

        // Reading Latitude
        String readLatitude = scanner.nextLine();


        String readLocationName = scanner.nextLine();


        String readLocationDesc = scanner.nextLine();

        LocationRecord loc = new LocationRecord(VALID_USER_ID, readLongitude, readLatitude, readLocationName, readLocationDesc);
        NewLocation newLocation = new NewLocation(mapId, loc);


        return newLocation.encode(buf);
    }







    private static String allOperation(int mapId) throws ValidationException {
        LocationRequest req = new LocationRequest(mapId);
        return req.toString();
    }


    private static char askToContinue() throws IOException {
        System.out.print("Continue (y/n) > ");
        return (char) System.in.read();
    }






    public static void main(String[] args) throws IOException, ValidationException {

        Scanner scanner = new Scanner(System.in);
        char option = YES;
        boolean isNewOperation = false, validOperation;
        String msg, server;
        int serverPort, mapId;



        // Test for Valid Arguments (Server Identity & Server Port)
        if(args.length != 2){
            System.out.println("Run As: java Client.java <ServerIdentity> <ServerPort>");
            return;
        }

        server = args[0];
        serverPort = Integer.parseInt(args[1]);



        while(option != NO) {

            do {

                //prompt user for type of message (ALL OR NEW)
                System.out.print("Operation > ");
                String operation = scanner.nextLine();

                // Ask Again if not ALL or NEW
                switch (operation) {
                    case ALL_OPERATION -> {
                        validOperation = true;
                    }
                    case NEW_OPERATION -> {
                        isNewOperation = true;
                        validOperation = true;
                    }
                    default -> {
                        System.out.println("Invalid Operation: " + operation);
                        validOperation = false;
                    }
                }

            } while (!validOperation);


            if ((mapId = validMapID(scanner)) == VALID_MAP_ID) {

                // Call New Operation
                if(isNewOperation)
                    msg = newOperation(mapId, scanner);

                else
                    msg = allOperation(mapId);



                // Creating Connection
                Socket socket = new Socket(server, serverPort);
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();


                // Sending the request
                out.write(msg.getBytes(StandardCharsets.UTF_8));


                // print the response / error message to the console
                BufferedReader socketReader = new BufferedReader(new InputStreamReader(in));
                String line = socketReader.readLine();    // reads a line of text
                System.out.println(line);



            }


            // Prompt the User to Continue
            System.out.print("Continue? (y/n) > ");
            option =



            //todo TEST FOR INVALID INPUT AT OPTION
        }




        //prompt for operation (switch)

        //
    }




}

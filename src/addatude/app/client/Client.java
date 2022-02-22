package addatude.app.client;

import addatude.serialization.Validator;

import java.io.*;
import java.net.Socket;
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
    private static String newOperation(int mapId, Scanner scanner){

        StringBuilder sb = new StringBuilder();
        sb.append(PROTOCOL + " " + mapId + " NEW " + VALID_USER_ID + " ");

        double readLongitude = scanner.nextDouble();
        //todo tests then append
        //Validator.validString();
        sb.append(readLongitude + " ");

        double readLatitude = scanner.nextDouble();
        sb.append(readLatitude + " ");


        String readLocationName = scanner.nextLine();
        sb.append(readLocationName.length() + " " + readLocationName);


        String readLocationDesc = scanner.nextLine();
        sb.append(readLocationDesc.length() + " " + readLocationDesc);

        sb.append("\r\n");


        return sb.toString();
    }







    private static String allOperation(int mapId) {
        return PROTOCOL + " " + mapId + " ALL \r\n";
    }






    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        char option = YES;
        boolean isNewOperation = false, validOperation;
        String msg, server;
        int serverPort, mapId;



        // Test for Valid Arguments (Server Identity & Server Port)
        if(args.length != 2){
            System.out.println("java Client.java <ServerIdentity> <ServerPort>");
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



                // Send a Message to the server
                Socket socket = new Socket(server, serverPort);
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();



                // print the response / error message to the console



            }
            // Prompt the User to Continue

            System.out.print("Continue (y/n) > ");
            option = (char) System.in.read();
            //todo TEST FOR INVALID INPUT AT OPTION
        }




        //prompt for operation (switch)

        //
    }




}

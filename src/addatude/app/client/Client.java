package addatude.app.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Client {

    private static final char YES = 'y';
    private static final char NO = 'n';
    private static final int VALID_MAP_ID = 345;
    private static final int VALID_USER_ID = 143; //653 % 255

    private static final String ALL_OPERATION = "ALL";
    private static final String NEW_OPERATION = "NEW";



    void allOperation(){

    }

    void newOperation(){

    }

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        char option = YES;
        boolean isAllOperation, validOperation;

        // Test for Valid Arguments (Server Identity & Server Port)



        while(option != NO){

            do{

                //prompt user for type of message (ALL OR NEW)
                System.out.print("Operation>");
                String operation = scanner.nextLine();

                // Ask Again if not ALL or NEW
                switch(operation){
                    case ALL_OPERATION -> {
                        isAllOperation = true;
                        validOperation = true;
                    }
                    case NEW_OPERATION -> {
                        isAllOperation = false;
                        validOperation = true;
                    }
                    default -> {
                        System.out.println("Invalid Operation: " + operation);
                        validOperation = false;
                    }
                }

            }while(!validOperation);



            // Input Appropriate Attribute Values

                // Ask for the MAP ID

                    // Restart the cycle if map id isn't VALID_MAP_ID






            // Send a Message to the server


            // print the response / error message to the console


            // Prompt the User to Continue
            System.out.print("Continue (y/n) > ");
            option = (char) System.in.read();
        }




        //prompt for operation (switch)

        //
    }


}

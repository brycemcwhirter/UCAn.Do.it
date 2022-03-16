package addatude.app.server;

import addatude.serialization.*;
import addatude.serialization.Error;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddatudeProtocol implements Runnable{

    private static final int VALID_MAP_ID = 345;

    private Socket clntSocket;
    private Logger logger;

    public AddatudeProtocol(Socket clntSocket, Logger logger) {
        this.clntSocket = clntSocket;
        this.logger = logger;
    }

    //TODO make sure that you're logging all info and warnings (no logging yet)

    public static void handleAddatudeClient(Socket clntSocket, Logger logger){

        try {
            MessageInput messageInput = new MessageInput(clntSocket.getInputStream());
            MessageOutput messageOutput = new MessageOutput(clntSocket.getOutputStream());
            LocationResponse response = new LocationResponse(345, "Class Map");

            // Decode the Message from the client
            Message clntMessage = Message.decode(messageInput);

            // If the mapID is not valid
              // return an error message "No such map: <rcvd mapID>"
            if(clntMessage.getMapId() != VALID_MAP_ID){
                // todo make sure this follow format "No such map: <rcvd mapID>"
                Error errorMsg = new Error(clntMessage.getMapId(), "No such map: ");
                errorMsg.encode(messageOutput);
            }

            else if(clntMessage.getOperation().equals(LocationResponse.OPERATION)){
                // If you received request for all locations todo IF STATEMENT HERE
                // Send Response with all locations
                handleAllResponse(messageOutput, messageInput, response);

            }

            else if(clntMessage.getOperation().equals(NewLocation.OPERATION)){
                // If you receive a new location request todo ELSE IF STATEMENT HERE
                handleNewLocationResponse(messageOutput, messageInput, response);
            }


            else{
                // If you didn't receive any of the messages above
                handleUnexpectedMessageType(messageOutput, messageInput);
            }




        } catch (IOException  e) {
            logger.log(Level.WARNING, "Exception thrown in Addatude Protocol: ", e);
        }
         catch (ValidationException e){
            // log warning: received a message with a validation exception
            // Return an error message "Unexpected version: "
         }

        finally {
            try {
                clntSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private static void handleUnexpectedMessageType(MessageOutput messageOutput, MessageInput messageInput) throws IOException {

        // return an error message "Unexpected message type: <rcvd operation>" w/ Map ID
          // from client message

        messageOutput.writeString("POOP");

    }

    private static void handleNewLocationResponse(MessageOutput messageOutput, MessageInput messageInput, LocationResponse response) throws IOException {

        // Generate a new Location Record from the input

        // if the location exists, then delete the old location record

        messageOutput.writeString("new Location response");


    }



    private static void handleAllResponse(MessageOutput messageOutput, MessageInput messageInput, LocationResponse response) throws IOException {

        // Send the list of locations with the list of locations with MapID from the AddATude Message


        messageOutput.writeString("All response");


    }




    public void run(){
        handleAddatudeClient(clntSocket, logger);
    }
}

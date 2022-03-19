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
                Error errorMsg = new Error(clntMessage.getMapId(), "No such map: " + clntMessage.getMapId());
                errorMsg.encode(messageOutput);
            }

            else if(clntMessage.getOperation().equals(LocationRequest.OPERATION)){
                // If you received request for all locations
                // Send Response with all locations
                handleAllResponse(messageOutput, response);

            }

            else if(clntMessage.getOperation().equals(NewLocation.OPERATION)){
                // If you receive a new location request
                NewLocation newLocationRequest = (NewLocation) clntMessage;
                handleNewLocationResponse(newLocationRequest, response);
            }


            else{
                // If you didn't receive any of the messages above
                handleUnexpectedMessageType(messageOutput, messageInput);
            }




        } catch (IOException  e) {
            logger.log(Level.SEVERE, "Exception thrown in Addattude Protocol: ", e);
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

    private static void handleNewLocationResponse(NewLocation newLocationRequest,  LocationResponse response) throws ValidationException {
        // TODO make sure the list clears the user id along with the location name

        // if the location exists, then delete the old location record
        if(response.getLocationRecordList().contains(newLocationRequest.getLocationRecord())){
            response.getLocationRecordList().remove(newLocationRequest.getLocationRecord());
        }

        // Add the Location Record to Location Response
        LocationRecord lr = newLocationRequest.getLocationRecord();
        response.getLocationRecordList().add(newLocationRequest.getLocationRecord());
    }
    
    
    
    

    private static void handleUnexpectedMessageType(MessageOutput messageOutput, MessageInput messageInput) throws IOException {

        // return an error message "Unexpected message type: <rcvd operation>" w/ Map ID
          // from client message


    }





    private static void handleAllResponse(MessageOutput messageOutput, LocationResponse response) throws IOException {

        // Send the list of locations with the list of locations with MapID from the AddATude Message
        response.encode(messageOutput);
    }




    public void run(){
        handleAddatudeClient(clntSocket, logger);
    }
}

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
            if(clntMessage.getMapId() != VALID_MAP_ID){

                // return an error message "No such map: <rcvd mapID>"
                Error errorMsg = new Error(clntMessage.getMapId(), "No such map: " + clntMessage.getMapId());
                errorMsg.encode(messageOutput);
            }



            // If you received request for all locations
            else if(clntMessage.getOperation().equals(LocationRequest.OPERATION)){

                // Send the list of locations with the list of locations with MapID from the AddATude Message
                response.encode(messageOutput);
            }



            // TODO make sure the list clears the user id along with the location name
            else if(clntMessage.getOperation().equals(NewLocation.OPERATION)){

                // If you receive a new location request
                NewLocation newLocationRequest = (NewLocation) clntMessage;


                // if the location exists, then delete the old location record
                if(response.getLocationRecordList().contains(newLocationRequest.getLocationRecord())){
                    response.getLocationRecordList().remove(newLocationRequest.getLocationRecord());
                }

                // Add the Location Record to Location Response
                response.getLocationRecordList().add(newLocationRequest.getLocationRecord());



            }


            else{
                // If you didn't receive any of the messages above
                new Error(clntMessage.getMapId(),"Unexpected message type: "+clntMessage.getOperation()).encode(messageOutput);
            }




        }
         catch (ValidationException e){
            // log warning: received a message with a validation exception
            // Return an error message "Unexpected version: "
         }

        catch (Exception e){
            logger.log(Level.SEVERE, "Exception thrown in Addatude Protocol: ", e);
        }

        finally {
            try {
                clntSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }






    public void run(){
        handleAddatudeClient(clntSocket, logger);
    }
}

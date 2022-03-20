package addatude.app.server;

import addatude.serialization.*;
import addatude.serialization.Error;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
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

    public static void handleAddatudeClient(Socket clntSocket, Logger logger) {

        Message clntMessage;
        MessageOutput messageOutput = null;

        try {
            MessageInput messageInput = new MessageInput(clntSocket.getInputStream());
            messageOutput = new MessageOutput(clntSocket.getOutputStream());
            LocationResponse response = new LocationResponse(345, "Class Map");


            // Decode the Message from the client
            clntMessage = Message.decode(messageInput);


            // If the mapID is not valid
            if(clntMessage.getMapId() != VALID_MAP_ID){

                logger.log(Level.WARNING, "Invalid MAP ID Received. Sending Error Message to Client: " + clntSocket.getInetAddress());


                // return an error message "No such map: <rcvd mapID>"
                Error errorMsg = new Error(clntMessage.getMapId(), "No such map: " + clntMessage.getMapId());
                errorMsg.encode(messageOutput);
            }



            // If you received request for all locations
            else if(clntMessage.getOperation().equals(LocationRequest.OPERATION)){

                logger.log(Level.INFO, "Received request for all locations. Sending Locations to Client: "+clntSocket.getInetAddress());


                // Send the list of locations with the list of locations with MapID from the AddATude Message
                response.encode(messageOutput);
            }



            else if(clntMessage.getOperation().equals(NewLocation.OPERATION)){

                logger.log(Level.INFO, "Received New Location Request from Client: "+clntSocket.getInetAddress());


                // If you receive a new location request
                NewLocation newLocationRequest = (NewLocation) clntMessage;


                // if the location exists, then delete the old location record
                if(response.getLocationRecordList().contains(newLocationRequest.getLocationRecord())){
                    logger.log(Level.INFO, "Deleting Previous Location for Client: "+clntSocket.getInetAddress());

                    //TODO Location Names have User ID's attached to them. Searching may be harder then
                    response.getLocationRecordList().remove(newLocationRequest.getLocationRecord());
                }

                // Add the Location Record to Location Response
                LocationRecord newLocationRecord = newLocationRequest.getLocationRecord();
                //TODO Remove the "Name:" and tag the username along with the location name
                newLocationRecord.setLocationName("Name: " + newLocationRecord.getLocationName());

                response.addLocationRecord(newLocationRecord);
                logger.log(Level.INFO, "Adding New Location to Location Records for Client: "+clntSocket.getInetAddress());


                response.encode(messageOutput);
            }


            else{
                // If you didn't receive any of the messages above
                logger.log(Level.WARNING, "Unexpected message type. Sending Error Message to Client: "+clntSocket.getInetAddress());
                new Error(clntMessage.getMapId(),"Unexpected message type: "+clntMessage.getOperation()).encode(messageOutput);
            }




        }
         catch (ValidationException e){
            // log warning: received a message with a validation exception
             logger.log(Level.SEVERE, "Validation Exception Caught: Sending Error Message to Client: " + clntSocket.getInetAddress());
            // Return an error message "Unexpected version: "
             try {
                 new Error(0,"Invalid Message: "+e.getMessage()).encode(messageOutput);
             } catch (IOException | ValidationException e1){
                 //TODO do we need to worry about this try catch here?
             }
         }

        catch (Exception e){
            logger.log(Level.SEVERE, "Exception thrown in Addatude Protocol: ", e);
        }

        finally {
            try {
                clntSocket.close();
            } catch (IOException e) {
            }
        }



    }






    public void run(){
        handleAddatudeClient(clntSocket, logger);

    }
}

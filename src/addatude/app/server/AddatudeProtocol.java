package addatude.app.server;

import addatude.serialization.*;
import addatude.serialization.Error;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddatudeProtocol implements Runnable{

    private static final int VALID_MAP_ID = 345;

    private static LocationResponse response;
    private Socket clntSocket;
    private Logger logger;
    private Map<Long, Server.User> userListMap;


    static {
        try {
            response = new LocationResponse(345, "Class Map");
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public AddatudeProtocol(Socket clntSocket, Logger logger, Map<Long, Server.User> userListMap) {
        this.clntSocket = clntSocket;
        this.logger = logger;
        this.userListMap = userListMap;
    }


    public static void handleAddatudeClient(Socket clntSocket, Logger logger, Map<Long, Server.User> userListMap) {

        Message clntMessage;
        MessageOutput messageOutput = null;

        try {
            MessageInput messageInput = new MessageInput(clntSocket.getInputStream());
            messageOutput = new MessageOutput(clntSocket.getOutputStream());



            // Decode the Message from the client
            clntMessage = Message.decode(messageInput);


            // If the mapID is not valid
            // return an error message "No such map: <rcvd mapID>"
            if(clntMessage.getMapId() != VALID_MAP_ID){
                logger.log(Level.WARNING, "Invalid MAP ID Received. Sending Error Message to Client: " + clntSocket.getInetAddress());
                Error errorMsg = new Error(clntMessage.getMapId(), "No such map: " + clntMessage.getMapId());
                errorMsg.encode(messageOutput);
            }




            // If you received request for all locations
            // Send the list of locations with the list of locations with MapID from the AddATude Message
            else if(clntMessage.getOperation().equals(LocationRequest.OPERATION)){
                logger.log(Level.INFO, "Received request for all locations. Sending Locations to Client: "+clntSocket.getInetAddress());
                response.encode(messageOutput);
            }


            // If you receive a new location request
            else if(clntMessage.getOperation().equals(NewLocation.OPERATION)){
                logger.log(Level.INFO, "Received New Location Request from Client: "+clntSocket.getInetAddress());
                NewLocation newLocationRequest = (NewLocation) clntMessage;
                LocationRecord newLocationRecord = newLocationRequest.getLocationRecord();



                // If the current user does not exist
                // send an error message w/ the User ID
                if(!userListMap.containsKey(newLocationRecord.getUserId())){
                    logger.log(Level.WARNING, "Unexpected User ID. Sending Error Message to Client: "+clntSocket.getInetAddress());
                    new Error(clntMessage.getMapId(),"No such user: "+newLocationRecord.getUserId()).encode(messageOutput);
                }

                else{
                    // Setting the Current User & Attaching the User Name to the Location Record Name
                    Server.User currentUser = userListMap.get(newLocationRecord.getUserId());
                    newLocationRecord.setLocationName(currentUser.getUserName()+": " + newLocationRecord.getLocationName());


                    // if the location exists, then delete the old location record
                    if(response.getLocationRecordList().contains(newLocationRequest.getLocationRecord())){
                        logger.log(Level.INFO, "Deleting Previous Location for Client: "+clntSocket.getInetAddress());
                        response.getLocationRecordList().remove(newLocationRequest.getLocationRecord());
                    }


                    // Add the Location Record to Location Response
                    response.addLocationRecord(newLocationRecord);
                    logger.log(Level.INFO, "Adding New Location to Location Records for Client: "+clntSocket.getInetAddress());


                    // Sending Locations Back to User
                    response.encode(messageOutput);
                }

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
        handleAddatudeClient(clntSocket, logger, userListMap);

    }
}

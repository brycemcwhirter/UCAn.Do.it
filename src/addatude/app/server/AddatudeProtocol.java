/************************************************
 *
 * Author: Bryce McWhirter
 * Assignment: Program 3
 * Class: Data Communications
 *
 ************************************************/

package addatude.app.server;

import addatude.serialization.Error;
import addatude.serialization.*;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import mapservice.Location;
import mapservice.MapBoxObserver;
import mapservice.MapManager;
import mapservice.MemoryMapManager;
import notifi.app.server.NoTiFiServer;


/**
 * This method handles the client connection
 * to the server.
 */
public class AddatudeProtocol implements Runnable{

    /**
     * The Valid Map ID 345
     */
    private static final int VALID_MAP_ID = 345;

    /**
     * The file to hold all locations implemented
     */
    private static final String LOCATIONFILE = "markers.js";

    /**
     * The Response Message to be returned
     */
    private static LocationResponse response;


    /**
     * The client socket
     */
    private final Socket clntSocket;

    /**
     * The Logger
     */
    private final Logger logger;

    /**
     *  The List of Users
     */
    private final Map<Long, Server.User> userListMap;


    static {
        try {
            response = new LocationResponse(345, "Class Map");
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param clntSocket The Client Connection
     * @param logger The logger of the connection
     * @param userListMap the list of all users
     */
    public AddatudeProtocol(Socket clntSocket, Logger logger, Map<Long, Server.User> userListMap) {
        this.clntSocket = clntSocket;
        this.logger = logger;
        this.userListMap = userListMap;
    }


    /** This method handles a client request
     * @param clntSocket The client socket
     * @param logger the log to input any loggin information
     * @param userListMap the list of all users
     * @throws IOException If an I/O Error Occurs
     * @throws ValidationException If the Client Message contains a validation exception
     */
    public static void handleClient(Socket clntSocket, Logger logger, Map<Long, Server.User> userListMap) throws IOException, ValidationException {

        // Establishing Client Message and Message Input & Output
        Message clntMessage;
        MessageInput messageInput;
        MessageOutput messageOutput = null;

        // Establishing Map Manager for Storing Locations
        MapManager mgr = new MemoryMapManager();
        mgr.register(new MapBoxObserver(LOCATIONFILE, mgr));





        try {

            // Establishing Message Input & Output & Decode the Message from the client
            messageInput = new MessageInput(clntSocket.getInputStream());
            messageOutput = new MessageOutput(clntSocket.getOutputStream());
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
                    // Setting the Current User & Attaching the Username to the Location Record Name
                    Server.User currentUser = userListMap.get(newLocationRecord.getUserId());
                    newLocationRecord.setLocationName(currentUser.getUserName()+":" + newLocationRecord.getLocationName());


                    // Add the Location Record to Location Response & Location Record List
                    response.addLocationRecord(newLocationRecord);
                    mgr.addLocation(new Location(newLocationRecord.getLocationName(), newLocationRecord.getLongitude(),
                            newLocationRecord.getLatitude(), newLocationRecord.getLocationDescription(), Location.Color.BLUE));
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
             new Error(0,"Invalid message").encode(messageOutput);
         }

        catch (Exception e){
            logger.log(Level.SEVERE, "Exception thrown in Addatude Protocol: ", e);
        }

        finally {
            try {
                clntSocket.close();
            } catch (IOException ignored) {
            }
        }



    }






    public void run(){
        try {
            handleClient(clntSocket, logger, userListMap);
        } catch (IOException | ValidationException ignore) {
            //handled by the handleClient method
        }

    }
}

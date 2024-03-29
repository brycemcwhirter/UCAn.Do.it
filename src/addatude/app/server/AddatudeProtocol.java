/*
 *
 * Author: Bryce McWhirter
 * Assignment: Program 3
 * Class: Data Communications
 *
 */

package addatude.app.server;

import addatude.serialization.Error;
import addatude.serialization.*;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
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
            // The Location Record Response
            response = new LocationResponse(345, "Class Map");
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }


    /**
     * A Location with a Euclidean Distance.
     *
     * This Class is used for sorting the list of location records
     * with euclidean distances of the location record.
     */
    static class LocationAndEuclideanRecord implements Comparable<LocationAndEuclideanRecord>{
        LocationRecord lr;
        double euclidean;


        /**
         * Constructs a Location And Euclidean Record
         * @param lr the location record
         * @param euclideanDistance the euclidean distance
         */
        public LocationAndEuclideanRecord(LocationRecord lr, double euclideanDistance) {
            this.lr = lr;
            this.euclidean = euclideanDistance;
        }

        /**
         * Specifies how to sort
         * the records
         * @param o the location and euclidean record
         * @return the specification on how to sort (ascending order)
         */
        @Override
        public int compareTo(LocationAndEuclideanRecord o) {
            return (int) Math.round(this.euclidean - o.euclidean);
        }
    }





    /**
     * Constructs an Addatude Protocol Instance
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
            else if(clntMessage.getOperation().equals("ALL")){
                logger.log(Level.INFO, "Received request for all locations. Sending Locations to Client: "+clntSocket.getInetAddress());
                response.encode(messageOutput);
            }


            // If you receive a new location request
            else if(clntMessage.getOperation().equals("NEW")){
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


                    NoTiFiServer.handleNewLocationAddition(newLocationRecord);


                }

            }

            else if(clntMessage.getOperation().equals("LOCAL")){
                logger.log(Level.INFO, "Received LOCAL request from client: "+clntSocket.getInetAddress());
                LocationResponse responseWithShortestDistance = new LocationResponse(response.getMapId(), response.getMapName());
                calculateEuclideanMessage(responseWithShortestDistance, clntMessage);
                responseWithShortestDistance.encode(messageOutput);
            }


            else{
                // If you didn't receive any of the messages above
                logger.log(Level.WARNING, "Unexpected message type. Sending Error Message to Client: "+clntSocket.getInetAddress());
                new Error(clntMessage.getMapId(),"Unexpected message type: "+clntMessage.getOperation()).encode(messageOutput);
            }




        }


        // log warning: received a message with a validation exception
        // Return an error message "Unexpected version: "
        catch (ValidationException e){
             logger.log(Level.SEVERE, "Validation Exception Caught: Sending Error Message to Client: " + clntSocket.getInetAddress());
             new Error(0,"Invalid message").encode(messageOutput);
         }


        // Catch any other exceptions thrown in the protocol
        // and log them.
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


    /**
     * This Method Calculates the Euclidean Distance
     * for the Local Location Request.
     * @param responseWithShortestDistance the response to send back to the client
     * @param clntMessage the Client Message with the Euclidean Coordinates
     */
    static void calculateEuclideanMessage(LocationResponse responseWithShortestDistance, Message clntMessage) throws ValidationException {

        ArrayList<LocationAndEuclideanRecord> list = new ArrayList<>();

        LocalLocationRequest clientMessage = (LocalLocationRequest) clntMessage;
        double x1 = Double.parseDouble(clientMessage.getLatitude());
        double y1 = Double.parseDouble(clientMessage.getLongitude());



        for (LocationRecord lr: response.getLocationRecordList()) {
            double x2 = Double.parseDouble(lr.getLatitude());
            double y2 = Double.parseDouble(lr.getLongitude());
            double euclideanDistance = Math.sqrt((y2-y1) * (y2-y1)+(x2-x1)*(x2-x1));
            list.add(new LocationAndEuclideanRecord(lr, euclideanDistance));
        }


        if(!list.isEmpty()){
            Collections.sort(list);
            responseWithShortestDistance.addLocationRecord(list.get(0).lr);
        }

    }





    /**
     * Run Method Implementation for
     * the addatude protocol.
     */
    public void run(){
        try {
            handleClient(clntSocket, logger, userListMap);
        } catch (IOException | ValidationException ignore) {
            //handled by the handleClient method
        }

    }
}

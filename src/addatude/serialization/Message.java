/************************************************
 *
 * Author: Bryce McWhirter
 * Assignment: Program 1
 * Class: Data Communications
 *
 ************************************************/

/*
 * Testing Partner: John Harrison
 */

package addatude.serialization;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Message Class
 *
 * This is the abstract class
 * for the message type of the
 * ADDATUDE Protocol.
 */
public abstract class Message {


    /**
     * The MapID Related to the message of the ADDATUDE Protocol
     */
    long mapId;


    /**
     * The Header of the Message
     */
    private static final String HEADER = "ADDATUDEv1";


    /**
     * The Footer of the Message
     */
    private static final String FOOTER = "\r\n";


    /**
     * Generates a new Message
     * @param operation The Operation that Describes the type of Message
     * @param mapID The MapID
     * @throws ValidationException
     *      If any of these parameters are invalid
     */
    public Message(String operation, long mapID) throws ValidationException {
        Validator.validUnsignedInteger("MapID", String.valueOf(mapID));
        Objects.requireNonNull(operation);
        Validator.validOperation(operation);
        this.mapId = mapID;
    }





    /**
     * Abstract Implementation of Encode
     * @param out The Output Stream to write to
     * @throws IOException
     *      If a write error occurs
     */
    public abstract void encode(MessageOutput out) throws IOException;






    /**
     * Decodes and creates a new message based on its operation
     * @param in The input stream to decode from
     * @return
     *      The Specific Message and its parameters
     * @throws ValidationException
     *      If any of these parameters are invalid
     * @throws NullPointerException
     *      If the input stream is null
     */
    public static Message decode(MessageInput in) throws ValidationException, NullPointerException {
        Objects.requireNonNull(in);
        Message a;

        // Read the Header & Make Sure it Matches
        String header = in.readUntilSpace();
        if(!header.equals(HEADER)){
            throw new ValidationException(header, "Invalid Protocol specified: " + header);
        }



        // Read the MAP ID
        String readMapID = in.readUntilSpace();
        Validator.validUnsignedInteger("MapID", readMapID);
        long mapIdVal = Long.parseLong(readMapID);


        // Read the Operation
        String operation = in.readUntilSpace();



        //Switch Operation
        switch (operation) {
            case NewLocation.OPERATION -> a = new NewLocation(mapIdVal, in);
            case LocationRequest.OPERATION -> a = new LocationRequest(mapIdVal);
            case LocationResponse.OPERATION -> a = new LocationResponse(mapIdVal, in);
            case Error.OPERATION -> a = new Error(mapIdVal, in);
            default -> throw new ValidationException(operation, "Invalid Operation");
        }


        // Test Valid Ending
        String footer = new String(in.readNumOfValues(2), StandardCharsets.UTF_8);
        if(!footer.equals(FOOTER)){
            throw new ValidationException(footer, "Invalid Stream Ending: " + new String(footer.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
        }


        return a;
    }





    /**
     * @return the mapID
     */
    public final long getMapId(){
        return mapId;
    }






    /**
     * @return The Operation of the Message
     */
    public abstract String getOperation();







    /**
     * Sets the MapID and returns the new Message
     * @param mapId The mapID to set to
     * @return the message with the new mapId
     * @throws ValidationException
     *      If the new mapID is invalid
     */
    public final Message setMapId(long mapId) throws ValidationException{
        Validator.validUnsignedInteger("MapID",String.valueOf(mapId));
        this.mapId = mapId;
        return this;
    }






    /**
     * Tests if two messages are equal
     * @param o The Message Object
     * @return a boolean describing if two messages are
     *   the same
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return mapId == message.mapId;
    }





    /**
     * Hash Code implementation
     * @return The Hash Code representation
     *   of the message.
     */
    @Override
    public int hashCode() {
        return Objects.hash(mapId);
    }




    /**
     * Writes the message header onto the output stream
     * @param mapId The mapID to be written
     * @param operation the operation to be written
     * @throws IOException
     *      If a write error
     */
    public static void writeMessageHeader(long mapId, String operation, MessageOutput out) throws IOException {
        String messageHeader = HEADER+' '+mapId+' '+operation+' ';
        out.write(messageHeader.getBytes(StandardCharsets.UTF_8));
    }



    /**
     * Writes the Message Footer onto the output stream
     * @throws IOException
     *      If a write error occurs
     */
    public static void writeMessageFooter(MessageOutput out) throws IOException {
        out.write(FOOTER.getBytes(StandardCharsets.UTF_8));
    }



}

package serialization;

import java.io.IOException;
import java.util.Objects;

public abstract class Message {

    public final String operation;
    long mapId;
    private static final String HEADER = "ADDATUDEv1";


    /**
     * Generates a new Message
     * @param operation The Operation that Describes the type of Message
     * @param mapID The MapID
     * @throws ValidationException
     *      If any of these parameters are invalid
     */
    public Message(String operation, long mapID) throws ValidationException {
        Validator.validUnsignedInteger(mapID);
        Objects.requireNonNull(operation);
        Validator.validCharacterList(operation);
        this.mapId = mapID;
        this.operation = operation;
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
     * @throws IOException
     *      If a read error occurs
     */
    public static Message decode(MessageInput in) throws ValidationException, NullPointerException, IOException {
        Objects.requireNonNull(in);

        // Read the Header & Make Sure it Matches
        String header = in.readUntilSpace();
        if(!header.equals(HEADER)){
            throw new ValidationException("invalid stream");
        }



        // Read the MAP ID
        long readMapID = Long.parseLong(in.readUntilSpace());
        Validator.validUnsignedInteger(readMapID);


        // Read the Operation
        String operation = in.readUntilSpace();



        //Switch Operation
        switch(operation){
            case "NEW": return in.readNewLocation(readMapID, operation);
            case "ALL": return new LocationRequest(readMapID);
            case "RESPONSE": return in.readResponse(readMapID, operation);
            case "ERROR": return in.readError(readMapID, operation);
            default:
                throw new ValidationException("Invalid Operation");
        }
    }


    /**
     * @return the mapID
     */
    public final long getMapID(){
        return mapId;

    }


    /**
     * @return The Operation of the Message
     */
    public final String getOperation(){
        return operation;
    }


    /**
     * Sets the MapID and returns the new Message
     * @param mapId The mapID to set to
     * @return the message with the new mapId
     * @throws ValidationException
     *      If the new mapID is invalid
     */
    public final Message setMapId(long mapId) throws ValidationException{
        Validator.validUnsignedInteger(mapId);
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
        return mapId == message.mapId && operation.equals(message.operation);
    }


    /**
     * Hash Code implementation
     * @return The Hash Code representation
     *   of the message.
     */
    @Override
    public int hashCode() {
        return Objects.hash(operation, mapId);
    }
}

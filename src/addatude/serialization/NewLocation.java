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
import java.util.Objects;

/**
 * New Location Class
 *
 * This class is a Message
 * specifying a New Location
 * to be added to a set of locations.
 */
public class NewLocation extends Message{

    LocationRecord location; // The New Location to be added
    public static final String OPERATION = "NEW"; // The Operation Describing the Message



    /**
     * Generates a NewLocation
     * @param mapId the mapID of the new location
     * @param location the new Location Record
     * @throws ValidationException
     *      if mapID is invalid or location is null
     */
    public NewLocation(long mapId, LocationRecord location) throws ValidationException{
        super(OPERATION, mapId);
        if(Objects.isNull(location))
            throw new ValidationException("Null Location Record", "Location Record cannot be null in New Location Instance");
        this.location = new LocationRecord(location);

    }


    /**
     * Generates a new location message.
     * @param mapId the map id associated with the new location
     * @param in the input stream holding the message
     * @throws ValidationException
     *      If a validation error occurs.
     */
    public NewLocation(long mapId, MessageInput in) throws ValidationException{
        super(OPERATION, mapId);
        location = new LocationRecord(in);
    }


    /**
     * Sets the Location Record of a NewLocation
     * @param location the new location record
     * @return the NewLocation message with the new location record
     * @throws ValidationException
     *      if the new Location is null
     */
    public NewLocation setLocationRecord(LocationRecord location) throws ValidationException {
        if(Objects.isNull(location))
            throw new ValidationException("Null Location Record", "Location Record cannot be null in New Location Instance");
        this.location = new LocationRecord(location);
        return this;
    }


    /**
     * String implementation of a NewLocation
     * @return a string implementation of a NewLocation
     */
    @Override
    public String toString() {
        return "NewLocation: map="+ getMapId()+" "+location;
    }


    @Override
    public String getOperation() {
        return OPERATION;
    }


    /**
     * encodes a NewLocation to the output stream
     * @param out The Output Stream to write to
     * @throws IOException
     *      If a write error occurs
     */
    @Override
    public void encode(MessageOutput out) throws IOException {
        Message.writeMessageHeader(getMapId(), getOperation(), out);
        location.encode(out);
        Message.writeMessageFooter(out);
    }


    /**
     * returns the location of a NewLocation
     * @return the location
     */
    public LocationRecord getLocationRecord() throws ValidationException {
        return new LocationRecord(location);
    }


    /**
     * Equals Implementation of a NewLocation
     * @param o The Message Object
     * @return a boolean describing if two NewLocations
     * are similar
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        NewLocation that = (NewLocation) o;
        return mapId == that.mapId && location.equals(that.location);
    }

    /**
     * Hash Code Implementation of a NewLocation
     * @return the hash implementation of a NewLocation
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), mapId, location);
    }
}

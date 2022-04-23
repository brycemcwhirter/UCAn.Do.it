/*
 *
 * Author: Bryce McWhirter
 * Assignment: Program 1
 * Class: Data Communications
 *
 */

/*
 * Testing Partner: John Harrison
 */

package addatude.serialization;

import java.io.IOException;

/**
 * A Location Request is used for
 * asking for a set of locations
 * specified by the ALL operation
 */
public class LocationRequest extends Message{

    /**
     * The Specified Operation for the location request
     */
    protected static final String OPERATION = "ALL";


    /**
     * Generates a new Location Request
     * @param mapId The mapID of the location requests
     * @throws ValidationException
     *   if the mapID is invalid
     */
    public LocationRequest(long mapId) throws ValidationException {
        super(OPERATION, mapId);
    }



    /**
     * String Implementation of a Location
     * Request
     * @return a string representation
     * of a location request
     */
    @Override
    public String toString() {
        return "LocationRequest: map="+ getMapId();
    }


    /**
     * @return The operation tied to the location request
     */
    @Override
    public String getOperation() {
        return OPERATION;
    }


    /**
     * Writes a Location Request to a Message
     * Output Stream
     * @param out The Output Stream to write to
     * @throws IOException
     *      if a write error occurs
     */
    @Override
    public void encode(MessageOutput out) throws IOException {
        Message.writeMessageHeader(getMapId(), getOperation(), out);
        Message.writeMessageFooter(out);
    }


    /**
     * Hash Code Implementation
     * of a Location Request
     * @return a hash representation
     * of a Location Request
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }


    /**
     * Equals Implementation
     * @param obj the Location Request being tested against
     * @return a boolean describing if two
     * Location Request are the same.
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}

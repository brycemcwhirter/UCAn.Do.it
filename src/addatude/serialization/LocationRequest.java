/************************************************
 *
 * Author: Bryce McWhirter
 * Assignment: Program 1
 * Class: Data Communications
 *
 ************************************************/

package addatude.serialization;

import java.io.IOException;

public class LocationRequest extends Message{

    private static final String OPERATION = "ALL";


    /**
     * Generates a new Location Request
     * @param mapId The mapID of the location requests
     * @throws ValidationException
     *   if the mapID is invalid
     */
    public LocationRequest(long mapId) throws ValidationException {
        super(OPERATION, mapId);
    }



    public LocationRequest(long mapId, MessageInput in) throws ValidationException{
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
        out.writeMessageHeader(getMapId(), getOperation());
        out.writeMessageFooter();
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

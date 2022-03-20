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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The Location Response is a specific
 * message that holds a list of location
 * records tied to a specific mapId.
 */
public class LocationResponse extends Message{

    /**
     *  A List of Location Record tied to the response
     */
    List<LocationRecord> locationRecordList = new ArrayList<>();


    /**
     * The Name of the Map tied to the Location Response
     */
    String mapName;


    /**
     * The Operation of the Location Response
     */
    public static final String OPERATION = "RESPONSE";


    /**
     * Generates a new LocationResponse
     * @param mapId the mapId
     * @param mapName the name of the map
     * @throws ValidationException
     *  if any of these parameters are invalid
     */
    public LocationResponse(long mapId, String mapName) throws ValidationException{
        super(OPERATION, mapId);
        Validator.validString("Map Name", mapName);
        Validator.validUnsignedInteger("Map Name Size", String.valueOf(mapName.length()));
        this.mapName = mapName;
    }








    /**
     * This Constructor creates a new Location Response
     * by decoding the Message Input.
     * @param mapID The Map ID of the Location Response
     * @param in The Message Input Stream
     * @throws ValidationException
     *      If Any Parameters are deemed invalid.
     */
    public LocationResponse(long mapID, MessageInput in) throws ValidationException{
        super(OPERATION, mapID);

        int size = in.readIntegerValue();
        String mapName = new String(in.readNumOfValues(size), StandardCharsets.UTF_8);
        Validator.validString("Map Name", mapName);

        int numOfLocation = in.readIntegerValue();

        this.mapName = mapName;

        for (int i = 0; i < numOfLocation; i++) {
            LocationRecord lr = new LocationRecord(in);
            this.addLocationRecord(lr);
        }


    }








    /**
     * Adds a new location record
     * @param locationRecord the new location record to add
     * @return the original response with the new record on the list
     * @throws ValidationException
     *      if the location record is null
     */
    public LocationResponse addLocationRecord(LocationRecord locationRecord) throws ValidationException {
        if(Objects.isNull(locationRecord)){
            throw new ValidationException(null, "Location Record cannot be null in adding to list");
        }

        locationRecordList.add(locationRecord);
        return this;
    }








    /**
     * Returns a copy of the Location Record List
     * @return a copy of the location record list
     */
    public List<LocationRecord> getLocationRecordList(){

        return new ArrayList<>(locationRecordList);
    }








    /**
     * @return the name of the map
     */
    public String getMapName() {

        return mapName;
    }






    /**
     * @return The Operation
     */
    @Override
    public String getOperation() {

        return OPERATION;
    }








    /**
     * Sets the map name
     * @param mapName the new map name
     * @return the original location response with the new name
     * @throws ValidationException
     *      if the name is invalid
     */
    public LocationResponse setMapName(String mapName) throws ValidationException {
        Validator.validString("Map Name", mapName);
        Validator.validUnsignedInteger("Map Name Size", String.valueOf(mapName.length()));
        this.mapName = mapName;
        return this;
    }








    /**
     * @return a string implementation of a location response
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("LocationResponse: map="+ getMapId()+" "+getMapName());

        if(!locationRecordList.isEmpty()) {
            sb.append(" [");
            for (LocationRecord lr : locationRecordList) {
                sb.append(lr.toString()).append(",");
            }
            sb.setCharAt(sb.length()-1, ']');
        }
        return sb.toString();
    }








    /**
     * Encodes a Location Response
     * @param out The Output Stream to write to
     * @throws IOException
     *      if a write error occurs
     */
    @Override
    public void encode(MessageOutput out) throws IOException {
        Message.writeMessageHeader(getMapId(), getOperation(), out);
        out.writeString(getMapName());

        out.write((locationRecordList.size() + " ").getBytes(StandardCharsets.UTF_8));

        if(locationRecordList.size() > 0) {


            for (LocationRecord lr : locationRecordList) {
                lr.encode(out);
            }

        }



        Message.writeMessageFooter(out);
    }








    /**
     * Equals implementation of Location Response
     * @param o The Message Object
     * @return
     *      A boolean describing if a location response is valid
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationResponse that = (LocationResponse) o;
        return locationRecordList.equals(that.locationRecordList) && mapName.equals(that.mapName);
    }








    /**
     * @return the hash implementation of a location response.
     */
    @Override
    public int hashCode() {

        return Objects.hash(locationRecordList, mapName);
    }



}

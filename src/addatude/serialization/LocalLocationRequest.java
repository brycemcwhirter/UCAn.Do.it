/*
 *
 * Author: Bryce McWhirter
 * Assignment: Program 6
 * Class: Data Communications
 *
 */

package addatude.serialization;


import java.io.IOException;
import java.nio.charset.StandardCharsets;


/**
 * The Local Location Request of the
 * addatude serialization.
 */
public class LocalLocationRequest extends Message{

    // Latitude of the Location Record
    String latitude;

    // Longitude of the Location Record
    String longitude;

    // The Map ID associated with the message
    long mapId;

    // The Operation of the Local Location Request
    public static final String OPERATION = "LOCAL";


    /**
     * Generates a local location request
     * @param mapId ID for Message Map
     * @param longitude longitude of center of request
     * @param latitude latitude of center of request
     * @throws ValidationException
     *      If any values are invalid
     */
    public LocalLocationRequest(long mapId, String longitude, String latitude) throws ValidationException{
        super(OPERATION, mapId);
        AddatudeValidator.validLongitude(longitude);
        AddatudeValidator.validLatitude(latitude);


        this.latitude = latitude;
        this.longitude = longitude;
        this.mapId = mapId;
    }


    /**
     * Deserializes and builds a Local Location Request
     * @param mapIdVal ID for Message Map
     * @param in the Message Input
     * @throws ValidationException
     *      if any values are invalid
     */
    public LocalLocationRequest(long mapIdVal, MessageInput in) throws ValidationException {
        super(OPERATION, mapIdVal);

        String readLongitude = in.readUntilSpace();
        setLongitude(readLongitude);


        String readLatitude = in.readUntilSpace();
        setLatitude(readLatitude);
    }


    /**
     * Returns latitude
     * @return latitude
     */
    public String getLatitude() {
        return latitude;
    }


    /**
     * Sets the latitude
     * @param latitude new latitude
     * @throws ValidationException
     *      if latitude is invalid
     */
    public void setLatitude(String latitude) throws ValidationException{
        AddatudeValidator.validLatitude(latitude);
        this.latitude = latitude;
    }


    /**
     * Returns longitude
     * @return longitude
     */
    public String getLongitude() {
        return longitude;
    }


    /**
     * sets the longitude
     * @param longitude new longitude
     * @throws ValidationException
     *      if the new longitude is invalid
     */
    public void setLongitude(String longitude) throws ValidationException {
        AddatudeValidator.validLongitude(longitude);
        this.longitude = longitude;
    }


    /**
     * Returns a String Representation
     * @return a string representation
     */
    @Override
    public String toString() {
        return "LocalLocationRequest: map="+mapId+"at("+longitude+","+latitude+")";
    }


    /**
     * Serializes a messsage
     * @param out The Output Stream to write to
     * @throws IOException
     *      if a write error occurs
     */
    @Override
    public void encode(MessageOutput out) throws IOException {

        writeMessageHeader(mapId, OPERATION, out);


        //write the longitude
        out.write((longitude + " ").getBytes(StandardCharsets.UTF_8));


        //write the latitude
        out.write((latitude +" ").getBytes(StandardCharsets.UTF_8));


        writeMessageFooter(out);


    }


    /**
     * Returns the operation
     * @return the operation
     */
    @Override
    public String getOperation(){
        return OPERATION;
    }





}

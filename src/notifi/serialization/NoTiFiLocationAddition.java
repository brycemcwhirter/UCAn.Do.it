/************************************************
 *
 * Author: Bryce McWhirter
 * Assignment: Program 4
 * Class: Data Communications
 *
 ************************************************/

package notifi.serialization;

import addatude.serialization.ValidationException;
import addatude.serialization.Validator;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * The NoTiFi Location Addition is a notification for a new
 * location record addition.
 */
public class NoTiFiLocationAddition extends NoTiFiMessage{

    /**
     * The Operation Code for the NoTiFi Location Addition
     */
    static final byte LOCATION_ADDITION_CODE = 1;


    /**
     * The User ID of the Location Record
     */
    int userId;


    /**
     * The Longitude of the Location Record
     */
    double longitude;


    /**
     * The Latitude of the Location Record
     */
    double latitude;


    /**
     * The name of the location
     */
    String locationName;


    /**
     * A Description of the location.
     */
    String locationDescription;







    /**
     * Constructs a NoTiFi Add location message
     * @param msgId message id
     * @param userId user id
     * @param longitude longitude of the new location
     * @param latitude latitude of the new location
     * @param locationName the name of the new location
     * @param locationDescription the description of the new location
     * @throws IllegalArgumentException
     *      If validation fails
     */
    public NoTiFiLocationAddition(int msgId, int userId, double longitude, double latitude, String locationName, String locationDescription) throws IllegalArgumentException{
        super(msgId, LOCATION_ADDITION_CODE);


        // Test for invalid Params
        try {
            Validator.validUnsignedInteger("UserId", Long.toString(userId));
            Validator.validLongitude(Double.toString(longitude));
            Validator.validLatitude(Double.toString(latitude));
            Validator.validString("Location Name", locationName);
            Validator.validString("Location Description", locationDescription);
        } catch (ValidationException e) {
            throw new IllegalArgumentException("Illegal Parameter", e);
        }

        // Set Values
        this.userId = userId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.locationName = locationName;
        this.locationDescription = locationDescription;
    }







    /**
     * Deserializes a NoTiFi Location Addition Message
     * @param msgID the message id
     * @param byteBuffer the byte buffer holding the message
     * @return the new NoTiFiLocationAddition Message
     */
    public static NoTiFiLocationAddition decode(int msgID, ByteBuffer byteBuffer) {

        // Read the User ID
        int readUserID = byteBuffer.getInt();



        // Read the Longitude
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        double readLongitude = Math.round(byteBuffer.getFloat());



        // Read the Latitude
        double readLatitude = Math.round(byteBuffer.getFloat());
        byteBuffer.order(ByteOrder.BIG_ENDIAN);



        // Read the Length of Location Name
        short length = byteBuffer.getShort();
        byte[] readNameBuf = new byte[length];


        // Read Location Name
        byteBuffer.get(readNameBuf, 0, length);
        String readName = new String(readNameBuf, StandardCharsets.US_ASCII);



        // Read the Length of Location Description
        length = byteBuffer.getShort();
        byte[] readDescBuf = new byte[length];



        // Read Location Description
        byteBuffer.get(readDescBuf, 0, length);
        String readDesc = new String(readDescBuf, StandardCharsets.US_ASCII);




        // Return a new NoTiFiLocationAddition
        return new NoTiFiLocationAddition(msgID, readUserID, readLongitude, readLatitude, readName, readDesc);

    }







    /**
     * Returns a String Representation
     * @return String Representation
     */
    @Override
    public String toString() {
        return "Addition: msgid="+msgId+" userid="+userId+':'+locationName+'-'+locationDescription+" ("+longitude+','+latitude+")";
    }





    /**
     * Serializes a NoTiFi Location Addition Message
     * @return the serialized message
     */
    @Override
    public byte[] encode() {

        ByteBuffer b = ByteBuffer.allocate(22 + locationDescription.length() + locationName.length());

        // Write The NoTiFi Header
        writeNoTiFiHeader(b, LOCATION_ADDITION_CODE);

        // Write the UserID
        b.putInt(userId);


        // Write the Longitude & Latitude
        b.order(ByteOrder.LITTLE_ENDIAN);
        b.putFloat((float) longitude);
        b.putFloat((float) latitude);
        b.order(ByteOrder.BIG_ENDIAN);

        // Write down the length of name and name
        b.putShort((short) locationName.length());
        b.put(locationName.getBytes(StandardCharsets.US_ASCII));

        // Write down the length of desc and desc
        b.putShort((short) locationDescription.length());
        b.put(locationDescription.getBytes(StandardCharsets.US_ASCII));


        return b.array();

    }







    /** returns user id
     * @return user id
     */
    public int getUserId() {
        return userId;
    }








    /** Sets the user id
     * @param userId the user id
     * @return this object with the new user id
     * @throws IllegalArgumentException
     *      if the user id is invalid
     */
    public NoTiFiLocationAddition setUserId(int userId) throws IllegalArgumentException{
        this.userId = userId;
        try {
            Validator.validUnsignedInteger("User Id", Long.toString(userId));
        } catch (ValidationException e) {
            throw new IllegalArgumentException("Invalid User Id", e);
        }
        return this;
    }









    /** Returns Longitude
     * @return longitude
     */
    public double getLongitude() {
        return longitude;
    }








    /** Sets the Longitude
     * @param longitude the longitude
     * @return this object with the new longitude
     */
    public NoTiFiLocationAddition setLongitude(double longitude) {
        this.longitude = longitude;
        try {
            Validator.validLongitude(Double.toString(longitude));
        } catch (ValidationException e) {
            throw new IllegalArgumentException("Invalid Longitude", e);
        }
        return this;
    }








    /** Gets the latitude
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }










    /** Sets the latitude
     * @param latitude the latitude
     * @return this object with the new latitude
     * @throws IllegalArgumentException
     *      if the latitude is invalid
     */
    public NoTiFiLocationAddition setLatitude(double latitude) throws IllegalArgumentException{
        this.latitude = latitude;
        try {
            Validator.validLatitude(Double.toString(latitude));
        } catch (ValidationException e) {
            throw new IllegalArgumentException("Invalid Latitude", e);
        }
        return this;
    }








    /** gets the location name
     * @return the location name
     */
    public String getLocationName() {
        return locationName;
    }








    /** sets the location name
     * @param locationName the location name
     * @return this object with the new location name
     * @throws IllegalArgumentException
     *      if the location name is invalid
     */
    public NoTiFiLocationAddition setLocationName(String locationName) throws IllegalArgumentException{
        try{
            Validator.validString("Location Name", locationName);
        }
        catch (ValidationException e){
            throw new IllegalArgumentException("Invalid Location Name: ", e);
        }
        this.locationName = locationName;
        return this;
    }







    /**
     * Returns the location description
     * @return location description
     */
    public String getLocationDescription() {
        return locationDescription;
    }







    /**
     * Sets the location description
     * @param locationDescription the location description
     * @return this object with the new location description
     * @throws IllegalArgumentException
     *      if the location description is invalid
     */
    public NoTiFiLocationAddition setLocationDescription(String locationDescription) throws IllegalArgumentException{

        // Test Valid Location Description
        try{
            Validator.validString("Location Description", locationDescription);
        }
        catch(ValidationException e){
            throw new IllegalArgumentException("Invalid Location Description: ", e);
        }


        // Setting
        this.locationDescription = locationDescription;


        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoTiFiLocationAddition that = (NoTiFiLocationAddition) o;
        return userId == that.userId && Double.compare(that.longitude, longitude) == 0 && Double.compare(that.latitude, latitude) == 0 && locationName.equals(that.locationName) && locationDescription.equals(that.locationDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, longitude, latitude, locationName, locationDescription);
    }
}

/************************************************
 *
 * Author: Bryce McWhirter
 * Assignment: Program 4
 * Class: Data Communications
 *
 ************************************************/

package notifi.serialization;

import addatude.serialization.ValidationException;
import addatude.serialization.AddatudeValidator;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
    public static final byte LOCATION_ADDITION_CODE = 1;


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
        super(msgId);

        setUserId(userId);
        setLongitude(longitude);
        setLatitude(latitude);
        setLocationName(locationName);
        setLocationDescription(locationDescription);
    }







    /**
     * Deserializes a NoTiFi Location Addition Message
     * @param msgID the message id
     * @param in the input stream holding the message
     * @return the new NoTiFiLocationAddition Message
     */
    public static NoTiFiLocationAddition decode(int msgID, DataInputStream in) {


        try{
            // Read the User ID
            int readUserID = in.readInt();


            // Reading Longitude & Latitude
            byte[] floatBuffer = new byte[16];
            in.read(floatBuffer, 0, 16);
            double readLongitude = NoTiFiReader.readLongitude(floatBuffer);
            double readLatitude = NoTiFiReader.readLatitude(floatBuffer);



            // Read the Name & Description
            String readName = NoTiFiReader.readStringWithLength(in);
            String readDesc = NoTiFiReader.readStringWithLength(in);

            NoTiFiValidator.validateSizeRead(readName, readDesc);

            if(in.available() != 0){
                throw new IllegalArgumentException("Illegal Packet Size for Location Addition");
            }



            return new NoTiFiLocationAddition(msgID, readUserID, readLongitude, readLatitude, readName, readDesc);

        }catch (IOException e){
            throw new IllegalArgumentException("Error in Reading Occurred. Message: "+ e.getMessage());
        }

        // Return a new NoTiFiLocationAddition

    }







    /**
     * Returns a String Representation
     * @return String Representation
     */
    @Override
    public String toString() {
        return "Addition: msgid="+msgId+" "+userId+':'+locationName+'-'+locationDescription+" ("+ (int) Math.ceil(longitude)+','+ (int) Math.ceil(latitude)+")";
    }





    /**
     * Serializes a NoTiFi Location Addition Message
     * @return the serialized message
     */
    @Override
    public byte[] encode() {

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteStream);

        try {
            // Write the Header & User ID
            NoTiFiWriter.writeNoTiFiHeader(out, LOCATION_ADDITION_CODE, msgId);
            out.writeInt(userId);

            // Write the Longitude & Latitude
            NoTiFiWriter.writeDoubleValue(out, longitude);
            NoTiFiWriter.writeDoubleValue(out, latitude);


            // Write the Byte Name Length & Name
            out.write((byte) locationName.length());
            out.write(locationName.getBytes(StandardCharsets.US_ASCII));


            // Write the Byte Description Length & Description
            out.write((byte) locationDescription.length());
            out.write(locationDescription.getBytes(StandardCharsets.US_ASCII));


            out.flush();
        } catch (IOException e) {
        }



        return byteStream.toByteArray();




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
        NoTiFiValidator.validUnsignedInteger(userId);
        this.userId = userId;
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
        try {
            AddatudeValidator.validLongitude(Double.toString(longitude));
            this.longitude = longitude;

        } catch (ValidationException e) {
            this.longitude = 50;
            throw new IllegalArgumentException("Invalid Latitude Value Set");
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
        try {
            AddatudeValidator.validLatitude(Double.toString(latitude));
            this.latitude = latitude;

        } catch (ValidationException e) {
            this.latitude = 50;
            throw new IllegalArgumentException("Invalid Latitude Value Set");
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

        NoTiFiValidator.validCharacterSequence("Location Name",locationName);

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
        NoTiFiValidator.validCharacterSequence("Location Description",locationDescription);


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




    @Override
    public int getCode() {
        return LOCATION_ADDITION_CODE;
    }


}

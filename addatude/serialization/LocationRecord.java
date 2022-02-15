/************************************************
 *
 * Author: Bryce McWhirter
 * Assignment: Program 0
 * Class: Data Communications
 *
 ************************************************/

package serialization;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class LocationRecord {

    long userID;
    String longitude;
    String latitude;
    String locationName;
    String locationDescription;

    public static final int LOCATION_NAME_AND_DESCRIPTION = 2;



    /**
     * Generates a New Location Record
     * @param userID  The ID of the User
     * @param longitude The Longitude Value of the Location
     * @param latitude The Latitude Value of the Location
     * @param locationName The Location Name
     * @param locationDescription The Location Description
     * @throws ValidationException
     *      If Any of these parameters are invalid
     */
    public LocationRecord(long userID, String longitude, String latitude, String locationName, String locationDescription) throws ValidationException {

        setUserID(userID);
        setLongitude(longitude);
        setLatitude(latitude);
        setLocationName(locationName);
        setLocationDescription(locationDescription);

    }


    /**
     * Generates a Location Record from a Message Input Stream
     * @param in The Input Stream to Deserialize the Location Record from
     * @throws ValidationException
     *      If any of these parameters read are invalid
     * @throws IOException
     *      If an error occurred during reading
     */
    public LocationRecord(MessageInput in) throws ValidationException, IOException {
        Objects.requireNonNull(in);

        //Read the UserID
        long readUserID = Long.parseLong(in.readUntilSpace());
        setUserID(readUserID);


        //Read the Longitude
        String readLongitude = in.readUntilSpace();
        setLongitude(readLongitude);


        //Read the Latitude
        String readLatitude =in.readUntilSpace();
        setLatitude(readLatitude);




        String[] tokens = new String[2];

        //For The Location Name and Description
        // Read the Size of the Value
        // Read 'Size' number of chars for Value
        for(int i = 0; i < LOCATION_NAME_AND_DESCRIPTION; i++){
            try {
                int size = in.readIntegerValue();
                byte[] buf = in.readNumOfValues(size);
                tokens[i] = new String(buf, StandardCharsets.UTF_8);
            }
            catch(NumberFormatException e){
                throw new ValidationException("Invalid Stream", "Location Record Stream has invalid value");
            }
        }


        in.testLocationRecordFullyRead();


        setLocationName(tokens[0]);
        setLocationDescription(tokens[1]);




    }


    /**
     * Encodes a Location Record to a MessageOutput
     * @param out The output of the written message
     * @throws IOException
     *      If an output error occurs
     */
    public void encode(MessageOutput out) throws IOException {

        //write the userID
        out.write((userID + " ").getBytes(StandardCharsets.UTF_8));


        //write the longitude
        out.write((longitude + " ").getBytes(StandardCharsets.UTF_8));


        //write the latitude
        out.write((latitude +" ").getBytes(StandardCharsets.UTF_8));


        // write the length & value of location mame
        out.write((locationName.length() + " " + locationName).getBytes(StandardCharsets.UTF_8));

        // write the length & value of location description
        out.write((locationDescription.length() + " " + locationDescription).getBytes(StandardCharsets.UTF_8));


    }


    /**
     * Generates a String Representation of a Location Record
     * @return a string representing a location record
     */
    @Override
    public String toString() {

        String longVal =  getLongitude();
        String latVal =  getLatitude();


        return getUserID() + ":" + getLocationName()+"-"+getLocationDescription()+" ("+latVal + ".0" +","+longVal+".0"+")";

    }


    /**
     * returns the user id
     * @return the user id
     */
    public long getUserID() {

        return userID;
    }


    /**
     * validates and sets userID
     * @param userID the new userID
     * @throws ValidationException
     *      If the new userID is invalid
     */
    public void setUserID(long userID) throws ValidationException {
        Validator.validUnsignedInteger(userID);
        this.userID = userID;
    }


    /**
     * returns the longitude
     * @return the longitude
     */
    public String getLongitude() {

        return longitude;
    }




    /**
     * validates and sets the longitude
     * @param longitude the new longitude
     * @throws ValidationException
     *      If the new longitude is invalid
     */
    public LocationRecord setLongitude(String longitude) throws ValidationException {
        Validator.validLongitude(longitude);
        this.longitude = longitude;
        return this;
    }


    /**
     * returns the latitude
     * @return the latitude
     */
    public String getLatitude() {

        return latitude;
    }


    /**
     * validates and sets the latitude
     * @param latitude the new latitude
     * @throws ValidationException
     *      if the latitude is invalid
     */
    public LocationRecord setLatitude(String latitude) throws ValidationException {
        Validator.validLatitude(latitude);
        this.latitude = latitude;
        return this;
    }


    /**
     * returns the location name
     * @return the location name
     */
    public String getLocationName() {

        return locationName;
    }


    /**
     * validates and sets the location name
     * @param locationName the new location name
     * @throws ValidationException
     *      if the location name is invalid
     */
    public LocationRecord setLocationName(String locationName) throws ValidationException {
        Validator.validCharacterList(locationName);
        this.locationName = locationName;
        return this;
    }


    /**
     * return the location description
     * @return the location description
     */
    public String getLocationDescription() {

        return locationDescription;
    }


    /**
     * validates and sets the location description
     * @param locationDescription the new location description
     * @throws ValidationException
     *      if the location description is invalid
     */
    public LocationRecord setLocationDescription(String locationDescription) throws ValidationException {
        Validator.validCharacterList(locationDescription);
        this.locationDescription = locationDescription;
        return this;
    }


    /**
     * Equals Implementation
     * @param o The Location Record to test against
     * @return a boolean describing if two location
     *  records are the same.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationRecord that = (LocationRecord) o;
        return userID == that.userID && longitude.equals(that.longitude) && latitude.equals(that.latitude) && locationName.equals(that.locationName) && locationDescription.equals(that.locationDescription);
    }


    /**
     * Hash Code implementation
     * @return A hash representation
     */
    @Override
    public int hashCode() {
        return Objects.hash(userID, longitude, latitude, locationName, locationDescription);
    }












}

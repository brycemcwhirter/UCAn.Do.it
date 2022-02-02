/************************************************
 *
 * Author: Bryce McWhirter
 * Assignment: Program 0
 * Class: Data Communications
 *
 ************************************************/

package serialization;

import javax.xml.stream.Location;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.regex.Pattern;

public class LocationRecord {

    long userID;
    double longitude;
    double latitude;
    String locationName;
    String locationDescription;

    public static final int LOCATION_NAME_AND_DESCRIPTION = 2;



    public LocationRecord(long userID, double longitude, double latitude, String locationName, String locationDescription) throws ValidationException {

        setUserID(userID);
        setLongitude(longitude);
        setLatitude(latitude);
        setLocationName(locationName);
        setLocationDescription(locationDescription);

    }






    public LocationRecord(MessageInput in) throws ValidationException, IOException {
        Objects.requireNonNull(in);

        //Read the UserID
        Long readUserID = Long.parseLong(in.readUntilSpace());


        //Read the Longitude
        Double readLongitude = Double.parseDouble(in.readUntilSpace());


        //Read the Latitude
        Double readLatitude = Double.parseDouble(in.readUntilSpace());




        String[] tokens = new String[2];

        //For The Location Name and Description
        // Read the Size of the Value
        // Read 'Size' number of chars for Value
        for(int i = 0; i < LOCATION_NAME_AND_DESCRIPTION; i++){
            int size = in.readIntegerValue();
            byte[] buf  = in.readNumOfValues(size);
            tokens[i] = new String(buf, StandardCharsets.UTF_8);
        }


        setUserID(readUserID);
        setLongitude(readLongitude);
        setLatitude(readLatitude);
        setLocationName(tokens[0]);
        setLocationDescription(tokens[1]);
    }









    public void encode(MessageOutput out) throws IOException {

        //write the userID
        out.write((Long.toString(userID) + " ").getBytes(StandardCharsets.UTF_8));


        //write the longitude
        out.write((Double.toString(longitude) + " ").getBytes(StandardCharsets.UTF_8));


        //write the latitude
        out.write((Double.toString(latitude)+" ").getBytes(StandardCharsets.UTF_8));


        // write the length & value of location mame
        out.write((Integer.toString(locationName.length()) + " " + locationName).getBytes(StandardCharsets.UTF_8));

        // write the length & value of location description
        out.write((Integer.toString(locationDescription.length()) + " " + locationDescription).getBytes(StandardCharsets.UTF_8));


    }








    @Override
    public String toString() {

        int longVal = (int) getLongitude();
        int latVal = (int) getLatitude();


        return String.valueOf(getUserID()) + ":" + getLocationName()+"-"+getLocationDescription()+" ("+latVal + ".0" +","+longVal+".0"+")";

    }






    public long getUserID() {
        return userID;
    }






    public void setUserID(long userID) throws ValidationException {
        validUserID(userID);
        this.userID = userID;
    }






    public double getLongitude() {
        return longitude;
    }






    public void setLongitude(double longitude) throws ValidationException {
        validLongitude(longitude);
        this.longitude = longitude;
    }






    public double getLatitude() {
        return latitude;
    }






    public void setLatitude(double latitude) throws ValidationException {
        validLatitude(latitude);
        this.latitude = latitude;
    }






    public String getLocationName() {
        return locationName;
    }






    public void setLocationName(String locationName) throws ValidationException {
        validString(locationName);
        this.locationName = locationName;
    }







    public String getLocationDescription() {
        return locationDescription;
    }







    public void setLocationDescription(String locationDescription) throws ValidationException {
        validString(locationDescription);
        this.locationDescription = locationDescription;
    }







    //Methods that test Valid Parameters
    void validUserID(long candidateID) throws ValidationException {
        if(candidateID < 0 || candidateID > Integer.MAX_VALUE){
            throw new ValidationException("User ID must be Unsigned Integer");
        }
    }






    void validString(String testString) throws ValidationException {

        //Make Sure String follows Regex #_word
        if(!Pattern.matches("^[a-zA-Z0-9_]*$", testString)){
            throw new ValidationException("String is not valid: "+testString);
        }

    }






    private void validDouble(String valString) throws ValidationException{


        if(Pattern.matches("^-?[0-9]+\\.[0-9]+$", valString) == false){
            throw new ValidationException("Longitude or Latitude value doesn't follow protocol specification");
        }


    }






    private void validLatitude(double latitude) throws ValidationException {

        validDouble(Double.toString(latitude));

        if(latitude > 90 || latitude < -90){
            throw new ValidationException("Latitude must be between -90 & 90");
        }
    }







    private void validLongitude(double longitude) throws ValidationException {

        validDouble(Double.toString(longitude));

        if(longitude > 180 || longitude < -180){
            throw new ValidationException("Longitude must be between -180 & 180");
        }
    }










}

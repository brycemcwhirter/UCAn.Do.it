package notifi.serialization;

import addatude.serialization.ValidationException;
import addatude.serialization.Validator;

public class NoTiFiLocationAddition extends NoTiFiMessage{

    static final int LOCATION_ADDITION_CODE = 1;

    int userId;
    double longitude;
    double latitude;
    String locationName;
    String locationDescription;

    public NoTiFiLocationAddition(int msgId, int userId, double longitude, double latitude, String locationName, String locationDescription) throws IllegalArgumentException{
        super(msgId, LOCATION_ADDITION_CODE);


        // Test for invalid Params
        try {
            Validator.validUnsignedInteger("UserId", Long.toString(userId));
            Validator.validLongitude(Double.toString(longitude));
            Validator.validLatitude(Double.toString(latitude));
            //todo what is valid aabout llocation name and description
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


    public int getUserId() {
        return userId;
    }

    public NoTiFiLocationAddition setUserId(int userId) throws IllegalArgumentException{
        this.userId = userId;
        try {
            Validator.validUnsignedInteger("User Id", Long.toString(userId));
        } catch (ValidationException e) {
            throw new IllegalArgumentException("Invalid User Id");
        }
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public NoTiFiLocationAddition setLongitude(double longitude) {
        this.longitude = longitude;
        try {
            Validator.validLongitude(Double.toString(longitude));
        } catch (ValidationException e) {
            throw new IllegalArgumentException("Invalid Longitude");
        }
        return this;
    }

    public double getLatitude() {
        return latitude;
    }







    public NoTiFiLocationAddition setLatitude(double latitude) throws IllegalArgumentException{
        this.latitude = latitude;
        try {
            Validator.validLatitude(Double.toString(latitude));
        } catch (ValidationException e) {
            throw new IllegalArgumentException("Invalid Latitude");
        }
        return this;
    }







    public String getLocationName() {
        return locationName;
    }






    public NoTiFiLocationAddition setLocationName(String locationName) throws IllegalArgumentException{
        this.locationName = locationName;
        return this;
    }






    public String getLocationDescription() {
        return locationDescription;
    }






    public NoTiFiLocationAddition setLocationDescription(String locationDescription) throws IllegalArgumentException{

        // Test Valid Location Description


        this.locationDescription = locationDescription;

        return this;
    }







    @Override
    public String toString() {
        return "Addition: msgid="+msgId+" userid="+userId+':'+locationName+'-'+locationDescription+" ("+longitude+','+latitude+")";
    }






    @Override
    byte[] encode() {
        return new byte[0];
    }
}

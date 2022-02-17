package addatude.serialization;

import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class Validator {

    static final int MAX_DOUBLE_SIZE = 10;


    /**
     * tests for a valid user ID
     * @param candidateInteger the candidate ID to be tested
     * @throws ValidationException
     *      if the candidateInteger is invalid
     */
    static void validUnsignedInteger(String candidateInteger) throws ValidationException {

        if(candidateInteger == null){
            throw new ValidationException("Null Unsigned Int", "Value cannot be null");
        }

        if(!candidateInteger.matches("\\d*")){
            throw new ValidationException("Invalid UserID", "User ID must be numeric: "+candidateInteger);
        }

        long val = Long.parseLong(candidateInteger);

        if(val < 0 || val > 99999){
            throw new ValidationException("Invalid Unsigned Integer", "Value must be Unsigned Integer");
        }
    }



    /**
     * tests if a character list is valid
     * @param testString the tested string
     * @throws ValidationException
     *      if character list is invalid
     */
    static void validString(String testString) throws ValidationException {

        if(testString == null){
            throw new ValidationException("null string", "value cannot be null: " + testString);
        }

        if(testString.isEmpty())
            return;

        if(!testString.matches(".*\\P{C}.*")){
            throw new ValidationException("Invalid String", "String Contains Unprintable Characters: "+testString);
        }


    }


    /**
     * tests if a double value is valid
     * @param valString the tested double value
     * @throws ValidationException
     *      if the value is not valid
     */
    static private void validDouble(String valString) throws ValidationException{

        if(valString == null){
            throw new ValidationException("Null String", "String Cannot be null: "+valString);
        }


        if(!Pattern.matches("^-?[0-9]+\\.[0-9]+$", valString) || valString.length() > MAX_DOUBLE_SIZE){
            throw new ValidationException("Invalid Double", "Double Value doesn't match Regex Pattern: "+ valString);
        }


    }


    /**
     * tests if a given latitude is valid
     * @param latitude the tested latitude
     * @throws ValidationException
     *      if the latitude is not valid
     */
    static void validLatitude(String latitude) throws ValidationException {

        validDouble(latitude);

        if(Double.parseDouble(latitude) > 90 || Double.parseDouble(latitude) < -90){
            throw new ValidationException("Invalid Latitude", "Latitude must be between -90 & 90");
        }
    }


    /**
     * tests if a given longitude is valid
     * @param longitude the given longitude
     * @throws ValidationException
     *      if the longitude is not valid
     */
    static void validLongitude(String longitude) throws ValidationException {

        validDouble(longitude);

        if(Double.parseDouble(longitude) > 180 || Double.parseDouble(longitude)  < -180){
            throw new ValidationException("Invalid Longitude", "Longitude must be between -180 & 180");
        }
    }


}

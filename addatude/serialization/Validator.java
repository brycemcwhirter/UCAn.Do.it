package serialization;

import java.util.regex.Pattern;

public class Validator {

    public static final int MAX_NUM_OF_CHARS_FOR_DOUBLE = 10;

    /**
     * tests for a valid user ID
     * @param candidateInteger the candidate ID to be tested
     * @throws ValidationException
     *      if the user ID is invalid
     */
    static void validUnsignedInteger(long candidateInteger) throws ValidationException {
        if(candidateInteger < 0 || candidateInteger > Integer.MAX_VALUE){
            throw new ValidationException("User ID must be Unsigned Integer");
        }
    }


    /**
     * tests if a character list is valid
     * @param testString the tested string
     * @throws ValidationException
     *      if character list is invalid
     */
    static void validCharacterList(String testString) throws ValidationException {

        //Make Sure String follows validChar
        if(!Pattern.matches("^[a-zA-Z0-9_]*$", testString)){
            throw new ValidationException("String is not valid: "+testString);
        }

    }


    /**
     * tests if a double value is valid
     * @param valString the tested double value
     * @throws ValidationException
     *      if the value is not valid
     */
    static private void validDouble(String valString) throws ValidationException{


        if(!Pattern.matches("^-?[0-9]+\\.[0-9]+$", valString) && (valString.length() <= MAX_NUM_OF_CHARS_FOR_DOUBLE)){
            throw new ValidationException("Longitude or Latitude value doesn't follow protocol specification");
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
            throw new ValidationException("Latitude must be between -90 & 90");
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
            throw new ValidationException("Longitude must be between -180 & 180");
        }
    }


    //todo write valid mapID (99999 is MAX (specification in Program1 Assignment))
}

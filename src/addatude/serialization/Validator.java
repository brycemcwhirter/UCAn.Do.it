/************************************************
 *
 * Author: Bryce McWhirter
 * Assignment: Program 0
 * Class: Data Communications
 *
 ************************************************/

/**
 * Testing Partner: John Harrison
 */

package addatude.serialization;

import java.util.Objects;
import java.util.regex.Pattern;

public class Validator {

    static final int MAX_DOUBLE_SIZE = 10;


    /**
     * tests for a valid user ID
     * @param candidateInteger the candidate ID to be tested
     * @throws ValidationException
     *      if the candidateInteger is invalid
     */
    public static void validUnsignedInteger(String parameter, String candidateInteger) throws ValidationException {

        try{
            Objects.requireNonNull(candidateInteger);

            if(!candidateInteger.matches("\\d*")){
                throw new ValidationException("Invalid UserID", parameter + " must be numeric: "+candidateInteger);
            }

            long val = Long.parseLong(candidateInteger);

            if(val < 0 || val > 99999){
                throw new ValidationException("Invalid Unsigned Integer", parameter + " must be Unsigned Integer");
            }
        }

        catch(NullPointerException e){
            throw new ValidationException("Null Unsigned Int", parameter + " cannot be null", e);
        }
    }



    /**
     * tests if a character list is valid
     *
     * @param parameter The parameter tested on
     * @param testString the tested string
     * @throws ValidationException
     *      if character list is invalid
     */
    public static void validString(String parameter, String testString) throws ValidationException {

        if(testString == null){
            throw new ValidationException("null string", parameter+" cannot be null: " + testString);
        }

        if(testString.isEmpty())
            return;



        if(!Pattern.matches("\\P{C}*", testString)){
            throw new ValidationException("Invalid String", parameter + " Contains Unprintable Characters: "+testString);
        }


    }


    /**
     * tests if a double value is valid
     * @param valString the tested double value
     * @throws ValidationException
     *      if the value is not valid
     */
    public static void validDouble(String parameter, String valString) throws ValidationException{

        if(valString == null){
            throw new ValidationException("Null String", parameter+ " Cannot be null: "+valString);
        }


        if(!Pattern.matches("^-?[0-9]+\\.[0-9]+$", valString) || valString.length() > MAX_DOUBLE_SIZE){
            throw new ValidationException("Invalid Double", "Double Value for "+parameter+" doesn't match Regex Pattern: "+ valString);
        }


    }


    /**
     * tests if a given latitude is valid
     * @param latitude the tested latitude
     * @throws ValidationException
     *      if the latitude is not valid
     */
    static void validLatitude(String latitude) throws ValidationException {

        validDouble("Latitude", latitude);

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

        validDouble("Longitude", longitude);

        if(Double.parseDouble(longitude) > 180 || Double.parseDouble(longitude)  < -180){
            throw new ValidationException("Invalid Longitude", "Longitude must be between -180 & 180");
        }
    }


    //TODO REMOVE OR ADD COMMENTS
    public static void validMessage(MessageInput in) throws ValidationException{
        if(!in.endOfStream()){
            throw new ValidationException("Invalid Stream", "Premature EOS");
        }
    }
}

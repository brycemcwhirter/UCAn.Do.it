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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;


/**
 * This class Validates the types
 * specific to the Addatude Protocol.
 */
public class Validator {


    /**
     * The maximum size for a double
     */
    static final int MAX_DOUBLE_SIZE = 10;

    /**
     * A list of operations.
     */
    private static final List<String> Operations = Arrays.asList("NEW", "ALL", "RESPONSE", "ERROR");



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
                throw new ValidationException(candidateInteger, parameter + " must be numeric");
            }

            long val = Long.parseLong(candidateInteger);

            if(val < 0 || val > 99999){
                throw new ValidationException(candidateInteger, parameter + " must be Unsigned Integer");
            }
        }

        catch(NullPointerException e){
            throw new ValidationException(null, parameter + " cannot be null", e);
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

        if(Objects.isNull(testString)){
            throw new ValidationException(null, parameter+" cannot be null");
        }

        if(testString.isEmpty())
            return;



        if(!Pattern.matches("\\P{C}*", testString)){
            throw new ValidationException(testString, parameter + " Contains Unprintable Characters");
        }
    }


    /**
     * tests if a double value is valid
     * @param valString the tested double value
     * @throws ValidationException
     *      if the value is not valid
     */
    public static void validDouble(String parameter, String valString) throws ValidationException{

        if(Objects.isNull(valString)){
            throw new ValidationException(null, parameter+ " Cannot be null ");
        }


        if(!Pattern.matches("^-?[0-9]+\\.[0-9]+$", valString) || valString.length() > MAX_DOUBLE_SIZE){
            throw new ValidationException(valString, "Double Value for "+parameter+" is not a valid double");
        }


    }


    /**
     * tests if a given latitude is valid
     * @param latitude the tested latitude
     * @throws ValidationException
     *      if the latitude is not valid
     */
    public static void validLatitude(String latitude) throws ValidationException {

        validDouble("Latitude", latitude);

        if(Double.parseDouble(latitude) > 90 || Double.parseDouble(latitude) < -90){
            throw new ValidationException(latitude, "Latitude must be between -90 & 90");
        }
    }


    /**
     * tests if a given longitude is valid
     * @param longitude the given longitude
     * @throws ValidationException
     *      if the longitude is not valid
     */
    public static void validLongitude(String longitude) throws ValidationException {

        validDouble("Longitude", longitude);

        if(Double.parseDouble(longitude) > 180 || Double.parseDouble(longitude)  < -180){
            throw new ValidationException(longitude, "Longitude must be between -180 & 180");
        }
    }


    /**
     * Tests to make a sure an operation is valid
     * @param operation the operation to test on
     */
    public static void validOperation(String operation) throws ValidationException {
        if (!Operations.contains(operation)){
            throw new ValidationException(operation, "Operation not allowed");
        }
    }



}

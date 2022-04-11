package notifi.serialization;

import addatude.serialization.AddatudeValidator;
import addatude.serialization.ValidationException;

import java.net.Inet4Address;
import java.util.regex.Pattern;

public class NoTiFiValidator {

    static final int LARGEST_PORT_VAL = 65535;




    public static void validUnsignedInteger(int userId) {

        if(userId < 0 || userId > 99999){
            throw new IllegalArgumentException("User ID Is Invalid: " + userId);
        }

    }





    public static void validCharacterSequence(String param, String value) throws IllegalArgumentException {


        if(value == null){
            throw new IllegalArgumentException(param + " cannot be null");
        }

        // Valid Character Sequence Length
        if(value.length() > 255){
            throw new IllegalArgumentException(param + "Does Not Fit in UDP Packet");
        }


        for(int i = 0; i < value.length(); i++){
            int val = value.charAt(i);

            if(val < 32 || val > 126){
                throw new IllegalArgumentException("Error Message must only contain ASCII-printable characters: "+ value);
            }
        }

    }


    /**
     * Test if an Address is valid
     * @param address the address
     * @throws IllegalArgumentException
     *      if the address is invalid
     */
    public static void validAddress(Inet4Address address) throws IllegalArgumentException{
        if(address == null){
            throw new IllegalArgumentException("Address cannot be null");
        }
    }


    public static void validateSizeRead(String readName, String readDesc) {



    }
}

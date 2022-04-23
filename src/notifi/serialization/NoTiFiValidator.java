/*
 *
 * Author: Bryce McWhirter
 * Assignment: Program 5
 * Class: Data Communications
 *
*/

package notifi.serialization;


import java.net.Inet4Address;


/**
 * This class helps in validating
 * NoTiFi Data
 */
public class NoTiFiValidator {

    // The largest port value available
    static final int LARGEST_PORT_VAL = 65535;


    /**
     * Validates an unsigned integer
     * @param candidate the integer
     */
    public static void validUnsignedInteger(int candidate) {

        if(candidate < 0 || candidate > 99999){
            throw new IllegalArgumentException("User ID Is Invalid: " + candidate);
        }

    }


    /**
     * Validates a character sequence
     * @param param the parameter of the value
     * @param value the candidate value
     * @throws IllegalArgumentException
     *      if the value is illegal
     */
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


    /**
     * Validates the Size of a Packet
     * @param actual the actual size
     * @param expected the expected size
     */
    public static void validatePacketSize(int actual, int expected){
        if(actual != expected){
            throw new IllegalArgumentException("Invalid Size of Packet");
        }
    }

}

/************************************************
 *
 * Author: Bryce McWhirter
 * Assignment: Program 1
 * Class: Data Communications
 *
 ************************************************/

package addatude.serialization;

import java.io.IOException;
import java.util.Objects;

public class Error extends Message{

    String errorMessage;
    private static final String OPERATION = "ERROR";





    /**
     * Creates a new Error Message
     * @param mapId The Associated mapID
     * @param errorMessage The Error Message
     * @throws ValidationException
     *      If any of these parameters are invalid
     */
    public Error(long mapId, String errorMessage) throws ValidationException{
        super(OPERATION, mapId);
        if(errorMessage == null){
            throw new ValidationException("Null Error Message", "Error Message Cannot be null");
        }
        Validator.validString("Param", errorMessage);
        this.errorMessage = errorMessage;

    }






    /**
     * @return the error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }




    /**
     * Sets the Error Message to a new message
     * @param errorMessage the new error message
     * @return the associate error with the new message
     */
    public Error setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }


    /**
     * To String Implementation
     * @return a String representation of
     *  an error message
     */
    @Override
    public String toString() {
        return " map="+getMapID()+" error="+errorMessage;
    }





    /**
     * Encode Implementation for an Error Messsage
     * @param out The Output Stream to write to
     * @throws IOException
     *      If a reading error occurs
     */
    @Override
    public void encode(MessageOutput out) throws IOException {
        out.writeMessageHeader(getMapID(), getOperation());
        out.writeString(errorMessage);
        out.writeMessageFooter();
    }





    /**
     * Equals Implementation
     * @param o The Message Object
     * @return a boolean describing if the
     *   two error messages are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Error error = (Error) o;
        return errorMessage.equals(error.errorMessage);
    }



    /**
     * Hash Code Implementation
     * @return Hash Code Representation
     *   of an error message
     */
    @Override
    public int hashCode() {
        return Objects.hash(errorMessage);
    }
}

/************************************************
 *
 * Author: Bryce McWhirter
 * Assignment: Program 1
 * Class: Data Communications
 *
 ************************************************/

/*
 * Testing Partner: John Harrison
 */

package addatude.serialization;

import java.io.IOException;
import java.util.Objects;


/**
 * The Error Class is a specific
 * Message that describes an Error within
 * the protocol.
 */
public class Error extends Message{

    String errorMessage; //The Specific Messaged tied to the Error
    public static final String OPERATION = "ERROR"; // The Operation of the Error Message





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
            throw new ValidationException(null, "Error Message Cannot be null");
        }
        Validator.validString("Error Message", errorMessage);
        Validator.validUnsignedInteger("Error Message Size", String.valueOf(errorMessage.length()));
        this.errorMessage = errorMessage;

    }








    /**
     * Creates a new error message from an input stream.
     * @param mapID The Map ID of the Error Message
     * @param in The Message Input holding the message
     * @throws ValidationException
     *      if an invalid parameter is within the message
     */
    public Error(long mapID, MessageInput in) throws ValidationException {
        super(OPERATION, mapID);
        String msgSize = in.readUntilSpace();
        Validator.validUnsignedInteger("Error Message Size", msgSize);


        int size = Integer.parseInt(msgSize);
        String testMsg = new String(in.readNumOfValues(size));
        Validator.validString("Error Message", testMsg);
        Validator.validUnsignedInteger("Error Message Size", String.valueOf(testMsg.length()));


        this.errorMessage = testMsg;
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
    public Error setErrorMessage(String errorMessage) throws ValidationException {
        Validator.validString("Error Message", errorMessage);
        Validator.validUnsignedInteger("Error Message Size", String.valueOf(errorMessage.length()));
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
        return "Error: map="+ getMapId()+" error="+errorMessage;
    }








    /**
     * Encode Implementation for an Error Messsage
     * @param out The Output Stream to write to
     * @throws IOException
     *      If a reading error occurs
     */
    @Override
    public void encode(MessageOutput out) throws IOException {
        Message.writeMessageHeader(getMapId(), getOperation(), out);
        out.writeString(errorMessage);
        Message.writeMessageFooter(out);
    }


    /**
     * @return The Operation
     */
    @Override
    public String getOperation() {
        return OPERATION;
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
        return Objects.hash(super.hashCode(), errorMessage);
    }
}

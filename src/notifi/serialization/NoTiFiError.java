/************************************************
 *
 * Author: Bryce McWhirter
 * Assignment: Program 4
 * Class: Data Communications
 *
 ************************************************/

package notifi.serialization;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * The NoTiFiError is a type of NoTiFiMessage that
 * specifies an error notification.
 */
public class NoTiFiError extends NoTiFiMessage{

    /**
     * The Operation Code for the Error
     * Message
     */
    static final byte ERROR_CODE = 2;


    /**
     * The Error Message for the Error Notification
     */
    String errorMessage;


    /**
     * Constructs a NoTiFi Error Message
     * @param msgId the message id
     * @param errorMessage the error message
     * @throws IllegalArgumentException
     *      if any of these parameters are invalid
     */
    public NoTiFiError(int msgId, String errorMessage) throws IllegalArgumentException{
        super(msgId, ERROR_CODE);

        // Test Invalid errorMessage
        testErrorMessage(errorMessage);

        this.errorMessage = errorMessage;
    }


    /**
     * Deserializes a NoTiFi Error Message
     * @param msgID the message id
     * @param byteBuffer the buffer holding the message
     * @return the new NoTiFi Error Message
     */
    public static NoTiFiError decode(int msgID, ByteBuffer byteBuffer){
        byte[] b = new byte[byteBuffer.remaining()];
        byteBuffer.get(b);
        return new NoTiFiError(msgID, new String(b, StandardCharsets.US_ASCII));
    }






    /** Returns the String Representation of the NoTiFi Error
     * @return NoTiFi Error
     */
    @Override
    public String toString() {
        return "Error: msgid="+msgId+' '+errorMessage;
    }






    /**
     * Sets the error message
     * @param errorMessage the new error message
     * @return this object with the new error message
     * @throws IllegalArgumentException
     *      if the error message is invalid
     */
    public NoTiFiError setErrorMessage(String errorMessage) throws IllegalArgumentException{
        // Test Invalid errorMessage
        testErrorMessage(errorMessage);
        this.errorMessage = errorMessage;
        return this;
    }







    /**
     * gets the error message
     * @return error message
     */
    public String getErrorMessage() {

        return errorMessage;
    }


    /**
     * Serializes a NoTiFi Error Message
     * @return the serialized message
     */
    @Override
    public byte[] encode() {

        ByteBuffer b = ByteBuffer.allocate(2+errorMessage.length());

        // Write Message Header
        writeNoTiFiHeader(b, ERROR_CODE);

        // Write the error message
        b.put(errorMessage.getBytes(StandardCharsets.US_ASCII));

        return b.array();
    }


    /**
     * Test if a NoTiFi Error Message is valid
     * @param errorMessage the error message
     */
    private void testErrorMessage(String errorMessage) {
        if(errorMessage.matches("\\A\\p{ASCII}*\\z")){
            return;
        }
        throw new IllegalArgumentException("Error Message must only contain ASCII-printable characters: "+ errorMessage);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoTiFiError that = (NoTiFiError) o;
        return errorMessage.equals(that.errorMessage);
    }



    @Override
    public int hashCode() {
        return Objects.hash(errorMessage);
    }
}

package notifi.serialization;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * The NoTiFiError is a type of NoTiFiMessage that
 * specifies an error notification.
 */
public class NoTiFiError extends NoTiFiMessage{

    /**
     * The Operation Code for the Error
     * Message
     */
    static final short ERROR_CODE = 2;


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
    NoTiFiError(int msgId, String errorMessage) throws IllegalArgumentException{
        super(msgId, ERROR_CODE);

        // Test Invalid errorMessage
        testErrorMessage(errorMessage);

        this.errorMessage = errorMessage;
    }


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









    //TODO Encode Implementation
    @Override
    public byte[] encode() {

        return new byte[0];
    }







    private void testErrorMessage(String errorMessage) {
        if(Charset.forName("US-ASCII").newEncoder().canEncode(errorMessage)){
            return;
        }
        throw new IllegalArgumentException("Error Message must only contain ASCII-printable characters: "+ errorMessage);
    }
}

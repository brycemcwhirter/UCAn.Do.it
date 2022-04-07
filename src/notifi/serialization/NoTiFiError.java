/************************************************
 *
 * Author: Bryce McWhirter
 * Assignment: Program 4
 * Class: Data Communications
 *
 ************************************************/

package notifi.serialization;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
     * @param in the Data Input Stream holding the message
     * @return the new NoTiFi Error Message
     */
    public static NoTiFiError decode(int msgID, DataInputStream in) throws IOException {
        String errorMessage = new String(in.readAllBytes(), StandardCharsets.US_ASCII);
        return new NoTiFiError(msgID, errorMessage);
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

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteStream);

        try {
            writeNoTiFiHeader(out, ERROR_CODE);
            out.write(errorMessage.getBytes(StandardCharsets.US_ASCII));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] data = byteStream.toByteArray();
        return data;

    }


    /**
     * Test if a NoTiFi Error Message is valid
     * @param errorMessage the error message
     */
    private void testErrorMessage(String errorMessage) {

        if(errorMessage == null){
            throw new IllegalArgumentException("Error Message cannot be null");
        }

        if(errorMessage.matches("\\A\\p{ASCII}*\\z")){
            return;
        }

        throw new IllegalArgumentException("Error Message must only contain ASCII-printable characters: "+ errorMessage);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        NoTiFiError that = (NoTiFiError) o;
        return errorMessage.equals(that.errorMessage);
    }



    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), errorMessage);
    }
}

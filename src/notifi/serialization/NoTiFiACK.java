/************************************************
 *
 * Author: Bryce McWhirter
 * Assignment: Program 4
 * Class: Data Communications
 *
 ************************************************/

package notifi.serialization;


import java.io.ByteArrayOutputStream;

/**
 * The NoTiFi Ack is a type of NoTiFi Message
 * that represents an acknowledgement of a received
 * notification.
 */
public class NoTiFiACK extends NoTiFiMessage{


    /**
     * The Operation Code Specifying an ACK
     */
    public static final short ACK_CODE = 3;


    /**
     * Constructs a NoTiFi Ack Message
     * @param msgId the message id
     * @throws IllegalArgumentException
     *      if invalid message id
     */
    public NoTiFiACK(int msgId) throws IllegalArgumentException{
        super(msgId, ACK_CODE);
    }


    //TODO Encode Implementation
    @Override
    public byte[] encode() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();


        //Write Message Header



        //ACK has no payload

        return null;
    }


    /** Returns a string representation of an ACK message
     * @return the string representation of an ACK
     */
    @Override
    public String toString() {
        return "ACK: msgid="+msgId;
    }
}

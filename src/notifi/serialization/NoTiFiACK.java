/************************************************
 *
 * Author: Bryce McWhirter
 * Assignment: Program 4
 * Class: Data Communications
 *
 ************************************************/

package notifi.serialization;


import java.nio.ByteBuffer;

/**
 * The NoTiFi Ack is a type of NoTiFi Message
 * that represents an acknowledgement of a received
 * notification.
 */
public class NoTiFiACK extends NoTiFiMessage{


    /**
     * The Operation Code Specifying an ACK
     */
    public static final byte ACK_CODE = 0x03;


    /**
     * Constructs a NoTiFi Ack Message
     * @param msgId the message id
     * @throws IllegalArgumentException
     *      if invalid message id
     */
    public NoTiFiACK(int msgId) throws IllegalArgumentException{
        super(msgId, ACK_CODE);
    }


    /**
     * Serializes a NoTiFiAck
     * @return a serialized NoTiFiAck
     */
    @Override
    public byte[] encode() {

        ByteBuffer b = ByteBuffer.allocate(2);

        // Write Message Header
        writeNoTiFiHeader(b, ACK_CODE);


        // ACK has no payload so write nothing else
        return b.array();
    }






    /** Returns a string representation of an ACK message
     * @return the string representation of an ACK
     */
    @Override
    public String toString() {
        return "ACK: msgid="+msgId;
    }



}

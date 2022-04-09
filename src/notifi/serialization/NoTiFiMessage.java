/************************************************
 *
 * Author: Bryce McWhirter
 * Assignment: Program 4
 * Class: Data Communications
 *
 ************************************************/

package notifi.serialization;


import java.io.*;
import java.nio.ByteBuffer;
import java.util.Objects;


/**
 * The NoTiFi Message is a Message to send
 * Notifications of interesting events in
 * the AddATude Protocol
 */
public abstract class NoTiFiMessage {

    /**
     * A Set of Valid OP Codes for the NoTiFiMessage
     */
    static final short[] VALID_OP_CODES = {0,1,2,3};


    /**
     * The Current NoTiFi Message Version
     */
    static final byte VALID_VERSION = 0x30;




    /**
     *  The Message ID of the Message
     */
    int msgId;





    /**
     * The Mask used to receive the NoTiFi Message Type
     */
    static final int CODE_MASK = 0x0f;





    /**
     * General Constructor for NoTiFi Message. Sets the Message
     * ID and the Operation Code
     * @param msgId the message id
     * @throws IllegalArgumentException
     *      if any of these parameters are invalid
     */
    public NoTiFiMessage(int msgId) throws IllegalArgumentException{

        // Test Valid msgId
        //Test Valid Operation code
        testMessageID(msgId);


        // Set the parameters
        this.msgId = msgId;
    }




    /**
     * Deserializes a message
     * @param pkt the serialized message
     * @return the new message
     *
     */
    public static NoTiFiMessage decode(byte[] pkt)  {

        ByteArrayInputStream bs = new ByteArrayInputStream(pkt);
        DataInputStream in = new DataInputStream(bs);
        NoTiFiMessage message;


        // Read the Version & code
        byte versionAndCode;




        try {
            versionAndCode = in.readByte();


            // Split the Version & The Code
            byte version = (byte) (versionAndCode >>> 4);
            byte code = (byte) (versionAndCode & CODE_MASK);

            // Validate the Version
            testValidVersion(version);

            // Read the Message ID
            int readID = in.readUnsignedByte();

            // Validate the Message ID
            testMessageID(readID);




            // Switch Statement Based On Code
            switch(code){
                case NoTiFiRegister.REGISTER_CODE -> message = NoTiFiRegister.decode(readID, in);


                case NoTiFiLocationAddition.LOCATION_ADDITION_CODE -> message = NoTiFiLocationAddition.decode(readID, in);


                case NoTiFiError.ERROR_CODE -> message =  NoTiFiError.decode(readID, in);


                case NoTiFiACK.ACK_CODE -> message = new NoTiFiACK(readID);


                default -> throw new IllegalArgumentException("Invalid Code on Notification Message: " + code);
            }

        } catch (IOException e) {
            throw new IllegalArgumentException("A Bad Read Occurred. Message: "+ e.getLocalizedMessage());

        }




        return message;
    }




    /**
     * Serializes Message
     * Declared abstract with respect to specific
     * NoTiFi Messages
     * @return the serialized byte message
     */
    public abstract byte[] encode();








    /**
     * Gets the operation code
     * @return operation code
     */
    public abstract int getCode();








    /**
     * Gets the Message ID
     * @return the message id
     */
    public int getMsgId() {
        return msgId;
    }








    /**
     * Sets the message ID
     * @param msgId the message id
     * @return this object with the new message id
     * @throws IllegalArgumentException
     *      if invalid message id
     */
    public NoTiFiMessage setMsgId(int msgId) throws IllegalArgumentException{
        testMessageID(msgId);
        this.msgId = msgId;
        return this;
    }






    /**
     * Tests if the message id is valid
     * @param msgId the message id
     */
    static void testMessageID(int msgId){
        if(msgId < 0 || msgId > 255){
            throw new IllegalArgumentException("Illegal Message ID Value: "+ msgId);
        }
    }






    /**
     * Tests if the operation code is valid
     * @param operationCode the operation code
     * @throws IllegalArgumentException
     *      if the operation code is invalid
     */
    private static void testValidOpCode(int operationCode) throws IllegalArgumentException{
        for (int val: VALID_OP_CODES) {
            if(operationCode == val) {
                return;
            }
        }

        throw new IllegalArgumentException("Invalid Operation Code: "+ operationCode);
    }






    /**
     * Tests if the Message Version is currently valid
     * @param versionCandidate the version of the received message
     */
    private static void testValidVersion(int versionCandidate) {
        byte version = (byte) (VALID_VERSION >>> 4);
        if(versionCandidate != version){
            throw new IllegalArgumentException("Invalid Version For Notification: " + versionCandidate);
        }

    }







    /**
     * Writes the NoTiFi Header on the Byte Buffer
     * @param daous The Data Output Stream
     * @param OP_CODE the Op Code of the respected Message
     */
    public void writeNoTiFiHeader(DataOutputStream daous, byte OP_CODE) throws IOException {
        byte versionAndCode = (byte) (VALID_VERSION | OP_CODE);

        //Write Message Header
        daous.write(versionAndCode);
        daous.write((byte) msgId);
    }







    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoTiFiMessage that = (NoTiFiMessage) o;
        return msgId == that.msgId;
    }






    @Override
    public int hashCode() {
        return Objects.hash(msgId);
    }



}

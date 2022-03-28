package notifi.serialization;


import addatude.serialization.ValidationException;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public abstract class NoTiFiMessage {

    /**
     * A Set of Valid OP Codes for the NoTiFiMessage
     */
    static final short VALID_OP_CODE[] = {0,1,2,3};


    /**
     * The Current NoTiFi Message Version
     */
    static final byte VALID_VERSION = 3;

    /**
     * The Operation Code of the Message
     */
    int operationCode;

    /**
     *  The Message ID of the Message
     */
    int msgId;


    /**
     * General Constructor for NoTiFi Message. Sets the Message
     * ID and the Operation Code
     * @param msgId the message id
     * @param operationCode the operation code
     * @throws IllegalArgumentException
     *      if any of these parameters are invalid
     */
    public NoTiFiMessage(int msgId, short operationCode) throws IllegalArgumentException{

        // Test Valid msgId
        //Test Valid Operation code
        testMessageID(msgId);
        testValidOpCode(operationCode);


        // Set the parameters
        this.msgId = msgId;
        this.operationCode = operationCode;
    }





    // TODO write decode implementation
    public static NoTiFiMessage decode(byte[] pkt) throws IOException, ValidationException {
        ByteBuffer byteBuffer = ByteBuffer.wrap(pkt);
        NoTiFiMessage message;

        // Read the Version
        byte version = byteBuffer.get();
        
        // Test if Valid Version
        testValidVersion(version);



        // Read the Code
        byte code = byteBuffer.get();

        // Validate Code
        testValidOpCode(code);



        // Read the Message ID
        short readID = byteBuffer.getShort();

        // Validate the Message ID
        testMessageID(readID);


        // Switch Statement Based On Code
        switch(code){
            case NoTiFiRegister.REGISTER_CODE -> {
                message = NoTiFiRegister.decode(readID, byteBuffer);
            }
            case NoTiFiLocationAddition.LOCATION_ADDITION_CODE -> {
                message = NoTiFiLocationAddition.decode(readID, byteBuffer);
            }
            case NoTiFiError.ERROR_CODE -> {
                message =  NoTiFiError.decode(readID, byteBuffer);
            }
            case NoTiFiACK.ACK_CODE -> {
                message = new NoTiFiACK(readID);
            }
            default -> {
                throw new IllegalArgumentException("Invalid Code on Notification Message: " + code);
            }
        }

        return message;
    }




    /** Serializes Message
     * Declared abstract with respect to specific
     * NoTiFi Messages
     * @return the serialized byte message
     */
    public abstract byte[] encode();








    /** Gets the operation code
     * @return operation code
     */
    public int getCode() {
        return operationCode;
    }








    /** Gets the Message ID
     * @return the message id
     */
    public int getMsgId() {
        return msgId;
    }








    /** Sets the message ID
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









    static void testMessageID(int msgId){
        if(msgId < 0 || msgId > 255){
            throw new IllegalArgumentException("Illegal Message ID Value: "+ msgId);
        }
    }









    static void testValidOpCode(short operationCode) throws IllegalArgumentException{
        for (int val: VALID_OP_CODE) {
            if(operationCode == val) {
                return;
            }
        }

        throw new IllegalArgumentException("Invalid Operation Code: "+ operationCode);
    }







    private static void testValidVersion(short versionCandidate) {
        if(versionCandidate != VALID_VERSION){
            throw new IllegalArgumentException("Invalid Version For Notification: " + versionCandidate);
        }

    }



}

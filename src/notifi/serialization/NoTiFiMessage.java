package notifi.serialization;


import java.util.Arrays;

public abstract class NoTiFiMessage {

    /**
     * A Set of Valid OP Codes for the NoTiFiMessage
     */
    static final int VALID_OP_CODE[] = {0,1,2,3};

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
    public NoTiFiMessage(int msgId, int operationCode) throws IllegalArgumentException{

        // Test Valid msgId
        //Test Valid Operation code
        testMessageID(msgId);
        testValidOpCode(operationCode);


        // Set the parameters
        this.msgId = msgId;
        this.operationCode = operationCode;
    }





    // TODO write decode implementation
    static NoTiFiMessage decode(byte[] pkt) {

        return null;
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



    void testMessageID(int msgId){
        if(msgId < 0 || msgId > 255){
            throw new IllegalArgumentException("Illegal Message ID Value: "+ msgId);
        }
    }



    void testValidOpCode(int operationCode) throws IllegalArgumentException{
        for (int val: VALID_OP_CODE) {
            if(operationCode == val) {
                return;
            }

        }

        throw new IllegalArgumentException("Invalid Operation Code: "+ operationCode);
    }
}

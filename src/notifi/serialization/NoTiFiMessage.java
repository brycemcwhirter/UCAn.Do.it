package notifi.serialization;

public abstract class NoTiFiMessage {

    int operationCode;
    int msgId;


    public NoTiFiMessage(int msgId, int operationCode) throws IllegalArgumentException{

        //Test Valid Operation code

        // Test Valid msgId

        // Set the parameters


    }

    static NoTiFiMessage decode(byte[] pkt) {

        return null;
    }


    abstract byte[] encode();



    public int getCode() {
        return operationCode;
    }


    public int getMsgId() {
        return msgId;
    }

    public NoTiFiMessage setMsgId(int msgId) {
        this.msgId = msgId;
        return this;
    }
}

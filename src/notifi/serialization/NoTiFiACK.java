package notifi.serialization;

public class NoTiFiACK extends NoTiFiMessage{

    static final int ACK_OPERATION_CODE = 3;


    NoTiFiACK(int msgId) throws IllegalArgumentException{
        super(msgId, ACK_OPERATION_CODE);

    }


    @Override
    byte[] encode() {
        //Write Message Header

        //ACK has no payload

        return null;
    }

    @Override
    public String toString() {
        return "ACK: msgid="+msgId;
    }
}

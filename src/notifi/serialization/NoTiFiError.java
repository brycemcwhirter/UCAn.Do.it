package notifi.serialization;

public class NoTiFiError extends NoTiFiMessage{

    static final int ERROR_ACK_CODE = 2;

    String errorMessage;

    NoTiFiError(int msgId, String errorMessage) throws IllegalArgumentException{
        super(msgId, ERROR_ACK_CODE);

        // Test Invalid errorMessage

        this.errorMessage = errorMessage;
    }




    public String getErrorMessage() {
        return errorMessage;
    }




    public NoTiFiError setErrorMessage(String errorMessage) throws IllegalArgumentException{

        // Test Invalid errorMessage


        this.errorMessage = errorMessage;

        return this;
    }




    @Override
    byte[] encode() {
        return new byte[0];
    }



    @Override
    public String toString() {
        return "Error: msgid="+msgId+' '+errorMessage;
    }
}

package serialization;

public class Error extends Message{

    String errorMessage;


    Error(long mapId, String errorMessage){

    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Error setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    @Override
    public String toString() {
        return "error";
        //todo Write the String Implementation of Error Message
    }
}

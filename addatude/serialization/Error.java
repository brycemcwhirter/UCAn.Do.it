package serialization;

import java.io.IOException;
import java.util.Objects;

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

    @Override
    public void encode(MessageOutput out) throws IOException {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Error error = (Error) o;
        return errorMessage.equals(error.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorMessage);
    }
}

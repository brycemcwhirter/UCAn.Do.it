package serialization;

import java.io.IOException;

public class LocationRequest extends Message{


    public LocationRequest(long mapId) throws ValidationException {
        super(mapId);
    }

    @Override
    public String toString() {
        return "location request";
        //todo write the string implementation of location request
    }

    @Override
    public void encode(MessageOutput out) throws IOException {

    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}

package serialization;

import java.io.IOException;

public class LocationRequest extends Message{


    LocationRequest(long mapId){
        this.mapId = mapId;
    }

    @Override
    public String toString() {
        return "location request";
        //todo write the string implementation of location request
    }

    @Override
    void encode(MessageOutput out) throws IOException {

    }
}

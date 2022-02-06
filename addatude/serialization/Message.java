package serialization;

import java.io.IOException;

public class Message {

    String operation;
    long mapId;




    static Message decode(MessageInput in) throws ValidationException, NullPointerException{

        return null;
    }





    void encode(MessageOutput out) throws IOException {

        //TODO Encode can be abstract

    }





    final long getMapID(){
        return mapId;

    }





    final String getOperation(){
        return operation;
    }





    final Message setMapId(long mapId) throws ValidationException{
        this.mapId = mapId;
        return this;
    }




}

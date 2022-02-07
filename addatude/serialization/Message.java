package serialization;

import java.io.IOException;

public abstract class Message {

    String operation;
    long mapId;


    abstract void encode(MessageOutput out) throws IOException;





    public static Message decode(MessageInput in) throws ValidationException, NullPointerException{

        return null;
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

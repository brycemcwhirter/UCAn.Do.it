package serialization;

import java.io.IOException;
import java.util.Objects;

public abstract class Message {

    String operation;
    long mapId;

    public Message(long mapID) throws ValidationException {
        Objects.requireNonNull(mapID);
        Validator.validUnsignedInteger(mapID);
        this.mapId = mapID;
    }




    public abstract void encode(MessageOutput out) throws IOException;





    public static Message decode(MessageInput in) throws ValidationException, NullPointerException{
        Objects.requireNonNull(in);

        return null;
    }






    public final long getMapID(){
        return mapId;

    }





    public final String getOperation(){
        return operation;
    }





    public final Message setMapId(long mapId) throws ValidationException{
        Validator.validUnsignedInteger(mapId);
        this.mapId = mapId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return mapId == message.mapId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mapId);
    }
}

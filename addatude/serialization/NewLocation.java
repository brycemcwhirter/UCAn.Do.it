package serialization;

import java.io.IOException;
import java.util.Objects;

public class NewLocation extends Message{

    long mapId;
    LocationRecord location;

    public NewLocation(long mapId, LocationRecord location) throws ValidationException{
        super("NEW", mapId);
        if(Objects.isNull(location))
            throw new ValidationException("Location Record cannot be null in New Location Instance");
        this.location = location;

    }



    public LocationRecord getLocation() {
        return location;
    }

    public NewLocation setLocation(LocationRecord location) throws ValidationException {
        if(Objects.isNull(location))
            throw new ValidationException("Location Record cannot be null in New Location Instance");
        this.location = location;
        return this;
    }

    //TODO make sure every class has valid string method


    @Override
    public String toString() {
        return "NewLocation{" +
                "mapId=" + mapId +
                ", location=" + location +
                '}';
    }

    @Override
    public void encode(MessageOutput out) throws IOException {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewLocation that = (NewLocation) o;
        return mapId == that.mapId && location.equals(that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mapId, location);
    }
}

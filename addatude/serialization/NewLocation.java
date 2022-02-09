package serialization;

import java.io.IOException;
import java.util.Objects;

public class NewLocation extends Message{

    long mapId;
    LocationRecord location;

    NewLocation(long mapId, LocationRecord location) throws ValidationException{

    }

    public LocationRecord getLocation() {
        return location;
    }

    public NewLocation setLocation(LocationRecord location) {
        this.location = location;
        return this;
    }

    @Override
    public String toString() {
        return "NewLocation{" +
                "mapId=" + mapId +
                ", location=" + location +
                '}';
    }

    @Override
    void encode(MessageOutput out) throws IOException {

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

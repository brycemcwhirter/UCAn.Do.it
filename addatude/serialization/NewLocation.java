package serialization;

public class NewLocation extends Message{

    long mapId;
    LocationRecord location;

    NewLocation(long mapId, LocationRecord location){

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
}

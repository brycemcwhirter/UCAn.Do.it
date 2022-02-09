package serialization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LocationResponse extends Message{

    List<LocationRecord> locationRecordList = new ArrayList<>();
    String mapName;





    LocationResponse(long mapId, String mapName) throws ValidationException{

    }



    public LocationResponse addLocationRecord(LocationRecord locationRecord){

        return this;
    }



    public List<LocationRecord> getLocationRecordList(){

        //todo Encapsulation! make a copy of list and send it back.
        return null;
    }



    public String getMapName() {
        return mapName;
    }



    public LocationResponse setMapName(String mapName) {
        this.mapName = mapName;
        return this;
    }



    @Override
    public String toString() {
        return "LocationResponse{" +
                "locationRecordList=" + locationRecordList +
                ", mapName='" + mapName + '\'' +
                '}';
    }

    @Override
    void encode(MessageOutput out) throws IOException {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationResponse that = (LocationResponse) o;
        return locationRecordList.equals(that.locationRecordList) && mapName.equals(that.mapName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationRecordList, mapName);
    }
}

package serialization;

import java.util.ArrayList;
import java.util.List;

public class LocationResponse extends Message{

    List<LocationRecord> locationRecordList = new ArrayList<>();
    String mapName;





    LocationResponse(long mapId, String mapName){

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
}

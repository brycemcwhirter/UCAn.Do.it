package serialization;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LocationResponse extends Message{

    List<LocationRecord> locationRecordList = new ArrayList<>();
    String mapName;
    private static final String OPERATION = "RESPONSE";


    /**
     * Generates a new LocationResponse
     * @param mapId the mapId
     * @param mapName the name of the map
     * @throws ValidationException
     *  if any of these parameters are invalid
     */
    public LocationResponse(long mapId, String mapName) throws ValidationException{
        super(OPERATION, mapId);
        Validator.validCharacterList(mapName);
        this.mapName = mapName;

    }






    public LocationResponse addLocationRecord(LocationRecord locationRecord) throws ValidationException {
        if(Objects.isNull(locationRecord)){
            throw new ValidationException("Location Record cannot be null in adding to list");
        }

        locationRecordList.add(locationRecord);
        return this;
    }






    public List<LocationRecord> getLocationRecordList(){
        List<LocationRecord> copy = new ArrayList<>(locationRecordList);
        return copy;
    }






    public String getMapName() {
        return mapName;
    }






    public LocationResponse setMapName(String mapName) throws ValidationException {
        Validator.validCharacterList(mapName);
        this.mapName = mapName;
        return this;
    }






    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(" map="+getMapID()+" "+getMapName());

        if(!locationRecordList.isEmpty()) {
            sb.append(" [");
            for (LocationRecord lr : locationRecordList) {
                sb.append(lr.toString()+",");
            }
            sb.setCharAt(sb.length()-1, ']');
        }
        return sb.toString();
    }







    @Override
    public void encode(MessageOutput out) throws IOException {
        out.writeMessageHeader(getMapID(), getOperation());
        out.writeString(getMapName());


        if(locationRecordList.size() > 0) {

            out.write((locationRecordList.size() + " ").getBytes(StandardCharsets.UTF_8));

            for (LocationRecord lr : locationRecordList) {
                lr.encode(out);
            }

        }

        out.writeMessageFooter();
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

package addatude.serialization;


import java.io.IOException;
import java.nio.charset.StandardCharsets;






public class LocalLocationRequest extends Message{

    String latitude;

    String longitude;

    long mapId;

    public static final String OPERATION = "LOCAL";




    public LocalLocationRequest(long mapId, String latitude, String longitude) throws ValidationException{
        super(OPERATION, mapId);
        AddatudeValidator.validLongitude(longitude);
        AddatudeValidator.validLatitude(latitude);


        this.latitude = latitude;
        this.longitude = longitude;
        this.mapId = mapId;
    }




    public LocalLocationRequest(long mapIdVal, MessageInput in) throws ValidationException {
        super(OPERATION, mapIdVal);

        String readLongitude = in.readUntilSpace();
        setLongitude(readLongitude);


        String readLatitude = in.readUntilSpace();
        setLatitude(readLatitude);
    }



    public String getLatitude() {
        return latitude;
    }




    public void setLatitude(String latitude) throws ValidationException{
        AddatudeValidator.validLatitude(latitude);
        this.latitude = latitude;
    }





    public String getLongitude() {
        return longitude;
    }





    public void setLongitude(String longitude) throws ValidationException {
        AddatudeValidator.validLongitude(longitude);
        this.longitude = longitude;
    }





    @Override
    public String toString() {
        return "LocalLocationRequest: map="+mapId+"at("+longitude+","+latitude+")";
    }





    @Override
    public void encode(MessageOutput out) throws IOException {

        writeMessageHeader(mapId, OPERATION, out);


        //write the longitude
        out.write((longitude + " ").getBytes(StandardCharsets.UTF_8));


        //write the latitude
        out.write((latitude +" ").getBytes(StandardCharsets.UTF_8));


        writeMessageFooter(out);


    }





    @Override
    public String getOperation(){
        return OPERATION;
    }





}

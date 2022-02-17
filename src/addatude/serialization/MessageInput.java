/************************************************
 *
 * Author: Bryce McWhirter
 * Assignment: Program 0
 * Class: Data Communications
 *
 ************************************************/

package addatude.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class MessageInput {

    private final InputStream in;


    public char readVal() throws IOException, ValidationException {
        int i = in.read();



        if(i == -1){
            throw new ValidationException("Invalid Stream", "Read Error Occurred");
        }



        return (char) i;
    }


    /**
     * Reads a new error message
     * @param mapID the mapID of the error message
     * @return the new Error Message Read
     * @throws IOException
     *      If a read error Occurs
     * @throws ValidationException
     *      If the read parameters are invalid
     */
    public Error readError(long mapID) throws IOException, ValidationException {
        String errorMsg = readUntilCRLF();
        return new Error(mapID, errorMsg);
    }





    /**
     * Generates a new location
     * @param readMapID the mapID
     * @return the new location with the parameters
     * @throws ValidationException
     *      if any of these parameters are invalid
     * @throws IOException
     *      if a read error occurs.
     */
    public NewLocation readNewLocation(long readMapID) throws ValidationException, IOException {
        LocationRecord locationRecord = new LocationRecord(this);
        return new NewLocation(readMapID, locationRecord);
    }





    /**
     * Reads a new Location Response
     * @param readMapID the read in mapID
     * @return the new location response
     * @throws ValidationException
     *      if a validation error occurs
     * @throws IOException
     *      if a read error occurs
     */
    public LocationResponse readResponse(long readMapID) throws ValidationException, IOException {
        int size = readIntegerValue();
        String mapName = new String(readNumOfValues(size), StandardCharsets.UTF_8);
        Validator.validString("Param", mapName);

        int numOfLocation = readIntegerValue();

        LocationResponse locationResponse = new LocationResponse(readMapID, mapName);

        for(int i = 0; i < numOfLocation; i++){
            LocationRecord lr = new LocationRecord(this);
            locationResponse.addLocationRecord(lr);
        }

        return locationResponse;
    }





    /**
     * Creates a new Message Input Object
     *
     * @param in The Input Stream associated with the message input object
     * @throws NullPointerException
     *      if the 'in' parameter is null
     */
    public MessageInput(InputStream in) throws NullPointerException{
        Objects.requireNonNull(in, "Input Stream Cannot Be Null");
        this.in = in;
    }



    /**
     * Reads the characters from the Byte Stream Until a ' ' occurs
     *
     * @return A string that is read from the Message Input Stream
     * @throws IOException
     *      If a reading error occurs
     */
    public String readUntilSpace() throws IOException, ValidationException {
        StringBuilder stringBuilder = new StringBuilder();

        while (true) {
            char c = readVal();

            if (c == ' ')
                break;

            stringBuilder.append(c);
        }

        return stringBuilder.toString();
    }



    /**
     * reads until CRLF is reached
     * @return the string read
     * @throws IOException
     *      if a read error occurs
     */
    public String readUntilCRLF() throws IOException, ValidationException {
        StringBuilder stringBuilder = new StringBuilder();

        while (true) {
            char c = readVal();

            if (c == '\r'){
                c = readVal();

                if(c == '\n') {
                    stringBuilder.append(c);
                    break;
                }
            }
            stringBuilder.append(c);
        }


        return stringBuilder.toString();

    }






    /**
     * reads an integer value
     * @return the integer value read
     * @throws IOException
     *      if a read error occurred
     */
    public int readIntegerValue() throws IOException, ValidationException {
        StringBuilder stringBuilder = new StringBuilder();


        String val = readUntilSpace();
        val.trim();
        stringBuilder.append(val);

        int value;
        try{
            value = Integer.parseInt(stringBuilder.toString());

        }
        catch (NumberFormatException e){
            throw new ValidationException("InvalidRead", "Value on stream must be Integer Value", e);
        }



        return value;

    }




    /**
     * reads a specific size of bytes from a Message Input
     * @param size the number of bytes to read
     * @return the buffer of read bytes
     * @throws IOException
     *      if a read error occurs
     * @throws ValidationException
     *      if the stream doesn't read the right number of bytes
     */
    public byte[] readNumOfValues(int size) throws IOException, ValidationException {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < size; i++){
            char j = readVal();

            if(j <= 0x7f){

            }

            else if(j <= 0x7ff){

            }

            else if(Character.isHighSurrogate(j)) {
                size += 3;
                j = readVal();
                sb.append(j);
                j = readVal();
                sb.append(j);
            }



            sb.append(j);
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);


    }


    /**
     * Equals Implementation of a message input
     * @param o the message input to be compared to
     * @return a boolean describing if two message
     * inputs are similar
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageInput that = (MessageInput) o;
        return in.equals(that.in);
    }


    /**
     * Hash Code Implementation for Message input
     * @return the hash implementation for messsage input
     */
    @Override
    public int hashCode() {
        return Objects.hash(in);
    }



}

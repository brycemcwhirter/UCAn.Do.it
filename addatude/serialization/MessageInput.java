/************************************************
 *
 * Author: Bryce McWhirter
 * Assignment: Program 0
 * Class: Data Communications
 *
 ************************************************/

package serialization;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class MessageInput {

    private InputStream in;


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
        Validator.validCharacterList(mapName);

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
    public String readUntilSpace() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        try {

            int r;
            while ((r = in.read()) != -1) {
                char c = (char) r;

                if (c == ' ')
                    break;

                stringBuilder.append(c);
            }

        } catch(IOException e) {
            throw new IOException("Error occurred during reading");
        }

        return stringBuilder.toString();
    }


    /**
     * reads until CRLF is reached
     * @return the string read
     * @throws IOException
     *      if a read error occurs
     */
    public String readUntilCRLF() throws IOException{
        StringBuilder stringBuilder = new StringBuilder();

        try {

            int r;
            while ((r = in.read()) != -1) {
                char c = (char) r;

                if (c == '\r'){
                    r = in.read();
                    c = (char) r;
                    if(c == '\n')
                        break;
                }


                stringBuilder.append(c);
            }

        } catch(IOException e) {
            throw new IOException("Error occurred during reading");
        }



        return stringBuilder.toString();

    }






    /**
     * reads an integer value
     * @return the integer value read
     * @throws IOException
     *      if a read error occurred
     */
    public int readIntegerValue() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        try {

            int r;
            while ((r = in.read()) != -1) {
                char c = (char) r;

                if (!Character.isDigit(c))
                    break;

                stringBuilder.append(c);
            }

        } catch(IOException e) {
            throw new IOException("Error occurred during reading");
        }

        return Integer.parseInt(stringBuilder.toString());

    }




    /**
     * reads a specific size of bytes from a Message Input
     * @param size the number of bytes to read
     * @return the buffer of read bytes
     * @throws IOException
     *      if a read error occurs
     */
    public byte[] readNumOfValues(int size) throws IOException {
        byte[] buf = new byte[size];
        int i = in.read(buf, 0, size);
        return buf;


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


    /**
     * Tests if the location record is fully read
     * @throws ValidationException
     *      if there is a size mismatch in the location
     *      description size
     * @throws IOException
     *      if a read error occurs.
     */
    public void testLocationRecordFullyRead() throws ValidationException, IOException {
        in.mark(1);
        int val = in.read();
        char c = (char) val;
        if (Character.isLetter(c))
            throw new ValidationException("InvalidStream", "Location Record is not fully read");
        in.reset();
    }
}

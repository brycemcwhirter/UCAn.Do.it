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
import java.util.Objects;

public class MessageInput {

    private InputStream in;

    //TODO Reading Messages for different operations

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
     * reads an integer value
     * @return the integer value read
     * @throws IOException
     *      if a read error occurred
     */
    public int readIntegerValue() throws IOException, ValidationException {
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
        in.read(buf, 0, size);
        return buf;


    }

    public boolean isEmpty() throws IOException {
        return(in.available() == 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageInput that = (MessageInput) o;
        return in.equals(that.in);
    }

    @Override
    public int hashCode() {
        return Objects.hash(in);
    }
}

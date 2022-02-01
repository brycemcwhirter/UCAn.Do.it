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
     * Performs the standard readAllBytes() offered by InputStream
     *
     * @return A byte array of the bytes read
     * @throws IOException
     *      If a reading error occurs
     */
    public byte[] readAllBytes() throws IOException {
        return in.readAllBytes();
    }






}

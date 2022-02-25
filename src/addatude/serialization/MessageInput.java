/************************************************
 *
 * Author: Bryce McWhirter
 * Assignment: Program 0
 * Class: Data Communications
 *
 ************************************************/

/**
 * Testing Partner: John Harrison
 */

package addatude.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * This Class Specifies the Methods
 * for Reading Messages from the
 * Input Stream.
 */
public class MessageInput {


    /**
     * The Input Stream Used for reading input.
     */
    private final InputStream in;





    /**
     * Reads one value from the input stream
     * @return The value read
     * @throws IOException
     *      If an I/O Error Occurs or read
     *      returns -1.
     */
    public char readVal() throws IOException {
        int i;

        i = in.read();

        if (i == -1){
            throw new IOException("Invalid Read");
        }


        return (char) i;
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
    public String readUntilSpace() throws ValidationException {
        StringBuilder stringBuilder = new StringBuilder();
        char c;

        try {

            while (true) {
                c = readVal();

                if (c == ' ')
                    break;

                stringBuilder.append(c);
            }
        }

        catch (IOException e) {
            throw new ValidationException("Invalid Read", "A space was never reached", e);
        }


        return stringBuilder.toString();
    }








    /**
     * reads an integer value
     * @return the integer value read
     * @throws ValidationException
     *      if an invalid read occurs
     */
    public int readIntegerValue() throws ValidationException {
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
    public byte[] readNumOfValues(int size) throws ValidationException {
        StringBuilder sb = new StringBuilder();
        boolean onUnicode=false;

        try {

            for (int i = 0; i < size; i++) {
                char j = readVal();

                if (j <= 0x7f) {
                    onUnicode = false;
                } else if (j <= 0x7ff && !onUnicode) {
                    size++;
                    onUnicode = true;
                }

                sb.append(j);
            }
        }
        catch (IOException e){
            throw new ValidationException("Invalid Read", "Size specified is larger than bytes on stream", e);
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);


    }


    /**
     * Closes the Input Stream
     * @throws IOException
     *      If an error occurs
     */
    public void closeMessageInputStream() throws IOException {
        in.close();
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
/************************************************
 *
 * Author: Bryce McWhirter
 * Assignment: Program 0
 * Class: Data Communications
 *
 ************************************************/

/*
 * Testing Partner: John Harrison
 */

package addatude.serialization;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class MessageOutput {

    /**
     * The Output Stream Tied to the Message Output
     */
    OutputStream os;




    /**
     * Generates a Message Output Object
     *
     * @param os the Output Stream associated with the Message Output
     * @throws NullPointerException
     *      If 'os' is null
     */
    public MessageOutput(OutputStream os) throws NullPointerException{
        Objects.requireNonNull(os, "OS cannot be null");
        this.os = os;
    }



    /**
     * Performs the standard write function given by 'Output Stream'
     *
     * @param b the byte array to write data from
     * @throws IOException
     *      If an IO error occurs
     */
    public void write(byte[] b) throws IOException {
        os.write(b);
    }





    /**
     * Writes a string onto an output stream
     * @param s the string to write
     * @throws IOException
     *      if a write error occurs
     */
    public void writeString(String s) throws IOException{
        os.write((s.length()+" "+s).getBytes(StandardCharsets.UTF_8));
    }





    /**
     * Equals Implementation
     * @param o The Compared MessageOutput object
     * @return a boolean describing if two MessageOutput objects
     *  are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageOutput that = (MessageOutput) o;
        return os.equals(that.os);
    }


    /**
     * Hash Code Implementation
     * @return the Hash implementation of a Message Output
     */
    @Override
    public int hashCode() {
        return Objects.hash(os);
    }



}

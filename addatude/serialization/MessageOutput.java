/************************************************
 *
 * Author: Bryce McWhirter
 * Assignment: Program 0
 * Class: Data Communications
 *
 ************************************************/

package serialization;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class MessageOutput {

    OutputStream os;

    /**
     * Generates a Message Output Object
     *
     * @param os the Output Stream associated with the Message Output
     * @throws NullPointerException
     *      If 'os' is null
     */
    public MessageOutput(OutputStream os) throws NullPointerException{
        Objects.requireNonNull(os);
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


}

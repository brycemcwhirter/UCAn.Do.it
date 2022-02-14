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
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class MessageOutput {

    OutputStream os;
    private static final String HEADER = "ADDATUDEv1";


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





    public void writeMessageHeader(long mapId, String operation) throws IOException {
        String messageHeader = HEADER+' '+mapId+' '+operation+' ';
        os.write(messageHeader.getBytes(StandardCharsets.UTF_8));
    }




    public void writeString(String s) throws IOException{
        os.write((s.length()+" "+s).getBytes());
    }




    public void writeMessageFooter() throws IOException {
        os.write("\r\n".getBytes());
    }








    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageOutput that = (MessageOutput) o;
        return os.equals(that.os);
    }





    @Override
    public int hashCode() {
        return Objects.hash(os);
    }



}

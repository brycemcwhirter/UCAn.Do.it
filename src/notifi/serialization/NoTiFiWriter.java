/************************************************
 *
 * Author: Bryce McWhirter
 * Assignment: Program 5
 * Class: Data Communications
 *
 ************************************************/

package notifi.serialization;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.nio.ByteBuffer;

/**
 * This class helps write
 * NoTiFi related data to an
 * output stream
 */
public class NoTiFiWriter {

    private static final int BYTE_ADDRESS_SIZE = 4;

    private static final int SHORT_SIZE = 2;



    /**
     * Writes the NoTiFi Header on the Byte Buffer
     * @param daous The Data Output Stream
     * @param OP_CODE the Op Code of the respected Message
     */
    public static void writeNoTiFiHeader(DataOutputStream daous, byte OP_CODE, int msgId) throws IOException {
        byte versionAndCode = (byte) (NoTiFiMessage.VALID_VERSION | OP_CODE);

        //Write Message Header
        daous.write(versionAndCode);
        daous.write((byte) msgId);
    }


    /**
     * Write the internet address
     * @param out the output stream
     * @param address the address
     * @throws IOException
     *      if an error occurs
     */
    public static void writeInetAddress(DataOutputStream out, Inet4Address address) throws IOException {
        ByteBuffer b = ByteBuffer.allocate(4);
        b.put(address.getAddress());
        byte[] buf = b.array();

        for(int i = 0; i < BYTE_ADDRESS_SIZE; i++){
            out.write(buf[BYTE_ADDRESS_SIZE - i - 1]);
        }
    }


    /**
     * Writes the port value
     * @param out the output stream
     * @param port the port
     * @throws IOException
     *      if an error occurs
     */
    public static void writePortValue(DataOutputStream out, int port) throws IOException{
        ByteBuffer b = ByteBuffer.allocate(2);
        b.putShort((short) port);
        byte[] buf = b.array();

        for(int i = 0; i < SHORT_SIZE; i++){
            out.write(buf[SHORT_SIZE - i - 1]);
        }
    }


    /**
     * Writes a double value
     * @param out the output stream
     * @param value the double value
     * @throws IOException
     *      if an error occurs
     */
    public static void writeDoubleValue(DataOutputStream out, double value) throws IOException {
        byte[] buf = new byte[8];

        ByteBuffer b = ByteBuffer.allocate(8);
        b.putDouble(value);
        byte[] val = b.array();

        for(int i = 0; i < val.length; i++){
            buf[i] = val[val.length - i - 1];
        }

        out.write(buf);
    }



}

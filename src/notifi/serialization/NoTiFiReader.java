/************************************************
 *
 * Author: Bryce McWhirter
 * Assignment: Program 5
 * Class: Data Communications
 *
 ************************************************/

package notifi.serialization;

import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * This class helps in reading
 * NoTiFi Data
 */
public class NoTiFiReader {

    // The size of a float value in bytes
    private static final int FLOAT_SIZE = 8;


    /**
     * Reads the Longitude parameter
     * @param floatBuffer the buffer
     * @return the longitude value
     */
    public static double readLongitude(byte[] floatBuffer) {

        byte [] buf = new byte[8];

        for(int i = 0 ; i < FLOAT_SIZE; i++){
            buf[i] = floatBuffer[FLOAT_SIZE - i - 1];
        }

        return ByteBuffer.wrap(buf).getDouble();
    }


    /**
     * Reads the latitude
     * @param floatBuffer the buffer
     * @return the latitude value
     */
    public static double readLatitude(byte[] floatBuffer) {

        byte [] buf = new byte[8];
        int ctr = 0;

        for(int i = 8 ; i < FLOAT_SIZE * 2; i++){
            buf[i - 8] = floatBuffer[FLOAT_SIZE * 2 - ctr - 1];
            ctr++;
        }

        return ByteBuffer.wrap(buf).getDouble();
    }


    /**
     * Reads a length and string of that length
     * @param in the input stream
     * @return the string
     * @throws IOException
     *      if a read error occcurs
     */
    public static String readStringWithLength(DataInputStream in) throws IOException {

        short length = in.readByte();

        if(length < 0){
            length = (short) (255 + length + 1);
        }

        byte[] readNameBuf = new byte[length];


        // Read Location Name
        in.read(readNameBuf, 0, length);
        return new String(readNameBuf, StandardCharsets.UTF_8);

    }


    /**
     * Reads an internet address
     * @param in the input stream
     * @return the internet address
     * @throws IOException
     *      if a read error occurs
     */
    public static Inet4Address readAddress(DataInputStream in) throws IOException {

        byte[] addressBuffer = new byte[4];
        byte[] readAddress = new byte[4];
        in.read(addressBuffer, 0, 4);

        for(int i = 0; i < addressBuffer.length; i++){
            readAddress[i] = addressBuffer[addressBuffer.length - i - 1];
        }

        return (Inet4Address) Inet4Address.getByAddress(readAddress);
    }


    /**
     * Reads the port
     * @param in the input stream
     * @return the port value
     * @throws IOException
     *      if a read error occurs
     */
    public static int readPort(DataInputStream in) throws IOException{

        byte[] readPort = new byte[2];
        byte[] portVal = new byte[2];
        in.read(readPort, 0, 2);


        for(int i = 0; i < readPort.length; i++){
            portVal[i] = readPort[readPort.length - 1 - i];
        }

        // convert port to integer
        int port = new BigInteger(portVal).intValue();

        // if port is negative
        // Take the 1's complement representation
        if(port < 0){
            port = NoTiFiValidator.LARGEST_PORT_VAL + port + 1;
        }

        return port;

    }

}

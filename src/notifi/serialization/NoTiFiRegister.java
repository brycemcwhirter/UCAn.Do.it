/************************************************
 *
 * Author: Bryce McWhirter
 * Assignment: Program 4
 * Class: Data Communications
 *
 ************************************************/

package notifi.serialization;

import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * The NoTiFi Register is a notification message that
 * describes a register client at given address and port.
 */
public class NoTiFiRegister extends NoTiFiMessage{

    /**
     * The Operation Code of the NoTiFi Register
     */
    static final byte REGISTER_CODE = 0;


    /**
     * The Client address of the Message
     */
    Inet4Address address;


    /**
     * The Client port of the Message
     */
    int port;




    /**
     * Constructs a NoTiFi Register message
     * @param msgId the message id
     * @param address the address to register
     * @param port the port to register
     * @throws IllegalArgumentException
     *      if any of these parameters are invalid
     */
    public NoTiFiRegister(int msgId, Inet4Address address, int port) throws IllegalArgumentException{
        super(msgId, REGISTER_CODE);

        //Tests invalid parameters
        testAddress(address);
        testPort(port);

        this.address = address;
        this.port = port;
    }









    /**
     * Decodes a NoTiFi Register Message
     * @param readID the message id
     * @param byteBuffer the byte buffer containing the message
     * @return a new NoTiFi Register Message
     * @throws IllegalArgumentException
     *      If any illegal parameters
     * @throws UnknownHostException
     *      If the readAddress is an Unknown Host
     */
    public static NoTiFiRegister decode(int readID, ByteBuffer byteBuffer) throws IllegalArgumentException, UnknownHostException {
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byte[] readAddress = new byte[4];
        byteBuffer.get(readAddress, 0, 4);
        byteBuffer.order(ByteOrder.BIG_ENDIAN);
        int port = byteBuffer.getInt();
        return new NoTiFiRegister(readID, (Inet4Address) Inet4Address.getByAddress(readAddress), port);
    }










    /**
     * Returns a String Representation
     * @return string representation
     */
    @Override
    public String toString() {
        return "Register msgid="+msgId+" address="+address+"_port="+port;
    }





    /**
     * Get the register address
     * @return register address
     */
    public Inet4Address getAddress() {
        return address;
    }






    /**
     * Sets the register address
     * @param address the address
     * @return this object with the new register address
     * @throws IllegalArgumentException
     *      if address is null
     */
    public NoTiFiRegister setAddress(Inet4Address address) throws IllegalArgumentException{
        testAddress(address);
        this.address = address;
        return this;
    }






    /**
     * Gets the register port
     * @return register port
     */
    public int getPort() {
        return port;
    }






    /**
     * Sets the register port
     * @param port the register port
     * @return this object with new value
     * @throws IllegalArgumentException
     *      if port is out of range
     */
    public NoTiFiRegister setPort(int port) throws IllegalArgumentException{
        testPort(port);
        this.port = port;
        return this;
    }






    /**
     * Get Address
     * @return register address
     */
    public InetSocketAddress getSocketAddress(){
        return new InetSocketAddress(address, port);
    }






    /**
     * Serializes a NoTiFi Register Message
     * @return the serialized message
     */
    @Override
    public byte[] encode() {
        ByteBuffer b = ByteBuffer.allocate(10);

        // Write Message Header
        writeNoTiFiHeader(b, REGISTER_CODE);

        // Put Address
        b.order(ByteOrder.LITTLE_ENDIAN);
        b.put(address.getAddress());
        b.order(ByteOrder.BIG_ENDIAN);


        // Put Port
        b.putInt(port);


        return b.array();
    }


    /**
     * Test if a port value is valid
     * @param port the port
     * @throws IllegalArgumentException
     *      if the port is invalid
     */
    public void testPort(int port) throws IllegalArgumentException{
        if(port < 0 || port > 65535){
            throw new IllegalArgumentException("Port is out of Range");
        }
    }


    /**
     * Test if an Address is valid
     * @param address the address
     * @throws IllegalArgumentException
     *      if the address is invalid
     */
    public void testAddress(Inet4Address address) throws IllegalArgumentException{
        if(address == null){
            throw new IllegalArgumentException("Address cannot be null");
        }
    }
}

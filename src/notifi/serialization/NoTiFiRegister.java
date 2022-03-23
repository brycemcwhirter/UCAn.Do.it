package notifi.serialization;

import java.net.Inet4Address;
import java.net.InetSocketAddress;

public class NoTiFiRegister extends NoTiFiMessage{

    static final int REGISTER_CODE = 0;


    Inet4Address address;
    int port;

    public NoTiFiRegister(int msgId, Inet4Address address, int port) throws IllegalArgumentException{
        super(msgId, REGISTER_CODE);

        //Tests invalid parameters
        testAddress(address);
        testPort(port);

        this.address = address;
        this.port = port;
    }

    @Override
    public String toString() {
        return "Register msgid="+msgId+" address="+address+"_port="+port;
    }

    public Inet4Address getAddress() {
        return address;
    }

    public InetSocketAddress getSocketAddress(){
        return null;
        //todo what does this mean?
    }

    public NoTiFiRegister setAddress(Inet4Address address) throws IllegalArgumentException{
        testAddress(address);
        this.address = address;
        return this;
    }

    public int getPort() {
        return port;
    }

    public NoTiFiRegister setPort(int port) {
        testPort(port);
        this.port = port;
        return this;
    }

    @Override
    byte[] encode() {
        return new byte[0];
        //todo overwrite
    }


    public void testPort(int port) throws IllegalArgumentException{
        if(port < 0 || port > 65535){
            throw new IllegalArgumentException("Port is out of Range");
        }
    }

    public void testAddress(Inet4Address address) throws IllegalArgumentException{
        if(address == null){
            throw new IllegalArgumentException("Address cannot be null");
        }
    }
}

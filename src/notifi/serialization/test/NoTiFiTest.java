package notifi.serialization.test;

import notifi.serialization.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Nested
public class NoTiFiTest {


    static final byte VERSION = 0x30;


    static final short MSG_ID = 123;




    @DisplayName("ACK Test")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class ackTest{






        @Test
        void encodeTest() throws  IOException {
            byte[] a = new NoTiFiACK(123).encode();
            NoTiFiACK ack = (NoTiFiACK) NoTiFiMessage.decode(a);
            assertEquals(123, ack.getMsgId());
            assertEquals(3, ack.getCode());
        }

        @Test
        void decode() throws IOException {
            NoTiFiACK ack = (NoTiFiACK) NoTiFiMessage.decode(new byte[] {0x33, 1});
            assertEquals(1, ack.getMsgId());
            assertEquals(3, ack.getCode());
        }

        @Test
        void toStringTest() throws IOException {
            NoTiFiACK ack = (NoTiFiACK) NoTiFiMessage.decode(new byte[] {0x33, 1});
            assertEquals("ACK: msgid="+1, ack.toString());
        }


    }






    @DisplayName("Error Test")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class errorTest{

        static String errorMessage = "This might work";



        @Test
        void encodeTest() throws IOException{
            byte[] pkt = new NoTiFiError(1, errorMessage).encode();
            NoTiFiError message = (NoTiFiError) NoTiFiMessage.decode(pkt);
            assert message != null;
            assertEquals(1, message.getMsgId());
            assertEquals(errorMessage, message.getErrorMessage());
        }



        @Test
        void decode() throws IOException {
            NoTiFiError noTiFiError = (NoTiFiError) NoTiFiMessage.decode(new byte[] {0x32, 1, 'T', 'h', 'i', 's', ' ', 'm', 'i', 'g', 'h', 't', ' ', 'w', 'o', 'r', 'k'});
            assertEquals(1, noTiFiError.getMsgId());
            assertEquals(2, noTiFiError.getCode());
            assertEquals(errorMessage, noTiFiError.getErrorMessage());
        }

        @Test
        void toStringTest() throws IOException {
            NoTiFiError noTiFiError = (NoTiFiError) NoTiFiMessage.decode(new byte[] {0x32, 1, 'T', 'h', 'i', 's', ' ', 'm', 'i', 'g', 'h', 't', ' ', 'w', 'o', 'r', 'k'});
            assertEquals("Error: msgid="+1+' '+"This might work", noTiFiError.toString());
        }






    }






    @DisplayName("Location Addition Test")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class addLocationTest{




        @Test
        void encodeTest() throws IOException {
            byte[] pkt = new NoTiFiLocationAddition(1, 321, 100.2, 89.0, "This", "Place").encode();
            NoTiFiLocationAddition message = (NoTiFiLocationAddition) NoTiFiMessage.decode(pkt);
            assert message != null;
            assertEquals(1, message.getMsgId());
            assertEquals(321, message.getUserId());
            assertEquals(100.0, message.getLongitude());
            assertEquals(89.0, message.getLatitude());
            assertEquals("This", message.getLocationName());
            assertEquals("Place", message.getLocationDescription());
        }




    }






    @DisplayName("Register Test")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class registerTest{

        static byte[] pkt;
        static Inet4Address ip;
        static final String address = "127.0.0.1";

        static final byte REG_CODE = 0;


        @BeforeAll
        static void init() throws UnknownHostException {
            ByteBuffer b = ByteBuffer.allocate(10);

            byte versionAndCode = (byte) (VERSION | REG_CODE);

            // Place the Version & REG Code
            b.put(versionAndCode);

            // Place the ID
            b.put((byte)MSG_ID);

            // Place the IP Address
            ip = (Inet4Address) Inet4Address.getByName(address);
            byte[] addressBytes = ip.getAddress();
            b.order(ByteOrder.LITTLE_ENDIAN);
            b.put(addressBytes);

            // Place the port
            b.order(ByteOrder.BIG_ENDIAN);
            b.putInt(1234);

            pkt = b.array();
        }






        @Test
        void testDecode() throws IOException {
            NoTiFiRegister test = (NoTiFiRegister) NoTiFiMessage.decode(pkt);
            assertEquals(123, test.getMsgId());
            assertEquals(1234, test.getPort());
            assertEquals(ip.getHostAddress(), address);
        }







        @Test
        void encodeTest() throws IOException{
            byte[] pkt = new NoTiFiRegister(1, ip, 1234).encode();
            NoTiFiRegister message = (NoTiFiRegister) NoTiFiMessage.decode(pkt);
            assert message != null;
            assertEquals(1, message.getMsgId());
            assertEquals(1234, message.getPort());
            assertEquals(address, message.getAddress().getHostAddress());
        }


    }

}

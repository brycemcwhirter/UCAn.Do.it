package notifi.serialization.test;

import addatude.serialization.ValidationException;
import notifi.serialization.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class NoTiFiTest {


    static final byte VERSION = 3;
    static final byte REG_CODE = 0;
    static final byte LOC_ADD_CODE = 1;
    static final byte ERROR_CODE = 2;
    static final byte ACK_CODE = 3;

    static final short MSG_ID = 123;




    @DisplayName("ACK Test")
    static class ackTest{

        static byte[] pkt;
        static ByteBuffer b = ByteBuffer.allocate(8);



        @BeforeAll
        static void init(){

            // Place the Version
            b.put(VERSION);

            // Place the OP Code
            b.put(ACK_CODE);

            // Place the ID
            b.putShort(MSG_ID);

            // Create the Byte Array
            pkt = b.array();
        }



        @Test
        void decodeTest() throws IOException, ValidationException {
            NoTiFiACK message = (NoTiFiACK) NoTiFiMessage.decode(pkt);
            assert message != null;
            assertEquals(123, message.getMsgId());
        }


        @Test
        void encodeTest(){


        }


    }






    @DisplayName("Error Test")
    static class errorTest{

        static byte[] pkt;
        static String errorMessage = "This is an error message";

        @BeforeAll
        static void init(){
            ByteBuffer b = ByteBuffer.allocate(28);


            // Place the Version
            b.put(VERSION);

            // Place the OP Code
            b.put(ERROR_CODE);

            // Place the ID
            b.putShort(MSG_ID);

            // Place the Error Message
            b.put(errorMessage.getBytes(StandardCharsets.US_ASCII));
            pkt = b.array();
        }

        @Test
        void decodeTest() throws IOException, ValidationException {
            NoTiFiError message = (NoTiFiError) NoTiFiMessage.decode(pkt);
            assert message != null;
            assertEquals(123, message.getMsgId());
            assertEquals(errorMessage, message.getErrorMessage());
        }

        // TODO Error Message should only contain ASCII Characters make a test for so





    }






    @DisplayName("Location Addition Test")
    static class addLocationTest{

        static byte[] pkt;

        @BeforeAll
        static void init(){
            ByteBuffer b = ByteBuffer.allocate(32);
            b.putShort(LOC_ADD_CODE);
            b.putInt(123);
            pkt = b.array();
        }




    }






    @DisplayName("Register Test")
    static class registerTest{

        static byte[] pkt;
        static Inet4Address ip;
        static final String address = "127.0.0.1";

        @BeforeAll
        static void init() throws UnknownHostException {
            ByteBuffer b = ByteBuffer.allocate(32);

            // Place the Version
            b.put(VERSION);

            // Place the OP Code
            b.put(REG_CODE);

            // Place the ID
            b.putShort(MSG_ID);

            // Place the IP Address
            ip = (Inet4Address) Inet4Address.getByName(address);
            byte[] addressBytes = ip.getAddress();
            b.order(ByteOrder.LITTLE_ENDIAN);
            b.put(addressBytes);

            // Place the port
            b.order(ByteOrder.BIG_ENDIAN);
            b.putInt(1234);


            //TODO make sure this test works.

            pkt = b.array();
        }

        @Test
        void testDecode() throws ValidationException, IOException {
            NoTiFiRegister test = (NoTiFiRegister) NoTiFiMessage.decode(pkt);
            assertEquals(123, test.getMsgId());
            assertEquals(1234, test.getPort());
            assertEquals(ip.getHostAddress(), address);

        }


    }

}

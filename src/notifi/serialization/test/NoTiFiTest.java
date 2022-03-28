package notifi.serialization.test;

import addatude.serialization.ValidationException;
import notifi.serialization.NoTiFiACK;
import notifi.serialization.NoTiFiError;
import notifi.serialization.NoTiFiMessage;
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


    @DisplayName("ACK Test")
    static class ackTest{

        static byte[] pkt;


        @BeforeAll
        static void init(){
            ByteBuffer b = ByteBuffer.allocate(8);
            b.putShort((short) 3);
            b.putShort((short) 3);
            b.putInt(123);
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
            ByteBuffer b = ByteBuffer.allocate(32);
            b.putShort((short) 3);
            b.putShort((short) 2);
            b.putInt(123);
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
            String hexRep = "3301";
            int it = Integer.parseInt(hexRep, 16);
            BigInteger bigInt = BigInteger.valueOf(it);
            pkt = (bigInt.toByteArray());
        }




    }






    @DisplayName("Register Test")
    static class registerTest{

        static byte[] pkt;

        @BeforeAll
        static void init() throws UnknownHostException {
            ByteBuffer b = ByteBuffer.allocate(32);
            b.putShort((short) 3);
            b.putShort((short) 0);

            b.putInt(123);

            Inet4Address ip = (Inet4Address) Inet4Address.getByName("127.0.0.1");
            byte[] addressBytes = ip.getAddress();
            b.order(ByteOrder.LITTLE_ENDIAN);
            b.put(addressBytes);

            b.order(ByteOrder.BIG_ENDIAN);
            b.putShort((short) 1234);


            //TODO make sure this test works.

            pkt = b.array();
        }



    }

}

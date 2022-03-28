package notifi.serialization.test;

import addatude.serialization.*;
import notifi.serialization.NoTiFiACK;
import notifi.serialization.NoTiFiError;
import notifi.serialization.NoTiFiMessage;
import org.junit.jupiter.api.*;


import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.*;


public class NoTiFiTest {

    public static final int VERSION = 0x03;

    @DisplayName("ACK Test")
    static class ackTest{

        static byte[] pkt;
        public static final int CODE = 0x03;


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
            ByteBuffer b = ByteBuffer.allocate(100);
            b.putShort((short) 3);
            b.putShort((short) 2);
            b.putInt(123);
            b.put(errorMessage.getBytes(StandardCharsets.US_ASCII));
            pkt = b.array();
        }

        @Test
        void decodeTest() throws IOException, ValidationException {
            NoTiFiError message = (NoTiFiError) NoTiFiMessage.decode(pkt);
            assertEquals(123, message.getMsgId());
            assertEquals(errorMessage, message.getErrorMessage());
        }




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
        static void init(){
            String hexRep = "3301";
            int it = Integer.parseInt(hexRep, 16);
            BigInteger bigInt = BigInteger.valueOf(it);
            pkt = (bigInt.toByteArray());
        }



    }

}

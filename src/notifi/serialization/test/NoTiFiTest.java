package notifi.serialization.test;

import notifi.serialization.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.stream.Stream;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;


@Nested
public class NoTiFiTest {


    static final byte VERSION = 0x30;


    static final short MSG_ID = 123;




    @DisplayName("ACK Test")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class ackTest{

        //TODO Bad Serialization Test






        @Test
        void encodeTest() {
            byte[] a = new NoTiFiACK(123).encode();
            NoTiFiACK ack = (NoTiFiACK) NoTiFiMessage.decode(a);
            assertEquals(123, ack.getMsgId());
            assertEquals(3, ack.getCode());
        }

        @Test
        void decode() throws IOException {
            NoTiFiACK ack = (NoTiFiACK) NoTiFiMessage.decode(new byte[] {51, -1});
            assertEquals(255, ack.getMsgId());
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

        static String errorMessage = "bad";



        @Test
        void encodeTest() throws IOException{
            byte[] pkt = new NoTiFiError(1, errorMessage).encode();
            NoTiFiError message = (NoTiFiError) NoTiFiMessage.decode(pkt);
            assert message != null;
            assertEquals(1, message.getMsgId());
            assertEquals(errorMessage, message.getErrorMessage());
        }



        @ParameterizedTest
        @MethodSource("validBytes")
        void decode(byte[] valid) throws IOException {
            NoTiFiError noTiFiError = (NoTiFiError) NoTiFiMessage.decode(valid);

        }

        @Test
        void toStringTest() throws IOException {
            NoTiFiError noTiFiError = (NoTiFiError) NoTiFiMessage.decode(new byte[] {0x32, 1, 'T', 'h', 'i', 's', ' ', 'm', 'i', 'g', 'h', 't', ' ', 'w', 'o', 'r', 'k'});
            assertEquals("Error: msgid="+1+' '+"This might work", noTiFiError.toString());
        }


        @Test
        void invalidDecodeStream(){
            assertThrows(IllegalArgumentException.class, ()-> NoTiFiMessage.decode(new byte[] {50,0,49,10,51}));
        }



        public Stream<Arguments> validBytes(){
            return Stream.of(
                    arguments(new byte[] {50,0,49,50,51})
            );
        }











    }






    @DisplayName("Location Addition Test")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class addLocationTest{
        NoTiFiLocationAddition noTiFiLocationAddition;

        @BeforeAll
        void init(){
            noTiFiLocationAddition = new NoTiFiLocationAddition(5, 1010, -75.0, 65.0, "here", "the re");

        }



        @Test
        void toStringTest(){
            assertEquals("Addition: msgid="+noTiFiLocationAddition.getMsgId()+" 1010:"+noTiFiLocationAddition.getLocationName()+"-"+noTiFiLocationAddition.getLocationDescription()
                    +" ("+(int) noTiFiLocationAddition.getLongitude()+","+(int) noTiFiLocationAddition.getLatitude()+")", noTiFiLocationAddition.toString());
        }


        @ParameterizedTest
        @ValueSource(strings = {"s\u007fring" })
        void invalidSetName(String badName){
            assertThrows(IllegalArgumentException.class, ()-> noTiFiLocationAddition.setLocationName(badName));
        }

        @ParameterizedTest
        @ValueSource(doubles = {-180.5, 180.5})
        void invalidLatitudeLongitudeSet(Double badVal){
            assertThrows(IllegalArgumentException.class, ()-> noTiFiLocationAddition.setLatitude(badVal));
            assertThrows(IllegalArgumentException.class, ()-> noTiFiLocationAddition.setLongitude(badVal));

        }

        // Todo this exception needs to be thrown
        @ParameterizedTest
        @ValueSource(ints = {-1, -2147483648, 100000, 2147483647})
        void invalidUserIDSet(int badVal){
            assertThrows(IllegalArgumentException.class, ()-> noTiFiLocationAddition.setUserId(badVal));

        }

        //TODO Attribute Constructor tests
        //  setting name & description to value that breaks UDP is not allowed


        // todo invalid location description set xxxxxxxxxxxx


        // todo bad set name xxxxxxxxxx


        @ParameterizedTest
        @MethodSource("validEncodeStreams")
        void decodeTest(byte[] pkt, int msgID, int userID, double longitude, double latitude, String name, String desc) throws IOException{
            NoTiFiLocationAddition message = (NoTiFiLocationAddition) NoTiFiMessage.decode(pkt);
            assert message != null;
            assertEquals(msgID, message.getMsgId());
            assertEquals(userID, message.getUserId());
            assertEquals(longitude, message.getLongitude());
            assertEquals(latitude, message.getLatitude());
            assertEquals(name, message.getLocationName());
            assertEquals(desc, message.getLocationDescription());
        }

        @ParameterizedTest
        @MethodSource("validEncodeStreams")
        void encodeTest(byte[] pkt, int msgID, int userID, double longitude, double latitude, String name, String desc) throws IOException {
            NoTiFiLocationAddition test = new NoTiFiLocationAddition(msgID, userID, longitude, latitude, name, desc);
            byte[] encodePkt = test.encode();

            for(int i = 0; i < pkt.length; i++){
                assertEquals(encodePkt[i], pkt[i]);
            }
        }


        public Stream<Arguments> validEncodeStreams(){
            return Stream.of(
                    arguments(new byte[] {49,1,0,0,0,6,51,51,51,51,51,51,21,-64,102,102,102,102,102,102,35,64,3,78,65,77,5,68,101,115,32,112}, 1, 6, -5.3, 9.7, "NAM", "Des p")
            );
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
            b.putShort((short) 1234);

            pkt = b.array();
        }











        @ParameterizedTest
        @MethodSource("validEncodeStreams")
        void encodeTest(byte[] pkt, int msgID, String address, int port) throws IOException{
            NoTiFiRegister message = (NoTiFiRegister) NoTiFiMessage.decode(pkt);
            assert message != null;
            assertEquals(msgID, message.getMsgId());
            assertEquals(port, message.getPort());
            assertEquals(address, message.getAddress().getHostAddress());
        }

        @ParameterizedTest
        @MethodSource("validEncodeStreams")
        void testDecode(byte[] pkt, int msgID, String address, int port) throws IOException {
            NoTiFiRegister test = new NoTiFiRegister(msgID, (Inet4Address) Inet4Address.getByName(address), port);
            byte[] encodePkt = test.encode();

            for(int i = 0; i < pkt.length; i++){
                assertEquals(encodePkt[i], pkt[i]);
            }
        }


        public Stream<Arguments> validEncodeStreams(){
            return Stream.of(
                    arguments(new byte[] {48,0,1,-108,62,-127,3,0}, 0, "129.62.148.1", 3),
                    arguments(new byte[] {48,-1,-1,-108,62,-127,-24,-3}, 255, "129.62.148.255", 65000)
            );
        }


        //TODO specific encode & decode test










    }

}

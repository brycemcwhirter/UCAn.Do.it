package serialization.test;


import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import serialization.LocationRecord;
import serialization.MessageInput;
import serialization.MessageOutput;
import serialization.ValidationException;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;


@DisplayName("Location Record")
public class LocationRecordTest {

    private static final Charset CHARENC = StandardCharsets.UTF_8;
    private static final double DBLDELTA = 0.00001;
    LocationRecord locationRecord;
    {
        try {
            locationRecord = new LocationRecord(1234, 145.0, 89.0, "Waco", "Texas");
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }


    @DisplayName("Attribute Constructor")
    @Nested
    static
    class AttributeConstructorTest{



        @DisplayName("Invalid Parameters")
        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("badParameters")
        void testInvalid(String message, long userID, double longitude, double latitude, String locationName, String locationDescription) throws IllegalArgumentException {

            assertThrows(ValidationException.class, ()-> {
                LocationRecord bad = new LocationRecord(userID, longitude, latitude, locationName, locationDescription);

            });

        }

        public static Stream<Arguments> badParameters(){
            return Stream.of(
                    arguments("Bad User ID", -123, 1.23, 3.23, "6 Baylor", "4 Bear"),
                    arguments("Bad Longitude", 123, 181, 3.23, "6 Baylor", "4 Bear"),
                    arguments("Bad Latitude", 123, 179, -91, "6 Baylor", "4 Bear"),
                    arguments("Bad Location Name", 123, 181, 3.23, "thisIsBad", "4 Bear"),
                    arguments("Bad Location Description", 123, 181, 3.23, "6 Baylor", "thisIsAlsoBad")

                    );
        }

        @DisplayName("Valid Creation")
        @ParameterizedTest
        @MethodSource("goodParameters")
        void testValid(long userID, double longitude, double latitude, String locationName, String locationDescription) throws IllegalArgumentException, ValidationException {

            LocationRecord test = new LocationRecord(userID, longitude, latitude, locationName, locationDescription);



            assertEquals(userID, test.getUserID());
            assertEquals(longitude, test.getLongitude(), DBLDELTA);
            assertEquals(latitude, test.getLatitude(), DBLDELTA);
            assertEquals(locationName, test.getLocationName());
            assertEquals(locationDescription, test.getLocationDescription());

        }



        public static Stream<Arguments> goodParameters(){
            return Stream.of(
                    arguments(123, 178.34, 23.34, "Baylor", "Bear"),
                    arguments(231, 180.0, 90.0, "Baylor", "Bear"),
                    arguments(453, -180.0, -90.0, "TexasTech", "Lubbock")
            );
        }




    }





    @DisplayName("Decode")
    @Nested
    static
    class DecodeConstructorTest{

        @Test
        @DisplayName("Basic Decode")
        public void testDecode() throws IOException, ValidationException {
            var in = new MessageInput(new ByteArrayInputStream(
                    "1 1.2 3.4 2 BU6 Baylor".getBytes(CHARENC)));
            var msg = new LocationRecord(in);
            assertEquals(1, msg.getUserID());
            assertEquals(1.2, msg.getLongitude(), DBLDELTA);
            assertEquals(3.4, msg.getLatitude(), DBLDELTA);
            assertEquals("BU", msg.getLocationName());
            assertEquals("Baylor", msg.getLocationDescription());
        }


        @Test
        @DisplayName("Null Decode")
        public void nullDecode() throws NullPointerException{
            assertThrows(NullPointerException.class, ()->{
               LocationRecord bad = new LocationRecord(null);
            });
        }


       //TODO how to test the end of stream


        //TODO Write Test for invalid decodes
        @ParameterizedTest
        @DisplayName("Invalid Decode Streams")
        @MethodSource("invalidDecodeStreams")
        void invalidDecodeStream(String badDecodeStream){
            assertThrows(ValidationException.class, ()->{
                var in = new MessageInput(new ByteArrayInputStream(
                        badDecodeStream.getBytes(CHARENC)));
                var msg = new LocationRecord(in);


            });
        }



        public static Stream<Arguments> invalidDecodeStreams(){
            return Stream.of(
                    arguments("1 1.2 3.4 2 BU6 BaylorSizeMismatch")
            );
        }




    }






    @DisplayName("Encode")
    @Nested
    class EncodeTest{

        @Test
        @DisplayName("Basic Encode")
        public void testEncode() throws IOException, ValidationException {
            var bOut = new ByteArrayOutputStream();
            var out = new MessageOutput(bOut);
            new LocationRecord(1, 1.2, 3.4, "BU", "Baylor").encode(out);
            assertArrayEquals("1 1.2 3.4 2 BU6 Baylor".getBytes(CHARENC), bOut.toByteArray());
        }

        @Test
        @DisplayName("Invalid OS Param")
        void invalidOutputStreamParameter(){
            assertThrows(NullPointerException.class, ()-> {
                MessageOutput bad = new MessageOutput(null);
            });
        }

    }






    @DisplayName("Getters & Setters")
    @Nested
    class GettersAndSettersTest{

        //set invalid userid
        @ParameterizedTest
        @DisplayName("Invalid User ID")
        @ValueSource(longs = {-123, Integer.MAX_VALUE+1})
        void invalidUserId(long badUserID){
            assertThrows(ValidationException.class, ()-> {
                locationRecord.setUserID(badUserID);
            });
        }

        //set invalid longitude
        @ParameterizedTest
        @DisplayName("Invalid Longitude")
        @ValueSource(doubles = {-180.1, 180.1})
        void invalidLongitude(double badLong){
            assertThrows(ValidationException.class, ()-> {
                locationRecord.setLongitude(badLong);
            });
        }

        //set invalid latitude
        @ParameterizedTest
        @DisplayName("Invalid Latitude")
        @ValueSource(doubles = {-90.1, 90.1})
        void invalidLatitude(double badLatidude){
            assertThrows(ValidationException.class, ()-> {
                locationRecord.setLatitude(badLatidude);
            });
        }

        //set invalid locationName (null)
        @ParameterizedTest
        @DisplayName("Invalid Location Name")
        @ValueSource(strings = {"\r\n\r\nNonPrintableCharacters", "3 invalid"})
        void invalidLocationName(String badName){
            assertThrows(ValidationException.class, ()-> {
                locationRecord.setLocationName(badName);
            });
        }

        //set invalid locationDescription (null)
        @ParameterizedTest
        @DisplayName("Invalid Location Description")
        @ValueSource(strings = {"1 sizeMisMatch", "noNumber", "\r\n\r\nNonPrintableCharacters", "5NoSpace", "5 ", "3.4 notInteger"})
        void invalidLocationDescription(String badDescription){
            assertThrows(ValidationException.class, ()-> {

                locationRecord.setLocationDescription(badDescription);


            });
        }

    }

    @DisplayName("To String")
    @Nested
    static
    class toStringTests{

        @DisplayName("Valid String")
        @ParameterizedTest
        @MethodSource("testParams")
        void testValidString(long userID, double longitude, double latitude, String locationName, String locationDescription) throws IllegalArgumentException, ValidationException {

            LocationRecord test = new LocationRecord(userID, longitude, latitude, locationName, locationDescription);

            String actualString = test.toString();
            String expectedString = String.valueOf(test.getUserID()) + ":" + test.getLocationName()+"-"+test.getLocationDescription()+" ("+test.getLatitude()+","+test.getLongitude()+")";

            assertEquals(actualString,expectedString);

        }

        public static Stream<Arguments> testParams(){
            return Stream.of(
                    arguments(123, 178.34, 23.34, "Baylor", "Bear"),
                    arguments(231, 180.0, 90.0, "Baylor", "Bear"),
                    arguments(453, -180.0, -90.0, "TexasTech", "Lubbock")
            );
        }


    }







}



package serialization.test;


import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
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



    @DisplayName("Attribute Constructor")
    @Nested
    class AttributeConstructorTest{



        @DisplayName("Invalid Parameters")
        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("badParameters")
        void testInvalid(String message, long userID, String longitude, String latitude, String locationName, String locationDescription) throws IllegalArgumentException {

            assertThrows(ValidationException.class, ()-> {
                LocationRecord bad = new LocationRecord(userID, longitude, latitude, locationName, locationDescription);

            });

        }

        public Stream<Arguments> badParameters(){
            return Stream.of(
                    arguments("Bad User ID", -123, 1.23, 3.23, "Baylor", "Bear"),
                    arguments("Bad Longitude", 123, 181, 3.23, "Baylor", "Bear"),
                    arguments("Bad Latitude", 123, 179, -91, "Baylor", "Bear"),
                    arguments("Bad Location Name", 123, 181, 3.23, "thisIsBad", "Bear"),
                    arguments("Bad Location Description", 123, 181, 3.23, "Baylor", "thisIsAlsoBad")

                    );
        }

        @DisplayName("Valid Creation")
        @ParameterizedTest
        @MethodSource("goodParameters")
        void testValid(long userID, String longitude, String latitude, String locationName, String locationDescription) throws IllegalArgumentException, ValidationException {

            LocationRecord test = new LocationRecord(userID, longitude, latitude, locationName, locationDescription);



            assertEquals(userID, test.getUserID());
            assertEquals(longitude, test.getLongitude(), DBLDELTA);
            assertEquals(latitude, test.getLatitude(), DBLDELTA);
            assertEquals(locationName, test.getLocationName());
            assertEquals(locationDescription, test.getLocationDescription());

        }



        public Stream<Arguments> goodParameters(){
            return Stream.of(
                    arguments(123, 178.34, 23.34, "Baylor", "Bear"),
                    arguments(231, 180.0, 90.0, "Baylor", "Bear"),
                    arguments(453, -180.0, -90.0, "TexasTech", "Lubbock")
            );
        }




    }





    @DisplayName("Decode")
    @Nested
    class DecodeConstructorTest{

        @ParameterizedTest(name = "Basic Decode")
        @MethodSource("validDecodeStreams")
        public void testDecode(String decodeStream) throws IOException, ValidationException {
            var in = new MessageInput(new ByteArrayInputStream(
                    decodeStream.getBytes(CHARENC)));
            var msg = new LocationRecord(in);
        }

        //TODO write a method for testing valid decode and asserting values


        @Test
        @DisplayName("Null Decode")
        public void nullDecode() throws NullPointerException{
            assertThrows(NullPointerException.class, ()->{
               LocationRecord bad = new LocationRecord(null);
            });
        }








        @ParameterizedTest(name = "Invalid Decode Streams")
        @MethodSource("invalidDecodeStreams")
        void invalidDecodeStream(String badDecodeStream){
            assertThrows(ValidationException.class, ()->{
                var in = new MessageInput(new ByteArrayInputStream(
                        badDecodeStream.getBytes(CHARENC)));
                var msg = new LocationRecord(in);
            });
        }



        public Stream<Arguments> invalidDecodeStreams(){
            return Stream.of(
                    arguments("1 1.2 3.4 2 BU6 BaylorSizeMismatch"),
                    arguments ("-123 1.2 3.4 2 BU13 invalidUserID"),
                    arguments("123 -190 invalid longitude"),
                    arguments("123 180.0 -923.3 invalid latitude"),
                    arguments("123 180.0 90.0 2 SizeMistMatch")

            );
        }

        public Stream<Arguments> validDecodeStreams(){
            return Stream.of(
                    arguments("1 1.2 3.4 2 BU6 Baylor"),
                    arguments("1 1.2 3.4 10 ABCDEFGHIJ16 KLMNOPQRSTUVWXYZ"),
                    arguments("4531 123.3 58.3 5 Lucia7 Cecilia"),
                    arguments("2003 -97.12 31.55 8 Magnolia13 ChipAndJoanna")
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
            new LocationRecord(1, "1.2", "3.4", "BU", "Baylor").encode(out);
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

        LocationRecord locationRecord;

        @BeforeEach
        void init() throws ValidationException {
            locationRecord = new LocationRecord(1234, "145.0", "89.0", "Waco", "Texas");
         }

        //set invalid userid
        @ParameterizedTest(name = "Invalid User ID")
        @ValueSource(longs = {-123, Integer.MAX_VALUE+1})
        void invalidUserId(long badUserID){
            assertThrows(ValidationException.class, ()-> {
                locationRecord.setUserID(badUserID);
            });
        }

        //set invalid longitude
        @ParameterizedTest(name = "Invalid Longitude")
        @ValueSource(strings = {"-180.1", "180.1", "1893.2"})
        void invalidLongitude(String badLong){
            assertThrows(ValidationException.class, ()-> {
                locationRecord.setLongitude(badLong);
            });
        }

        //set invalid latitude
        @ParameterizedTest(name = "Invalid Latitude")
        @ValueSource(strings = {"-90.1", "90.1"})
        void invalidLatitude(String badLatidude){
            assertThrows(ValidationException.class, ()-> {
                locationRecord.setLatitude(badLatidude);
            });
        }

        //set invalid locationName (null)
        @ParameterizedTest(name = "Invalid Location Name")
        @MethodSource("invalidStrings")
        void invalidLocationName(String badName){
            assertThrows(ValidationException.class, ()-> {
                locationRecord.setLocationName(badName);
            });
        }

        //set invalid locationDescription (null)
        @ParameterizedTest(name = "Invalid Location Description")
        @MethodSource("invalidStrings")
        void invalidLocationDescription(String badDescription){
            assertThrows(ValidationException.class, ()-> {

                locationRecord.setLocationDescription(badDescription);


            });
        }

        public Stream<Arguments> invalidStrings(){
            return Stream.of(
                    arguments("\r\n\r\nNonPrintableCharacters"),
                    arguments("3 invalid")
            );
        }

    }








    @DisplayName("To String")
    @Nested
    class toStringTests{

        @ParameterizedTest(name = "Valid String")
        @MethodSource("testParams")
        void testValidString(long userID, String longitude, String latitude, String locationName, String locationDescription) throws IllegalArgumentException, ValidationException {

            LocationRecord test = new LocationRecord(userID, longitude, latitude, locationName, locationDescription);

            String actualString = test.toString();

            String longVal =  test.getLongitude();
            String latVal =  test.getLatitude();
            String expectedString = String.valueOf(test.getUserID()) + ":" + test.getLocationName()+"-"+test.getLocationDescription()+" ("+latVal+".0,"+longVal+".0)";

            assertEquals(actualString,expectedString);

        }

        public Stream<Arguments> testParams(){
            return Stream.of(
                    arguments(123, "178.34", "23.34", "Baylor", "Bear"),
                    arguments(231, "180.0", "90.0", "Baylor", "Bear"),
                    arguments(453, "-180.0", "-90.0", "TexasTech", "Lubbock")
            );
        }


    }

    @DisplayName("Equals & Hashcode")
    class equalsAndHashCode{

        @Test
        void testEqualObjects() {

        }

        @Test
        void testUnequalObjects() {

        }

        @Test
        void testHashCode() {

        }
    }

}



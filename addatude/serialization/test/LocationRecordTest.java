
package serialization.test;

import serialization.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;


@DisplayName("Location Record")
@Nested
public class LocationRecordTest {

    private static final Charset CHARENC = StandardCharsets.UTF_8;


    @DisplayName("Attribute Constructor")
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
                    arguments("Bad User ID", -123, "1.23", "3.23", "Baylor", "Bear"),
                    arguments("Bad Longitude", 123, "181", "3.23", "Baylor", "Bear"),
                    arguments("Bad Latitude", 123, "179", "-91", "Baylor", "Bear")


                    );
        }

        @DisplayName("Valid Creation")
        @ParameterizedTest
        @MethodSource("goodParameters")
        void testValid(long userID, String longitude, String latitude, String locationName, String locationDescription) throws IllegalArgumentException, ValidationException {

            LocationRecord test = new LocationRecord(userID, longitude, latitude, locationName, locationDescription);



            assertEquals(userID, test.getUserId());
            assertEquals(longitude, test.getLongitude());
            assertEquals(latitude, test.getLatitude());
            assertEquals(locationName, test.getLocationName());
            assertEquals(locationDescription, test.getLocationDescription());

        }



        public Stream<Arguments> goodParameters(){
            return Stream.of(
                    arguments(123, "178.34", "23.34", "Baylor", "Bear"),
                    arguments(231, "180.0", "90.0", "Baylor", "Bear"),
                    arguments(453, "-180.0", "-90.0", "TexasTech", "Lubbock")
            );
        }




    }





    @DisplayName("Decode")
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class DecodeConstructorTest{



        @ParameterizedTest(name = "Basic Decode")
        @MethodSource("validDecodeStreams")
        public void testDecode(String decodeStream) throws IOException, ValidationException {
            var in = new MessageInput(new ByteArrayInputStream(
                    decodeStream.getBytes(CHARENC)));
            var msg = new LocationRecord(in);

        }




        @Test
        @DisplayName("Null Decode")
        public void nullDecode() throws NullPointerException{
            assertThrows(NullPointerException.class, ()->{
               LocationRecord bad = new LocationRecord(null);
            });
        }



        @ParameterizedTest(name = "Invalid Decode Streams")
        //@MethodSource("invalidDecodeStreams")
        @ValueSource(strings = {"123 180.0 90.0 2 SizeMistMatch", "1 5.0 -10.4 4 here5"})
        void invalidDecodeStream(String badDecodeStream){
            assertThrows(ValidationException.class, ()->{
                var in = new MessageInput(new ByteArrayInputStream(
                        badDecodeStream.getBytes(CHARENC)));
                var msg = new LocationRecord(in);
            });
        }



        public Stream<Arguments> invalidDecodeStreams(){
            return Stream.of(
                    arguments ("-123 1.2 3.4 2 BU13 invalidUserID"),
                    arguments("123 -190 invalid longitude"),
                    arguments("123 180.0 -923.3 invalid latitude"),
                    arguments("123 180.0 90.0 2 SizeMistMatch"),
                    arguments("A 5.0 -10.0 4 here5 there"),
                    arguments("100000L 5.0 -10.0 4 here5 there"),
                    arguments("5X5.0 -10.0 4 here5 there"),
                    arguments("? 5.0 -10.0 4 here5 there"),
                    arguments("5 50.01245671 -10.0 4 here5 there"),
                    arguments("1 5.0 -10.0123456 4 here5 there"),
                    arguments("1 5.0 -10.0 4 here5 ther"),
                    arguments("1 5.0 -10.4 4 here5"),
                    arguments("1 5.0 -10.4 4 here100 there")

            );
        }


        public Stream<Arguments> validDecodeStreams(){
            return Stream.of(
                    arguments("1 1.2 3.4 2 BU6 Baylor"),
                    arguments("1 1.2 3.4 10 ABCDEFGHIJ16 KLMNOPQRSTUVWXYZ"),
                    arguments("4531 123.3 58.3 5 Lucia7 Cecilia"),
                    arguments("2003 -97.12 31.55 8 Magnolia13 ChipAndJoanna"),
                    arguments("5 5.0 -10.0 4 here5 there"),
                    arguments("99999 5.0 -10.0 4 here5 there"),
                    arguments("0 180.0 -90.0 5 o n e12 hello there!"),
                    arguments("5 5.0 -10.0 4 h\u00AEre5 ther\u00AE")

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
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class GettersAndSettersTest{

        LocationRecord locationRecord;

        @BeforeEach
        void init() throws ValidationException {
            locationRecord = new LocationRecord(1234, "145.0", "89.0", "Waco", "Texas");
         }

        @ParameterizedTest(name = "Valid User ID")
        @ValueSource(ints = {0,1,56,160,999})
        void name(int val) throws ValidationException {
            locationRecord.setUserId(val);
            assertEquals(val, locationRecord.getUserId());

        }



        @ParameterizedTest(name = "Valid Location Descriptions")
        @MethodSource("validDescriptions")
        void validLocationDescriptionSet(String good) throws ValidationException {
            locationRecord.setLocationDescription(good);
            assertEquals(good, locationRecord.getLocationDescription());
        }

        @ParameterizedTest(name = "Valid Latitude")
        @ValueSource(strings = {"90.0"})
        void validLatitudeSet(String good) throws ValidationException{
            locationRecord.setLatitude(good);
            assertEquals(good, locationRecord.getLatitude());
        }


        //set invalid userid
        @ParameterizedTest(name = "Invalid User ID")
        @ValueSource(longs = {-123, 999999, -99999, 100000})
        void invalidUserId(long badUserID){
            assertThrows(ValidationException.class, ()-> {
                locationRecord.setUserId(badUserID);
            });
        }

        //set invalid longitude
        @ParameterizedTest(name = "Invalid Longitude")
        @ValueSource(strings = {"-180.1", "180.1", "1893.2", "-100.184835"})
        void invalidLongitude(String badLong){
            assertThrows(ValidationException.class, ()-> locationRecord.setLongitude(badLong));

        }

        //set invalid latitude
        @ParameterizedTest(name = "Invalid Latitude")
        @ValueSource(strings = {"-90.1", "90.1", "-10.1848303"})
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


        @Test
        void invalidSetNull(){
            assertThrows(ValidationException.class, ()-> locationRecord.setLatitude(null));
            assertThrows(ValidationException.class, () -> locationRecord.setLongitude(null));
            assertThrows(ValidationException.class, () -> locationRecord.setLocationName(null));
            assertThrows(ValidationException.class, () -> locationRecord.setLocationDescription(null));

        }

        public Stream<Arguments> invalidStrings(){
            return Stream.of(
                    arguments("\u0009"),
                    arguments("\u0009ring")
            );
        }

        public Stream<Arguments> validDescriptions(){
            return Stream.of(
                    arguments("go! team"),
                    arguments("here"),
                    arguments(""),
                    arguments("x"),
                    arguments("s ring"),
                    arguments("?ring")
            );
        }





    }








    @DisplayName("To String")
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class toStringTests{

        @ParameterizedTest(name = "Valid String")
        @MethodSource("testParams")
        void testValidString(long userID, String longitude, String latitude, String locationName, String locationDescription) throws IllegalArgumentException, ValidationException {

            LocationRecord test = new LocationRecord(userID, longitude, latitude, locationName, locationDescription);

            String actualString = test.toString();

            String longVal =  test.getLongitude();
            String latVal =  test.getLatitude();
            String expectedString = String.valueOf(test.getUserId()) + ":" + test.getLocationName()+"-"+test.getLocationDescription()+" ("+longVal+","+latVal+")";

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
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class equalsAndHashCode{

        LocationRecord a, b, c;

        @BeforeAll
        void init() throws ValidationException {
            a = new LocationRecord(123, "178.34", "23.34", "Baylor", "Bear");
            b = new LocationRecord(453, "-180.0", "-90.0", "TexasTech", "Lubbock");
            c = new LocationRecord(123, "178.34", "23.34", "Baylor", "Bear");
        }

        @Test
        void testEqualObjects() {
            assertEquals(a, c);
        }

        @Test
        void testUnequalObjects() {
            assertNotEquals(a, b);

        }

        @Test
        void testHashCode() {
            assertEquals(a.hashCode() , c.hashCode());
        }
    }

}



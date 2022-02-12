import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import serialization.*;
import serialization.Error;

import javax.xml.stream.Location;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayName("Location Response")
public class LocationResponseTest {

    @DisplayName("Constructor")
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class constructorTest {

        // Happy Path

        @Test
        void validCreation() throws ValidationException {
            LocationResponse locationResponse = new LocationResponse(123, "myMap");
            assertEquals(123, locationResponse.getMapID());
            assertEquals("myMap", locationResponse.getMapName());
        }


        // Invalid Arguments (mapName, mapID) (Everything throws ValidationException)
        @ParameterizedTest(name = "{0}")
        @DisplayName("Invalid Values for Location Response")
        @MethodSource("badParams")
        void badParamsTest(String msg, long badMapID, String badMapName) throws ValidationException {
            assertThrows(ValidationException.class, () -> {
                LocationResponse bad = new LocationResponse(badMapID, badMapName);

            });
        }

        public Stream<Arguments> badParams() {
            return Stream.of(
                    arguments("Negative Map ID", -123, "darn")
            );
        }


    }


    @DisplayName("Decode & Encode")
    @Nested
    class decodeTest {


        @Test
        @DisplayName("Null Pointer Exception if in is null")
        void nullIn() {
            assertThrows(NullPointerException.class, () -> {
                LocationResponse bad = (LocationResponse) LocationResponse.decode(null);

            });
        }

        @Test
        @DisplayName("Decode Test")
        void decodeTest() throws ValidationException {
            byte[] buf = "ADDATUDEv1 123 RESPONSE 8 aMapName1 1 1.2 3.4 2 BU6 Baylor\r\n".getBytes(StandardCharsets.UTF_8);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
            MessageInput in = new MessageInput(byteArrayInputStream);
            LocationResponse test = (LocationResponse) Message.decode(in);
        }

        @Test
        @DisplayName("Encode Test")
        void encodeTest() throws ValidationException, IOException{
            var bOut = new ByteArrayOutputStream();
            var out = new MessageOutput(bOut);
            new LocationResponse(123, "aMap").encode(out);
            assertArrayEquals("ADDATUDEv1 123 RESPONSE \r\n".getBytes(StandardCharsets.UTF_8), bOut.toByteArray());
        }
    }


    @DisplayName("Add Location Record")
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class addLocationRecordTest {

        LocationResponse locationResponse;
        LocationRecord locationRecord;

        @BeforeAll
        void init() throws ValidationException {
            locationResponse = new LocationResponse(123, "myMap");
            locationRecord = new LocationRecord(123, "178.34", "23.34", "Baylor", "Bear");
        }

        // Happy Path
        @Test
        @DisplayName("Valid Add")
        void happyPath() throws ValidationException {
            locationResponse.addLocationRecord(locationRecord);
            assertEquals(true, locationResponse.getLocationRecordList().contains(locationRecord));
        }

        @Test
        @DisplayName("Invalid Add")
        void invalidAdd() {
            assertThrows(ValidationException.class, () -> {
                locationResponse.addLocationRecord(null);
            });
        }

        // get Location Record list (encapsulation test)
        @DisplayName("Location Record List Encapsulation")
        @Test
        void encapsulationGetList() {
            List<LocationRecord> list = locationResponse.getLocationRecordList();
            list.clear();
            assertNotEquals(list, locationResponse.getLocationRecordList());

        }


    }


    @DisplayName("Getters & Setters")
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class gettersAndSettersTest {

        LocationResponse locationResponse;

        @BeforeAll
        void init() throws ValidationException {
            locationResponse = new LocationResponse(123, "myMap");

        }


        @Test
        @DisplayName("Valid Set")
        void happySet() throws ValidationException {
            locationResponse.setMapName("myNewMapName");
            assertEquals("myNewMapName", locationResponse.getMapName());
        }


        // Validation Exception (invalid MapNames)
        //TODO invalid map name tests (make an argument test class?)

        @ParameterizedTest(name="{0} test")
        @DisplayName("Invalid Map Name")
        @MethodSource("invalidMapNames")
        void invalidMapName(String type, String badMapName) throws ValidationException {

            assertThrows(ValidationException.class, ()->{
                locationResponse.setMapName(badMapName);
            });
        }

        public Stream<Arguments> invalidMapNames(){
            return Stream.of(
                    arguments("unprintable chars","\r\f\n\f")
            );
        }


    }


    @DisplayName("To String")
    @Nested
    class toStringTest {

        @Test
        @DisplayName("Valid String")
        void happySet() throws ValidationException {
            LocationResponse locationResponse = new LocationResponse(123, "myMap");
            String lrString = locationResponse.toString();
            assertEquals(" map=123 myMap", lrString);
        }


    }


    @DisplayName("Equals & Hashcode")
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class equalsAndHashCode {

        LocationResponse a, b, c;

        @BeforeAll
        void setUp() throws ValidationException {
            a = new LocationResponse(123, "mapName");
            b = new LocationResponse(456, "anotherMapName");
            c = new LocationResponse(123, "mapName");

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
            assertEquals(a.hashCode(), c.hashCode());
        }
    }
}



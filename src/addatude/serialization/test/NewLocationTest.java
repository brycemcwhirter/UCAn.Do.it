package addatude.serialization.test;

import addatude.serialization.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayName("New Location Test")
public class NewLocationTest {

    @DisplayName("Constructor")
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class constructorTest{


        @Test
        @DisplayName("Valid Constructor")
        void validConstructor() throws ValidationException {
            LocationRecord locationRecord = new LocationRecord(123, "178.34", "23.34", "Baylor", "Bear");
            NewLocation newLocation = new NewLocation(123, locationRecord);
            assertEquals(123, newLocation.getMapID());
            assertEquals(locationRecord, newLocation.getLocation());
        }



        @DisplayName("Invalid Params")
        @ParameterizedTest(name="{0}")
        @MethodSource("invalidParams")
        void invalidConstructor(String msg, long mapid, LocationRecord locationRecord) {
            assertThrows(ValidationException.class, ()->{
                NewLocation newLocation = new NewLocation(mapid, locationRecord);
            });
        }



        public Stream<Arguments> invalidParams(){
            return Stream.of(
                    arguments("Null Location Record", 123, null),
                    arguments("negative MAP id", -123, null)
            );
        }



    }

    @DisplayName("Decode & Encode")
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class decodeTest{


        @Test
        @DisplayName("Null Pointer Exception if in is null")
        void nullIn(){
            assertThrows(NullPointerException.class, ()->{
                NewLocation bad = (NewLocation) NewLocation.decode(null);
            });
        }


        @Test
        @DisplayName("Decode Test")
        void decodeTest() throws ValidationException, IOException {
            byte[] buf = "ADDATUDEv1 123 NEW 1 1.2 3.4 2 BU6 Baylor\r\n".getBytes(StandardCharsets.UTF_8);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
            MessageInput in = new MessageInput(byteArrayInputStream);
            NewLocation test = (NewLocation) Message.decode(in);
        }

        @Test
        @DisplayName("Encode Test")
        void encodeTest() throws IOException, ValidationException {
            var bOut = new ByteArrayOutputStream();
            var out = new MessageOutput(bOut);
            LocationRecord d = new LocationRecord(123, "178.34", "23.34", "Baylor", "Bear");
            new NewLocation(123, d).encode(out);
            assertArrayEquals("ADDATUDEv1 123 NEW 123 178.34 23.34 6 Baylor4 Bear\r\n".getBytes(StandardCharsets.UTF_8), bOut.toByteArray());
        }
    }

    @DisplayName("Getters And Setters")
    @Nested
    class gettersAndSetters{

        LocationRecord d, e;
        NewLocation a;

        @BeforeEach
        void init() throws ValidationException {
            d = new LocationRecord(123, "178.34", "23.34", "Baylor", "Bear");
            e = new LocationRecord(2003, "178.34", "23.34", "Magnolia", "Waco");
            a = new NewLocation(456, d);
        }



        @Test
        void validLocationRecordSet() throws ValidationException {
            a.setLocation(e);
            assertEquals(e, a.getLocation());
        }


        // null location record (ValidationException)
        @Test
        void invalidLocationRecordSet() throws ValidationException{
            assertThrows(ValidationException.class, ()->{
                a.setLocation(null);
            });
        }

    }

    @DisplayName("To String")
    @Nested
    class toString{

        @Test
        void validString() throws ValidationException {
            LocationRecord d = new LocationRecord(123, "178.34", "23.34", "Baylor", "Bear");
            NewLocation a = new NewLocation(456, d);

            String NewLocationString = a.toString();
            assertEquals(" map=456 "+ d, NewLocationString);

        }
    }

    @DisplayName("Equals & Hashcode")
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class equalsAndHashCode{

        NewLocation a,b,c;
        LocationRecord d, e;

        @BeforeAll
        void init() throws ValidationException {
            d = new LocationRecord(123, "178.34", "23.34", "Baylor", "Bear");
            e = new LocationRecord(2003, "178.34", "23.34", "Magnolia", "Waco");
            
            a = new NewLocation(123, d);
            b = new NewLocation(456, e);
            c = new NewLocation(123, d);
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

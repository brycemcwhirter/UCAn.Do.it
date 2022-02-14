import org.junit.jupiter.api.*;
import serialization.*;
import serialization.Error;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Location Request")
@Nested
public class LocationRequestTest {

    @DisplayName("Constructor Test")
    @Nested
    class constructorTest{

        @Test
        void validCreation() throws ValidationException {
            LocationRequest locationRequest = new LocationRequest(123);
            assertEquals(locationRequest.getMapID(), 123);
        }
    }

    @DisplayName("Decode & Encode")
    @Nested
    class decodeTest{


        @Test
        @DisplayName("Null Pointer Exception if in is null")
        void nullIn(){
            assertThrows(NullPointerException.class, ()->{
                LocationRequest bad = (LocationRequest) LocationRequest.decode(null);

            });
        }

        @Test
        @DisplayName("Decode Test")
        void decodeTest() throws ValidationException, IOException {
            byte[] buf = "ADDATUDEv1 123 ALL \r\n".getBytes(StandardCharsets.UTF_8);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
            MessageInput in = new MessageInput(byteArrayInputStream);
            LocationRequest test = (LocationRequest) Message.decode(in);
        }

        @Test
        @DisplayName("Encode")
        void encodeTest() throws IOException, ValidationException {
            var bOut = new ByteArrayOutputStream();
            var out = new MessageOutput(bOut);
            new LocationRequest(234).encode(out);
            assertArrayEquals("ADDATUDEv1 234 ALL \r\n".getBytes(StandardCharsets.UTF_8), bOut.toByteArray());
        }

    }








    @DisplayName("To String")
    @Nested
    class toStringTest{

        @Test
        void toStringTest() throws ValidationException {
            LocationRequest lr = new LocationRequest(123);
            String lrString = lr.toString();
            assertEquals(" map=123", lrString);
        }

    }

    @DisplayName("Equals & Hashcode")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class equalsAndHashCode{

        LocationRequest a, b, c;


        @BeforeAll
        void init() throws ValidationException {
            a = new LocationRequest(123);
            b = new LocationRequest(456);
            c = new LocationRequest(123);

        }

        @Test
        void testEqualObjects() {
            assertTrue(a.equals(c));

        }

        @Test
        void testUnequalObjects() {
            assertFalse(a.equals(b));
        }

        @Test
        void testHashCode() {
            assertTrue(a.hashCode() == c.hashCode());

        }
    }


}

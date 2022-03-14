package serialization.test;

import serialization.*;
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


@DisplayName("Location Request")
@Nested
public class LocationRequestTest {

    @DisplayName("Constructor Test")
    @Nested
    class constructorTest{

        @Test
        void validCreation() throws ValidationException {
            LocationRequest locationRequest = new LocationRequest(123);
            assertEquals(locationRequest.getMapId(), 123);
        }
    }





    @DisplayName("Decode & Encode")
    @Nested
    static
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





        @ParameterizedTest
        @DisplayName("Invalid Decode Test")
        @MethodSource("invalidDecodeStreams")
        void invalidDecode(String bad)  {
            assertThrows(ValidationException.class, ()->{
                byte[] buf = bad.getBytes(StandardCharsets.UTF_8);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
                MessageInput in = new MessageInput(byteArrayInputStream);
                LocationRequest test = (LocationRequest) Message.decode(in);
            });
        }

        public static Stream<Arguments> invalidDecodeStreams(){
            return Stream.of(
                    arguments("ADDATUDEv1 5 ALL ADDATUDEv1 345 ALL")
            );
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
            assertEquals("LocationRequest: map=123", lrString);
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

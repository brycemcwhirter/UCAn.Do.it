import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import serialization.*;
import serialization.Error;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;



@DisplayName("Error")
public class ErrorTest {



    @DisplayName("Constructor")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class constructorTest{



        @Test
        @DisplayName("Valid Set")
        void validSet() throws ValidationException {
            Error er = new Error(123, "error");
            assertEquals(er.getErrorMessage(), "error");
            assertEquals(er.getMapID(), 123);
        }




        @ParameterizedTest
        @DisplayName("Invalid Message")
        @MethodSource("invalidArguments")
        void invalidMessage(long userID, String errorMsg) throws ValidationException{
            assertThrows(ValidationException.class, () ->{
                Error bad = new Error(userID, errorMsg);
            });
        }




        public Stream<Arguments> invalidArguments(){
            return Stream.of(
                    arguments(123, null),
                    arguments(-123, "negativeUnsignedInt"),
                    arguments(123, "\nUnprintableCharacters"),
                    arguments(Integer.MAX_VALUE+1, "Bad Integer")
            );
        }

    }

    @DisplayName("Decode & Encode")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class decodeTest{


        @Test
        @DisplayName("Null Pointer Exception if in is null")
        void nullIn(){
            assertThrows(NullPointerException.class, ()->{
                Error bad = (Error) Error.decode(null);
            });
        }

        @Test
        @DisplayName("Decode Test")
        void decodeTest() throws ValidationException, IOException {
            byte[] buf = "ADDATUDEv1 123 ERROR bad\r\n".getBytes(StandardCharsets.UTF_8);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
            MessageInput in = new MessageInput(byteArrayInputStream);
            Error test = (Error) Message.decode(in);
        }


        @ParameterizedTest(name = "{0}")
        @DisplayName("Invalid Decode")
        @MethodSource("invalidDecodeStreams")
        void invalidDecode(String name, String badStream) throws ValidationException, IOException{
            assertThrows(ValidationException.class, ()->{
                byte[] buf = badStream.getBytes(StandardCharsets.UTF_8);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
                MessageInput in = new MessageInput(byteArrayInputStream);
                LocationRequest test = (LocationRequest) Message.decode(in);
            });
        }

        public Stream<Arguments> invalidDecodeStreams(){
            return Stream.of(arguments
                    ("Invalid Header", "ADDATUDEv2")
            );
        }



        @Test
        @DisplayName("Valid Encode")
        void validEncode() throws ValidationException, IOException {
            var bOut = new ByteArrayOutputStream();
            var out = new MessageOutput(bOut);
            new Error(123, "bad").encode(out);
            assertArrayEquals("ADDATUDEv1 123 ERROR 3 bad\r\n".getBytes(StandardCharsets.UTF_8), bOut.toByteArray());
        }



    }








    @DisplayName("Getters & Setters")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class gettersAndSettersTest{

        Error a;


        @BeforeAll
        void init() throws ValidationException{
            a = new Error(123, "errormsg");

        }

        @Test
        @DisplayName("Valid Set Error Message")
        void validSet(){
            a.setErrorMessage("Error Message");
            assertEquals("Error Message", a.getErrorMessage());
        }

        // Invalid Set (everything throws validation exception)

    }








    @DisplayName("To String")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class toStringTest{

        Error test;

        @Test
        void validString() throws ValidationException{
            test = new Error(123, "bad");
            String toStringRepresentation = test.toString();
            String valid = " map="+test.getMapID()+" error="+test.getErrorMessage();
            assertEquals(valid, toStringRepresentation);
        }
    }








    @DisplayName("Equals & Hashcode")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class equalsAndHashCode{

        Error a,b,c;

        @BeforeAll
        void init() throws ValidationException {
            a = new Error(123, "bad");
            b = new Error(456, "notGood");
            c = new Error(123, "bad");
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

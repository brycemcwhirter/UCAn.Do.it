

import addatude.serialization.*;
import addatude.serialization.Error;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;


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
            assertEquals(er.getMapId(), 123);
        }




        @ParameterizedTest
        @DisplayName("Invalid Message")
        @MethodSource("invalidArguments")
        void invalidMessage(long userID, String errorMsg) {
            assertThrows(ValidationException.class, () ->{
                Error bad = new Error(userID, errorMsg);
            });
        }




        public Stream<Arguments> invalidArguments(){
            return Stream.of(
                    arguments(123, null),
                    arguments(-123, "negativeUnsignedInt"),
                    arguments(123, "\nUnprintableCharacters")
            );
        }

    }

    @DisplayName("Decode & Encode")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class decodeTest{


        private String name;

        @Test
        @DisplayName("Null Pointer Exception if in is null")
        void nullIn(){
            assertThrows(NullPointerException.class, ()->{
                Error bad = (Error) Error.decode(null);
            });
        }

        @DisplayName("Decode Test")
        @ParameterizedTest(name="{1} is valid")
        @MethodSource("ValidErrorDecodeStreams")
        void validDecode(long mapId, String errorMsg) throws ValidationException, IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MessageOutput out = new MessageOutput(baos);
            new Error(mapId, errorMsg).encode(out);


            byte[] buf = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(buf);
            MessageInput in = new MessageInput(bais);
            Error test = (Error) Message.decode(in);


            assertEquals(mapId, test.getMapId());
            assertEquals(errorMsg, test.getErrorMessage());

        }

        public Stream<Arguments> ValidErrorDecodeStreams(){
            return Stream.of(
                    arguments(0, ""),
                    arguments(5 ,"there"),
                    arguments(12, "hello there!"),
                    arguments(12345, "1234512345123451234512345123451234512345123451234512345123451234512345123451234512345123451234512345")
            );
        }


        @ParameterizedTest(name = "{0}")
        @DisplayName("Invalid Decode")
        @MethodSource("invalidDecodeStreams")
        void invalidDecode(String name, String badStream) {
            this.name = name;
            assertThrows(ValidationException.class, ()->{
                byte[] buf = badStream.getBytes(StandardCharsets.UTF_8);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
                MessageInput in = new MessageInput(byteArrayInputStream);
                LocationRequest test = (LocationRequest) Message.decode(in);
            });
        }

        public Stream<Arguments> invalidDecodeStreams(){
            return Stream.of(
                    arguments("Invalid Header", "ADDATUDEv2"),
                    arguments("Invalid Size", "ADDATUDEv1 5 ERROR 5 ther"),
                    arguments("Invalid Operations", "ADDATUDEv1 345 ERROR 2 GoADDATUDEv1 345 ALL"),
                    arguments("Two Decode Streams", "ADDATUDEv1 345 ERROR 2 GoADDATUDEv1 345 ALL\r\n")
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
        void validSet() throws ValidationException {
            a.setErrorMessage("Error Message");
            assertEquals("Error Message", a.getErrorMessage());
        }

        // Invalid Set (everything throws validation exception)
        @ParameterizedTest(name="{0} is invalid")
        @ValueSource(strings = {"1111111111111111111111111111111111111111111111111111111111111111111111111111"})
        void invalidErrorMessage(String bad){
            assertThrows(ValidationException.class, ()-> a.setErrorMessage(bad));
        }

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
            String valid = "Error: map="+test.getMapId()+" error="+test.getErrorMessage();
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

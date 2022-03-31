import addatude.serialization.*;
import addatude.serialization.Error;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@Nested
@DisplayName("Donahoo Test")
public class failingTest {

    @Nested
    class LocationRecordTest{

        //Decode Test
        // to do you are failing this (passing right now but instance you're not thinking of)
        @ParameterizedTest(name = "Basic Decode")
        @MethodSource("validDecodeStreams")
        public void testDecode(String decodeStream) throws ValidationException {
            var in = new MessageInput(new ByteArrayInputStream(
                    decodeStream.getBytes(StandardCharsets.UTF_8)));
            var msg = new LocationRecord(in);

        }


        public static Stream<Arguments> validDecodeStreams() {
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

    @Nested
    class NewLocationTest{

        // Equals Test
            //non-equal objects prefer non-equal hash codes
            // make sure two non equal objects don't have the same hash code?





        @Test
        void invalidDecodeStream() {

            assertThrows(ValidationException.class, ()->{
                byte[] invalid = "ADDATUDEv1 5 NEW 6 7.0 8.0 3 xyz4 qwerADDATUDEv1 345 ALL\r\n".getBytes(StandardCharsets.UTF_8);
                ByteArrayInputStream bais = new ByteArrayInputStream(invalid);
                MessageInput in = new MessageInput(bais);
                var a = Message.decode(in);
            });

        }

        //Throw Validation Exception in IO Problem
            // close the message input before decoding? (ADDATUDEv1 5 NEW 6 7.0 8.0 3 xyz4 qwer )

        @Test
        void validationExceptionIO() {
            assertThrows(ValidationException.class, ()->{
                byte[] invalid = "ADDATUDEv1 5 NEW 6 7.0 8.0 3 xyz4 qwer\r\n".getBytes(StandardCharsets.UTF_8);
                ByteArrayInputStream bais = new ByteArrayInputStream(invalid);
                MessageInput in = new MessageInput(bais);
                in.closeMessageInputStream();
                var a = Message.decode(in);
            });
        }


        @Test
        void decodeSpecificStream() throws ValidationException {
            byte[] valid = "ADDATUDEv1 99999 NEW 99999 180.0 -90.0 5 o n e12 hello there!\r\n".getBytes(StandardCharsets.UTF_8);
            ByteArrayInputStream bais = new ByteArrayInputStream(valid);
            MessageInput in = new MessageInput(bais);
            var a = Message.decode(in);
        }
    }

    @Nested
    class ErrorTest{

        Error a = new Error(123, "errormsg");

        ErrorTest() throws ValidationException {
        }







    }

    @Nested
    class LocationResponseTest{





    }

}

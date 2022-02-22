import addatude.serialization.*;
import addatude.serialization.Error;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.xml.stream.Location;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@Nested
@DisplayName("Donahoo Test")
public class failingTest {

    @Nested
    static
    class LocationRecordTest{

        //Decode Test
        // todo you are failing this (passing right now but instance you're not thinking of)
        @ParameterizedTest(name = "Basic Decode")
        @MethodSource("validDecodeStreams")
        public void testDecode(String decodeStream) throws IOException, ValidationException {
            var in = new MessageInput(new ByteArrayInputStream(
                    decodeStream.getBytes(StandardCharsets.UTF_8)));
            var msg = new addatude.serialization.LocationRecord(in);

        }


        static public Stream<Arguments> validDecodeStreams() {
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
    static
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
    static
    class ErrorTest{

        Error a = new Error(123, "errormsg");

        ErrorTest() throws ValidationException {
        }

        //repeated 1's?
        @ParameterizedTest(name="{0} is invalid")
        @ValueSource(strings = {"1111111111111111111111111111111111111111111111111111111111111111111111111111"})
        void invalidErrorMessage(String bad){
            assertThrows(ValidationException.class, ()-> a.setErrorMessage(bad));
        }

        // Premature EOS [byte array?]

    }

    @Nested
    static
    class LocationResponseTest{

        // Premature EOS [byte array?]

        //[36m| | |   '--[0m [31mmapid = 0 LRs = [1:NAM-Des p (-5.3,9.7), 2:NAME-Desc (-5.3,9.7), 34567:012345678...[0m [31m[X][0m [31mProtocol: ADDATUDEv1[0m



    }

}

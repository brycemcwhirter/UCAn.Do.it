import addatude.serialization.*;
import addatude.serialization.Error;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;


public class LocalLocationRequestTest {




    @Nested
    class constructorTest{

        @DisplayName("Invalid Parameters")
        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("badParameters")
        void testInvalid(String message, long mapId, String longitude, String latitude) throws IllegalArgumentException {

            assertThrows(ValidationException.class, ()-> {
                LocalLocationRequest localLocationRequest = new LocalLocationRequest(mapId, longitude, latitude);
            });

        }

        public static Stream<Arguments> badParameters(){
            return Stream.of(
                    arguments("Bad Map ID", -123, "1.23", "3.23"),
                    arguments("Bad Longitude", 123, "181", "3.23"),
                    arguments("Bad Latitude", 123, "179", "-91")
            );
        }

    }



    @Nested
    class encodeAndDecode{


        @Test
        @DisplayName("Valid Encode Test")
        void validDecodeTest() throws ValidationException, IOException {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            MessageOutput out = new MessageOutput(byteArrayOutputStream);
            new LocalLocationRequest(123, "3.2", "3.2").encode(out);
            byte[] buf = byteArrayOutputStream.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(buf);
            MessageInput in = new MessageInput(bais);
            LocalLocationRequest test = (LocalLocationRequest) Message.decode(in);


            assertEquals(123, test.getMapId());
            assertEquals("3.2", test.getLatitude());
            assertEquals("3.2", test.getLongitude());




        }



        @ParameterizedTest(name = "Invalid Decode Streams")
        @MethodSource("invalidDecodeStreams")
        void invalidDecodeStream(String badDecodeStream){
            assertThrows(ValidationException.class, ()->{
                var in = new MessageInput(new ByteArrayInputStream(
                        badDecodeStream.getBytes(StandardCharsets.UTF_8)));
                var msg = Message.decode(in);
            });
        }


        public static Stream<Arguments> invalidDecodeStreams() {
            return Stream.of(
                    arguments("ADDATUDEv1 5 LOCAL 123 -190 invalid longitude"),
                    arguments("ADDATUDEv1 5 LOCAL 123 180.0 -923.3 invalid latitude"),
                    arguments("ADDATUDEv1 5 LOCAL -456 180.0 -923.3 invalid mapId"));
        }

    }






    @Nested
    class GettersAndSetters{


        LocalLocationRequest localLocationRequest;



        @BeforeEach
        void init() throws ValidationException{
            localLocationRequest = new LocalLocationRequest(123, "3.2", "2.3");
        }



        @ParameterizedTest(name = "Invalid Longitude")
        @ValueSource(strings = {"-180.1", "180.1", "1893.2", "-100.184835"})
        void invalidLongitude(String badLongitude){
            assertThrows(ValidationException.class, ()-> localLocationRequest.setLongitude(badLongitude));

        }

        @ParameterizedTest(name = "Invalid Latitude")
        @ValueSource(strings = {"-90.1", "90.1", "-10.1848303"})
        void invalidLatitude(String badLatidude){
            assertThrows(ValidationException.class, ()-> localLocationRequest.setLatitude(badLatidude));
        }



    }


}

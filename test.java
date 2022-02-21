package addatude.serialization.test;

import addatude.serialization.*;

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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;



@Nested
class decodeTest {


    @ParameterizedTest(name = "Basic Decode")
    @MethodSource("validDecodeStreams")
    public void testDecode(String decodeStream) throws IOException, ValidationException {
        var in = new MessageInput(new ByteArrayInputStream(
                decodeStream.getBytes(CHARENC)));
        var msg = new LocationRecord(in);

    }


    public Stream<Arguments> validDecodeStreams() {
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
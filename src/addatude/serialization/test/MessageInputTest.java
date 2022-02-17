package addatude.serialization.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import addatude.serialization.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class MessageInputTest {

    @Test
    @DisplayName("Null Input Stream")
    void testNullOutputStream() throws NullPointerException{
        assertThrows(NullPointerException.class, ()->{
            MessageInput bad = new MessageInput(null);
        });
    }

    @Nested
    static
    class readingFunctions{

        static MessageInput in;

        @BeforeAll
        static void init(){
            byte[] buf = "This is a test".getBytes(StandardCharsets.UTF_8);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
            in = new MessageInput(byteArrayInputStream);
        }

        @Test
        @DisplayName("Read Until Space Valid")
        void testReadUntilSpaceValid() throws IOException {
            String test = in.readUntilSpace();
            assertEquals("This", test);

        }

        @ParameterizedTest
        @DisplayName("Reading Size & Value")
        @ValueSource(strings = {"2 BU5 Baylor", "8 Arkansas12 FAYETTEVILLE"})
        void testReadSizeAndValue(String string) throws IOException{
            byte[] buf = string.getBytes(StandardCharsets.UTF_8);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
            MessageInput in1 = new MessageInput(byteArrayInputStream);


        }

    }

    @DisplayName("Equals & Hashcode")
    class equalsAndHashCode{

        @Test
        void testEqualObjects() {

        }

        @Test
        void testUnequalObjects() {

        }

        @Test
        void testHashCode() {

        }
    }






}

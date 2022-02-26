package serialization.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import serialization.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class MessageOutputTest {

    @Test
    @DisplayName("Null Output Stream")
    void testNullOutputStream() throws NullPointerException{
        assertThrows(NullPointerException.class, ()-> new MessageOutput(null));
    }

    @Test
    @DisplayName("Valid Write")
    void testValidWrite() throws IOException {
        byte[] buf = "This is a test of write".getBytes(StandardCharsets.UTF_8);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MessageOutput os = new MessageOutput(byteArrayOutputStream);

        os.write(buf);


        assertEquals("This is a test of write", byteArrayOutputStream.toString());

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

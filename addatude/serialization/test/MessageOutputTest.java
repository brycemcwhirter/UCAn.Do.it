package serialization.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import serialization.LocationRecord;
import serialization.MessageOutput;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class MessageOutputTest {

    @Test
    @DisplayName("Null Output Stream")
    void testNullOutputStream() throws NullPointerException{
        assertThrows(NullPointerException.class, ()->{
            MessageOutput bad = new MessageOutput(null);
        });
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




}

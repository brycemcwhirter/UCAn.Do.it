package serialization.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import serialization.MessageInput;
import serialization.MessageOutput;
import serialization.ValidationException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertThrows;

public class MessageInputTest {

    @Test
    @DisplayName("Null Input Stream")
    void testNullOutputStream() throws NullPointerException{
        assertThrows(NullPointerException.class, ()->{
            MessageInput bad = new MessageInput(null);
        });
    }






}

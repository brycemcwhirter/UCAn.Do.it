import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import addatude.serialization.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class MessageInputTest {

    @Test
    @DisplayName("Null Input Stream")
    void testNullOutputStream() throws NullPointerException{
        assertThrows(NullPointerException.class, ()->{
            MessageInput bad = new MessageInput(null);
        });
    }

    @Nested
    class readingFunctions{

        MessageInput in;

        @BeforeAll
        static void init(){
            byte[] buf = "This is a test\r\n".getBytes(StandardCharsets.UTF_8);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
            in = new MessageInput(byteArrayInputStream);
        }

        @Test
        @DisplayName("Read Until Space Valid")
        void testReadUntilSpaceValid() throws ValidationException {
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


        @ParameterizedTest
        @DisplayName("Read Integer Value")
        @ValueSource(strings = {"12345 ", "1234 "})
        void testReadIntegerValue(String string) throws ValidationException {
            byte[] buf = string.getBytes(StandardCharsets.UTF_8);
            ByteArrayInputStream bais = new ByteArrayInputStream(buf);
            MessageInput in1 = new MessageInput(bais);
            assertEquals(Integer.parseInt(string.trim()), in1.readIntegerValue());
        }


        /*@ParameterizedTest
        @DisplayName("EOS Stream")
        @MethodSource("EOSStream")
        void blockingEOSStream(String value) throws ValidationException {
            byte[] buf = value.getBytes(StandardCharsets.UTF_8);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
            BlockingInputStream blockingInputStream = new BlockingInputStream(byteArrayInputStream);
            MessageInput in = new MessageInput(blockingInputStream);


            var decode = Message.decode(in);

        }

        public static Stream<Arguments> EOSStream(){
            return Stream.of(
                    arguments("ADDATUDEv1 99999 NEW 99999 180.0 -90.0 5 O N E12 hello there!\r\n")
            );
        }
*/







    }

   static class BlockingInputStream extends InputStream{

        private InputStream in;

        BlockingInputStream(InputStream in){
            this.in = in;
        }

       @Override
       public int read() throws IOException {
           int rv = in.read();
           if(rv == -1){
               try {
                   block();
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
           return rv;
       }

       private synchronized void block() throws InterruptedException {
            while(true) {
                wait();
            }
       }
   }






}

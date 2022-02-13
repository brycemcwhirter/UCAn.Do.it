import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import serialization.Message;
import serialization.MessageOutput;
import serialization.ValidationException;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Message Test")
public class MessageTest {

    static class ConcreteMessage extends Message {

        ConcreteMessage(long mapID) throws ValidationException {
            super("ConcreteMessage", mapID);

        }


        @Override
        public void encode(MessageOutput out) {

        }
    }






    @DisplayName("Getters & Setters")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class gettersAndSetters{
        ConcreteMessage msg, test;


        @BeforeAll
        void setUp() throws ValidationException {
            msg = new ConcreteMessage(123);
        }


        @Test
        @DisplayName("Happy Path for set Map ID")
        void happySetMapId() throws ValidationException {
            msg.setMapId(456);
            assertEquals(456, msg.getMapID());
        }


        @ParameterizedTest
        @DisplayName("Invalid Map ID")
        @ValueSource(ints={Integer.MAX_VALUE+1, -123})
        void sadSetMapID(int badMapID) {
            assertThrows(ValidationException.class, ()->{
               msg.setMapId(badMapID);
            });
        }

    }

    @DisplayName("Equals & Hashcode")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class equalsAndHashCode{

        ConcreteMessage a, b, c;

        @BeforeAll
        void setUp() throws ValidationException {
            a = new ConcreteMessage(123);
            b = new ConcreteMessage(456);
            c = new ConcreteMessage(123);

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
            assertEquals(a.hashCode() , c.hashCode());
        }
    }
}

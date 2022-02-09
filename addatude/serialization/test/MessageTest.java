import org.junit.BeforeClass;
import org.junit.jupiter.api.*;
import serialization.Message;
import serialization.MessageOutput;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ConcreteMessage extends Message {

    ConcreteMessage(){

    }



    @Override
    public void encode(MessageOutput out) throws IOException {

    }
}

@DisplayName("Message Test")
public class MessageTest {


    // TODO Write Hash Code & Equal Test for all message types
    // TODO encode happy path / decode for all message types




    @DisplayName("Getters & Setters")
    class gettersAndSetters{
        ConcreteMessage msg, test;


        @BeforeAll
        void setUp() {
            msg = new ConcreteMessage();
            //TODO How to tests abstract class methods?
        }

        // set valid mapID happy path

        @Test
        @DisplayName("Happy Path for set Map ID")
        void happySetMapId() {


        }


        // invalid mapID

    }

    @DisplayName("Equals & Hashcode")
    class equalsAndHashCode{

        ConcreteMessage a, b;

        @BeforeAll
        void setUp() {
            a = new ConcreteMessage();
            b = new ConcreteMessage();

        }

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

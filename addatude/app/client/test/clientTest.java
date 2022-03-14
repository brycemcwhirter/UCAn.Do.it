package app.client.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

@Nested
public class clientTest {


    //TODO make binary input/output streams

    //todo use SYStem.setIN / System.setOut to set streams to compare

    // TODO set up different servers to send bad input


    @Nested
    class specification{

        // happy path (connects to sever)

        @Test
        void happyPath() {

        }


        // Arguments != 2


        // wind & 12345 (invalid name & Port)
        // Failed Connected Server

    }

    @Nested
    class userInput{

        // Happy Path

        // Invalid Operations

        // Unicode Other

        // Anything not ALL or NEW


        // Follows Execution Path Correctly
        //Operation -> Map ID

        // GetMapID

        // Happy Path

        // Invalid MapID
        //-1, 346, abc, UnicodeOther

    }

    @Nested
    class testforNEW{

        // NEW
        // Execution Path
        // Operation -> MapID -> UserID -> ....

        // If any parameter invalid
        // start over operation

    }

    @Nested
    class testForALL{

        // ALL
        // Execution Path
        // Operation -> MapID -> (Receive Message)

    }





    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class serverInput{

        @Test
        @DisplayName("Happy Path")
        void testHappyPath(){
            // happy path
            // correct value received tests to screen correctly
            // ALL & NEW

        }

        // invalid values
        //not an ADDATUDEv1
        // Bad header
        // Bad Operation
        // Invalid MapID
        // Bad Parameters
        // no '\r\n'

        // Receive an Error
        // print valid error message

        // Receive Unexpected Message
        // print error message unexpected messgae

        // Server Disconnected
        // check for problem communicated with server
        // terminates program

    }




}

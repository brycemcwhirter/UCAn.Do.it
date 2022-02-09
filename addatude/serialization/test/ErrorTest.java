import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import serialization.Error;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Error")
public class ErrorTest {



    @DisplayName("Constructor")
    class constructorTest{

        // invalid map id

        // invalid char list

        // Null ErrorMessage (Validation Exception)

        // char count doesn't match char list size
    }

    @DisplayName("Decode Implementation")
    class decodeTest{


        @Test
        @DisplayName("Null Pointer Exception if in is null")
        void nullIn(){
            assertThrows(NullPointerException.class, ()->{
                Error bad = (Error) Error.decode(null);

            });
        }
    }

    @DisplayName("Getters & Setters")
    class gettersAndSettersTest{

        @Test
        @DisplayName("Valid Set Error Message")
        void validSet(){

            // AssertEquals

        }

        // Invalid Set (everything throws validation exception)

    }

    @DisplayName("To String")
    class toStringTest{

        // Tests Happy Path

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

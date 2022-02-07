import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import serialization.Message;
import serialization.NewLocation;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("New Location Test")
public class NewLocationTest {

    @DisplayName("Constructor")
    class constructorTest{

        // Happy Path

        // Invalid mapID

        // Null Location



    }

    @DisplayName("Decode Implementation")
    class decodeTest{


        @Test
        @DisplayName("Null Pointer Exception if in is null")
        void nullIn(){
            assertThrows(NullPointerException.class, ()->{
                NewLocation bad = (NewLocation) NewLocation.decode(null);

            });
        }
    }

    @DisplayName("Getters And Setters")
    class gettersAndSetters{

        // valid set location record

        // null location record (ValidationException)

    }

    @DisplayName("To String")
    class toString{

        // Happy Path

    }


}

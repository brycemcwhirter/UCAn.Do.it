import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import serialization.LocationResponse;
import serialization.Message;
import serialization.NewLocation;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Location Response")
public class LocationResponseTest {

    @DisplayName("Constructor")
    class constructorTest{

        // Happy Path

        // Invalid Arguments (mapName, mapID)





    }

    @DisplayName("Decode Implementation")
    class decodeTest{


        @Test
        @DisplayName("Null Pointer Exception if in is null")
        void nullIn(){
            assertThrows(NullPointerException.class, ()->{
                LocationResponse bad = (LocationResponse) LocationResponse.decode(null);

            });
        }
    }

    @DisplayName("Add Location Record")
    class addLocationRecordTest{

    }

    @DisplayName("Getters & Setters")
    class gettersAndSettersTest{

    }

    @DisplayName("To String")
    class toStringTest{

    }



}

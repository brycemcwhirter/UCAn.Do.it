import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import serialization.Error;
import serialization.LocationRequest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Location Request")
public class LocationRequestTest {

    @DisplayName("Constructor Test")
    class constructorTest{

    }

    @DisplayName("Decode Implementation")
    class decodeTest{


        @Test
        @DisplayName("Null Pointer Exception if in is null")
        void nullIn(){
            assertThrows(NullPointerException.class, ()->{
                LocationRequest bad = (LocationRequest) LocationRequest.decode(null);

            });
        }
    }

    @DisplayName("To String")
    class toStringTest{

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

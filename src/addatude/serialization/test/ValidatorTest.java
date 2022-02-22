import addatude.serialization.ValidationException;
import addatude.serialization.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.Assert.assertThrows;

public class ValidatorTest {


    @Nested
    class UnsignedInteger{

        @ParameterizedTest(name = "{0} is valid")
        @DisplayName("Valid Unsigned Integer")
        @ValueSource(strings = {"0", "1", "56", "150", "99999"})
        void validUnsignedIntegerTest(String testString) throws ValidationException {
            Validator.validUnsignedInteger("test", testString);


        }

        @ParameterizedTest(name = "{0} is invalid")
        @DisplayName("Invalid Unsigned Integer")
        @ValueSource(strings = {"A", "-1", "100000L", "-999999", "999999"})
        void invalidUnsignedInteger(String invalidString){
            assertThrows(ValidationException.class, ()-> Validator.validUnsignedInteger("test", invalidString));
        }

        @Test
        @DisplayName("Null Unsigned Integer")
        void nullUnsignedInteger(){
            assertThrows(ValidationException.class, ()-> Validator.validUnsignedInteger("null", null));
        }
    }


    @Nested
    class validString{

        @ParameterizedTest(name="{0} is valid")
        @ValueSource(strings = {"string", ""})
        void validStringTest(String s) throws ValidationException {
            Validator.validString("Param", s);
        }

        @ParameterizedTest(name="{0} is invalid")
        @ValueSource(strings = {"\u00d6\u008dring"})
        void invalidStringTest(String s)  {
            assertThrows(ValidationException.class, ()->Validator.validString("Param", s));
        }


    }

    @Nested
    class validDouble{

        @ParameterizedTest(name="{0} is valid")
        @ValueSource(strings = {"10.8", "4.2"})
        void validStringTest(String s) throws ValidationException {
            Validator.validDouble("Param", s);
        }

        @ParameterizedTest(name="{0} is invalid")
        @ValueSource(strings = {"10.123456789123"})
        void invalidStringTest(String s)  {
            assertThrows(ValidationException.class, ()->Validator.validDouble("Param", s));
        }


    }



}

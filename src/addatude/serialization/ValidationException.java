/************************************************
 *
 * Author: Bryce McWhirter
 * Assignment: Program 0
 * Class: Data Communications
 *
 ************************************************/

/**
 * Testing Partner: John Harrison
 */

package addatude.serialization;


public class ValidationException extends Exception {

    String invalidToken;
    private static final long serialVersionUID = 1234567L;

    /**
     * Creates a new Validation Exception
     * @param message the message for the exception
     */
    public ValidationException(String invalidToken, String message) {
        super(message);
        this.invalidToken = invalidToken;
    }



    public ValidationException(String invalidToken, String message, Throwable cause){
        super(message, cause);
        this.invalidToken = invalidToken;
    }



    /**
     * Returns the invalid Token
     * @return
     */
    public String getInvalidToken() {
        return invalidToken;
    }
}
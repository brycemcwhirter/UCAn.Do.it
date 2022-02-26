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

package serialization;


/**
 * A validation exception is thrown when a
 * parameter is reached that is deemed invalid
 * according to the ADDATUDE protocol.
 */
public class ValidationException extends Exception {

    String invalidToken;
    private static final long serialVersionUID = 1234567L;


    /**
     * @param invalidToken The token associated with the exception
     * @param message The message tied to the exception
     */
    public ValidationException(String invalidToken, String message) {
        super(message);
        this.invalidToken = invalidToken;
    }


    /**
     * Creates a new Validation Exception
     * @param invalidToken The token associated with the exception
     * @param message the messages associated with the exception
     * @param cause the cause of the original exception
     */
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

/************************************************
 *
 * Author: Bryce McWhirter
 * Assignment: Program 0
 * Class: Data Communications
 *
 ************************************************/

package serialization;

public class ValidationException extends Exception{


    /**
     * Creates a new Validation Exception
     * @param message the message for the exception
     */
    public ValidationException(String message) {
        super(message);
    }
}

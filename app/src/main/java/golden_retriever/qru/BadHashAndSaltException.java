package golden_retriever.qru;

/**
 * Created by daniel on 3/19/18.
 */

public class BadHashAndSaltException extends Exception{
    public BadHashAndSaltException() {
        super();
    }

    public BadHashAndSaltException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadHashAndSaltException(String message) {
        super(message);
    }

    public BadHashAndSaltException(Throwable cause) {
        super(cause);
    }
}

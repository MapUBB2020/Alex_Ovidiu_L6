package lab3.exceptions;

public class IDInUseException extends RuntimeException {
    public IDInUseException(String message) {
            super(message);
        }
}

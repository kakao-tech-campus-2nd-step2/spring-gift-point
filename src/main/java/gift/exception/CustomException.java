package gift.exception;

public class CustomException {
    public static class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
    }

    public static class EntityNotFoundException extends RuntimeException {
        public EntityNotFoundException(String message) {
            super(message);
        }
    }

    public static class GenericException extends RuntimeException {
        public GenericException(String message) {
            super(message);
        }
    }

    public static class InvalidCredentialsException extends RuntimeException {
        public InvalidCredentialsException(String message) {
            super(message);
        }
    }

    public static class InvalidQuantityException extends RuntimeException {
        public InvalidQuantityException(String message) {super(message);}
    }
}

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

    public static class AccessDeniedException extends RuntimeException {
        public AccessDeniedException(String message) { super(message); }
    }

    public static class InvalidCredentialsException extends RuntimeException {
        public InvalidCredentialsException(String message) {
            super(message);
        }
    }

    public static class InvalidQuantityException extends RuntimeException {
        public InvalidQuantityException(String message) {super(message);}
    }

    public static class InvalidPasswordException extends RuntimeException {
        public InvalidPasswordException(String message) {super(message);}
    }

    public static class GenericException extends RuntimeException {
        public GenericException(String message) {
            super(message);
        }
        public GenericException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class EntityAlreadyExistException extends RuntimeException {
        public EntityAlreadyExistException(String message) {
            super(message);
        }
    }
}

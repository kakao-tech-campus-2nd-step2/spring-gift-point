package gift.exception;

public class HttpHeaderNotValidException extends UnauthenticatedException {

    public HttpHeaderNotValidException() {
        super("Http Header Not Valid");
    }

    public HttpHeaderNotValidException(String message) {
        super(message);
    }
}

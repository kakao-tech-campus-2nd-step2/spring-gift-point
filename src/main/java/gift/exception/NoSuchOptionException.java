package gift.exception;

public class NoSuchOptionException extends RuntimeException {

    public NoSuchOptionException() {
        super("해당 옵션이 존재하지 않습니다.");
    }
}

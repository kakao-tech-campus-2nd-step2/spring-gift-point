package gift.exception;

public class NoSuchWishedProductException extends RuntimeException {

    public NoSuchWishedProductException() {
        super("해당 위시 상품이 존재하지 않습니다.");
    }
}


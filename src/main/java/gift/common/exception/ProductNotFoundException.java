package gift.common.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super(id + "에 해당하는 상품이 존재하지 않습니다.");
    }
}

package gift.global.exception.product;

public class ProductDuplicateException extends RuntimeException {

    public ProductDuplicateException(String productName) {

        super(productName + " 이름을 가진 상품이 이미 존재합니다.");
    }
}

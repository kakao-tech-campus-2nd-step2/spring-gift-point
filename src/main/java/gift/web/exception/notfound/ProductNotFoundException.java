package gift.web.exception.notfound;

public class ProductNotFoundException extends NotFoundException {
    public ProductNotFoundException() {
        super("상품이 없슴다!");
    }
}

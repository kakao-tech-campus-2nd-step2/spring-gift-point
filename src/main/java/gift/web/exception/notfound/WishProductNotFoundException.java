package gift.web.exception.notfound;

public class WishProductNotFoundException extends NotFoundException{

    public WishProductNotFoundException() {
        super("위시 제품이 없슴다!");
    }
}

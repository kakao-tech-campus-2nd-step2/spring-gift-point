package gift.web.exception.notfound;

public class CategoryNotFoundException extends NotFoundException{
    public CategoryNotFoundException() {
        super("카테고리가 없슴다!");
    }
}
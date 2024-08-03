package gift.web.exception.duplicate;

public class CategoryDuplicatedException extends DuplicatedException{
    public CategoryDuplicatedException() {
        super("카테고리가 이미 있슴다!");
    }
}

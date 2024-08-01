package gift.web.exception.notfound;

public class OptionNotFoundException extends NotFoundException {
    public OptionNotFoundException() {
        super("옵션이 없슴다!");
    }
}
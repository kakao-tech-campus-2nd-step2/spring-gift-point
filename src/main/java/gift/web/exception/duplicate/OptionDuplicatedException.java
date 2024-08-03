package gift.web.exception.duplicate;

public class OptionDuplicatedException extends DuplicatedException{
    public OptionDuplicatedException() {
        super("옵션이 이미 있슴다!");
    }
}

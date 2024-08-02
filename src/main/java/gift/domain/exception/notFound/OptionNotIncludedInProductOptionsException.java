package gift.domain.exception.notFound;
import gift.domain.exception.ErrorCode;

public class OptionNotIncludedInProductOptionsException extends NotFoundException {

    public OptionNotIncludedInProductOptionsException() {
        super(ErrorCode.OPTION_NOT_INCLUDED_IN_PRODUCT_OPTIONS);
    }
}

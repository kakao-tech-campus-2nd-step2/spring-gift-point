package gift.domain.exception.notFound;
import gift.domain.exception.ErrorCode;

public class OptionNotIncludedInProductOptionsException extends NotFoundException {

    public OptionNotIncludedInProductOptionsException() {
        super("The option is not included in the product's options.", ErrorCode.OPTION_NOT_INCLUDED_IN_PRODUCT_OPTIONS, null);
    }
}

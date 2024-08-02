package gift.domain.exception.notFound;

import gift.domain.exception.ErrorCode;

public class WishNotFoundException extends NotFoundException {

    public WishNotFoundException() {
        super(ErrorCode.WISH_NOT_FOUND);
    }
}

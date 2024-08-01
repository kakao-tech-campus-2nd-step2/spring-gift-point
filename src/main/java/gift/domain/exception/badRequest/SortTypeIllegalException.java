package gift.domain.exception.badRequest;

import gift.domain.exception.ErrorCode;

public class SortTypeIllegalException extends BadRequestException {

    public SortTypeIllegalException() {
        super(ErrorCode.SORT_TYPE_ILLEGAL);
    }
}

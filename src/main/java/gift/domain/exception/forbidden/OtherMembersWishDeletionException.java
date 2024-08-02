package gift.domain.exception.forbidden;

import gift.domain.exception.ErrorCode;

public class OtherMembersWishDeletionException extends ForbiddenException {

    public OtherMembersWishDeletionException() {
        super(ErrorCode.OTHER_MEMBERS_WISH_DELETION);
    }
}

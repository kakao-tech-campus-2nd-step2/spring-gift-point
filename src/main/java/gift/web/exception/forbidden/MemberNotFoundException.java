package gift.web.exception.forbidden;

import gift.web.exception.notfound.NotFoundException;

public class MemberNotFoundException extends ForbiddenException {
    public MemberNotFoundException() {
        super("멤버가 없슴다!");
    }
}
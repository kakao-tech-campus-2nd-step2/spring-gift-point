package gift.domain.member.exception;

import gift.global.exception.NotFoundException;

public class MemberNotFoundException extends NotFoundException {

    public MemberNotFoundException(String message) {
        super(message);
    }
}

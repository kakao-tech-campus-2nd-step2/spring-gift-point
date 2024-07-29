package gift.domain.option.exception;

import gift.global.exception.NotFoundException;

public class OptionNotFoundException extends NotFoundException {

    public OptionNotFoundException(String message) {
        super(message);
    }
}

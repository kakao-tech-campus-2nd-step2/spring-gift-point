package gift.doamin.category.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CategoryNotFoundException extends ResponseStatusException {

    public CategoryNotFoundException() {
        super(HttpStatus.BAD_REQUEST, "해당 카테고리가 존재하지 않습니다");
    }
}

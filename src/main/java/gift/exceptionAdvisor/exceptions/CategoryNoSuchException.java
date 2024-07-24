package gift.exceptionAdvisor.exceptions;

import org.springframework.http.HttpStatus;

public class CategoryNoSuchException extends CategoryServiceException{

    public CategoryNoSuchException() {
        super("카테고리가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
    }
}

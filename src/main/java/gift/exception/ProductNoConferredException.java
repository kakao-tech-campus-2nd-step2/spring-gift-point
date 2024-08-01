package gift.exception;

import java.util.List;

import org.springframework.http.HttpStatus;

public class ProductNoConferredException extends ProductException {
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "[%s] 가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다";

    public ProductNoConferredException(List<String> regexes) {
        super(String.format(MESSAGE, String.join(", ", regexes)), HTTP_STATUS);
    }

}
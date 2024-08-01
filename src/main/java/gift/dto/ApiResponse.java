package gift.dto;

import gift.model.HttpResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@Getter
public class ApiResponse {

    private HttpResult result;
    private String message;
    private HttpStatus httpStatus;

}
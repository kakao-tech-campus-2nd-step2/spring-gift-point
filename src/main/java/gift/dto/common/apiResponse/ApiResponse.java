package gift.dto.common.apiResponse;

import org.springframework.http.HttpStatusCode;

public class ApiResponse<B> {
    private B body;
    private HttpStatusCode status;

    public ApiResponse(B body, HttpStatusCode status) {
        this.body = body;
        this.status = status;
    }
}

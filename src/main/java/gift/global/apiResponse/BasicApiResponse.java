package gift.global.apiResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatusCode;

/**
 * ResponseEntity의 body에 담길 기본 객체(Map<String, Object)를 생성하는 유틸리티 클래스
 */
public class BasicApiResponse {

    private final LocalDateTime timestamp;
    private final Integer status;
    private final Boolean success;

    public BasicApiResponse(
        @JsonProperty(value = "timestamp", required = true) LocalDateTime timestamp,
        @JsonProperty(value = "status", required = true) Integer status,
        @JsonProperty(value = "success", required = true) Boolean success

    ) {
        this.timestamp = timestamp;
        this.status = status;
        this.success = success;
    }

    public BasicApiResponse(Integer status) {
        this(LocalDateTime.now(), status, true);
    }

    public BasicApiResponse(HttpStatusCode statusCode) {
        this(statusCode.value(), true);
    }

    public BasicApiResponse(Integer status, Boolean success) {
        this(LocalDateTime.now(), status, success);
    }

    public BasicApiResponse(HttpStatusCode statusCode, Boolean success) {
        this(statusCode.value(), success);
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public Boolean getSuccess() {
        return success;
    }
}

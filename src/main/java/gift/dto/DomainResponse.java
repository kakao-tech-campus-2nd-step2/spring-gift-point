package gift.dto;

import gift.model.HttpResult;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

public class DomainResponse {

    private HttpResult httpResult;
    private List<Map<String, Object>> domain;
    private HttpStatus httpStatus;

    public DomainResponse() {}

    public DomainResponse(HttpResult httpResult, List<Map<String, Object>> domain, HttpStatus httpStatus) {
        this.httpResult = httpResult;
        this.domain = domain;
        this.httpStatus = httpStatus;
    }

    public HttpResult getHttpResult() {
        return httpResult;
    }

    public void setHttpResult(HttpResult httpResult) {
        this.httpResult = httpResult;
    }

    public List<Map<String, Object>> getDomain() {
        return domain;
    }

    public void setDomain(List<Map<String, Object>> domain) {
        this.domain = domain;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
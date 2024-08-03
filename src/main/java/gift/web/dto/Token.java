package gift.web.dto;

import org.springframework.http.HttpHeaders;

public record Token(String token) {

    public HttpHeaders makeTokenHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        return headers;
    }
}

package gift.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;

import org.springframework.stereotype.Component;

@Component
public class AuthorizationExtractor { // 토큰 추출
    private static final String AUTHORIZATION = "Authorization";
    private static final String ACCESS_TOKEN_TYPE = "Bearer";

    public String extract(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders(AUTHORIZATION);// 헤더의 AUTHORIZATION 필드의 값을 갖고옴
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if (value.toLowerCase().startsWith(ACCESS_TOKEN_TYPE.toLowerCase())) {// bearer타입이면
                return value.substring(ACCESS_TOKEN_TYPE.length()).trim();
            }
        }
        throw new IllegalArgumentException("토큰 추출 실패");
    }
}

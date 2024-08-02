package gift.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class AcceptanceTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String ALLOWED_METHOD_NAMES = "GET,POST,PUT,DELETE,OPTIONS,HEAD";
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_TOKEN = "Bearer test1234";

    @Test
    @DisplayName("CORS 설정 확인 - 모든 Origin 허용")
    void cors_allOrigins() throws Exception {
        mockMvc.perform(
                options("/api/products")
                    .header(HttpHeaders.ORIGIN, "http://localhost:8080")
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
            )
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ALLOWED_METHOD_NAMES))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.LOCATION))
            .andDo(print());
    }

    @Test
    @DisplayName("CORS 설정 확인 - 특정 Origin 허용")
    void cors_specificOrigin() throws Exception {
        mockMvc.perform(
                options("/api/products")
                    .header(HttpHeaders.ORIGIN, "http://localhost:3000")
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
            )
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:3000"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ALLOWED_METHOD_NAMES))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.LOCATION))
            .andDo(print());
    }

    @Test
    @DisplayName("CORS 설정 확인 - 모든 메서드 허용")
    void cors_allMethods() throws Exception {
        mockMvc.perform(
                options("/api/products")
                    .header(HttpHeaders.ORIGIN, "http://localhost:8080")
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "PUT")
            )
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ALLOWED_METHOD_NAMES))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.LOCATION))
            .andDo(print());
    }

    @Test
    @DisplayName("CORS 설정 확인 - 모든 헤더 허용")
    void cors_allHeaders() throws Exception {
        mockMvc.perform(
                options("/api/products")
                    .header(HttpHeaders.ORIGIN, "http://localhost:8080")
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "DELETE")
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "Content-Type, Authorization")
            )
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ALLOWED_METHOD_NAMES))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type, Authorization"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.LOCATION))
            .andDo(print());
    }

    @Test
    @DisplayName("CORS 설정 확인 - 자격 증명 허용")
    void cors_credentials() throws Exception {
        mockMvc.perform(
                options("/api/products")
                    .header(HttpHeaders.ORIGIN, "http://localhost:8080")
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
            )
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ALLOWED_METHOD_NAMES))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.LOCATION))
            .andDo(print());
    }

    @Test
    @DisplayName("CORS 설정 확인 - pre-flight 요청 캐시 시간 설정")
    void cors_preFlightMaxAge() throws Exception {
        mockMvc.perform(
                options("/api/products")
                    .header(HttpHeaders.ORIGIN, "http://localhost:8080")
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
            )
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ALLOWED_METHOD_NAMES))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.LOCATION))
            .andDo(print());
    }

    @Test
    @DisplayName("CORS 설정 확인 - /api/wishes GET 요청 (토큰 포함)")
    void cors_apiWishesGet_withToken() throws Exception {
        mockMvc.perform(
                options("/api/wishes")
                    .header(HttpHeaders.ORIGIN, "http://localhost:8080")
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
                    .header(AUTH_HEADER, BEARER_TOKEN)
            )
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ALLOWED_METHOD_NAMES))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.LOCATION))
            .andDo(print());
    }

    @Test
    @DisplayName("CORS 설정 확인 - /api/wishes GET 요청 (토큰 미포함)")
    void cors_apiWishesGet_withoutToken() throws Exception {
        mockMvc.perform(
                options("/api/wishes")
                    .header(HttpHeaders.ORIGIN, "http://localhost:8080")
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
            )
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ALLOWED_METHOD_NAMES))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.LOCATION))
            .andDo(print());
    }

    @Test
    @DisplayName("CORS 설정 확인 - /api/wishes POST 요청 (토큰 포함)")
    void cors_apiWishesPost_withToken() throws Exception {
        mockMvc.perform(
                options("/api/wishes")
                    .header(HttpHeaders.ORIGIN, "http://localhost:8080")
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
                    .header(AUTH_HEADER, BEARER_TOKEN)
            )
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ALLOWED_METHOD_NAMES))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.LOCATION))
            .andDo(print());
    }

    @Test
    @DisplayName("CORS 설정 확인 - /api/orders POST 요청 (토큰 포함)")
    void cors_apiOrdersPost_withToken() throws Exception {
        mockMvc.perform(
                options("/api/orders")
                    .header(HttpHeaders.ORIGIN, "http://localhost:8080")
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
                    .header(AUTH_HEADER, BEARER_TOKEN)
            )
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ALLOWED_METHOD_NAMES))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.LOCATION))
            .andDo(print());
    }
}

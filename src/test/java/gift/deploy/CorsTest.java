package gift.deploy;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc

public class CorsTest {

    @Autowired
    private MockMvc mockMvc;

    private static String ALLOWED_METHOD_NAMES = "GET,POST,PUT,PATCH,DELETE,OPTIONS";

    /**
     *
     * HTTP METHOD OPTINOS: 브라우저 ~ 서버 간에 통신하기 전 통신이 정상적으로 성립되는지
     * 확인하기 위해 브라우저에서 미리 보내는 일종의 예비 요청
     */
    @DisplayName("CORS 설정이 정상적으로 적용되었는지 테스트")
    @Test
    void corsAcceptance() throws Exception {
        String requestOrigin = "http://localhost:3000";
        String requestMethod = "GET";

        mockMvc.perform(
            options("/")
                .header(HttpHeaders.ORIGIN, requestOrigin)
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, requestMethod))
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, requestOrigin))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ALLOWED_METHOD_NAMES))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.LOCATION))
            .andDo(print());
    }
}

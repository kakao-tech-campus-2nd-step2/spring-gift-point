package gift.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.product.dto.auth.MemberDto;
import gift.product.service.AuthService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AcceptanceTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AuthService authService;

    String ALLOWED_METHOD_NAMES = "GET,POST,PUT,DELETE,OPTIONS";

    String CLIENT_ORIGIN = "http://localhost:3000";

    @Test
    void CORS_설정_테스트() throws Exception {
        MemberDto memberDto = new MemberDto("test@test.com", "test");
        authService.register(memberDto);
        String accessToken = authService.login(memberDto).accessToken();

        mockMvc.perform(
                options("/api/products")
                    .header(HttpHeaders.ORIGIN, CLIENT_ORIGIN)
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            )
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, CLIENT_ORIGIN))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
                ALLOWED_METHOD_NAMES))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
                HttpHeaders.LOCATION))
            .andDo(print())
        ;
    }
}

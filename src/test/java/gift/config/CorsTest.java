package gift.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.dto.betweenClient.member.MemberDTO;
import gift.service.MemberService;
import gift.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CorsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MemberService memberService;

    private MemberDTO memberDTO = new MemberDTO("1@1.com", "1", "basic", "12", "12");


    @Test
    void cors() throws Exception {
        memberService.register(memberDTO);

        mockMvc.perform(
                options("/api/products?categoryId=2")
                        .header(HttpHeaders.ORIGIN, "Http://localhost:8080")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
                        .header("Authorization", "Bearer " + jwtUtil.generateToken(memberDTO))
        )
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*"))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET,POST,PUT,DELETE,OPTIONS"))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.LOCATION))
                .andDo(print());
    }
}
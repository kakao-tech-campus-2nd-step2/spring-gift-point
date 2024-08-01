package gift.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.auth.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CorsTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void successPostRequestWithCors() throws Exception {
        var postRequest = post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ORIGIN, "http://localhost:3000")
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
                .content(objectMapper.writeValueAsString(new RegisterRequest("test@naver.com", "testPassword")));
        //when
        var result = mockMvc.perform(postRequest);
        //then
        result.andExpect(status().isOk());
    }

    @Test
    void failPostRequestWithCors() throws Exception {
        var postRequest = post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ORIGIN, "http://localhost:3001")
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
                .content(objectMapper.writeValueAsString(new RegisterRequest("test@naver.com", "testPassword")));
        //when
        var result = mockMvc.perform(postRequest);
        //then
        result.andExpect(status().isOk());
    }
}

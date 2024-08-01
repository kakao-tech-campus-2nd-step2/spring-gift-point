package gift;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.order.dto.request.OrderRequest;
import gift.wish.dto.request.CreateWishRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AcceptanceTest {

    private static final String ALLOWED_METHOD_NAMES = "GET,POST,PUT,DELETE,PATCH";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void corsGetProduct() throws Exception {
        mockMvc.perform(
                options("/api/products?page=0&size=10&sort=name,asc&categoryId=1")
                    .header(HttpHeaders.ORIGIN, "http://localhost:8080")
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
            )
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ALLOWED_METHOD_NAMES))
            .andDo(print());
    }

    @Test
    void corsGetWishes() throws Exception {
        mockMvc.perform(
                options("/api/wishes")
                    .header(HttpHeaders.ORIGIN, "http://localhost:8080")
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
            )
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ALLOWED_METHOD_NAMES))
            .andDo(print());
    }

    @Test
    void corsPostWishes() throws Exception {
        mockMvc.perform(
                options("/api/wishes")
                    .header(HttpHeaders.ORIGIN, "http://localhost:8080")
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
                    .content(objectMapper.writeValueAsString(new CreateWishRequest(1L)))
            )
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ALLOWED_METHOD_NAMES))
            .andDo(print());
    }

    @Test
    void corsGetOptions() throws Exception {
        mockMvc.perform(
                options("/api/products/1/options")
                    .header(HttpHeaders.ORIGIN, "http://localhost:8080")
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
            )
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ALLOWED_METHOD_NAMES))
            .andDo(print());
    }

    @Test
    void corsPostOrder() throws Exception {
        mockMvc.perform(
                options("/api/orders")
                    .header(HttpHeaders.ORIGIN, "http://localhost:8080")
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
                    .content(objectMapper.writeValueAsString(new OrderRequest(1L, 2, "Please handle this order with care.")))
            )
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ALLOWED_METHOD_NAMES))
            .andDo(print());
    }
}

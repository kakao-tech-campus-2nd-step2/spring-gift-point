package gift;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AcceptanceTest {

    private static final String ALLOWED_METHOD_NAMES = "GET,POST,PUT,DELETE";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void corsProducts() throws Exception {
        mockMvc.perform(
                options("/api/products")
                    .header(HttpHeaders.ORIGIN, "http://localhost:8080")
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "Content-Type, Authorization")
            )
            .andExpect(status().isOk())
            .andExpect(
                header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080"))
            .andExpect(
                header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ALLOWED_METHOD_NAMES))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
                "Authorization, Location"))
            .andDo(print());
    }

    @Test
    void corsWishes() throws Exception {
        mockMvc.perform(
                options("/api/wishes")
                    .header(HttpHeaders.ORIGIN, "http://localhost:8080")
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "Content-Type, Authorization")
            )
            .andExpect(status().isOk())
            .andExpect(
                header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET,POST,DELETE"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization"))
            .andDo(print());
    }

    @Test
    void corsCategories() throws Exception {
        mockMvc.perform(
                options("/api/categories")
                    .header(HttpHeaders.ORIGIN, "http://localhost:8080")
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "Content-Type, Authorization")
            )
            .andExpect(status().isOk())
            .andExpect(
                header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080"))
            .andExpect(
                header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ALLOWED_METHOD_NAMES))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
                "Authorization, Location"))
            .andDo(print());
    }

    @Test
    void corsOrders() throws Exception {
        mockMvc.perform(
                options("/api/orders")
                    .header(HttpHeaders.ORIGIN, "http://localhost:8080")
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "Content-Type, Authorization")
            )
            .andExpect(status().isOk())
            .andExpect(
                header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization"))
            .andDo(print());
    }

    @Test
    void corsMembers() throws Exception {
        mockMvc.perform(
                options("/api/members/register")
                    .header(HttpHeaders.ORIGIN, "http://localhost:8080")
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "Content-Type")
            )
            .andExpect(status().isOk())
            .andExpect(
                header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization"))
            .andDo(print());
    }

    @Test
    void corsOptions() throws Exception {
        mockMvc.perform(
                options("/api/products/1/options")
                    .header(HttpHeaders.ORIGIN, "http://localhost:8080")
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "Content-Type, Authorization")
            )
            .andExpect(status().isOk())
            .andExpect(
                header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080"))
            .andExpect(
                header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ALLOWED_METHOD_NAMES))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
                "Authorization, Location"))
            .andDo(print());
    }
}

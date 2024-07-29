package gift;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.DTO.ProductDTO;
import gift.Model.Category;
import gift.DTO.OrderRequestDTO;
import gift.DTO.OrderResponseDTO;
import gift.Model.Product;
import gift.Service.LoginService;
import gift.Service.OrderService;
import gift.Service.ProductService;
import java.util.ArrayList;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class OrderTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    ProductService productService;
    @Autowired
    LoginService loginService;
    @Test
    void makeResponse(){ // response 확인
        Category category = new Category(1L, "교환권","#6c95d1","https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png","",null);
        ProductDTO product = new ProductDTO(null, "test",1000,"test",category, new ArrayList<>());
        productService.addProduct(product);

        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setOptionId(1L);
        requestDTO.setQuantity(1);
        requestDTO.setMessage("test");

        OrderResponseDTO actual= orderService.makeResponse(requestDTO,product.getId());

        assertAll(
            () -> assertThat(actual.getId()).isEqualTo(product.getId()),
            () -> assertThat(actual.getOptionId()).isEqualTo(requestDTO.getOptionId()),
            () -> assertThat(actual.getQuantity()).isEqualTo(requestDTO.getQuantity()),
            () -> assertThat(actual.getMessage()).isEqualTo(requestDTO.getMessage())
        );

    }

    @Test
    void sendMessage() throws JsonProcessingException {
        var response = loginService.makeResponse("인가코드");
        ResponseEntity<String> actual = orderService.sendMessage("test",loginService.abstractToken(response));

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> responseBody;

        responseBody = objectMapper.readValue(actual.getBody(), Map.class);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseBody.get("result_code")).isEqualTo(0);
    }

}

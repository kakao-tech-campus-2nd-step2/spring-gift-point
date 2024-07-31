package gift.product.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.product.model.Category;
import gift.product.model.Product;
import gift.product.service.ProductService;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ApiProductController.class)
public class ApiProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    public void testShowProductList() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Product product = new Product(1L, "상품", 1000, "image", any(Category.class));
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(product), pageable, 1);

        Mockito.when(productService.getAllProducts(pageable)).thenReturn(productPage);

        ResultActions result = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/products")
                .param("page", "0")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
            .andDo(print());
    }
}

package gift.controller;//package gift.controller;
//
//import gift.dto.ProductDto;
//import gift.service.ProductService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Arrays;
//import java.util.Optional;
//
//import static org.hamcrest.Matchers.hasSize;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(controllers = AdminController.class)
//@ActiveProfiles("test")
//public class AdminControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ProductService productService;
//
//    @Test
//    @DisplayName("상품 전체 조회")
//    void listProducts() throws Exception {
//        ProductDto product1 = new ProductDto(1L, "Product 1", 100, "img1.jpg", 1L);
//        ProductDto product2 = new ProductDto(2L, "Product 2", 200, "img2.jpg", 2L);
//
//        given(productService.findAll()).willReturn(Arrays.asList(product1, product2));
//
//        mockMvc.perform(get("/admin/products"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("list"))
//                .andExpect(model().attributeExists("products"))
//                .andExpect(model().attribute("products", hasSize(2)));
//    }
//
//    @Test
//    @DisplayName("상품 상세정보 조회")
//    void viewProduct() throws Exception {
//        ProductDto product1 = new ProductDto(1L, "Product 1", 100, "img1.jpg", 1L);
//
//        given(productService.findById(1L)).willReturn(Optional.of(product1));
//
//        mockMvc.perform(get("/admin/product/1"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("view"))
//                .andExpect(model().attributeExists("product"))
//                .andExpect(model().attribute("product", product1));
//    }
//
//    @Test
//    @DisplayName("존재하지 않는 상품 조회시 404 발생 테스트")
//    void viewProductNotFound() throws Exception {
//        given(productService.findById(1L)).willReturn(Optional.empty());
//
//        mockMvc.perform(get("/admin/product/1"))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @DisplayName("상품 추가 폼 반환 테스트")
//    void showAddProductForm() throws Exception {
//        mockMvc.perform(get("/admin/product/add"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("add"))
//                .andExpect(model().attributeExists("product"));
//    }
//
//    @Test
//    @DisplayName("상품 추가 기능 테스트")
//    void addProduct() throws Exception {
//        mockMvc.perform(post("/admin/product/add")
//                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                        .param("name", "New Product")
//                        .param("price", "300")
//                        .param("imgUrl", "img3.jpg")
//                        .param("categoryId", "3"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/admin/products"));
//
//        Mockito.verify(productService).save(any(ProductDto.class));
//    }
//
//    @Test
//    @DisplayName("상품 수정 폼 반환 테스트")
//    void showEditProductForm() throws Exception {
//        ProductDto product1 = new ProductDto(1L, "Product 1", 100, "img1.jpg", 1L);
//
//        given(productService.findById(1L)).willReturn(Optional.of(product1));
//
//        mockMvc.perform(get("/admin/product/edit/1"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("edit"))
//                .andExpect(model().attributeExists("product"))
//                .andExpect(model().attribute("product", product1));
//    }
//
//    @Test
//    @DisplayName("상품 수정 기능 테스트")
//    void editProduct() throws Exception {
//        mockMvc.perform(post("/admin/product/edit/1")
//                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                        .param("name", "Updated Product")
//                        .param("price", "400")
//                        .param("imgUrl", "updated_img.jpg")
//                        .param("categoryId", "4"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/admin/products"));
//
//        Mockito.verify(productService).update(eq(1L), any(ProductDto.class));
//    }
//
//    @Test
//    @DisplayName("상품 삭제 테스트")
//    void deleteProduct() throws Exception {
//        mockMvc.perform(post("/admin/product/delete/1"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/admin/products"));
//
//        Mockito.verify(productService).delete(1L);
//    }
//}
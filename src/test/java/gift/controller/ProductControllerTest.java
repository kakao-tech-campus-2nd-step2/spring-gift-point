package gift.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import gift.core.interceptor.AuthenticationInterceptor;
import gift.core.jwt.JwtProvider;
import gift.product.controller.admin.ProductRestController;
import gift.feat.product.contoller.dto.ProductRequestDto;
import gift.product.domain.Product;
import gift.product.service.ProductService;

@WebMvcTest(ProductRestController.class)
public class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;

	@MockBean
	private JwtProvider jwtProvider;

	@MockBean
	private AuthenticationInterceptor authenticationInterceptor;

	@BeforeEach
	public void setup() {
		Mockito.when(authenticationInterceptor.preHandle(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);
		mockMvc = MockMvcBuilders.standaloneSetup(new ProductRestController(productService))
			.addInterceptors(authenticationInterceptor)
			.build();
	}


	@Test
	@DisplayName("상품 등록 테스트")
	public void testRegisterProduct() throws Exception {

		//given
		when(productService.saveProduct(any(ProductRequestDto.class))).thenReturn(1L);

		//when @ then
		mockMvc.perform(post("/api/v1/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"Product Name\",\"price\":1000,\"imageUrl\":\"http://image.url\"}"))
			.andExpect(status().isOk())
			.andExpect(content().string("1"));
	}

	@Test
	@DisplayName("상품 페이징 조회 테스트")
	public void testGetProductsWithPaging() throws Exception {

	}

	@Test
	@DisplayName("상품 조회 테스트")
	public void testGetProduct() throws Exception {
		//given
		Product product = Product.of("Product Name", 1000L, "http://image.url");
		when(productService.getProductById(1L)).thenReturn(product);

		//when @ then
		mockMvc.perform(get("/api/v1/product/1"))
			.andExpect(status().isOk())
			.andExpect(content().json("{\"name\":\"Product Name\",\"price\":1000,\"imageUrl\":\"http://image.url\"}"));
	}

	@Test
	@DisplayName("상품 수정 테스트")
	public void testUpdateProduct() throws Exception {

		//given
		Product product = Product.of("Updated Name", 1500L, "http://newimage.url");
		when(productService.updateProduct(any(Long.class), any(ProductRequestDto.class))).thenReturn(1L);

		//when @ then
		mockMvc.perform(put("/api/v1/product/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"Updated Name\",\"price\":1500,\"imageUrl\":\"http://newimage.url\"}"))
			.andExpect(status().isOk())
			.andExpect(content().string("1"));
	}

	@Test
	@DisplayName("상품 삭제 테스트")
	public void testDeleteProduct() throws Exception {
		//when @ then
		mockMvc.perform(delete("/api/v1/product/1"))
			.andExpect(status().isOk());
	}
}

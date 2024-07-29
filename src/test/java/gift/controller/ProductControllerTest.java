package gift.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.exception.ErrorCode;
import gift.dto.OptionDto;
import gift.dto.ProductDto;
import gift.dto.request.ProductCreateRequest;
import gift.dto.request.ProductUpdateRequest;
import gift.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class ProductControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @DisplayName("[GET] 모든 상품 정보를 조회한다.")
    @Test
    void productList() throws Exception {
        //given
        given(productService.getProducts(any(Pageable.class))).willReturn(List.of());

        //when
        ResultActions result = mvc.perform(get("/api/products"));

        //then
        result
                .andExpect(status().isOk());

        then(productService).should().getProducts(any(Pageable.class));
    }

    @DisplayName("[GET] 하나의 상품 정보를 조회한다.")
    @Test
    void productOne() throws Exception {
        //given
        Long productId = 1L;
        ProductDto product = new ProductDto();

        given(productService.getProduct(productId)).willReturn(product);

        //when
        ResultActions result = mvc.perform(get("/api/products/{productId}", productId));

        //then
        result
                .andExpect(status().isOk());

        then(productService).should().getProduct(productId);
    }

    @DisplayName("[POST] 상품 하나를 추가한다.")
    @Test
    void productAdd() throws Exception {
        //given
        Long categoryId = 1L;
        ProductDto request = new ProductDto("아이스티", 2500, "https://example.com", categoryId, List.of(new OptionDto("옵션", 12L)));

        willDoNothing().given(productService).addProduct(any(ProductDto.class));

        //when
        ResultActions result = mvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result
                .andExpect(status().isCreated());

        then(productService).should().addProduct(any(ProductDto.class));
    }

    @DisplayName("[POST/Exception] 상품 하나를 추가하는데, 상품 이름이 주어지지 않으면 예외를 던진다.")
    @Test
    void productAddWithoutName() throws Exception {
        //given
        String request = "{\"price\": 2500, \"imageUrl\": \"https://example.com\"}";

        //when
        ResultActions result = mvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request));

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.VALIDATION_ERROR.getMessage()));
    }

    @DisplayName("[POST/Exception] 상품 하나를 추가하는데, 상품명이 15자가 넘으면 예외를 던진다.")
    @Test
    void productAddWithNameExceedingMaxLength() throws Exception {
        //given
        Long categoryId = 1L;
        ProductCreateRequest request = new ProductCreateRequest("프리미엄 오가닉 그린티 블렌드", 2500, "https://example.com", categoryId, List.of());

        //when
        ResultActions result = mvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.VALIDATION_ERROR.getMessage()));
    }

    @DisplayName("[POST/Exception] 상품 하나를 추가하는데, 상품명에 허용되지 않는 특수문자가 포함되어 있으면 예외를 던진다.")
    @Test
    void productAddWithInvalidCharactersInName() throws Exception {
        //given
        Long categoryId = 1L;
        ProductCreateRequest request = new ProductCreateRequest("그린티 *", 2500, "https://example.com", categoryId, List.of());

        //when
        ResultActions result = mvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.VALIDATION_ERROR.getMessage()));
    }

    @DisplayName("[POST/Exception] 상품 하나를 추가하는데, 상품명에 '카카오'가 포함되어 있으면 예외를 던진다.")
    @Test
    void productAddWithNameContainingKakao() throws Exception {
        //given
        Long categoryId = 1L;
        ProductCreateRequest request = new ProductCreateRequest("카카오 굿즈", 2500, "https://example.com", categoryId, List.of());

        //when
        ResultActions result = mvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.VALIDATION_ERROR.getMessage()));
    }

    @DisplayName("[POST/Exception] 상품 하나를 추가하는데, 가격 정보가 주어지지 않으면 예외를 던진다.")
    @Test
    void productAddWithoutPrice() throws Exception {
        //given
        String request = "{\"name\": \"아이스티\", \"imageUrl\": \"https://example.com\"}";

        //when
        ResultActions result = mvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request));

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.VALIDATION_ERROR.getMessage()));
    }

    @DisplayName("[PUT] 상품 정보를 수정한다.")
    @Test
    void productEdit() throws Exception {
        //given
        Long productId = 1L;
        Long categoryId = 1L;
        ProductDto request = new ProductDto("아이스티", 2500, "https://example.com", categoryId);

        willDoNothing().given(productService).editProduct(anyLong(), any(ProductDto.class));

        //when
        ResultActions result = mvc.perform(put("/api/products/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result
                .andExpect(status().isOk());

        then(productService).should().editProduct(anyLong(), any(ProductDto.class));
    }


    @DisplayName("[PUT/Exception] 상품 정보를 수정하는데, 상품 이름이 주어지지 않으면 예외를 던진다.")
    @Test
    void productEditWithoutName() throws Exception {
        //given
        String request = "{\"price\": 2500, \"imageUrl\": \"https://example.com\"}";
        Long productId = 1L;

        //when
        ResultActions result = mvc.perform(put("/api/products/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request));

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.VALIDATION_ERROR.getMessage()));
    }

    @DisplayName("[PUT/Exception] 상품 정보를 수정하는데, 상품명이 15자가 넘으면 예외를 던진다.")
    @Test
    void productEditWithNameExceedingMaxLength() throws Exception {
        //given
        Long categoryId = 1L;
        ProductUpdateRequest request = new ProductUpdateRequest("프리미엄 오가닉 그린티 블렌드", 2500, "https://example.com", categoryId);
        Long productId = 1L;

        //when
        ResultActions result = mvc.perform(put("/api/products/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.VALIDATION_ERROR.getMessage()));
    }

    @DisplayName("[PUT/Exception] 상품 정보를 수정하는데, 상품명에 허용되지 않는 특수문자가 포함되어 있으면 예외를 던진다.")
    @Test
    void productEditWithInvalidCharactersInName() throws Exception {
        //given
        Long productId = 1L;
        Long categoryId = 1L;
        ProductUpdateRequest request = new ProductUpdateRequest("그린티 *", 2500, "https://example.com", categoryId);

        //when
        ResultActions result = mvc.perform(put("/api/products/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.VALIDATION_ERROR.getMessage()));
    }

    @DisplayName("[PUT/Exception] 상품 정보를 수정하는데, 상품명에 '카카오'가 포함되어 있으면 예외를 던진다.")
    @Test
    void productEditWithNameContainingKakao() throws Exception {
        //given
        Long productId = 1L;
        Long categoryId = 1L;
        ProductUpdateRequest request = new ProductUpdateRequest("카카오 굿즈", 2500, "https://example.com", categoryId);

        //when
        ResultActions result = mvc.perform(put("/api/products/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.VALIDATION_ERROR.getMessage()));
    }

    @DisplayName("[PUT/Exception] 상품 정보를 수정하는데, 가격 정보가 주어지지 않으면 예외를 던진다.")
    @Test
    void productEditWithoutPrice() throws Exception {
        //given
        String request = "{\"name\": \"아이스티\", \"imageUrl\": \"https://example.com\"}";
        Long productId = 1L;

        //when
        ResultActions result = mvc.perform(put("/api/products/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request));

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.VALIDATION_ERROR.getMessage()));
    }

    @DisplayName("[DELETE] 상품 하나를 삭제한다.")
    @Test
    void productRemove() throws Exception {
        //given
        Long productId = 1L;

        willDoNothing().given(productService).removeProduct(productId);

        //when
        ResultActions result = mvc.perform(delete("/api/products/{productId}", productId));

        //then
        result
                .andExpect(status().isOk());

        then(productService).should().removeProduct(productId);
    }

    @DisplayName("[GET] 상품에 존재하는 모든 옵션을 조회한다.")
    @Test
    void optionList() throws Exception {
        //given
        Long productId = 1L;

        given(productService.getOptions(productId)).willReturn(List.of());

        //when
        ResultActions result = mvc.perform(get("/api/products/{productId}/options", productId));

        //then
        result
                .andExpect(status().isOk());

        then(productService).should().getOptions(productId);
    }

    @DisplayName("[POST] 상품에 옵션 하나를 추가한다.")
    @Test
    void optionAdd() throws Exception {
        //given
        Long productId = 1L;
        gift.dto.OptionDto request = new gift.dto.OptionDto("옵션", 2500L);

        willDoNothing().given(productService).addOption(anyLong(), any(gift.dto.OptionDto.class));

        //when
        ResultActions result = mvc.perform(post("/api/products/{productId}/options", productId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result
                .andExpect(status().isCreated());

        then(productService).should().addOption(anyLong(), any(gift.dto.OptionDto.class));
    }

    @DisplayName("[DELETE] 상품에 존재하는 옵션 하나를 삭제한다.")
    @Test
    void optionRemove() throws Exception {
        //given
        Long productId = 1L;
        Long optionId = 1L;

        willDoNothing().given(productService).removeOption(productId, optionId);

        //when
        ResultActions result = mvc.perform(delete("/api/products/{productId}/options/{optionId}", productId, optionId));

        //then
        result
                .andExpect(status().isOk());

        then(productService).should().removeOption(productId, optionId);
    }

}

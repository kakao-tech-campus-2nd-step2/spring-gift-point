package gift.service;

import gift.entity.Category;
import gift.entity.Product;
import gift.exception.DataNotFoundException;
import gift.repository.ProductRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    Category category = mock(Category.class);
    Product product = new Product("Product", 1000, "imageUrl.url", category);
    Product product1 = new Product("Product1", 10001, "imageUrl.url1", category);

    private final Long fakeProductId = 1L;
    private static final int PAGE_SIZE = 5;


    @Test
    @DisplayName("Save Test")
    void saveProduct() {
        //given
        when(productRepository.save(product)).thenReturn(product);

        //when
        productService.saveProduct(product);

        //then
        verify(productRepository).save(product);
    }

    @Test
    @DisplayName("GetAllProduct Test")
    void getAllProducts() {
        //given
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        productList.add(product1);
        when(productRepository.findAll()).thenReturn(productList);

        //when
        List<Product> actual = productService.getAllProducts();

        //then
        assertAll(
            () -> assertThat(actual.size()).isEqualTo(2),
            () -> assertThat(actual).containsExactly(product, product1)
        );

    }

    @Test
    @DisplayName("Id로 조회: 성공")
    void successGetProductById() {
        //given
        when(productRepository.findById((fakeProductId))).thenReturn(Optional.of(product));

        //when
        Product product = productService.getProductById(fakeProductId);

        //then
        assertAll(
            () -> assertThat(product.getName()).isEqualTo("Product"),
            () -> assertThat(product.getPrice()).isEqualTo(1000),
            () -> assertThat(product.getImageUrl()).isEqualTo("imageUrl.url"),
            () -> assertThat(product.getCategory()).isEqualTo(category)
        );

    }

    @Test
    @DisplayName("Id로 조회: 없는 ProductId일때")
    void failedGetProductById() {
        //given
        when(productRepository.findById((fakeProductId))).thenReturn(Optional.empty());

        //when & then
        assertThrows(DataNotFoundException.class,
            () -> productService.getProductById(fakeProductId));

    }

    @Test
    void updateProduct() {
        //given
        when(productRepository.findById(fakeProductId)).thenReturn(Optional.of(product));

        //when
        productService.updateProduct(product1, fakeProductId);

        //then
        verify(productRepository).save(product);
        assertAll(
            () -> assertEquals("Product1", product.getName()),
            () -> assertEquals(10001, product.getPrice()),
            () -> assertEquals("imageUrl.url1", product.getImageUrl())
        );

    }

    @Test
    void deleteProduct() {
        //given
        when(productRepository.findById(fakeProductId)).thenReturn(Optional.of(product));

        //when
        productService.deleteProduct(fakeProductId);

        //then
        verify(productRepository).deleteById(fakeProductId);
    }

    @Test
    void getProductPage() {
        // given
        int page = 1; // 테스트할 페이지 번호
        List<Product> products = new ArrayList<>();

        products.add(product);
        products.add(product1);

        Page<Product> productPage = new PageImpl<>(products,
            PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Order.asc("id"))), products.size());

        // Mock repository call
        when(productRepository.findAll(
            PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Order.asc("id")))))
            .thenReturn(productPage);

        // when
        Page<Product> actual = productService.getProductPage(page);

        // then
        assertEquals(products.size(), actual.getNumberOfElements());
        assertEquals(page, actual.getNumber());
        verify(productRepository).findAll(
            PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Order.asc("id"))));
    }

}
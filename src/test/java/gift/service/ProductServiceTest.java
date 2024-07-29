package gift.service;

import gift.DTO.Product.ProductRequest;
import gift.DTO.Product.ProductResponse;
import gift.domain.Category;
import gift.domain.Option;
import gift.domain.Product;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private OptionRepository optionRepository;
    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("saveTest")
    void test1() {
        // given
        ArgumentCaptor<String> captor_s = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Product> captor_p = ArgumentCaptor.forClass(Product.class);
        ArgumentCaptor<Option> captor_o = ArgumentCaptor.forClass(Option.class);
        ProductRequest productRequest = new ProductRequest(
                "product", 4500, "url", "신규", "[0] 기본"
        );
        given(categoryRepository.findByName(captor_s.capture())).willAnswer(invocation -> {
            String capturedName = captor_s.getValue();
            return new Category(capturedName);
        });
        given(productRepository.save(captor_p.capture())).willAnswer(invocation -> captor_p.getValue());
        given(optionRepository.save(captor_o.capture())).willAnswer(invocation -> captor_o.getValue());
        // when
        ProductResponse savedProduct = productService.save(productRequest);
        // then
        Assertions.assertThat(savedProduct).isNotNull();
        Assertions.assertThat(savedProduct.getName()).isEqualTo("product");
        Assertions.assertThat(savedProduct.getPrice()).isEqualTo(4500);
        Assertions.assertThat(savedProduct.getImageUrl()).isEqualTo("url");
        Assertions.assertThat(savedProduct.getCategory().getName()).isEqualTo("신규");
        Assertions.assertThat(savedProduct.getOptions().getFirst().getName()).isEqualTo("[0] 기본");
    }

    @Test
    @DisplayName("readAllProductASCTest")
    void test2() {
        // given
        List<Product> products = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Category category = new Category("물품");
            Product product = new Product("product" + i, 4500, "url" + i, category);
            Option option = new Option("[0] 기본", 100L);
            product.addOption(option);
            products.add(product);
        }

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("name"));
        Pageable pageable = PageRequest.of(0, 5, Sort.by(sorts));
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), products.size());
        List<Product> subList = products.subList(start, end);

        Page<Product> page = new PageImpl<>(subList, pageable, products.size());
        given(productRepository.findAll(pageable)).willAnswer(invocation -> page);
        // when
        Page<ProductResponse> pageResult = productService.readAllProductASC(0, 5, "name");
        // then
        Assertions.assertThat(pageResult).isNotNull();
        Assertions.assertThat(pageResult.get().count()).isEqualTo(5);
        List<ProductResponse> content = pageResult.getContent();
        for(int i = 1; i <= 5; i++){
            String name = content.get(i-1).getName();
            Assertions.assertThat(name).isEqualTo("product"+i);
        }
    }

    @Test
    @DisplayName("readAllProductDESCTest")
    void test3() {
        // given
        List<Product> products = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Category category = new Category("물품");
            Product product = new Product("product" + i, 4500, "url" + i, category);
            Option option = new Option("[0] 기본", 100L);
            product.addOption(option);
            products.add(product);
        }

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("name"));
        Pageable pageable = PageRequest.of(0, 5, Sort.by(sorts));
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), products.size());
        List<Product> subList = products.subList(start, end);

        Page<Product> page = new PageImpl<>(subList, pageable, products.size());
        given(productRepository.findAll(pageable)).willAnswer(invocation -> page);
        // when
        Page<ProductResponse> pageResult = productService.readAllProductDESC(0, 5, "name");
        // then
        Assertions.assertThat(pageResult).isNotNull();
        Assertions.assertThat(pageResult.get().count()).isEqualTo(5);
        List<ProductResponse> content = pageResult.getContent();
        for(int i = 5; i >= 1; i--){
            String name = content.get(i-1).getName();
            Assertions.assertThat(name).isEqualTo("product"+i);
        }
    }

    @Test
    @DisplayName(("updateTest"))
    void test4(){
        // given
        ProductRequest productRequest = new ProductRequest(
                "newProduct", 4000, "url", "신규", ""
        );

        Category category = new Category("물품");
        Option option = new Option("[0] 기본", 1L);
        Optional<Product> product = Optional.of(
                new Product("product", 4500, "none", category));
        product.get().addOption(option);

        given(productRepository.findById(any(Long.class))).willAnswer(invocation -> product);

        Category newCategory = new Category("신규");
        given(categoryRepository.findByName(any(String.class))).willAnswer(invocation -> newCategory);

        // when
        productService.updateProduct(productRequest, 1L);

        // then
        Assertions.assertThat(product.get().getName()).isEqualTo("newProduct");
        Assertions.assertThat(product.get().getPrice()).isEqualTo(4000);
        Assertions.assertThat(product.get().getImageUrl()).isEqualTo("url");
        Assertions.assertThat(product.get().getCategory().getName()).isEqualTo("신규");
        Assertions.assertThat(product.get().getOptions().getFirst().getName()).isEqualTo("[0] 기본");
    }

    @Test
    @DisplayName("deleteTest")
    void test5() {
        // when
        productService.deleteProduct(1L);
        // then
        then(productRepository).should(times(1)).deleteById(1L);
    }
}

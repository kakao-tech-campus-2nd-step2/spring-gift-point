package gift;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.Model.Category;
import gift.Model.Product;
import gift.Repository.OptionRepository;
import gift.Repository.ProductRepository;

import gift.Service.ProductService;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
public class ProductServiceTest {


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;
    //데이터 sql로 이미 값이 존재하는 상태이므로 똑같은 값 만들기
    private final Category category = new Category(1L, "교환권","#6c95d1","https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png","",null);

    @DirtiesContext
    @Test
    void findAll(){
        Product expected1 = new Product(1L,"A",1000,"A",category,new ArrayList<>());
        Product expected2 = new Product(2L,"B",2000,"B",category,new ArrayList<>());

        productService.addProduct(expected1);
        productService.addProduct(expected2);

        List<Product> products = productRepository.findAll();

        Product actual1 = products.get(0);
        Product actual2 = products.get(1);
        System.out.println(actual1.getOptions().isEmpty());
        System.out.println(actual1.getOptions().getFirst().getName());
        System.out.println(actual1.getOptions().getFirst().getQuantity());

        assertAll(
            () -> assertThat(actual1.getId()).isNotNull(),
            () -> assertThat(actual1.getName()).isEqualTo(expected1.getName()),
            () -> assertThat(actual1.getPrice()).isEqualTo(expected1.getPrice()),
            () -> assertThat(actual1.getImageUrl()).isEqualTo(expected1.getImageUrl()),

            () -> assertThat(actual2.getId()).isNotNull(),
            () -> assertThat(actual2.getName()).isEqualTo(expected2.getName()),
            () -> assertThat(actual2.getPrice()).isEqualTo(expected2.getPrice()),
            () -> assertThat(actual2.getImageUrl()).isEqualTo(expected2.getImageUrl()),

            //카테고리 테스트 추가
            () -> assertThat(actual1.getCategory().getId()).isEqualTo(category.getId()),
            () -> assertThat(actual1.getCategory().getName()).isEqualTo(category.getName()),
            () -> assertThat(actual1.getCategory().getColor()).isEqualTo(category.getColor()),
            () -> assertThat(actual1.getCategory().getImageUrl()).isEqualTo(category.getImageUrl()),
            () -> assertThat(actual1.getCategory().getDescription()).isEqualTo(category.getDescription()),

            () -> assertThat(actual2.getCategory().getId()).isEqualTo(category.getId()),
            () -> assertThat(actual2.getCategory().getName()).isEqualTo(category.getName()),
            () -> assertThat(actual2.getCategory().getColor()).isEqualTo(category.getColor()),
            () -> assertThat(actual2.getCategory().getImageUrl()).isEqualTo(category.getImageUrl()),
            () -> assertThat(actual2.getCategory().getDescription()).isEqualTo(category.getDescription()),
            // 옵션 테스트
            () -> assertThat(actual1.getOptions()).isNotNull(),
            () -> assertThat(actual2.getOptions()).isNotNull()
        );

    }
    @DirtiesContext
    @Test
    void findProductById(){
        Product expected = new Product(1L,"A",1000,"A",category,new ArrayList<>());
        Product product = productService.addProduct(expected);
        Product actual = productService.getProductById(product.getId());

        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
            () -> assertThat(actual.getPrice()).isEqualTo(expected.getPrice()),
            () -> assertThat(actual.getImageUrl()).isEqualTo(expected.getImageUrl()),

            () -> assertThat(actual.getCategory().getId()).isEqualTo(category.getId()),
            () -> assertThat(actual.getCategory().getName()).isEqualTo(category.getName()),
            () -> assertThat(actual.getCategory().getColor()).isEqualTo(category.getColor()),
            () -> assertThat(actual.getCategory().getImageUrl()).isEqualTo(category.getImageUrl()),
            () -> assertThat(actual.getCategory().getDescription()).isEqualTo(category.getDescription()),
            // 기본 옵션은 product의 이름이랑 같고 수량은 1
            () -> assertThat(actual.getOptions().getFirst().getId()).isEqualTo(1L),
            () -> assertThat(actual.getOptions().getFirst().getName()).isEqualTo(expected.getName()),
            () -> assertThat(actual.getOptions().getFirst().getQuantity()).isEqualTo(1)
        );
    }
    @DirtiesContext
    @Test
    void save(){
        Product expected = new Product(1L,"A",1000,"A",category,new ArrayList<>());
        Product actual = productService.addProduct(expected);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
            () -> assertThat(actual.getPrice()).isEqualTo(expected.getPrice()),
            () -> assertThat(actual.getImageUrl()).isEqualTo(expected.getImageUrl()),

            () -> assertThat(actual.getCategory().getId()).isEqualTo(category.getId()),
            () -> assertThat(actual.getCategory().getName()).isEqualTo(category.getName()),
            () -> assertThat(actual.getCategory().getColor()).isEqualTo(category.getColor()),
            () -> assertThat(actual.getCategory().getImageUrl()).isEqualTo(category.getImageUrl()),
            () -> assertThat(actual.getCategory().getDescription()).isEqualTo(category.getDescription()),

            () -> assertThat(actual.getOptions().getFirst().getId()).isEqualTo(1L),
            () -> assertThat(actual.getOptions().getFirst().getName()).isEqualTo(expected.getName()),
            () -> assertThat(actual.getOptions().getFirst().getQuantity()).isEqualTo(1)
        );
    }
    @DirtiesContext
    @Test
    void deleteById(){
        Product expected = new Product(1L,"A",1000,"A",category,new ArrayList<>());
        Product product = productService.addProduct(expected);
        productService.deleteProduct(product.getId());
        assertAll(
            () -> assertThat(productService.getProductById(product.getId())).isNull()
        );
    }
}

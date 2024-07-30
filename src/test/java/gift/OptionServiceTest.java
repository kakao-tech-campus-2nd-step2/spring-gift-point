package gift;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.DTO.CategoryDTO;
import gift.DTO.OptionDTO;
import gift.DTO.ProductDTO;
import gift.Model.Category;
import gift.Model.Option;
import gift.Model.Product;
import gift.Repository.OptionRepository;
import gift.Service.CategoryService;
import gift.Service.OptionService;

import gift.Service.ProductService;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
public class OptionServiceTest {

    @Autowired
    private OptionService optionService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    private final Category category = new Category(1L, "교환권","#6c95d1","https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png","",null);
    private final CategoryDTO categoryDTO = new CategoryDTO(1L, "교환권","#6c95d1","https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png","",null);
    @DirtiesContext
    @Test
    void getAllOptions() {

        categoryService.addCategory(categoryDTO);

        ProductDTO productDTO = new ProductDTO(1L, "test", 1000, "test", category, new ArrayList<>());
        Product product = new Product(productDTO.getId(), productDTO.getName(), productDTO.getPrice(), productDTO.getImageUrl(), productDTO.getCategory(), productDTO.getOptions());
        productService.addProduct(productDTO);
        // 기본 옵션과 옵션 2개 생성하여 총 3개 생성
        OptionDTO expect2 = new OptionDTO(2L, product, "option1", 100);
        OptionDTO expect3 = new OptionDTO(3L, product, "option2", 200);
        optionService.addOption(expect2,product.getId());
        optionService.addOption(expect3,product.getId());

        List<Option> options = optionService.getAllOptions(product.getId());

        Option actual1 = options.get(0);
        Option actual2 = options.get(1);
        Option actual3 = options.get(2);
        assertAll(

            // 옵션1(기본 옵셥) 테스트
            () -> assertThat(actual1.getName()).isEqualTo("test"),
            () -> assertThat(actual1.getQuantity()).isEqualTo(1),
            // 옵션2 테스트
            () -> assertThat(actual2.getName()).isEqualTo(expect2.getName()),
            () -> assertThat(actual2.getQuantity()).isEqualTo(expect2.getQuantity()),
            // 옵션3 테스트
            () -> assertThat(actual3.getName()).isEqualTo(expect3.getName()),
            () -> assertThat(actual3.getQuantity()).isEqualTo(expect3.getQuantity())

        );
    }

    @DirtiesContext
    @Test
    void addDefaultOption(){
        categoryService.addCategory(categoryDTO);

        ProductDTO product = new ProductDTO(1L, "test", 1000, "test", category, new ArrayList<>());
        productService.addProduct(product);

        Option actual = productService.getProductById(product.getId()).getOptions().getFirst();
        assertAll(
            ()-> assertThat(actual.getId()).isEqualTo(1L),
            ()-> assertThat(actual.getName()).isEqualTo(product.getName()),
            ()-> assertThat(actual.getQuantity()).isEqualTo(1)
        );
    }

    @DirtiesContext
    @Test
    void addNewOption(){
        categoryService.addCategory(categoryDTO);

        ProductDTO productDTO = new ProductDTO(1L, "test", 1000, "test", category, new ArrayList<>());
        Product product = new Product(productDTO.getId(), productDTO.getName(), productDTO.getPrice(), productDTO.getImageUrl(), productDTO.getCategory(), productDTO.getOptions());
        productService.addProduct(productDTO);

        OptionDTO expect = new OptionDTO(2L, product, "option1", 100);
        optionService.addOption(expect,product.getId());
        Option actual = productService.getProductById(product.getId()).getOptions().get(1);
        assertAll(
            ()-> assertThat(actual.getId()).isEqualTo(expect.getId()),
            ()-> assertThat(actual.getName()).isEqualTo(expect.getName()),
            ()-> assertThat(actual.getQuantity()).isEqualTo(expect.getQuantity())
        );
    }
}

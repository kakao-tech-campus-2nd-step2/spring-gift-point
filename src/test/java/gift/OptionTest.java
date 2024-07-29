package gift;

import gift.category.CategoryRequest;
import gift.category.CategoryResponse;
import gift.category.CategoryService;
import gift.option.OptionRequest;
import gift.option.OptionResponse;
import gift.option.OptionService;
import gift.product.ProductRequest;
import gift.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class OptionTest {

    @Autowired
    OptionService optionService;
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;

    @BeforeEach
    @Transactional
    public void setting(){
        CategoryResponse categoryResponse = categoryService.insertNewCategory(new CategoryRequest("카테고리1", "색1", "이미지URL", "설명"));
        OptionRequest option = new OptionRequest("기본 옵션", 200L);
        productService.insertNewProduct(new ProductRequest("상품1", 1000L, "이미지URL2", categoryResponse.id()), option);
    }

    @Test
    @Transactional
    @DisplayName("옵션 저장 테스트 - 한개, 성공")
    public void saveOptionSuccess(){
        OptionRequest option = new OptionRequest("옵션1", 100L);
        OptionResponse optionResponse = optionService.insertProductNewOption(1L, option);
        assertThat(optionService.findByOptionID(optionResponse.id())).isEqualTo(optionResponse);
    }

    @Test
    @Transactional
    @DisplayName("옵션 저장 테스트 - 여러개, 성공")
    public void saveOptionsSuccess(){
        List<OptionRequest> options = new ArrayList<>();
        OptionRequest option1 = new OptionRequest("옵션1", 100L);
        options.add(option1);
        OptionRequest option2 = new OptionRequest("옵션2", 200L);
        options.add(option2);
        List<OptionResponse> responses = optionService.insertProductNewOptions(1L, options);
        assertThat(optionService.findByOptionID(responses.get(0).id())).isEqualTo(responses.get(0));
        assertThat(optionService.findByOptionID(responses.get(1).id())).isEqualTo(responses.get(1));
    }

    @Test
    @Transactional
    @DisplayName("옵션 저장 테스트 - 실패 (이름 중복)")
    public void saveOptionFailedByDuplicatedName(){
        OptionRequest option = new OptionRequest("옵션1", 100L);
        optionService.insertProductNewOption(1L, option);
        OptionRequest option2 = new OptionRequest("옵션1", 200L);
        assertThrows(IllegalArgumentException.class, () -> optionService.insertProductNewOption(1L, option2));
    }

    @Test
    @Transactional
    @DisplayName("옵션 수정 테스트 - 성공")
    public void updateOptionSuccess(){
        OptionRequest option = new OptionRequest("옵션1", 100L);
        OptionResponse optionResponse = optionService.insertProductNewOption(1L, option);
        OptionRequest update = new OptionRequest("옵션2", 200L);
        OptionResponse expected = new OptionResponse(optionResponse.id(), "옵션2", 200L);
        assertThat(optionService.updateOption(1L, optionResponse.id(), update)).isEqualTo(expected);
    }

    @Test
    @Transactional
    @DisplayName("옵션 수정 테스트 - 실패 (이름 중복)")
    public void updateOptionFail(){
        optionService.insertProductNewOption(1L, new OptionRequest("옵션1", 100L));
        OptionRequest option = new OptionRequest("옵션2", 100L);
        OptionResponse optionResponse = optionService.insertProductNewOption(1L, option);

        OptionRequest update = new OptionRequest("옵션1", 200L);
        assertThrows(IllegalArgumentException.class, () -> optionService.updateOption(1L, optionResponse.id(), update));

    }

    @Test
    @Transactional
    @DisplayName("옵션 수량 빼기 테스트 - 성공")
    public void subtractOptionQuantitySuccess(){
        OptionRequest option = new OptionRequest("옵션1", 100L);
        OptionResponse optionResponse = optionService.insertProductNewOption(1L, option);
        optionService.subtractOptionQuantity(optionResponse.id(), 20L);
        assertThat(optionService.findByOptionID(optionResponse.id()).quantity()).isEqualTo(80L);
    }

    @Test
    @Transactional
    @DisplayName("옵션 수량 빼기 테스트 - 실패")
    public void subtractOptionQuantityFail(){
        OptionRequest option = new OptionRequest("옵션1", 100L);
        OptionResponse optionResponse = optionService.insertProductNewOption(1L, option);
        assertThrows(IllegalArgumentException.class, () -> optionService.subtractOptionQuantity(optionResponse.id(), 120L));
    }
}

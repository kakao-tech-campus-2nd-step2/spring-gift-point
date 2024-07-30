package gift.main.service;

import gift.main.Exception.CustomException;
import gift.main.dto.OptionChangeQuantityRequest;
import gift.main.dto.OptionRequest;
import gift.main.entity.Category;
import gift.main.entity.Option;
import gift.main.entity.Product;
import gift.main.entity.User;
import gift.main.repository.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@SpringBootTest
class OptionServiceTest {

    private final ProductService productService;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final WishProductRepository wishProductRepository;
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;
    private final EntityManager entityManager;
    private final OptionService optionService;

    @Autowired
    public OptionServiceTest(
            ProductService productService,
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            WishProductRepository wishProductRepository,
            ProductRepository productRepository,
            OptionRepository optionRepository,
            EntityManager entityManager, OptionService optionService) {

        this.productService = productService;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.wishProductRepository = wishProductRepository;
        this.productRepository = productRepository;
        this.optionRepository = optionRepository;
        this.entityManager = entityManager;
        this.optionService = optionService;
    }

    @BeforeEach
    void setUp() {
        User seller = new User("seller", "seller@", "1234", "ADMIN");
        User saveSeller = userRepository.save(seller);
        Category saveCategory = categoryRepository.findByName("패션").get();

        Product product = new Product("testProduct1", 12000, "Url", saveSeller, saveCategory);
        Product saveProduct = productRepository.save(product);

        List<Option> options = new ArrayList<>();
        options.add(new Option("1번", 100, saveProduct));
        options.add(new Option("2번", 100, saveProduct));
        options.add(new Option("3번", 100, saveProduct));

        options.forEach(option -> optionRepository.save(option));

    }

    @Test
    @Transactional
    @Rollback
    void findOptionAllTest() {
        //given
        //when
        List<?> options = optionService.findAllOption(1L);

        //then
        assertThat(options.size()).isEqualTo(3);
    }

    @Test
    @Transactional
    @Rollback
    void deleteOptionTest() {
        //given
        Product saveProduct = productRepository.findById(1L).get();
        List<Option> options = optionRepository.findAllByProductId(saveProduct.getId()).get();

        //when
        optionService.deleteOption(saveProduct.getId(), options.get(0).getId());
        entityManager.flush();
        entityManager.clear();

        //then
        List<Option> saveOptions = optionRepository.findAllByProductId(saveProduct.getId()).get();
        assertThat(saveOptions.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    @Rollback
    void deleteOptionAllTest() {
        //given\
        Product saveProduct = productRepository.findById(1L).get();
        List<Option> options = optionRepository.findAllByProductId(saveProduct.getId()).get();
        optionService.deleteOption(saveProduct.getId(), options.get(0).getId());
        optionService.deleteOption(saveProduct.getId(), options.get(1).getId());

        //when
        //then
        assertThatThrownBy(() -> optionService.deleteOption(saveProduct.getId(), options.get(2).getId()))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @Transactional
    @Rollback
    void addOptionTest() {
        //given
        Product saveProduct = productRepository.findById(1L).get();
        OptionRequest optionRequest = new OptionRequest("4번", 4);

        //when
        optionService.addOption(saveProduct.getId(), optionRequest);

        //then
        assertThat(optionRepository.findAllByProductId(saveProduct.getId()).get().size())
                .isEqualTo(4);
    }

    /*
     * 중복되는 옵션을 넣었을때 실패하는 메서드
     */
    @Test
    @Transactional
    @Rollback
    void addDuplicateOptionTest() {
        //given
        Product saveProduct = productRepository.findById(1L).get();
        OptionRequest optionRequest = new OptionRequest("3번", 3);

        //when
        //then
        assertThatThrownBy(() -> optionService.addOption(saveProduct.getId(), optionRequest))
                .isInstanceOf(CustomException.class);

    }

    /*
     * 옵션 수정 검증 테스트
     */
    @Test
    @Transactional
    @Rollback
    void updateOptionTest() {
        //given
        Product saveProduct = productRepository.findById(1L).get();
        OptionRequest optionRequest = new OptionRequest("4번", 4);

        //when
        optionService.updateOption(saveProduct.getId(), 1L, optionRequest);

        //then
        assertThat(optionRepository.findById(1L).get().getOptionName())
                .isEqualTo("4번");
    }

    // 중복된 옵션으로 수정할 시 오류 발생
    @Test
    @Transactional
    @Rollback
    void updateDuplicateOptionTest() {
        //given
        Product saveProduct = productRepository.findById(1L).get();
        OptionRequest optionRequest = new OptionRequest("3번", 3);

        //when
        //then
        assertThatThrownBy(() -> optionService.updateOption(saveProduct.getId(), 1L, optionRequest))
                .isInstanceOf(CustomException.class);
    }

    //옵션 수량 변경 테스트 - 정상적으로
    @Test
    @Transactional
    @Rollback
    void removeOptionQuantityTest() {
        //given
        Product saveProduct = productRepository.findById(1L).get();
        Option saveOption = optionRepository.findByProductIdAndOptionName(saveProduct.getId(), "1번").get();
        OptionChangeQuantityRequest optionChangeQuantityRequest = new OptionChangeQuantityRequest(3);

        //when
        optionService.removeOptionQuantity(saveOption.getId(), optionChangeQuantityRequest);

        //then
        assertThat(saveOption.getQuantity()).isEqualTo(97);
    }

    //옵션 수량 변경 테스트 - 삭제시 음수가 되어 불가능해짐.
    @Test
    @Transactional
    @Rollback
    void RemoveInvalidOptionQuantityTest() {
        //given
        Product saveProduct = productRepository.findById(1L).get();
        Option saveOption = optionRepository.findByProductIdAndOptionName(saveProduct.getId(), "1번").get();
        OptionChangeQuantityRequest optionChangeQuantityRequest = new OptionChangeQuantityRequest(300);

        //when
        //thrn
        assertThatThrownBy(() -> optionService.removeOptionQuantity(saveOption.getId(), optionChangeQuantityRequest))
                .isInstanceOf(CustomException.class);
    }

}
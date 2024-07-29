package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

import gift.dto.betweenClient.option.OptionRequestDTO;
import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import gift.exception.BadRequestExceptions.BadRequestException;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

class OptionServiceTest {
    @Mock
    private OptionRepository optionRepository;

    @Mock
    private ProductRepository productRepository;

    OptionService optionService;

    private Product product1;
    private Option option1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        optionService = new OptionService(optionRepository, productRepository);

        optionService = spy(optionService);

        product1 = new Product("커피", 10000,
                "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
                new Category("테스트1", "#000001", "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", "테스트 1")
                );
        option1 = new Option(product1, "옵션1", 100);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void getOneProductIdAllOptions() {
        given(optionService.getOneProductIdAllOptions(any())).willReturn(new ArrayList<>());
        assertThat(optionService.getOneProductIdAllOptions(any())).isNotNull();
    }

    @Test
    void addOption() {
        given(optionRepository.save(any())).willReturn(option1);
        given(productRepository.findById(any())).willReturn(Optional.of(product1));
        doNothing().when(optionService).checkUniqueOptionName(any(), any());

        assertThatNoException().isThrownBy(() -> optionService.addOption(1L,
                new OptionRequestDTO(option1.getName(), option1.getQuantity())));
    }

    @Test
    void duplicatedAddOptionTest(){
        given(optionRepository.save(any())).willReturn(option1);
        given(productRepository.findById(any())).willReturn(Optional.of(product1));
        doThrow(new DataIntegrityViolationException("테스트")).when(optionService)
                .checkUniqueOptionName(any(), any());

        assertThrows(BadRequestException.class, () -> optionService.addOption(1L,
                new OptionRequestDTO(option1.getName(), option1.getQuantity())));
    }

    @Test
    void updateOption() {
        given(productRepository.findById(any())).willReturn(Optional.of(product1));
        given(optionRepository.findById((Long) any())).willReturn(Optional.of(option1));
        doNothing().when(optionService).checkUniqueOptionName(any(), any());

        assertThatNoException().isThrownBy(() -> optionService.updateOption(1L, 1L,
                new OptionRequestDTO(option1.getName(), option1.getQuantity())));
    }

    @Test
    void duplicateUpdateOptionTest(){
        given(productRepository.findById(any())).willReturn(Optional.of(product1));
        given(optionRepository.findById((Long) any())).willReturn(Optional.of(option1));
        doThrow(new DataIntegrityViolationException("테스트")).when(optionService)
                .checkUniqueOptionName(any(), any());

        assertThrows(BadRequestException.class, () -> optionService.updateOption(1L, 1L,
                new OptionRequestDTO(option1.getName(), option1.getQuantity())));
    }

    @Test
    void deleteOption() {
        given(optionRepository.findByIdAndProductId(any(), any())).willReturn(Optional.of(option1));
        given(optionRepository.countByProduct(any())).willReturn(2);

        doNothing().when(optionRepository).delete(any());
        assertThatNoException().isThrownBy(() -> optionService.deleteOption(1L, 1L));
    }

    @Test
    void deleteButNotFound(){
        given(optionRepository.findByIdAndProductId(any(), any())).willReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> optionService.deleteOption(1L, 1L));
    }

    @Test
    void deleteButProhibitedOrphanOption(){
        given(optionRepository.findByIdAndProductId(any(), any())).willReturn(Optional.of(option1));
        given(optionRepository.countByProduct(any())).willReturn(1);

        assertThrows(BadRequestException.class, () -> optionService.deleteOption(1L, 1L));
    }

    @Test
    void subtractOptionQuantity(){
        given(optionRepository.findById((Long) any())).willReturn(Optional.of(option1));
        assertThatNoException().isThrownBy(() -> optionService.subtractOptionQuantity(1L, 50));
        assertThrows(BadRequestException.class, () -> optionService.subtractOptionQuantity(1L, 100));
        assertThrows(BadRequestException.class, () -> optionService.subtractOptionQuantity(1L, 0));
    }
}
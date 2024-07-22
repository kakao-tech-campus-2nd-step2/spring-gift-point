package gift.product;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gift.category.model.dto.Category;
import gift.product.exception.ProductCustomException.NotEnoughStockException;
import gift.product.exception.ProductCustomException.ProductMustHaveOptionException;
import gift.product.model.OptionRepository;
import gift.product.model.dto.option.CreateOptionRequest;
import gift.product.model.dto.option.Option;
import gift.product.model.dto.option.UpdateOptionRequest;
import gift.product.model.dto.product.Product;
import gift.product.service.OptionService;
import gift.user.model.dto.AppUser;
import gift.user.model.dto.Role;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class OptionServiceTest {

    @Mock
    private OptionRepository optionRepository;

    @InjectMocks
    private OptionService optionService;

    private Product product;
    private Option option;
    private CreateOptionRequest createRequest;
    private UpdateOptionRequest updateRequest;

    @BeforeEach
    public void setUp() {
        Category defaultCategory = new Category("기본", "기본 카테고리");
        AppUser defaultSeller = new AppUser("aabb@kakao.com", "1234", Role.USER, "aaaa");
        product = new Product("test", 100, "image", defaultSeller, defaultCategory);
        product.setId(5L);
        option = new Option("option", 10, 300, product);
        createRequest = new CreateOptionRequest("option", 10, 300);
        updateRequest = new UpdateOptionRequest("option2", 10, 300);
    }

    @Test
    @DisplayName("옵션을 추가할 수 있다")
    public void addOption_ShouldAddOption() {// when
        optionService.addOption(product, createRequest);

        // then
        verify(optionRepository, times(1)).save(any(Option.class));
    }

    @Test
    @DisplayName("수정 요청 시 옵션이 존재할 때 옵션을 수정할 수 있다")
    public void updateOption_ShouldUpdateOption_WhenProductExists() {
        // given
        when(optionRepository.findById(option.getId())).thenReturn(Optional.of(option));

        // when, then
        assertDoesNotThrow(() -> optionService.updateOption(product, option.getId(), updateRequest));
        verify(optionRepository, times(1)).save(any(Option.class));
        assertEquals(updateRequest.name(), option.getName());
    }

    @Test
    @DisplayName("수정 요청 시 옵션이 존재하지 않으면 EntityNotFoundException을 던진다.")
    public void updateOption_ShouldThrowEntityNotFoundException_WhenOptionNotExists() {
        // given
        when(optionRepository.findById(option.getId())).thenReturn(Optional.empty());

        // when,then
        assertThrows(EntityNotFoundException.class,
                () -> optionService.updateOption(product, option.getId(), updateRequest));
        verify(optionRepository, times(0)).save(any(Option.class));
    }

    @Test
    @DisplayName("삭제 요청 시 2개 이상의 옵션이 존재할 때 옵션을 삭제할 수 있다.")
    public void deleteOption_ShouldDeleteOption_WhenProductExists() {
        // given
        when(optionRepository.findById(option.getId())).thenReturn(Optional.of(option));
        when(optionRepository.countByProductId(product.getId())).thenReturn(2);

        // when
        optionService.deleteOption(product, option.getId());

        // then
        verify(optionRepository, times(1)).delete(any(Option.class));
    }

    @Test
    @DisplayName("삭제 요청 시 1개의 옵션이 존재할 때 옵션을 삭제할 수 없다.")
    public void deleteOption_ShouldThrowProductMustHaveOptionExceptiong_WhenProductExists() {
        // given
        when(optionRepository.findById(option.getId())).thenReturn(Optional.of(option));
        when(optionRepository.countByProductId(product.getId())).thenReturn(1);

        // when, then
        assertThrows(ProductMustHaveOptionException.class, () -> optionService.deleteOption(product, option.getId()));
        verify(optionRepository, times(0)).save(any(Option.class));
    }

    @Test
    @DisplayName("삭제 요청 시 옵션이 존재하지 않으면 EntityNotFoundException을 던진다.")
    public void deleteOption_ShouldThrowEntityNotFoundException_WhenProductExists() {
        // given
        when(optionRepository.findById(option.getId())).thenReturn(Optional.empty());

        // when,then
        assertThrows(EntityNotFoundException.class, () -> optionService.deleteOption(product, option.getId()));
        verify(optionRepository, times(0)).save(any(Option.class));
    }

    @Test
    @DisplayName("옵션의 수량이 충분히 남아있다면 옵션의 수량을 감소시킨다")
    void subtractOptionQuantity_ShouldSubtractQuantity_WhenEnoughQuantity() {
        //given
        int oldQuantity = option.getQuantity();
        int requestedQuantity = 5;
        when(optionRepository.findById(option.getId())).thenReturn(Optional.of(option));

        //when
        optionService.subtractOptionQuantity(option.getId(), requestedQuantity);

        //then
        assertEquals(oldQuantity - requestedQuantity, option.getQuantity());
        verify(optionRepository, times(1)).save(any(Option.class));
    }

    @Test
    @DisplayName("옵션의 수량이 충분히 남아있지 않다면 오류를 발생시킨다")
    void subtractOptionQuantity_ShouldThrowIllegalArgumentException_WhenEnoughQuantity() {
        //given
        int oldQuantity = option.getQuantity();
        int requestedQuantity = oldQuantity + 1;
        when(optionRepository.findById(option.getId())).thenReturn(Optional.of(option));

        //when, then
        assertThrows(NotEnoughStockException.class,
                () -> optionService.subtractOptionQuantity(option.getId(), requestedQuantity));
        verify(optionRepository, times(0)).save(any(Option.class));
    }
}

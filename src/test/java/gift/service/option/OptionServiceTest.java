package gift.service.option;

import gift.dto.option.OptionRequest;
import gift.model.category.Category;
import gift.model.gift.Gift;
import gift.model.option.Option;
import gift.repository.gift.GiftRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OptionServiceTest {

    @Mock
    private GiftRepository giftRepository;

    @InjectMocks
    private OptionService optionService;


    private Gift gift;

    @BeforeEach
    void setUp() {
        Category category = new Category(10L, "test", "test", "test", "test");
        Option option1 = new Option("testOption", 10);
        List<Option> options = Arrays.asList(option1);

        gift = new Gift("Test Gift", 100, "test.jpg", category, options);
    }

    @Test
    @DisplayName("중복값을 넣었을때 에러가 잘 뜨는지 테스트")
    void testAddOptionWithDuplicateName() {
        // given
        OptionRequest.Create duplicateOptionRequest = new OptionRequest.Create("testOption", 10);
        when(giftRepository.findById(gift.getId())).thenReturn(Optional.of(gift));

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            optionService.addOptionToGift(gift.getId(), duplicateOptionRequest);
        });
    }


}
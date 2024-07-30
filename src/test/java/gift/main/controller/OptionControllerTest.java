package gift.main.controller;

import gift.main.dto.OptionRequest;
import gift.main.repository.OptionRepository;
import gift.main.service.OptionService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
class OptionControllerTest {

    private final OptionController optionController;
    private final OptionService optionService;
    private final OptionRepository optionRepository;

    @Autowired
    public OptionControllerTest(OptionController optionController,
                                OptionService optionService,
                                OptionRepository optionRepository) {
        this.optionController = optionController;
        this.optionService = optionService;
        this.optionRepository = optionRepository;
    }

    @Test
    @Transactional
    void sendInvalidOptionRequestTest() {
        OptionRequest invalidOption = new OptionRequest("#$%^&**&^%$WQ#$%^", 12);

        assertThatThrownBy(() -> optionController.addOption(1L, invalidOption))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
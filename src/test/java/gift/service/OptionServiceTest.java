package gift.service;

import gift.dto.OptionRequestDto;
import gift.dto.OptionResponseDto;
import gift.repository.OptionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OptionServiceTest {

    @Autowired
    private OptionService optionService;

    @Autowired
    private OptionRepository optionRepository;

    @AfterEach
    public void tearDown() {
        optionRepository.deleteAll();
    }

    @Test
    @Rollback
    public void 옵션_추가_성공() {
        OptionRequestDto requestDto = new OptionRequestDto("사이즈 S");
        OptionResponseDto createdOption = optionService.addOption(requestDto);

        assertNotNull(createdOption);
        assertNotNull(createdOption.getId());
        assertEquals("사이즈 S", createdOption.getName());
    }

    @Test
    @Rollback
    public void 옵션_수정_성공() {
        OptionRequestDto requestDto = new OptionRequestDto("사이즈 S");
        OptionResponseDto createdOption = optionService.addOption(requestDto);
        Long optionId = createdOption.getId();

        OptionRequestDto updateDto = new OptionRequestDto("사이즈 M");
        OptionResponseDto updatedOption = optionService.updateOption(optionId, updateDto);

        assertNotNull(updatedOption);
        assertEquals(optionId, updatedOption.getId());
        assertEquals("사이즈 M", updatedOption.getName());
    }

    @Test
    @Rollback
    public void 옵션_삭제_성공() {
        OptionRequestDto requestDto = new OptionRequestDto("사이즈 S");
        OptionResponseDto createdOption = optionService.addOption(requestDto);
        Long optionId = createdOption.getId();

        optionService.deleteOption(optionId);

        assertFalse(optionRepository.findById(optionId).isPresent());
    }

    @Test
    @Rollback
    public void 모든_옵션_조회_성공() {
        OptionRequestDto requestDto1 = new OptionRequestDto("사이즈 S");
        OptionRequestDto requestDto2 = new OptionRequestDto("사이즈 M");
        optionService.addOption(requestDto1);
        optionService.addOption(requestDto2);

        List<OptionResponseDto> options = optionService.getAllOptions();

        assertEquals(2, options.size());
    }
}

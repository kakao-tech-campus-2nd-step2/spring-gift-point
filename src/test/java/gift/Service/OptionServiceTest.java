package gift.Service;

import gift.DTO.OptionDTO;
import gift.Entity.OptionEntity;
import gift.Entity.ProductEntity;
import gift.Repository.OptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OptionServiceTest {

    @InjectMocks
    private OptionService optionService;

    @Mock
    private OptionRepository optionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOption() {
        OptionDTO optionDTO = createSampleOptionDTO();
        OptionEntity optionEntity = createSampleOptionEntity();
        when(optionRepository.save(any(OptionEntity.class))).thenReturn(optionEntity);

        OptionDTO createdOption = optionService.createOption(optionDTO);

        assertOption(optionDTO, createdOption);
    }

    @Test
    void testGetOptionById() {
        OptionEntity optionEntity = createSampleOptionEntity();
        when(optionRepository.findById(1L)).thenReturn(Optional.of(optionEntity));

        OptionDTO optionDTO = optionService.getOptionById(1L);

        assertOption(optionEntity, optionDTO);
    }

    @Test
    void testGetAllOptions() {
        List<OptionEntity> optionEntities = new ArrayList<>();
        optionEntities.add(createSampleOptionEntity());
        when(optionRepository.findAll()).thenReturn(optionEntities);

        List<OptionDTO> optionDTOs = optionService.getAllOptions();

        assertEquals(optionEntities.size(), optionDTOs.size());
    }

    @Test
    void testUpdateOption() {
        OptionDTO optionDTO = new OptionDTO(1L, "Option1", 10L, 1L);
        OptionEntity optionEntity = createSampleOptionEntity();
        when(optionRepository.findById(1L)).thenReturn(Optional.of(optionEntity));
        when(optionRepository.save(any(OptionEntity.class))).thenReturn(optionEntity);

        OptionDTO updatedOption = optionService.updateOption(1L, optionDTO);

        assertOption(optionDTO, updatedOption);
    }

    @Test
    void testDeleteOption() {
        doNothing().when(optionRepository).deleteById(1L);

        optionService.deleteOption(1L);

        verify(optionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testValidateOptionNameUniqueness() {
        List<OptionEntity> optionEntities = new ArrayList<>();
        optionEntities.add(createSampleOptionEntity());
        when(optionRepository.findByProductId(1L)).thenReturn(optionEntities);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            optionService.createOption(createSampleOptionDTO());
        });

        assertEquals("동일한 상품 내에서 옵션 이름이 중복될 수 없습니다.", exception.getMessage());
    }

    private void assertOption(OptionDTO expected, OptionDTO actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getQuantity(), actual.getQuantity());
    }

    private void assertOption(OptionEntity expected, OptionDTO actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getQuantity(), actual.getQuantity());
    }

    private OptionEntity createSampleOptionEntity() {
        return new OptionEntity("Option1", 10L, new ProductEntity());
    }

    private OptionDTO createSampleOptionDTO() {
        return new OptionDTO(1L, "Option1", 10L, 1L);
    }


}

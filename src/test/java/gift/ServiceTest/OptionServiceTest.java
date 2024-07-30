package gift.ServiceTest;

import gift.Entity.Option;
import gift.Mapper.Mapper;
import gift.Model.OptionDto;
import gift.Model.OrderRequestDto;
import gift.Repository.OptionJpaRepository;
import gift.Service.OptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OptionServiceTest {

    private OptionJpaRepository optionJpaRepository;
    private Mapper mapper;
    private OptionService optionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        optionJpaRepository = mock(OptionJpaRepository.class);
        mapper = mock(Mapper.class);
        optionService = new OptionService(optionJpaRepository, mapper);
    }

    @Test
    public void testGetAllOptionsByProductId() {
        // given
        long productId = 1L;
        when(optionJpaRepository.findAllByProductId(productId)).thenReturn(Collections.emptyList());
        when(mapper.optionToDto(any(Option.class))).thenReturn(new OptionDto());

        // when
        List<OptionDto> optionDto = optionService.getAllOptionsByProductId(productId);

        // then
        verify(optionJpaRepository, times(1)).findAllByProductId(productId);
        assertNotNull(optionDto);
    }

    @Test
    public void testAddOption() {
        // given
        OptionDto optionDto = new OptionDto(1L, 1L, "Option1", 1000, 1);
        Option option = new Option(1L, null, "Option1", 1000, 1);
        when(mapper.optionDtoToEntity(any(OptionDto.class))).thenReturn(option);
        when(optionJpaRepository.save(any(Option.class))).thenReturn(option);

        // when
        optionService.addOption(optionDto);

        // then
        verify(optionJpaRepository, times(1)).save(any(Option.class));
    }

    @Test
    public void testUpdateOption() {
        // given
        OptionDto optionDto = new OptionDto(1L, 1L, "Option1", 1000, 1);
        Option option = new Option(1L, null, "Option1", 1000, 1);
        when(mapper.optionDtoToEntity(any(OptionDto.class))).thenReturn(option);
        when(optionJpaRepository.save(any(Option.class))).thenReturn(option);

        // when
        optionService.updateOption(optionDto);

        // then
        verify(optionJpaRepository, times(1)).save(any(Option.class));
    }

    @Test
    public void testDeleteOption() {
        // given
        long optionId = 1L;

        // when
        optionService.deleteOption(optionId);

        // then
        verify(optionJpaRepository, times(1)).deleteById(optionId);
    }

    @Test
    public void testSubtractOption() {
        //given
        OptionDto optionDto = new OptionDto(1L, 1L, "Option1", 1000, 5);
        Option option = new Option(1L, null, "Option1", 1000, 10);

        OrderRequestDto orderRequestDto = new OrderRequestDto(1L, 1L, 5, 1L);
        when(optionJpaRepository.findById(orderRequestDto.getOptionId())).thenReturn(java.util.Optional.of(option));
        when(optionJpaRepository.save(any(Option.class))).thenReturn(option);

        //when
        optionService.subtractOption(orderRequestDto);

        //then
        verify(optionJpaRepository, times(1)).findById(optionDto.getId());
        verify(optionJpaRepository, times(1)).save(any(Option.class));

    }

    @Test
    public void testSubtractOptionWhenQuantityIsNegative() {
        //given
        OrderRequestDto orderRequestDto = new OrderRequestDto(1L, 1L, -5, 1L);

        //when
        optionService.subtractOption(orderRequestDto);

        //then
        verify(optionJpaRepository, never()).findById(orderRequestDto.getOptionId());
        verify(optionJpaRepository, never()).save(any(Option.class));
    }
}

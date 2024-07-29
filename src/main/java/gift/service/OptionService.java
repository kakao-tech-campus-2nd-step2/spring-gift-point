package gift.service;

import gift.dto.OptionRequestDto;
import gift.dto.OptionResponseDto;
import gift.entity.Option;
import gift.entity.OptionName;
import gift.exception.BusinessException;
import gift.exception.ErrorCode;
import gift.mapper.OptionMapper;
import gift.repository.OptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OptionService {
    private final OptionRepository optionRepository;

    public OptionService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    public OptionResponseDto addOption(OptionRequestDto optionRequestDto) {
        Option option = new Option(new OptionName(optionRequestDto.getName()));
        Option createdOption = optionRepository.save(option);
        return OptionMapper.toOptionResponseDto(createdOption);
    }

    public OptionResponseDto updateOption(Long id, OptionRequestDto optionRequestDto) {
        Option existingOption = getOptionEntityById(id);
        existingOption.update(new OptionName(optionRequestDto.getName()));
        optionRepository.save(existingOption);
        return OptionMapper.toOptionResponseDto(existingOption);
    }

    public List<OptionResponseDto> getAllOptions() {
        List<Option> options = optionRepository.findAll();
        return options.stream()
                .map(OptionMapper::toOptionResponseDto)
                .collect(Collectors.toList());
    }

    public OptionResponseDto getOptionById(Long id) {
        Option option = getOptionEntityById(id);
        return OptionMapper.toOptionResponseDto(option);
    }

    public void deleteOption(Long id) {
        Option option = getOptionEntityById(id);
        optionRepository.delete(option);
    }

    private Option getOptionEntityById(Long id) {
        return optionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.OPTION_NOT_FOUND, "ID: " + id));
    }
}

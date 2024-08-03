package gift.option.service;

import gift.option.dto.OptionRequestDto;
import gift.option.dto.OptionResponseDto;
import gift.option.entity.Option;
import gift.option.repository.OptionRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptionService {

    private final OptionRepository optionRepository;

    @Autowired
    public OptionService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    // 여기에서는 종속된 옵션을 만들지만, 실제로 제품에 추가하지는 않습니다. 제품에 추가하는 과정은 Product에서 시행합니다.
    @Transactional
    public void insertOption(OptionRequestDto optionRequestDto, long productId) {
        var option = new Option(optionRequestDto.name(), optionRequestDto.quantity(),
            productId);

        optionRepository.save(option);
    }

    // 전체 옵션을 조회하는 메서드 (관리자 권한 필요)
    @Transactional(readOnly = true)
    public List<OptionResponseDto> selectOptions() {
        var options = optionRepository.findAll();

        return options.stream().map(OptionResponseDto::fromOption).toList();
    }

    // 하나의 옵션을 조회하는 메서드 (관리자 권한 필요). Product 추가 시에 사용 가능.
    @Transactional(readOnly = true)
    public OptionResponseDto selectOption(long optionId) {
        var option = optionRepository.findById(optionId)
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 옵션입니다."));

        return OptionResponseDto.fromOption(option);
    }
}

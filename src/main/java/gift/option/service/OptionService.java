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

    // 종속된 옵션을 만들면서 제품에 추가합니다. (로직 변경). productService에서만 호출 가능
    @Transactional
    public Option insertOption(OptionRequestDto optionRequestDto, long productId) {
        var option = new Option(optionRequestDto.name(), optionRequestDto.quantity(),
            productId);

        if (optionRepository.existsByNameAndProductId(option.getName(), productId)) {
            throw new IllegalArgumentException("옵션명이 중복됩니다.");
        }

        return optionRepository.save(option);
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

    // 제품에 종속된 옵션들을 조회하는 메서드
    @Transactional(readOnly = true)
    public List<OptionResponseDto> selectProductOptions(long productId) {
        var options = optionRepository.findByProductId(productId);
        return options.stream().map(OptionResponseDto::fromOption).toList();
    }
}

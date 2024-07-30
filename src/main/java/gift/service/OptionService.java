package gift.service;

import gift.dto.OptionRequestDto;
import gift.dto.OptionResponseDto;
import gift.entity.Option;
import gift.entity.Product;
import gift.exception.BusinessException;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptionService {

    private final OptionRepository optionRepository;

    private final ProductRepository productRepository;

    @Autowired
    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    public List<OptionResponseDto> getAllOptions() {
        List<Option> options = optionRepository.findAll();

        return options.stream()
            .map(OptionResponseDto::new)
            .collect(Collectors.toList());
    }

    public OptionResponseDto getOptionById(Long optionId) {
        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new BusinessException("해당 id에 대한 옵션이 없습니다."));
        return new OptionResponseDto(option);
    }

    public List<OptionResponseDto> getOptionsByProductId(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new BusinessException("해당 id의 상품이 존재하지 않습니다."));

        return product.getOptions().stream().map(OptionResponseDto::new)
            .collect(Collectors.toList());
    }

    public void addOptionToProduct(Long productId, OptionRequestDto request) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new BusinessException("해당 id의 상품이 존재하지 않습니다."));

        if (optionRepository.existsByNameAndProductId(request.getName(), productId)) {
            throw new BusinessException("동일한 상품 내의 옵션 이름은 중복될 수 없습니다.");
        }

        var option = new Option(request.getName(), request.getQuantity(), product);
        optionRepository.save(option);
    }

    @Transactional
    public void subtractOptionQuantity(Long optionId, Long requestQuantity) {
        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new BusinessException("해당 id에 대한 옵션이 없습니다."));
        Long currentQuantity = option.getQuantity();

        if (currentQuantity - requestQuantity < 0) {
            throw new BusinessException("수량은 0보다 작을 수 없습니다.");
        }

        option.subtract(requestQuantity);
    }

    public void deleteOption(Long productId, Long optionId) {
        Option option = optionRepository.findById(optionId).orElseThrow(() -> new BusinessException("해당 id에 대한 옵션이 없습니다."));
        optionRepository.delete(option);
    }
}
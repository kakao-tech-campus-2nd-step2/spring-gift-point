package gift.option.service;

import gift.option.domain.Option;
import gift.option.dto.OptionListResponseDto;
import gift.option.dto.OptionResponseDto;
import gift.option.dto.OptionServiceDto;
import gift.option.exception.DuplicateOptionNameException;
import gift.option.exception.OptionNotFoundException;
import gift.option.repository.OptionRepository;
import gift.product.domain.Product;
import gift.product.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class OptionService {
    private final OptionRepository optionRepository;
    private final ProductService productService;

    public OptionService(OptionRepository optionRepository, ProductService productService) {
        this.optionRepository = optionRepository;
        this.productService = productService;
    }

    public OptionListResponseDto getAllOptions() {
        return OptionListResponseDto.optionListToOptionListResponseDto(optionRepository.findAll());
    }

    public OptionListResponseDto getOptionsByProductId(Long productId) {
        return OptionListResponseDto.optionListToOptionListResponseDto(optionRepository.findByProductId(productId));
    }

    public OptionResponseDto getOptionResponseDtoById(Long id) {
        return OptionResponseDto.optionToOptionResponseDto(getOptionById(id));
    }

    public Option getOptionById(Long id) {
        return optionRepository.findById(id)
                .orElseThrow(OptionNotFoundException::new);
    }

    public Option createOption(OptionServiceDto optionServiceDto) {
        validateNameUnique(optionServiceDto);
        Product product = productService.getProductById(optionServiceDto.productId());
        return optionRepository.save(optionServiceDto.toOption(product));
    }

    public Option updateOption(OptionServiceDto optionServiceDto) {
        validateOptionExists(optionServiceDto.id());
        validateNameUnique(optionServiceDto);
        Product product = productService.getProductById(optionServiceDto.productId());
        return optionRepository.save(optionServiceDto.toOption(product));
    }

    public void deleteOption(Long id) {
        validateOptionExists(id);
        optionRepository.deleteById(id);
    }

/*    @Transactional
    public void orderOption(OrderRequestDto orderRequestDto) {
        Option option = optionRepository.findById(orderRequestDto.optionId())
                .orElseThrow(OptionNotFoundException::new);
        option.subtract(orderRequestDto.count());
        optionRepository.save(option);
        sendMessage(orderRequestDto.message());
    }*/

    private void validateOptionExists(Long id) {
        if (!optionRepository.existsById(id)) {
            throw new OptionNotFoundException();
        }
    }

    private void validateNameUnique(OptionServiceDto optionServiceDto) {
        if (optionRepository.existsByName(optionServiceDto.name())) {
            throw new DuplicateOptionNameException();
        }
    }

    private void sendMessage(String message) {
    }
}

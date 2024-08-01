package gift.service.option;

import gift.domain.option.Option;
import gift.domain.option.OptionRepository;
import gift.domain.option.Options;
import gift.domain.product.Product;
import gift.domain.product.ProductRepository;
import gift.mapper.OptionMappper;
import gift.mapper.ProductMapper;
import gift.web.dto.OptionDto;
import gift.web.dto.ProductDto;
import gift.web.exception.OptionNotFoundException;
import gift.web.exception.ProductNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptionService {

    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;
    private final OptionMappper optionMapper;
    private final ProductMapper productMapper;

    public OptionService(OptionRepository optionRepository, OptionMappper optionMapper,
        ProductRepository productRepository, ProductMapper productMapper) {
        this.optionRepository = optionRepository;
        this.optionMapper = optionMapper;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Transactional
    public OptionDto createOption(Long productId, OptionDto optionDto) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException("제품이 없슴다."));

        Option option = new Option(optionDto.name(), optionDto.quantity(), product);

        final Options options = new Options(optionRepository.findAllByProductId(productId));
        options.validate(option);

        optionRepository.save(optionMapper.toEntity(optionDto, product));
        return optionMapper.toDto(option);
    }

    public Option getOptionEntityByOptionId(Long optionId) {
        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new OptionNotFoundException("옵션이 없슴다."));

        return option;
    }

    public ProductDto getProduct(Long optionId) {
        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new OptionNotFoundException("옵션이 없슴다."));

        return productMapper.toDto(option.getProduct());
    }

    public List<OptionDto> getOptionsByProductId(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException("제품이 없슴다."));

        return optionRepository.findAllByProductId(productId)
            .stream()
            .map(optionMapper::toDto)
            .toList();
    }

    @Transactional
    public OptionDto updateOption(Long optionId, Long productId, OptionDto optionDto) {
        Option option = optionRepository.findByIdAndProductId(optionId, productId)
            .orElseThrow(() -> new OptionNotFoundException("옵션이 없슴다."));

        option.updateOption(optionDto.name(), optionDto.quantity());

        return optionMapper.toDto(option);
    }

    @Transactional
    public OptionDto subtractOptionQuantity(Long optionId, Long productId, Long quantity) {
        Option option = optionRepository.findByIdAndProductId(optionId, productId)
            .orElseThrow(() -> new OptionNotFoundException("옵션이 없슴다."));

        option.subtractQuantity(quantity);

        return optionMapper.toDto(option);
    }

    public void deleteOption(Long optionId, Long productId) {
        Option option = optionRepository.findByIdAndProductId(optionId, productId)
            .orElseThrow(() -> new OptionNotFoundException("옵션이 없슴다."));

        optionRepository.delete(option);
    }
}

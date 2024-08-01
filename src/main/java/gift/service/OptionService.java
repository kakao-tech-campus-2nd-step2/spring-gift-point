package gift.service;

import gift.dto.optionDto.OptionDto;
import gift.dto.optionDto.OptionMapper;
import gift.dto.optionDto.OptionResponseDto;
import gift.exception.ValueNotFoundException;
import gift.model.product.Option;
import gift.model.product.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OptionService {
    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;
    private final OptionMapper optionMapper;

    public OptionService(OptionRepository optionRepository,ProductRepository productRepository, OptionMapper optionMapper){
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
        this.optionMapper = optionMapper;
    }

    public List<OptionResponseDto> getAllOptionsById(Long productId){
        List<Option> options = optionRepository.findByProductId(productId);
        return options.stream()
                .map(optionMapper::tooOptionResponseDto)
                .collect(Collectors.toList());
    }

    public OptionResponseDto addNewOption(Long productId, OptionDto optionDto){
        Product product = productRepository.findById(productId).
                orElseThrow(() -> new ValueNotFoundException("Product not exists in Database"));

        Option option = new Option(product, optionDto.optionName(), optionDto.quantity());
        Option savedOption = optionRepository.save(option);
        return optionMapper.tooOptionResponseDto(savedOption);
    }

    public OptionResponseDto updateOption(Long optionId,OptionDto optionDto){
        Option option = optionRepository.findById(optionId).
                orElseThrow(() -> new ValueNotFoundException("Option not exists in Database"));
        option.updateOption(optionDto);
        Option savedOption = optionRepository.save(option);
        return optionMapper.tooOptionResponseDto(savedOption);
    }

    public void deleteOption(Long optionId){
        Option option = optionRepository.findById(optionId).
                orElseThrow(() -> new ValueNotFoundException("Option not exists in Database"));
        optionRepository.delete(option);
    }

    @Transactional
    public void subtractAmount(Long optionId, int amount){
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new ValueNotFoundException("Option not found with ID"));
        option.updateAmount(amount);
        optionRepository.save(option);
    }
}

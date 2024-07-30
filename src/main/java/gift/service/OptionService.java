package gift.service;


import gift.dto.request.OptionRequestDTO;
import gift.dto.response.OptionResponseDTO;
import gift.entity.Option;
import gift.entity.Product;
import gift.exception.ProductException;
import gift.exception.optionException.DuplicatedOptionException;
import gift.exception.optionException.OptionException;
import gift.exception.optionException.OptionNotFoundException;
import gift.exception.productException.ProductNotFoundException;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    @Description("option get method")
    public OptionResponseDTO getOption(Long optionId) {
        Option option = getOptionEntity(optionId);
        return toDto(option);
    }

    @Description("option get method")
    public List<OptionResponseDTO> getOptions(Long productId) {
        List<Option> options = optionRepository.findByProductId(productId);
        return options.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Description("option add method")
    public void addOption(OptionRequestDTO optionRequestDTO) {
        Long productId = optionRequestDTO.productId();
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        String optionName = optionRequestDTO.name();

        if(existingProduct.existsOptionName(optionName)) {
            throw new DuplicatedOptionException(optionName + " : 중복되는 옵션 이름입니다.");
        }
        Option option = toEntity(optionRequestDTO, existingProduct);
        optionRepository.save(option);
    }

    @Description("option remove method")
    public void removeOption(Long optionId) {
        Option option = getOptionEntity(optionId);
        optionRepository.delete(option);
    }

    @Description("option update method")
    public void updateOption(Long optionId , OptionRequestDTO optionRequestDTO) {
        Option option = getOptionEntity(optionId);
        option.updateOption(optionRequestDTO);
        optionRepository.save(option);
    }

    @Description("option subtract method")
    public void subtractOption(Long optionId, int quantity) {
        Option option = getOptionEntity(optionId);
        option.subtract(quantity);
        optionRepository.save(option);
    }

    private Option getOptionEntity(Long optionId){
        return optionRepository.findById(optionId)
                .orElseThrow(()-> new OptionNotFoundException(optionId+"번 옵션을 찾을 수 없습니다"));
    }

    private OptionResponseDTO toDto(Option option) {
        return new OptionResponseDTO(
                option.getId(),
                option.getName(),
                option.getQuantity()
        );
    }

    private Option toEntity(OptionRequestDTO optionRequestDTO, Product product) {
        return new Option(
                optionRequestDTO.name(),
                optionRequestDTO.quantity(),
                product
        );
    }
}

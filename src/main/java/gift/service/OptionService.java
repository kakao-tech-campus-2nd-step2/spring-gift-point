package gift.service;

import gift.dto.OptionRequestDTO;
import gift.dto.OptionsPageResponseDTO;
import gift.exceptions.CustomException;
import gift.model.Option;
import gift.model.Product;
import gift.repository.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptionService {
    private final OptionRepository optionRepository;
    private final ProductService productService;

    public OptionService(OptionRepository optionRepository, ProductService productService) {
        this.optionRepository = optionRepository;
        this.productService = productService;
    }

    public OptionsPageResponseDTO getAllOptions(Pageable pageable) {
        Page<Option> optionPage = optionRepository.findAll(pageable);

        return new OptionsPageResponseDTO(optionPage.getContent(),
                optionPage.getNumber(),
                optionPage.getTotalPages());
    }

    public OptionsPageResponseDTO getProductOptions(Long productId, Pageable pageable) {
        Page<Option> optionPage = optionRepository.findAllByProductId(productId, pageable);

        return new OptionsPageResponseDTO(optionPage.getContent(),
                optionPage.getNumber(),
                optionPage.getTotalPages());
    }

    public void createOption(Long productId, OptionRequestDTO optionRequestDTO) {
        Product product = productService.getProduct(productId);
        Option option = new Option(optionRequestDTO.name(), optionRequestDTO.quantity(), product);

        optionRepository.save(option);
    }

    @Transactional
    public void subtractOptionQuantity(Long optionId, Long amount) {
        Option option = optionRepository.findById(optionId)
                .orElseThrow(CustomException::optionNotFoundException);

        option.subtract(amount);
        optionRepository.save(option);
    }
}

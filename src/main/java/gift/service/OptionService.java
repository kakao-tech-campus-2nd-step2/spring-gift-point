package gift.service;

import gift.dto.option.OptionRequestDTO;
import gift.dto.option.OptionResponseDTO;
import gift.dto.option.OptionsResponseDTO;

import gift.exceptions.CustomException;

import gift.model.Option;
import gift.model.Product;

import gift.repository.OptionRepository;
import gift.repository.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OptionService {
    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    public OptionsResponseDTO getProductOptions(Long productId) {
        List<Option> optionList = optionRepository.findAllByProductId(productId);

        return new OptionsResponseDTO(optionList);
    }

    public OptionResponseDTO createOption(Long productId, OptionRequestDTO optionRequestDTO) {
        Product product = productRepository.findById(productId).orElseThrow(CustomException::productNotFoundException);
        Option option = new Option(optionRequestDTO.name(), optionRequestDTO.quantity(), product);
        Option savedOption = optionRepository.save(option);

        return new OptionResponseDTO(savedOption.getId(), savedOption.getName(), savedOption.getQuantity());
    }

    public OptionResponseDTO modifyOption(Long productId, Long optionId, OptionRequestDTO optionRequestDTO) {
        Product product = productRepository.findById(productId).orElseThrow(CustomException::productNotFoundException);
        Option option = optionRepository.findById(optionId).orElseThrow(CustomException::optionNotFoundException);

        if (!option.getProduct().getId().equals(productId)) {
            throw CustomException.optionNotBelongProductException();
        }

        option.setName(optionRequestDTO.name());
        option.setQuantity(optionRequestDTO.quantity());
        Option updatedOption = optionRepository.save(option);

        return new OptionResponseDTO(updatedOption.getId(), updatedOption.getName(), updatedOption.getQuantity());
    }

    @Transactional
    public void deleteOption(Long productId, Long optionId) {
        Product product = productRepository.findById(productId).orElseThrow(CustomException::productNotFoundException);
        Option option = optionRepository.findById(optionId).orElseThrow(CustomException::optionNotFoundException);

        if (!option.getProduct().getId().equals(productId)) {
            throw CustomException.optionNotBelongProductException();
        }

        optionRepository.deleteById(optionId);
    }

    @Transactional
    public void subtractOptionQuantity(Long optionId, Long amount) {
        Option option = optionRepository.findById(optionId)
                .orElseThrow(CustomException::optionNotFoundException);

        option.subtract(amount);
        optionRepository.save(option);
    }
}

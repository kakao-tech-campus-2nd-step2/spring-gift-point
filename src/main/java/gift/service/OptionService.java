package gift.service;

import gift.converter.OptionConverter;
import gift.dto.OptionDTO;
import gift.dto.PageRequestDTO;
import gift.model.Option;
import gift.model.OptionName;
import gift.model.OptionQuantity;
import gift.model.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }


    public Page<OptionDTO> findAllOptions(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.toPageRequest();
        Page<Option> options = optionRepository.findAll(pageable);
        return options.map(OptionConverter::convertToDTO);
    }

    public List<OptionDTO> findOptionsByProductId(Long productId) {
        List<Option> options = optionRepository.findByProductId(productId);
        return options.stream()
            .map(OptionConverter::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public Long addOption(OptionDTO optionDTO) {
        Product product = productRepository.findById(optionDTO.getProductId())
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Option option = new Option(
            null,
            new OptionName(optionDTO.getName().getName()),
            new OptionQuantity(optionDTO.getQuantity().getQuantity()),
            product
        );

        optionRepository.save(option);
        return option.getId();
    }

    public Optional<OptionDTO> findOptionById(Long id) {
        return optionRepository.findById(id)
            .map(OptionConverter::convertToDTO);
    }

    @Transactional
    public void updateOption(OptionDTO optionDTO) {
        Option existingOption = optionRepository.findById(optionDTO.getId())
            .orElseThrow(() -> new IllegalArgumentException("옵션을 찾을 수 없습니다."));

        existingOption.update(new OptionName(optionDTO.getName().getName()), new OptionQuantity(optionDTO.getQuantity().getQuantity()));
        optionRepository.save(existingOption);
    }

    public List<OptionDTO> findAllOptions() {
        List<Option> options = optionRepository.findAll();
        return options.stream()
            .map(OptionConverter::convertToDTO)
            .collect(Collectors.toList());
    }

    public void deleteOption(Long id) {
        optionRepository.deleteById(id);
    }

    @Transactional
    public boolean decreaseOptionQuantity(Long optionId, int amount) {
        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new IllegalArgumentException("옵션을 찾을 수 없습니다."));

        if (option.getQuantity().getQuantity() < amount) {
            return false;
        }

        option.decreaseQuantity(amount);
        optionRepository.save(option);
        return true;
    }

    public String getOptionNameById(Long optionId) {
        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new IllegalArgumentException("옵션을 찾을 수 없습니다."));
        return option.getName().getName();
    }

}
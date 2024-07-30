package gift.product.option.service;

import gift.product.option.domain.Option;
import gift.product.option.domain.OptionDTO;
import gift.product.option.repository.OptionRepository;
import gift.product.domain.Product;
import gift.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    public List<OptionDTO> findAll() {
        return optionRepository.findAll()
            .stream().map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public Option findById(Long id) {
        return optionRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Option with id " + id + " not found."));
    }

    public List<OptionDTO> findAllByProductId(Long productId) {
        return optionRepository.findAllByProductId(productId)
            .stream().map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public List<Option> saveAll(List<OptionDTO> optionDTOList) {
        return optionRepository.saveAll(optionDTOList.stream()
            .map(this::convertToEntity)
            .collect(Collectors.toList()));
    }

    @Transactional
    public List<Option> saveAllwithProductId(List<OptionDTO> optionDTOList, Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new EntityNotFoundException("Product with id " + productId + " not found"));

        List<Option> options = optionDTOList.stream()
            .map(optionDTO -> {
                Option option = convertToEntity(optionDTO);
                option.setProduct(product);
                return option;
            })
            .collect(Collectors.toList());

        return optionRepository.saveAll(options);
    }

    @Transactional
    public Option save(OptionDTO optionDTO) {
        Option option = convertToEntity(optionDTO);
        option.setProduct(productRepository.findById(optionDTO.getProductId())
            .orElseThrow(() -> new IllegalArgumentException("Product with id " + optionDTO.getProductId() + " not found.")));
        return optionRepository.save(option);
    }

    @Transactional
    public Option subtract(OptionDTO optionDTO, Long orderedQuantity) {
        Option option = optionRepository.findByProductIdAndName(optionDTO.getProductId(), optionDTO.getName())
            .orElseThrow(() -> new IllegalArgumentException("Option not found for productId " + optionDTO.getProductId() + " and name " + optionDTO.getName()));
        try {
            option.subtract(orderedQuantity);
        } catch (ConstraintViolationException e) {
            option.setQuantity(0L);
        }
        return optionRepository.save(option);
    }

    @Transactional
    public OptionDTO addOption(Long productId, OptionDTO optionDTO) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Product with id " + productId + " not found."));
        Option option = convertToEntity(optionDTO);
        option.setProduct(product);
        optionRepository.save(option);

        return convertToDTO(option);
    }

    @Transactional
    public OptionDTO updateOption(Long productId, Long optionId, OptionDTO optionDTO) {
        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new IllegalArgumentException("Option with id " + optionId + " not found."));
        option.setQuantity(optionDTO.getQuantity());
        option.setName(optionDTO.getName());
        option.setProduct(productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Product with id " + productId + " not found.")));
        return convertToDTO(optionRepository.save(option));
    }

    @Transactional
    public void deleteOption(Long productId, Long optionId) {
        optionRepository.deleteById(optionId);
    }

    public OptionDTO convertToDTO(Option option) {
        return new OptionDTO(
            option.getId(),
            option.getName(),
            option.getQuantity(),
            option.getProduct().getId()
        );
    }

    public Option convertToEntity(OptionDTO optionDTO) {
        Option option = new Option(optionDTO.getName(), optionDTO.getQuantity());
        option.setProduct(productRepository.findById(optionDTO.getProductId())
            .orElseThrow(() -> new IllegalArgumentException("Product with id " + optionDTO.getProductId() + " not found.")));
        return option;
    }
}

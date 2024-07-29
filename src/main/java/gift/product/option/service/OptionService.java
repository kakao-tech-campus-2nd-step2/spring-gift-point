package gift.product.option.service;

import gift.product.option.domain.Option;
import gift.product.option.domain.OptionDTO;
import gift.product.option.repository.OptionRepository;
import gift.product.domain.Product;
import gift.product.domain.ProductDTO;
import gift.product.repository.ProductRepository;
import gift.product.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository,
        ProductService productService) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
        this.productService = productService;
    }

    public List<OptionDTO> findAll(){
        return optionRepository.findAll()
            .stream().map(this::convertToDTO)
            .toList();
    }
    public Option findById(Long id){
        return optionRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("optionService: optionid가 없다."));
    }

    public List<OptionDTO> findAllByProductId(Long productId){
        return optionRepository.findAllByProductId(productId)
            .stream().map(this::convertToDTO)
            .toList();
    }
    public List<Option> saveAll(List<OptionDTO> optionDTOList){
        return optionRepository.saveAll(optionDTOList.stream()
            .map(this::convertToEntity)
            .toList());
    }

    public List<Option> saveAllwithProductId(List<OptionDTO> optionDTOList, Long productId){
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new EntityNotFoundException("Product with id " + productId + " not found"));

        List<Option> options = optionDTOList.stream()
            .map(optionDTO -> {
                Option option = convertToEntity(optionDTO);
                option.setProduct(product);
                return option;
            })
            .toList();

        return optionRepository.saveAll(options);
    }

    public Option save(OptionDTO optionDTO){
        return optionRepository.save(convertToEntity(optionDTO));
    }

    public Option subtract(OptionDTO optionDTO, Long orderedQuantity) {
        Option option = optionRepository.findByProductIdAndName(optionDTO.getProductId(), optionDTO.getName())
            .orElseThrow(() -> new IllegalArgumentException("option이 존재하지 않습니다."));
        try{
            option.subtract(orderedQuantity);
        }catch(ConstraintViolationException e){
            option.setQuantity(0L);
        }
        return optionRepository.save(option);
    }
    public OptionDTO addOption(Long productId, Long optionId, OptionDTO optionDTO){
        ProductDTO productDTO = productService.getProductDTOById(productId)
            .orElseThrow(() -> new IllegalArgumentException("productId" + productId + "가 없습니다."));
        productDTO.getOptionDTOList().add(optionDTO);
        productService.updateProduct(productId, productDTO);

        return convertToDTO(save(optionDTO));
    }
    public OptionDTO updateOption(Long productId, Long optionId, OptionDTO optionDTO){
        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new IllegalArgumentException("optionId " + optionId + "가 없습니다."));
        option.setQuantity(optionDTO.getQuantity());
        option.setName(optionDTO.getName());
        option.setProduct(productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("productId " + productId + "가 없습니다.")));
        return convertToDTO(optionRepository.save(option));
    }

    public void deleteOption(Long productId, Long optionId){
        optionRepository.deleteById(optionId);
    }

    public OptionDTO convertToDTO(Option option){
        return new OptionDTO(
            option.getId(),
            option.getName(),
            option.getQuantity(),
            option.getProduct().getId()
        );
    }

    public Option convertToEntity(OptionDTO optionDTO){
        Option option = new Option(optionDTO.getName(), optionDTO.getQuantity());
        option.setProduct(productRepository.findById(optionDTO.getProductId())
            .orElseThrow(() -> new IllegalArgumentException("productId 없음.")));
        return option;
    }
}

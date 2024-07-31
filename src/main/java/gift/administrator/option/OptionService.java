package gift.administrator.option;

import gift.administrator.product.ProductService;
import gift.error.NotFoundIdException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductService productService;

    public OptionService(OptionRepository optionRepository, ProductService productService) {
        this.optionRepository = optionRepository;
        this.productService = productService;
    }

    public List<OptionDTO> getAllOptionsByProductId(long productId) {
        return optionRepository.findAllByProductId(productId).stream().map(OptionDTO::fromOption)
            .toList();
    }

    public List<OptionDTO> getAllOptions() {
        return optionRepository.findAll().stream().map(OptionDTO::fromOption).toList();
    }

    public List<OptionDTO> getAllOptionsByOptionId(List<Long> list) {
        return optionRepository.findAllById(list).stream().map(OptionDTO::fromOption).toList();
    }

    public boolean existsByOptionIdAndProductId(long optionId, long productId) {
        return optionRepository.existsByIdAndProductId(optionId, productId);
    }

    public void existsByNameSameProductIdNotOptionId(String name, long productId, long optionId) {
        if (optionRepository.existsByNameAndProductIdAndIdNot(name, productId, optionId)) {
            throw new IllegalArgumentException("같은 상품 내에서 동일한 이름의 옵션은 불가합니다.");
        }
    }

    public int countAllOptionsByProductId(long productId) {
        return optionRepository.countAllByProductId(productId);
    }

    public OptionDTO updateOption(long optionId, OptionDTO optionDTO) {
        Option option = findByOptionId(optionId);
        option.update(optionDTO.getName(), optionDTO.getQuantity());
        return OptionDTO.fromOption(optionRepository.save(option));
    }

    public OptionDTO findOptionById(long optionId) {
        return OptionDTO.fromOption(findByOptionId(optionId));
    }

    private Option findByOptionId(long optionId) {
        return optionRepository.findById(optionId)
            .orElseThrow(() -> new NotFoundIdException("옵션 아이디를 찾을 수 없습니다."));
    }

    public OptionDTO addOptionByProductId(long productId, OptionDTO optionDTO){
        Option option = optionDTO.toOption(productService.getNotDTOProductById(productId));
        return OptionDTO.fromOption(optionRepository.save(option));
    }

    public void subtractOptionQuantityErrorIfNotPossible(long optionId, int quantity){
        Option option = findByOptionId(optionId);
        if (option.getQuantity() < quantity) {
            throw new IllegalArgumentException("옵션의 재고가 부족합니다.");
        }
    }

    public Option subtractOptionQuantity(long optionId, int quantity) {
        Option option = findByOptionId(optionId);
        option.subtract(quantity);
        optionRepository.save(option);
        return option;
    }

    public void deleteOptionByOptionId(long optionId) {
        Option option = findByOptionId(optionId);
        option.getProduct().removeOption(option);
        optionRepository.deleteById(option.getId());
    }
}

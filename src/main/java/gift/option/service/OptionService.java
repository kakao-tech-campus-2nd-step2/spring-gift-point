package gift.option.service;


import gift.exception.ProductNotFoundException;
import gift.exception.ResourceNotFoundException;
import gift.option.dto.OptionRequest;
import gift.option.model.Option;
import gift.option.model.Options;
import gift.option.repository.OptionRepository;
import gift.product.model.Product;
import gift.product.repository.ProductRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    // 옵션 생성
    public Option createOption(final OptionRequest optionRequest) {
        var product = productRepository.findByName(optionRequest.getProductName())
            .orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다"));
        var option = new Option(optionRequest.getName(), optionRequest.getQuantity(), product);
        var foundOption = optionRepository.findAllByProduct(product);
        var options = new Options(foundOption);
        product.getOptionList().add(option);
        options.validate();
        return optionRepository.save(option);  // 여기서 저장
    }

    // 옵션 탐색
    public List<Option> findOptionsByProductId(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException("없는 상품입니다"));
        return optionRepository.findAllByProduct(product);
    }

    // 옵션 제거
    public void deleteOptions(Long productId, Long optionId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException("없는 상품입니다."));
        List<Option> optionList = optionRepository.findAllByProduct(product);
        Option deletedOption = optionList.stream().filter(option -> option.getId().equals(optionId))
            .findFirst().orElseThrow(() -> new ResourceNotFoundException("없는 옵션입니다."));
        optionRepository.delete(deletedOption);
    }

    // 옵션 ID 탐색
    public Option retrieveOption(Long id) {
        return optionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("없는 옵션입니다."));
    }

    // 옵션 수정
    public Option updateOption(Long productId, Long optionId, final OptionRequest optionRequest) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException("존재하지 않는 상품입니다."));
        List<Option> allOptionByProduct = optionRepository.findAllByProduct(product);
        Option option = allOptionByProduct.stream()
            .filter(o -> o.getId().equals(optionId))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 옵션입니다"));

        option.setName(optionRequest.getName());
        option.setQuantity(optionRequest.getQuantity());
        option.setProduct(product);
        return optionRepository.save(option);
    }


    // 옵션 수량 변경
    public boolean updateOptionQuantity(Long id, int quantity) {
        var option = optionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("존재 하지 않는 옵션입니다."));
        if (option.getQuantity() < quantity) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        option.subtract(quantity);
        return true;
    }
}

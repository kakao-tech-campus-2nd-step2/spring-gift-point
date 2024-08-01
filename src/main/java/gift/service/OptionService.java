package gift.service;


import gift.dto.OptionRequest;
import gift.exception.ResourceNotFoundException;
import gift.model.Option;
import gift.model.Options;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
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
    public List<Option> retreiveOptions() {
        return optionRepository.findAll();
    }

    // 옵션 제거
    public void deleteOptions(Long id) {
        if (!optionRepository.existsById(id)) {
            throw new ResourceNotFoundException("없는 옵션입니다.");
        }
        optionRepository.deleteById(id);
    }

    // 옵션 ID 탐색
    public Option retrieveOption(Long id) {
        return optionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("없는 옵션입니다."));
    }

    // 옵션 수정
    public Option updateOption(final OptionRequest optionRequest) {
        var option = optionRepository.findById(optionRequest.getId())
            .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 옵션입니다"));
        var product = productRepository.findByName(optionRequest.getProductName())  // 여기를 수정
            .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 상품입니다."));
        option.setName(optionRequest.getName());
        option.setQuantity(optionRequest.getQuantity());  // 수량만 업데이트
        option.setProduct(product);  // 상품 업데이트
        return optionRepository.save(option);
    }


    // 옵션 수량 변경
    public boolean updateOptionQuantity(Long id, int quantity) {
        var option = optionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("존재 하지 않는 옵션입니다."));
        if(option.getQuantity() < quantity){
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        option.subtract(quantity);
        return true;
    }
}

package gift.option;

import gift.product.Product;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OptionService {

    private final OptionRepository optionRepository;

    public OptionService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    public Option addOption(OptionRequest optionRequest, Product product){
        isValidRequest(optionRequest, product.getId());

        return optionRepository.save(option(optionRequest, product));
    }

    public void deleteOption(Long id){
        Option option = optionRepository.findById(id).orElseThrow();
        optionRepository.deleteById(option.getId());
    }

    public void updateOption(OptionRequest optionRequest, Long productId, Long optionId){
        Option option = optionRepository.findById(optionId).orElseThrow();
        if(!optionRequest.getName().equals(option.getName())){
            isValidRequest(optionRequest, productId);
        }
        option.update(optionRequest.getName(), optionRequest.getQuantity());

        optionRepository.save(option);
    }

    public List<Option> findAllByProductId(Long id){
        return optionRepository.findAllByProductId(id);
    }

    public Option getOption(Long id){
        return optionRepository.findById(id).orElseThrow();
    }

    public Product getProduct(Long id){
        Option option = getOption(id);
        return option.getProduct();
    }

    private void isValidRequest(OptionRequest optionRequest, Long productId){
        if(isExistName(optionRequest, productId)){
            throw new IllegalArgumentException(" 동일한 상품 내의 옵션 이름은 중복될 수 없습니다. ");
        }
    }

    private boolean isExistName(OptionRequest optionRequest, Long productId){
        return findAllByProductId(productId)
            .stream()
            .map(Option::getName)
            .anyMatch(optionRequest.getName()::equals);
    }

    private Option option(OptionRequest optionRequest, Product product){
        return new Option(null, optionRequest.getName(),optionRequest.getQuantity(),product);
    }

}

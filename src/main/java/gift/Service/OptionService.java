package gift.Service;

import gift.Model.Option;
import gift.Model.OrderRequestDTO;
import gift.Model.Product;
import gift.Repository.OptionRepository;
import gift.Repository.ProductRepository;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptionService {
    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository){
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    public Option getOption(Long optionId){
        return optionRepository.findOptionById(optionId);
    }

    public List<Option> getAllOptions(Long productId){
        return optionRepository.findAllById(productId);
    }

    public Option addOption(Option option, Long productId){
        Product product = productRepository.findProductById(productId);
        Option newOption = new Option(option.getId(), product, option.getName(), option.getQuantity());
        return optionRepository.save(newOption);
    }

    public Option updateOption(Option option, Long productId, Long optionId){
        Product product = productRepository.findProductById(productId);
        Option newOption = new Option(optionId, product, option.getName(), option.getQuantity());
        return optionRepository.save(newOption);
    }

    @Transactional
    public Option deleteOption(Long optionId){
        Option deletedOption = optionRepository.findOptionById(optionId);
        Product product = productRepository.findProductById(deletedOption.getProduct().getId());
        if(product.getOptions().size() == 1){ // size가 1이면 option 삭제 후 옵셥이 없어지므로 조건에 위배되어 product 삭제
            productRepository.deleteById(product.getId());
        }
        optionRepository.deleteById(optionId);
        return deletedOption;
    }

    @Transactional
    public int subtractQuantity(OrderRequestDTO orderRequestDTO ){
        Option option = optionRepository.findOptionById(orderRequestDTO.getOptionId());
        int optionQuantity = option.subtract(orderRequestDTO.getQuantity());
        if(optionQuantity == 0 ){
            this.deleteOption(option.getId());
            return 0;
        }
        optionRepository.save(option);
        return optionQuantity;
    }
}

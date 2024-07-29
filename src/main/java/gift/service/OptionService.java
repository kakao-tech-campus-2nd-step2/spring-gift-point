package gift.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import gift.dto.OptionDto;
import gift.dto.response.OptionResponse;
import gift.entity.Option;
import gift.entity.Product;
import gift.exception.CustomException;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
public class OptionService {
    
    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository){
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    public OptionResponse findByProductId(Long productId){

        productRepository.findById(productId)
            .orElseThrow(() -> new CustomException("Product with id " + productId + " not exist", HttpStatus.NOT_FOUND));
        
        List<Option> options = optionRepository.findByProductId(productId);

        return new OptionResponse(
            options
            .stream()
            .map(OptionDto::fromEntity)
            .toList()
        );
    }

    @Transactional
    public void addOption(OptionDto optionDto, Long productId){

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new CustomException("Product with id " + productId + " not exist", HttpStatus.NOT_FOUND));
        
        if(optionRepository.findByName(optionDto.getName()).isEmpty()){
            Option option = new Option(product, optionDto.getName(), optionDto.getQuantity());
            optionRepository.save(option);
        }else{
            throw new CustomException("Option with name " + optionDto.getName() + " exist", HttpStatus.CONFLICT);
        }
        
    }

    @Transactional
    public Option subtractQuantity(Long optionId, int subtractQuantity) {

        Option option = optionRepository.findById(optionId)
                        .orElseThrow(() -> new CustomException("Option with id" + optionId + " not exists", HttpStatus.NOT_FOUND));
        
        Option updatedOption = option.substract(subtractQuantity);

        optionRepository.delete(option);
        return optionRepository.save(updatedOption);
    }

    public Product findProductByOptionId(Long optionId){

        Option option = optionRepository.findById(optionId)
                        .orElseThrow(() -> new CustomException("Option with id" + optionId + " not exists", HttpStatus.NOT_FOUND));
        return option.getProduct();
    }
}

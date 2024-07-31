package gift.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import gift.dto.OptionDto;
import gift.dto.request.OptionRequest;
import gift.dto.response.GetOptionsResponse;
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

    public GetOptionsResponse findByProductId(Long productId){

        productRepository.findById(productId)
            .orElseThrow(() -> new CustomException("Product with id " + productId + " not exist", HttpStatus.NOT_FOUND, -40401));
        
        List<Option> options = optionRepository.findByProductId(productId);

        return new GetOptionsResponse(
            options
            .stream()
            .map(OptionDto::fromEntity)
            .toList()
        );
    }

    @Transactional
    public OptionResponse addOption(OptionRequest optionRequest, Long productId){

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new CustomException("Product with id " + productId + " not exist", HttpStatus.NOT_FOUND, -40401));
        
        if(optionRepository.findByName(optionRequest.getOptionName()).isEmpty()){
            Option option = new Option(product, optionRequest.getOptionName(), optionRequest.getStockQuantity());
            Option savedOption = optionRepository.save(option);
            return new OptionResponse(new OptionDto(savedOption.getId(), savedOption.getName(), savedOption.getStockQuantity()));
        }else{
            throw new CustomException("Option with name " +optionRequest.getOptionName() + " exist", HttpStatus.CONFLICT, -40904);
        }   
    }

    @Transactional
    public void updateOption(OptionRequest optionRequest, Long productId, Long optionId){
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new CustomException("Product with id " + productId + " not exist", HttpStatus.NOT_FOUND, -40401));
        
        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new CustomException("Option with id " + optionId + " not exist", HttpStatus.NOT_FOUND, -40404));

        if(option.getProduct().equals(product)){
            option.updateOption(optionRequest);
        }else{
            throw new CustomException("option does not belong to product", HttpStatus.NOT_FOUND, -40405);
        }
    }

    @Transactional
    public void deleteOption(Long productId, Long optionId){
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new CustomException("Product with id " + productId + " not exist", HttpStatus.NOT_FOUND, -40401));
        
        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new CustomException("Option with id " + optionId + " not exist", HttpStatus.NOT_FOUND, -40404));

        if(option.getProduct().equals(product)){
            if(product.getOptions().size() != 1){
                optionRepository.delete(option);
            }else{
                throw new CustomException("There must be at least one option", HttpStatus.BAD_REQUEST, -40002);
            }
            
        }else{
            throw new CustomException("option does not belong to product", HttpStatus.NOT_FOUND, -40405);
        }
    }

    @Transactional
    public Option subtractQuantity(Long optionId, int subtractQuantity) {

        Option option = optionRepository.findById(optionId)
                        .orElseThrow(() -> new CustomException("Option with id" + optionId + " not exists", HttpStatus.NOT_FOUND, -40404));
        
        Option updatedOption = option.substract(subtractQuantity);

        optionRepository.delete(option);
        return optionRepository.save(updatedOption);
    }

    public Product findProductByOptionId(Long optionId){

        Option option = optionRepository.findById(optionId)
                        .orElseThrow(() -> new CustomException("Option with id" + optionId + " not exists", HttpStatus.NOT_FOUND, -40404));
        return option.getProduct();
    }
}

package gift.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import gift.dto.OptionRequest;
import gift.dto.OptionResponse;
import gift.entity.Option;
import gift.entity.Product;
import gift.exception.DuplicateOptionException;
import gift.exception.InvalidOptionException;
import gift.exception.InvalidProductException;
import gift.exception.InvalidUserException;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;

@Service
public class OptionService {

	private final OptionRepository optionRepository;
	private final ProductRepository productRepository;

	public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
		this.optionRepository = optionRepository;
		this.productRepository = productRepository;
	}

	public List<OptionResponse> getOptions(Long productId) {
		List<Option> options = optionRepository.findByProductId(productId);
		return toOptionResponses(options);
	}
	
	public void addOption(Long productId, OptionRequest request, BindingResult bindingResult) {
		validateBindingResult(bindingResult);
		
		Product product = findProductById(productId);
		validateDuplicateOption(productId, request.getName());
		
		Option option = request.toEntity(product);
		optionRepository.save(option);
	}
	
	public void decreaseOptionQuantity(Long optionId, int quantity) {
		Option option = findOptionById(optionId);
		option.decreaseQuantity(quantity);
		optionRepository.save(option);
	}
	
	private void validateBindingResult(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
        	String errorMessage = bindingResult
					.getFieldError()
					.getDefaultMessage();
			throw new InvalidUserException(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }

	private Product findProductById(Long productId) {
    	return productRepository.findById(productId)
    			.orElseThrow(() -> new InvalidProductException("Product not found"));
    }
	
	private Option findOptionById(Long optionId) {
		return optionRepository.findById(optionId)
				.orElseThrow(() -> new InvalidOptionException("Option not found"));
	}

	private List<OptionResponse> toOptionResponses(List<Option> options) {
		return options.stream()
				.map(Option::toDto)
				.collect(Collectors.toList());
	}
	
	private void validateDuplicateOption(Long productId, String optionName) {
		if (optionRepository.findByProductIdAndName(productId, optionName).isPresent()) {
			throw new DuplicateOptionException("Options with the same name cannot appear within a product.");
		}
	}
}

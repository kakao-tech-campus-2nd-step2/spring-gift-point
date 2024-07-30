package gift.service;

import gift.dto.CategoryUpdateRequest;
import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import gift.dto.ProductUpdateRequest;
import gift.entity.Category;
import gift.entity.Product;
import gift.exception.InvalidProductException;
import gift.exception.InvalidUserException;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class ProductService {

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	
	public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
	}
	
    public Page<ProductResponse> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(Product::toDto);
    }

    public ProductResponse getProduct(Long productId) {
        return findProductById(productId).toDto();
    }

    public void createProduct(ProductRequest request, BindingResult bindingResult) {
    	validateBindingResult(bindingResult);
    	Category category = validateCategory(request.getCategoryName());
    	Product product = request.toEntity(category);
        productRepository.save(product);
    }

    public void updateProduct(Long productId, ProductUpdateRequest request, BindingResult bindingResult) {
    	validateBindingResult(bindingResult);
    	Product updateProduct = findProductById(productId);
    	request.updateEntity(updateProduct);
    	productRepository.save(updateProduct);
    }
    
    public void updateProductCategory(Long productId, CategoryUpdateRequest request, 
    		BindingResult bindingResult) {
    	validateBindingResult(bindingResult);
    	Product updateProduct = findProductById(productId);
    	Category category = validateCategory(request.getCategoryName());
    	updateProduct.setCategory(category);
    	productRepository.save(updateProduct);
    }

    public void deleteProduct(Long productId) {
    	validateProductId(productId);
        productRepository.deleteById(productId);
    }
    
    private void validateBindingResult(BindingResult bindingResult) {
    	if (bindingResult.hasErrors()) {
    		String errorMessage = bindingResult
					.getFieldError()
					.getDefaultMessage();
			throw new InvalidUserException(errorMessage, HttpStatus.BAD_REQUEST);
    	}
    }
    
    private void validateProductId(Long productId) {
    	if (!productRepository.existsById(productId)) {
    		throw new InvalidProductException("Product not found");
    	}
    }
    
    public Product findProductById(Long productId) {
	    return productRepository.findById(productId)
	    		.orElseThrow(() -> new InvalidProductException("Product not found"));
    }
    
    public Category validateCategory(String categoryName) {
    	return categoryRepository.findByName(categoryName)
    			.orElseThrow(() -> new InvalidProductException("Category not found"));
    }
}

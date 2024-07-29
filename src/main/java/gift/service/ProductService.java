package gift.service;

import gift.domain.Option;
import gift.domain.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;

    public ProductService(ProductRepository productRepository, OptionRepository optionRepository) {
        this.productRepository = productRepository;
        this.optionRepository = optionRepository;
    }

    public Page<Product> getAllProducts(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
    }

    public Option getOptionById(Long optionId) {
        return optionRepository.findById(optionId).orElseThrow(() -> new IllegalArgumentException("Invalid option ID"));
    }

    public void addProduct(Product product) {
        productRepository.save(product);
    }

    public void updateProduct(Long id, Product product) {
        product.setId(id);
        productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public void addOptionToProduct(Long productId, Option option) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("해당 product ID가 존재하지 않음"));

        try {
            option.setProduct(product);
            optionRepository.save(option);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("동일한 상품 내에 동일한 옵션 이름이 존재합니다.");
        }
    }

    public void subtractOptionQuantity(Long productId, String optionName, int quantity) {
        Option option = optionRepository.findByProductIdAndName(productId, optionName)
                .orElseThrow(() -> new IllegalArgumentException("해당 옵션이 존재하지 않습니다."));

        option.subtractQuantity(quantity);
        optionRepository.save(option);
    }
}

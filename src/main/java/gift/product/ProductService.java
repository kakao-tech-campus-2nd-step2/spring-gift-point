package gift.product;

import gift.option.Option;
import gift.option.OptionResponse;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;


@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public Product createProduct(Product newProduct) {
        return productRepository.save(newProduct);
    }

    public Product updateProduct(Product changeProduct) {
        Product product = findById(changeProduct.getId());
        product.update(changeProduct.getName(),changeProduct.getPrice(),changeProduct.getImageUrl(),
            changeProduct.getCategoryId());

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = findById(id);
        for(Option option : product.getOptions()){
            deleteOption(product.getId(), option);
        }
        productRepository.deleteById(id);
    }

    public Product findById(Long productId) {
        return productRepository.findById(productId).orElseThrow();
    }

    public List<OptionResponse> addOption(Long productId, Option option){
        Product product = findById(productId);
        product.getOptions().add(option);

        return product.getOptionResponses();
    }

    public List<Option> deleteOption(Long productId, Option option){
        Product product = findById(productId);
        if(!product.getOptions().remove(option)){
            throw new NoSuchElementException("삭제하고자 하는 옵션이 Product에 없습니다.");
        }

        return product.getOptions();
    }

}
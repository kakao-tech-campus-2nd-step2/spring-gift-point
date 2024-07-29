package gift.service;

import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import gift.exception.NotFoundException;
import gift.model.Category;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Product makeProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new NotFoundException("해당 카테고리가 존재하지 않습니다."));
        Product product = new Product(
                request.name(),
                request.price(),
                request.imageUrl(),
                category
        );
        productRepository.save(product);
        return product;
    }

    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        Page<Product> pageProducts = productRepository.findAll(pageable);
        Page<ProductResponse> pageToDto = pageProducts.map(product -> new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getCategory()
        ));
        return pageToDto;
    }

    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 id의 상품이 존재하지 않습니다."));
        ProductResponse response = new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getCategory()
        );
        return response;
    }

    @Transactional
    public Product putProduct(ProductRequest request) {
        Product product = productRepository.findById(request.id())
                .orElseThrow(() -> new NotFoundException("수정하려는 해당 id의 상품이 존재하지 않습니다."));
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new NotFoundException("해당 카테고리가 존재하지 않습니다."));

        Product updateProduct = product.update(
                request.name(),
                request.price(),
                request.imageUrl(),
                category
        );
        return updateProduct;

    }

    public void deleteProduct(Long id) {
        productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("삭제하려는 해당 id의 상품이 존재하지 않습니디."));
        productRepository.deleteById(id);
    }
}

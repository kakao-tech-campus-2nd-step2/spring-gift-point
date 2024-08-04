package gift.product.service;

import gift.category.entity.Category;
import gift.category.service.CategoryService;
import gift.member.entity.Member;
import gift.product.dto.AddProductResponse;
import gift.product.dto.GetProductResponse;
import gift.product.dto.OptionDto;
import gift.product.dto.ProductDto;
import gift.product.entity.Product;
import gift.product.exception.NoSuchProductException;
import gift.product.repository.ProductRepository;
import gift.wishedProduct.repository.WishedProductRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final OptionService optionService;
    private final WishedProductRepository wishedProductRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryService categoryService,
        OptionService optionService, WishedProductRepository wishedProductRepository) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.optionService = optionService;
        this.wishedProductRepository = wishedProductRepository;
    }

    public Page<GetProductResponse> getProducts(Member member, Pageable pageable) {
        return productRepository.findAll(pageable)
            .map(product -> product.toGetProductResponse(isWish(member, product)));
    }

    public GetProductResponse getProduct(Member member, Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(NoSuchProductException::new);
        return product.toGetProductResponse(isWish(member, product));
    }

    public AddProductResponse addProduct(ProductDto productDto, List<OptionDto> optionDtos) {
        Category category = categoryService.getCategory(productDto.categoryId()).toEntity();
        Product product = productRepository.save(productDto.toEntity(category));
        for (OptionDto optionDto : optionDtos) {
            optionService.addOption(product.getId(), optionDto);
        }
        return product.toAddProductResponse();
    }

    public ProductDto updateProduct(long id, ProductDto productDto) {
        productRepository.findById(id)
            .orElseThrow(NoSuchProductException::new);
        Category category = categoryService.getCategory(productDto.categoryId()).toEntity();
        Product product = new Product(id, productDto.name(), productDto.price(), productDto.imageUrl(), category);
        return productRepository.save(product).toDto();
    }

    public ProductDto deleteProduct(long id) {
        Product deletedProduct = productRepository.findById(id)
            .orElseThrow(NoSuchProductException::new);
        productRepository.delete(deletedProduct);
        return deletedProduct.toDto();
    }

    private boolean isWish(Member member, Product product) {
        if(member == null) {
            return false;
        }
        return wishedProductRepository.existsByMemberAndProduct(member, product);
    }
}

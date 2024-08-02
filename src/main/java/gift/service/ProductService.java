package gift.service;

import gift.domain.Category;
import gift.domain.Product;
import gift.dto.option.OptionDto;
import gift.dto.member.MemberDto;
import gift.dto.product.AddProductResponse;
import gift.dto.product.GetProductResponse;
import gift.dto.product.ProductDto;
import gift.exception.NoSuchProductException;
import gift.repository.ProductRepository;
import gift.repository.WishedProductRepository;
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

    public Page<GetProductResponse> getProducts(MemberDto memberDto, Pageable pageable) {
        return productRepository.findAll(pageable)
            .map(product -> product.toGetProductResponse(isWish(memberDto, product)));
    }

    public GetProductResponse getProduct(MemberDto memberDto, Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(NoSuchProductException::new);
        return product.toGetProductResponse(isWish(memberDto, product));
    }

    public AddProductResponse addProduct(ProductDto productDto, List<OptionDto> optionDtos) {
        Category category = categoryService.getCategory(productDto.categoryId()).toEntity();
        Product product = productRepository.save(productDto.toEntity(category));
        for (OptionDto optionDto : optionDtos) {
            optionService.addOption(product.getId(), optionDto);
        }
        return product.toAddProductResponse();
    }

    public ProductDto updateProduct(long id, ProductDto productDTO) {
        productRepository.findById(id)
            .orElseThrow(NoSuchProductException::new);
        Category category = categoryService.getCategory(productDTO.categoryId()).toEntity();
        Product product = new Product(id, productDTO.name(), productDTO.price(), productDTO.imageUrl(), category);
        return productRepository.save(product).toDto();
    }

    public ProductDto deleteProduct(long id) {
        Product deletedProduct = productRepository.findById(id)
            .orElseThrow(NoSuchProductException::new);
        productRepository.delete(deletedProduct);
        return deletedProduct.toDto();
    }

    private boolean isWish(MemberDto memberDto, Product product) {
        if(memberDto == null) {
            return false;
        }
        return wishedProductRepository.existsByMemberAndProduct(memberDto.toEntity(), product);
    }
}

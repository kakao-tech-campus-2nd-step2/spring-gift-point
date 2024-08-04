package gift.service;

import gift.dto.categoryDTO.CategoryResponseDTO;
import gift.dto.optionDTO.OptionResponseDTO;
import gift.dto.pageDTO.ProductPageResponseDTO;
import gift.dto.productDTO.ProductAddRequestDTO;
import gift.dto.productDTO.ProductAddResponseDTO;
import gift.dto.productDTO.ProductGetResponseDTO;
import gift.dto.productDTO.ProductUpdateRequestDTO;
import gift.dto.productDTO.ProductUpdateResponseDTO;
import gift.exception.NotFoundException;
import gift.model.Category;
import gift.model.Option;
import gift.model.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final WishlistRepository wishlistRepository;
    private final OptionRepository optionRepository;
    private final CategoryService categoryService;

    public ProductService(ProductRepository productRepository,
        WishlistRepository wishlistRepository, OptionRepository optionRepository,
        CategoryService categoryService) {
        this.productRepository = productRepository;
        this.wishlistRepository = wishlistRepository;
        this.optionRepository = optionRepository;
        this.categoryService = categoryService;
    }

    public ProductPageResponseDTO findAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return new ProductPageResponseDTO(products);
    }

    public ProductGetResponseDTO findProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("상품을 찾을 수 없습니다."));
        return toGetResponseDTO(product);
    }

    @Transactional
    public ProductAddResponseDTO saveProduct(ProductAddRequestDTO productAddRequestDTO) {
        CategoryResponseDTO categoryResponseDTO = categoryService.findCategoryById(
            productAddRequestDTO.categoryId());
        Category category = categoryService.responseToEntity(categoryResponseDTO);
        Product product = new Product(null, productAddRequestDTO.name(),
            productAddRequestDTO.price(),
            category, productAddRequestDTO.imageUrl());
        productRepository.save(product);
        List<Option> options = productAddRequestDTO.options().stream()
            .map(optionRequestDTO -> new Option(null, optionRequestDTO.name(),
                optionRequestDTO.quantity(), product))
            .toList();
        optionRepository.saveAll(options);
        return toAddResponseDTO(product, options);
    }

    @Transactional
    public ProductUpdateResponseDTO updateProduct(ProductUpdateRequestDTO productUpdateRequestDTO,
        Long id) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new NotFoundException("상품을 찾을 수 없습니다."));
        CategoryResponseDTO categoryResponseDTO = categoryService.findCategoryById(
            productUpdateRequestDTO.categoryId());
        Category category = categoryService.responseToEntity(categoryResponseDTO);
        existingProduct.updateProduct(productUpdateRequestDTO.name(),
            productUpdateRequestDTO.price(), category, productUpdateRequestDTO.imageUrl());
        productRepository.save(existingProduct);
        return toUpdateResponseDTO(existingProduct);
    }

    @Transactional
    public void deleteProductAndWishlistAndOptions(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("상품을 찾을 수 없습니다."));
        List<Option> options = optionRepository.findAllByProductId(id);
        wishlistRepository.deleteByOptionIn(options);
        optionRepository.deleteAll(options);
        productRepository.delete(product);
    }

    private ProductAddResponseDTO toAddResponseDTO(Product product, List<Option> options) {
        List<OptionResponseDTO> optionResponseDTOS = options.stream()
            .map(option -> new OptionResponseDTO(option.getId(), option.getName(),
                option.getQuantity(), option.getProduct().getId()))
            .toList();
        return new ProductAddResponseDTO(product.getId(), product.getName(), product.getPrice(),
            product.getImageUrl(), product.getCategory().getId(), optionResponseDTOS);
    }

    private ProductGetResponseDTO toGetResponseDTO(Product product) {
        List<OptionResponseDTO> optionResponseDTOS = optionRepository.findAllByProductId(
                product.getId()).stream()
            .map(option -> new OptionResponseDTO(option.getId(), option.getName(),
                option.getQuantity(), option.getProduct().getId()))
            .toList();
        return new ProductGetResponseDTO(product.getId(), product.getName(), product.getPrice(),
            product.getImageUrl(), product.getCategory().getId(), optionResponseDTOS);
    }

    private ProductUpdateResponseDTO toUpdateResponseDTO(Product product) {
        return new ProductUpdateResponseDTO(product.getId(), product.getName(), product.getPrice(),
            product.getImageUrl(), product.getCategory().getId());
    }
}

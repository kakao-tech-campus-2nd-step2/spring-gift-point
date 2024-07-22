package gift.service;

import gift.exception.ErrorCode;
import gift.exception.RepositoryException;
import gift.model.Category;
import gift.model.Option;
import gift.model.OptionDTO;
import gift.model.Product;
import gift.model.ProductDTO;
import gift.model.ProductPageDTO;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OptionRepository optionRepository;

    public ProductService(ProductRepository productRepository,
        CategoryRepository categoryRepository, OptionRepository optionRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.optionRepository = optionRepository;
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        Category category = categoryRepository.findById(productDTO.categoryId()).orElseThrow(
            () -> new RepositoryException(ErrorCode.CATEGORY_NOT_FOUND, productDTO.categoryId()));
        Product product = new Product(productDTO.id(), productDTO.name(), productDTO.price(),
            productDTO.imageUrl(), category);
        Option option = new Option("[기본 옵션] 추후 수정바랍니다", 1L,  product);
        Product createdProduct = productRepository.save(product);
        return convertToDTO(createdProduct);
    }

    public ProductPageDTO getAllProduct(int pageNum, int size) {
        Pageable pageable = PageRequest.of(pageNum, size);
        List<ProductDTO> productPage = productRepository.findAll(pageable)
            .map(this::convertToDTO)
            .stream()
            .toList();

        return new ProductPageDTO(pageNum, size, productPage.size(), productPage);
    }

    public List<ProductDTO> getAllProductByList() {
        List<Product> products = productRepository.findAll();
        return products.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public ProductDTO getProductById(long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RepositoryException(ErrorCode.PRODUCT_NOT_FOUND, id));
        return convertToDTO(product);
    }

    public List<OptionDTO> getOptionsByProductId(long productId, Pageable pageable) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RepositoryException(ErrorCode.PRODUCT_NOT_FOUND, productId));
        List<OptionDTO> optionDTOList = product.getOptions()
            .stream()
            .map(this::convertToOptionDTO)
            .toList();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), optionDTOList.size());
        return optionDTOList.subList(start, end);
    }

    public ProductDTO updateProduct(ProductDTO productDTO) {
        Product currentProduct = productRepository.findById(productDTO.id())
            .orElseThrow(
                () -> new RepositoryException(ErrorCode.PRODUCT_NOT_FOUND, productDTO.id()));
        Category category = categoryRepository.findById(productDTO.categoryId()).orElseThrow(
            () -> new RepositoryException(ErrorCode.CATEGORY_NOT_FOUND, productDTO.categoryId()));
        currentProduct.updateProduct(productDTO.name(), productDTO.price(), productDTO.imageUrl(),
            category);
        return convertToDTO(productRepository.save(currentProduct));
    }

    public String deleteProduct(long id) {
        try {
            productRepository.deleteById(id);
        } catch (Exception e) {
            throw new RepositoryException(ErrorCode.PRODUCT_NOT_FOUND, "");
        }
        return "성공적으로 삭제되었습니다.";
    }

    private ProductDTO convertToDTO(Product product) {
        return new ProductDTO(product.getId(), product.getName(), product.getPrice(),
            product.getImageUrl(), product.getCategory().getId());
    }

    private OptionDTO convertToOptionDTO(Option option) {
        return new OptionDTO(option.getId(), option.getName(), option.getQuantity());
    }
}

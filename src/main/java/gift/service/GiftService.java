package gift.service;

import gift.controller.dto.OptionResponse;
import gift.controller.dto.ProductOptionRequest;
import gift.controller.dto.ProductRequest;
import gift.controller.dto.ProductResponse;
import gift.domain.Category;
import gift.domain.Option;
import gift.domain.Product;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import gift.utils.error.CategoryNotFoundException;
import gift.utils.error.NotpermitNameException;
import gift.utils.error.OptionNameDuplicationException;
import gift.utils.error.ProductNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GiftService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public GiftService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }


    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findByIdWithCategoryAndOption(id)
            .orElseThrow(() -> new ProductNotFoundException("Product NOT FOUND"));
        return convertToProductResponse(product);
    }

    public Page<ProductResponse> getAllProduct(Pageable pageable) {
        Page<Product> products = productRepository.findAllWithCategoryAndOption(pageable);
        return products.map(this::convertToProductResponse);
    }

    public ProductResponse postProducts(ProductRequest productRequest) {
        validateProductName(productRequest.getName());

        Category category = categoryRepository.findById(productRequest.getCategoryId())
            .orElseThrow(() -> new CategoryNotFoundException("Category NOT FOUND"));

        List<ProductOptionRequest> options = productRequest.getOptions();
        validateUniqueOptionNames(options);

        Product product = new Product(productRequest.getName(),
            productRequest.getPrice(), productRequest.getImageUrl());

        options.forEach( option -> {
            Option saveoption = new Option(option.getName(), option.getQuantity());
            product.addOption(saveoption);
        });

        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        return convertToProductResponse(savedProduct);
    }

    public ProductResponse putProducts(ProductRequest productRequest, Long id) {
        validateProductName(productRequest.getName());

        Product productById = productRepository.findByIdWithCategoryAndOption(id)
            .orElseThrow(() -> new ProductNotFoundException("Product NOT FOUND"));

        Category category = categoryRepository.findById(productRequest.getCategoryId())
            .orElseThrow(() -> new CategoryNotFoundException("Category NOT FOUND"));

        List<ProductOptionRequest> options = productRequest.getOptions();
        validateUniqueOptionNames(options);

        productById.updateProduct(productRequest.getName(),productRequest.getPrice(),productRequest.getImageUrl());
        productById.setCategory(category);
        productById.getOptions().forEach(productById::removeOption);

        options.forEach( option -> {
            Option saveoption = new Option(option.getName(), option.getQuantity());
            productById.addOption(saveoption);
        });

        Product savedProduct = productRepository.save(productById);

        return convertToProductResponse(savedProduct);
    }

    @Transactional
    public Long deleteProducts(Long id) {
        productRepository.findById(id).orElseThrow(
            () -> new ProductNotFoundException("Product NOT FOUND"));
        productRepository.deleteById(id);
        return id;
    }

    public List<OptionResponse> getOption(Long id){
        Product product = productRepository.findByIdWithOption(id).orElseThrow(
            () -> new ProductNotFoundException("Product Not Found")
        );
        List<OptionResponse> list = product.getOptions().stream().map(options ->
            new OptionResponse(options.getId(),options.getName(),options.getQuantity())
        ).toList();

        return list;
    }


    private void validateProductName(String name) {
        if (name.replace(" ", "").contains("카카오")) {
            throw new NotpermitNameException("\"카카오\"가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있다.");
        }
    }
    private ProductResponse convertToProductResponse(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl(),
            new Category(product.getCategory().getId(),product.getCategory().getName(),
                product.getCategory().getColor(), product.getCategory().getImageUrl(),
                product.getCategory().getDescription()),
            product.getOptions().stream().toList()
        );
    }

    private void validateUniqueOptionNames(List<ProductOptionRequest> options) {
        Set<String> optionNames = new HashSet<>();
        if (!options.stream().allMatch(option -> optionNames.add(option.getName()))) {
            throw new OptionNameDuplicationException("Option Name Duplication");
        }
    }

}

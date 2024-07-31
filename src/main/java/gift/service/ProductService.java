package gift.service;

import gift.common.exception.EntityNotFoundException;
import gift.controller.dto.request.OptionRequest;
import gift.controller.dto.response.OptionResponse;
import gift.controller.dto.response.PagingResponse;
import gift.controller.dto.response.ProductResponse;
import gift.model.Category;
import gift.model.Option;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import gift.service.dto.CreateProductDto;
import gift.service.dto.UpdateProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public PagingResponse<ProductResponse.WithOption> findAllProductPaging(Pageable pageable) {
        Page<ProductResponse.WithOption> pages = productRepository.findProductAndCategoryFetchJoin(pageable)
                .map(ProductResponse.WithOption::from);
        return PagingResponse.from(pages);
    }

    @Transactional(readOnly = true)
    public ProductResponse.WithOption findById(Long id) {
        Product product = productRepository.findAllByIdFetchJoin(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Product with id " + id + " not found"));
        return ProductResponse.WithOption.from(product);
    }

    @Transactional
    public Long save(CreateProductDto request) {
        List<Option> options = request.options().stream().map(
                optionRq -> new Option(optionRq.name(), optionRq.quantity())
        ).toList();
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + request.categoryId() + " not found"));
        Product product = productRepository.save(new Product(request.name(), request.price(), request.imageUrl(), category, options));
        return product.getId();
    }

    @Transactional
    public void updateProduct(UpdateProductDto request) {
        Product product = productRepository.findById(request.id())
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + request.id() + " not found"));
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category with name " + request.categoryId() + " not found"));
        product.updateProduct(request.name(), request.price(), request.imageUrl(), category);
    }

    @Transactional
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse.Info> findProductsByCategoryId(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return products.stream()
                .map(ProductResponse.Info::from)
                .toList();
    }

    @Transactional
    public void deleteByIdAndOptionId(Long id, Long optionId) {
        Product product = productRepository.findProductAndOptionByIdFetchJoin(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + id + " not found"));
        Option option = product.findOptionByOptionId(optionId);
        product.subOption(option);
    }

    @Transactional(readOnly = true)
    public List<OptionResponse> getAllOptions(Long productId) {
        Product product = productRepository.findProductAndOptionByIdFetchJoin(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + productId + " not found"));
        return product.getOptions().stream()
                .map(OptionResponse::from)
                .toList();
    }

    @Transactional
    public void addOption(OptionRequest.CreateOption request) {
        checkProductExist(request.productId());
        Product product = productRepository.getReferenceById(request.productId());
        Option option = new Option(request.name(), request.quantity(), product);
        product.addOption(option);
    }

    @Transactional
    public void updateOption(OptionRequest.UpdateOption request) {
        Product product = productRepository.findProductAndOptionByIdFetchJoin(request.productId())
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + request.productId() + " not found"));
        Option option = product.findOptionByOptionId(request.id());
        product.checkDuplicateOptionName(request.id(), request.name());
        option.updateOption(request.name(), request.quantity());
    }

    @Transactional(readOnly = true)
    public OptionResponse findOptionById(Long id, Long optionId) {
        Product product = productRepository.findProductAndOptionByIdFetchJoin(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + id + " not found"));
        Option option = product.findOptionByOptionId(optionId);
        return OptionResponse.from(option);
    }

    @Transactional
    public void subtractQuantity(Long productId, Long optionId, int amount) {
        Product product = productRepository.findProductAndOptionByIdFetchJoin(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + productId + " not found"));
        product.subtractOptionQuantity(optionId, amount);
    }

    private void checkProductExist(Long id) {
        if(!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Product with id " + id + " not found");
        }
    }
}

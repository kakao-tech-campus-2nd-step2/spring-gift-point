package gift.administrator.product;

import gift.administrator.category.Category;
import gift.administrator.category.CategoryService;
import gift.administrator.option.Option;
import gift.administrator.option.OptionDTO;
import gift.error.NotFoundIdException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductService(ProductRepository productRepository,
        CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    public Page<ProductDTO> getAllProducts(int page, int size, String sortBy, Direction direction, Long categoryId) {
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageRequest = PageRequest.of(page, size, sort);
        Page<Product> productPage;
        if(categoryId == null){
            productPage = productRepository.findAllByCategoryId(categoryId, pageRequest);
        }
        else {
            productPage = productRepository.findAll(pageRequest);
        }
        List<ProductDTO> products = productPage.stream()
            .map(ProductDTO::fromProduct)
            .toList();
        return new PageImpl<>(products, pageRequest, productPage.getTotalElements());
    }

    public Page<ProductDTO> getAllProducts(int page, int size, String sortBy, Direction direction) {
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageRequest = PageRequest.of(page, size, sort);

        Page<Product> productPage = productPage = productRepository.findAll(pageRequest);

        List<ProductDTO> products = productPage.stream()
            .map(ProductDTO::fromProduct)
            .toList();
        return new PageImpl<>(products, pageRequest, productPage.getTotalElements());
    }

    public List<String> getAllCategoryName() {
        return productRepository.findDistinctCategoryNamesWithProducts();
    }

    public ProductDTO getProductById(long id) {
        Product product = findByProductId(id);
        return ProductDTO.fromProduct(product);
    }

    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    public void existsByNameThrowException(String name) {
        if (existsByName(name)) {
            throw new IllegalArgumentException("존재하는 이름입니다.");
        }
    }

    public Product getNotDTOProductById(long productId){
        return findByProductId(productId);
    }

    private Product findByProductId(long productId) {
        return productRepository.findById(productId).orElseThrow(() ->
            new NotFoundIdException("없는 상품 아이디입니다."));
    }

    public ProductDTO addProduct(ProductDTO productDTO) {
        existsByNameThrowException(productDTO.getName());
        Category category = getCategoryById(productDTO.getCategoryId());

        Product product = productDTO.toProduct(productDTO, category);
        category.addProducts(product);
        Product savedProduct = productRepository.save(product);
        return ProductDTO.fromProduct(savedProduct);
    }

    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {

        Product existingProduct = findByProductId(productId);
        existsByNameAndIdNotThrowException(productDTO.getName(), productId);
        Category newCategory = updateCategory(productDTO.getCategoryId(), existingProduct);

        existingProduct.update(productDTO.getName(), productDTO.getPrice(),
            productDTO.getImageUrl(), newCategory);

        List<Option> oldOptions = new ArrayList<>(existingProduct.getOptions());
        existingProduct.removeOptions(oldOptions);

        List<Option> options = optionDTOListToOptionList(productDTO.getOptions(), existingProduct);
        existingProduct.addOptions(options);

        Product savedProduct = productRepository.save(existingProduct);
        return ProductDTO.fromProduct(savedProduct);
    }

    public boolean existsByProductId(long productId){
        return !productRepository.existsById(productId);
    }

    private void existsByNameAndIdNotThrowException(String name, long productId) {
        if (productRepository.existsByNameAndIdNot(name, productId)) {
            throw new IllegalArgumentException("존재하는 이름입니다.");
        }
    }

    private List<Option> optionDTOListToOptionList(List<OptionDTO> optionList, Product product) {
        return optionList.stream().map(optionDTO -> optionDTO.toOption(product)).toList();
    }

    private Category updateCategory(long categoryId, Product product) {
        Category newCategory = getCategoryById(categoryId);
        product.setCategory(newCategory);
        return newCategory;
    }

    public void existsByNamePutResult(String name, BindingResult result) {
        if (existsByName(name)) {
            result.addError(new FieldError("productDTO", "name", "존재하는 이름입니다."));
        }
    }

    public void existsByNameAddingProducts(ProductDTO productDTO){
        if(!existsByName(productDTO.getName())){
            Set<String> optionNames = new HashSet<>();
            for (OptionDTO option : productDTO.getOptions()) {
                if (!optionNames.add(option.getName())) {
                    throw new IllegalArgumentException("추가하려는 옵션에 중복된 이름이 있습니다.");
                }
            }
        }
    }

    public void existsByNameAndIdPutResult(String name, long id, BindingResult result) {
        if (existsByName(name) && !Objects.equals(getProductById(id).getName(), name)) {
            result.addError(new FieldError("productDTO", "name", "존재하는 이름입니다."));
        }
    }

    public void existsByNameAndId(String name, long id) {
        if (existsByName(name) && !Objects.equals(getProductById(id).getName(), name)) {
            throw new IllegalArgumentException("존재하는 이름입니다.");
        }
    }

    public void deleteProduct(long id) {
        Product product = findByProductId(id);
        product.getCategory().removeProduct(product);
        List<Option> options = new ArrayList<>(product.getOptions());
        product.removeOptions(options);
        productRepository.delete(product);
    }

    public Category getCategoryById(long categoryId) {
        return categoryService.getCategoryById(categoryId).toCategory();
    }
}

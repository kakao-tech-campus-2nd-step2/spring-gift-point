package gift.Service;

import gift.DTO.ProductDTO;
import gift.DTO.ProductResponseDTO;
import gift.Model.Category;
import gift.Model.Option;
import gift.Model.Product;
import gift.Repository.CategoryRepository;
import gift.Repository.OptionRepository;
import gift.Repository.ProductRepository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OptionRepository optionRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, OptionRepository optionRepository){
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.optionRepository = optionRepository;
    }

    public Page<Product> findAll(Pageable pageable){
        return productRepository.findAll(pageable);
    }

    public Product getProductById(Long productId){
        return productRepository.findProductById(productId);
    }
    public ProductResponseDTO getProductResponseDTOById(Long productId){
        Product product = getProductById(productId);
        return new ProductResponseDTO(product.getId(),product.getName(), product.getPrice(), product.getImageUrl(), product.getCategory().getId());
    }

    public Product addProduct(ProductDTO productDTO){
        Product product = new Product(productDTO.getId(), productDTO.getName(), productDTO.getPrice(), productDTO.getImageUrl(), productDTO.getCategory(), productDTO.getOptions());
        Option defaultOption = new Option(null, product,product.getName(),1);
        productRepository.save(product);
        product.getOptions().add(optionRepository.save(defaultOption));
        return productRepository.findProductById(product.getId());
    }

    public Product updateProduct(ProductDTO productDTO){
        Product product = new Product(productDTO.getId(), productDTO.getName(), productDTO.getPrice(), productDTO.getImageUrl(), productDTO.getCategory(), productDTO.getOptions());
        return productRepository.save(product);
    }

    public Product deleteProduct(Long productId){
        Product deleteProduct = productRepository.findProductById(productId);
        optionRepository.deleteByProductId(productId);
        productRepository.deleteById(productId);
        return deleteProduct;
    }

    public List<Category> getAllCategory(){
        return categoryRepository.findAll();
    }

    public Sort getSort(String[] sort){
        Sort newSort = Sort.by(Sort.Order.asc(sort[0])); // 기본으로 asc인 sort[0]에 대해서 Sort 객체 생성
        if (sort.length > 1 && "desc".equalsIgnoreCase(sort[1])) { // 올바른 요청이면 길이가 2이고 desc 요청이 들어오면
            newSort = Sort.by(Sort.Order.desc(sort[0])); // desc로 객체 생성
        }
        return newSort;
    }

    public Page<Product> findAllByCategory(Pageable pageable, Long categoryId){
        return productRepository.findByCategoryId(pageable, categoryId);
    }

    public Page<ProductResponseDTO> getResponse(Pageable pageable, Long categoryId){
        Page<Product> productPage = findAllByCategory(pageable, categoryId);
        List<ProductResponseDTO> products =  productPage.stream().map(product -> new ProductResponseDTO(product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl(),
            product.getCategory().getId())).toList();
        return new PageImpl<>(products, pageable, productPage.getTotalElements());
    }
}

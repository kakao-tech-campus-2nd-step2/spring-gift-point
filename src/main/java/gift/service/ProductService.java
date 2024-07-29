package gift.service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import gift.dto.OptionDto;
import gift.dto.ProductDto;
import gift.dto.request.ProductCreateRequest;
import gift.dto.response.ProductPageResponse;
import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import gift.entity.WishList;
import gift.exception.CustomException;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import gift.repository.WishListRepository;
import jakarta.transaction.Transactional;

@Service
public class ProductService{

    private ProductRepository productRepository;
    private WishListRepository wishListRepository;
    private CategoryRepository categoryRepository;
    private OptionRepository optionRepository;

    private OptionService optionService;

    public ProductService(ProductRepository productRepository, WishListRepository wishListRepository, CategoryRepository categoryRepository, OptionRepository optionRepository, OptionService optionService) {
        this.productRepository = productRepository;
        this.wishListRepository = wishListRepository;
        this.categoryRepository = categoryRepository;
        this.optionRepository = optionRepository;
        this.optionService = optionService;
    }

    public ProductPageResponse getPage(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Product> response = productRepository.findByOrderByNameDesc(pageable);

        List<ProductDto> products = response.getContent()
                                        .stream()
                                        .map(ProductDto::fromEntity)
                                        .toList();

        return new ProductPageResponse(
            products,
            response.getNumber(), 
            response.hasPrevious(), 
            response.getTotalPages(),
            response.hasNext()
        );
    }

    public ProductDto findById(Long id){
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new CustomException("Product with id " + id + " not found", HttpStatus.NOT_FOUND));
        return new ProductDto(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), product.getCategory().getName());
    }

    @Transactional
    public void addProduct(ProductCreateRequest productCreateRequest) {
  
        if(productRepository.findByNameAndPriceAndImageUrl(productCreateRequest.getProductName(), 
                                                            productCreateRequest.getPrice(),
                                                            productCreateRequest.getImageUrl()).isEmpty()){
        
            Category category = categoryRepository.findByName(productCreateRequest.getCategory())
                    .orElseThrow(() -> new CustomException("Category with name" + productCreateRequest.getCategory() + "NOT FOUND" , HttpStatus.NOT_FOUND));
            
            Product product = new Product(productCreateRequest.getProductName(), 
                                          productCreateRequest.getPrice(),
                                          productCreateRequest.getImageUrl(),
                                          category);
            
            Product savedProduct = productRepository.save(product);
            
            optionService.addOption(new OptionDto(0L, productCreateRequest.getOptionName(), productCreateRequest.getQuantity()), savedProduct.getId());
        }else{
            throw new CustomException("Product with name " + productCreateRequest.getProductName() + "exists", HttpStatus.CONFLICT);
        }
    }

    @Transactional
    public void updateProduct(ProductDto productDto) {

        Optional<Product> optionalProduct = productRepository.findById(productDto.getId());

        if (optionalProduct.isPresent()) {
            Product product = toEntity(productDto);
            productRepository.delete(optionalProduct.get());
            productRepository.save(product);
        }else{
            throw new CustomException("Product with id " + productDto.getId() + " not found", HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    public void deleteProduct(Long id) {

        productRepository.findById(id)
            .orElseThrow(() -> new CustomException("Product with id " + id + " not found", HttpStatus.NOT_FOUND));

        List<WishList> wishList = wishListRepository.findByProductId(id);
        wishListRepository.deleteAll(wishList);
        
        List<Option> options = optionRepository.findByProductId(id);
        optionRepository.deleteAll(options);

        productRepository.deleteById(id);
    }

    public Product toEntity(ProductDto productDto){
        Category category = categoryRepository.findByName(productDto.getCategory())
                    .orElseThrow(() -> new CustomException("Category with name" + productDto.getCategory() + "NOT FOUND" , HttpStatus.NOT_FOUND));
        return new Product(productDto.getName(), productDto.getPrice(), productDto.getImageUrl(), category);
    }
}
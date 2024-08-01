package gift.service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import gift.dto.ProductDto;
import gift.dto.ProductInfo;
import gift.dto.request.OptionRequest;
import gift.dto.request.ProductRequest;
import gift.dto.response.GetProductsResponse;
import gift.dto.response.ProductPageResponse;
import gift.dto.response.ProductResponse;
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
    private CategoryService categoryService;

    public ProductService(
        ProductRepository productRepository, 
        WishListRepository wishListRepository, 
        CategoryRepository categoryRepository, 
        OptionRepository optionRepository, 
        OptionService optionService,
        CategoryService categoryService) {
        this.productRepository = productRepository;
        this.wishListRepository = wishListRepository;
        this.categoryRepository = categoryRepository;
        this.optionRepository = optionRepository;
        this.optionService = optionService;
        this.categoryService = categoryService;
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

    public GetProductsResponse findAll(Long categoryId, String sort){

        List<Product> products = findProductsBySort(categoryId, sort);
        List<ProductInfo> productInfos = products.stream().map(ProductInfo::fromEntity).toList();
        return new GetProductsResponse(categoryService.findById(categoryId), productInfos);

    }

    public List<Product> findProductsBySort(Long categoryId, String sort){

        String[] sortParams = sort.split(",");
        if(sortParams.length != 2) {
            throw new CustomException("sort value error", HttpStatus.BAD_REQUEST, -40001);
        }

        if(sortParams[0].equals("name")){
            if(sortParams[1].equals("asc")){
                return productRepository.findByCategoryIdOrderByNameAsc(categoryId);
            }else if(sortParams[1].equals("desc")){
                return productRepository.findByCategoryIdOrderByNameDesc(categoryId);
            }else{
                throw new CustomException("sort value error", HttpStatus.BAD_REQUEST, -40001);
            }
        }else if(sortParams[0].equals("price")){
            if(sortParams[1].equals("asc")){
               return productRepository.findByCategoryIdOrderByPriceAsc(categoryId);
            }else if(sortParams[1].equals("desc")){
               return productRepository.findByCategoryIdOrderByPriceDesc(categoryId);   
            }else{
                throw new CustomException("sort value error", HttpStatus.BAD_REQUEST, -40001);
            }
        }else{
            throw new CustomException("sort value error", HttpStatus.BAD_REQUEST, -40001);

        }
    }

    public ProductResponse findById(Long productId){
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new CustomException("Product with id " + productId + " not found", HttpStatus.NOT_FOUND, -40401));
        return new ProductResponse(ProductDto.fromEntity(product));
    }

    @Transactional
    public ProductResponse addProduct(ProductRequest productRequest) {
  
        if(productRepository.findByNameAndPriceAndImageUrl(productRequest.getProductName(), 
                                                            productRequest.getPrice(),
                                                            productRequest.getImageUrl()).isEmpty()){
                                                                
            Category category = categoryRepository.findById(productRequest.getCategoryId())
                    .orElseThrow(() -> new CustomException("Category with id" + productRequest.getCategoryId() + "NOT FOUND" , HttpStatus.NOT_FOUND, -40402));
            
            if(productRequest.getOptions().isEmpty()){
                throw new CustomException("Option must exist", HttpStatus.BAD_REQUEST, -40002);
            }
            Product product = new Product(productRequest.getProductName(), 
                                          productRequest.getPrice(),
                                          productRequest.getImageUrl(),
                                          category);
            
            Product savedProduct = productRepository.save(product);

            for (OptionRequest optionRequest : productRequest.getOptions()) {
                optionService.addOption(optionRequest, savedProduct.getId());
            }

            return new ProductResponse(ProductDto.fromEntity(savedProduct));

        }else{
            throw new CustomException("Product with name " + productRequest.getProductName() + "exists", HttpStatus.CONFLICT, -40903);
        }
    }

    @Transactional
    public void updateProduct(Long productId, ProductRequest productRequest) {

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new CustomException("Product with id " + productId + " not found", HttpStatus.NOT_FOUND, -40401));
        
        Category category = categoryRepository.findById(productRequest.getCategoryId())
            .orElseThrow(() -> new CustomException("Category with id" + productRequest.getCategoryId() + "NOT FOUND" , HttpStatus.NOT_FOUND, -40402));
        
        if(productRepository.findByNameAndPriceAndImageUrl(productRequest.getProductName(), 
            productRequest.getPrice(),
            productRequest.getImageUrl()).isEmpty()){
                product.update(productRequest, category);
            }else{
               throw new CustomException("Product with name " + productRequest.getProductName() + "exists", HttpStatus.CONFLICT, -40903);
            }
    }

    @Transactional
    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
            .orElseThrow(() -> new CustomException("Product with id " + id + " not found", HttpStatus.NOT_FOUND, -40401));

        List<WishList> wishList = wishListRepository.findByProductId(id);
        wishListRepository.deleteAll(wishList);
        
        List<Option> options = optionRepository.findByProductId(id);
        optionRepository.deleteAll(options);

        productRepository.delete(product);
    }

    public Product toEntity(ProductDto productDto){
        Category category = categoryRepository.findById(productDto.getCategoryId())
            .orElseThrow(() -> new CustomException("Category with id" + productDto.getCategoryId() + "NOT FOUND" , HttpStatus.NOT_FOUND, -40402));
        return new Product(productDto.getName(), productDto.getPrice(), productDto.getImageUrl(), category);
    }
}
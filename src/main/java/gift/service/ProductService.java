package gift.service;


import gift.domain.Product;
import gift.domain.Product.ProductDetail;
import gift.domain.Product.ProductSimple;
import gift.domain.Product.getList;
import gift.domain.ProductOption.CreateOption;
import gift.entity.CategoryEntity;
import gift.entity.ProductEntity;
import gift.entity.ProductOptionEntity;
import gift.mapper.ProductMapper;
import gift.mapper.ProductOptionMapper;
import gift.repository.CategoryRepository;
import gift.repository.ProductOptionRepository;
import gift.repository.ProductRepository;
import gift.util.errorException.BaseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {


    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductMapper productMapper;
    private final ProductOptionMapper productOptionMapper;

    @Autowired
    public ProductService(ProductRepository productRepository,
        CategoryRepository categoryRepository,
        ProductOptionRepository productOptionRepository, ProductMapper productMapper,
        ProductOptionMapper productOptionMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productOptionRepository = productOptionRepository;
        this.productMapper = productMapper;
        this.productOptionMapper = productOptionMapper;
    }

    public Page<ProductDetail> getProductList(getList param) {

        return productMapper.toDetailList(productRepository.findAll(param.toPageable()));
    }

    public Page<ProductSimple> getSimpleProductList(getList param) {
        return productMapper.toSimpleList(productRepository.findAll(param.toPageable()));
    }

    public ProductDetail getProduct(Long id) {
        return productMapper.toDetail(productRepository.findById(id)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "상품이 존재하지 않습니다.")));
    }

    @Transactional
    public Long createProduct(Product.CreateProduct create) {

        CategoryEntity category = categoryRepository.findById(create.getCategoryId())
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 카테고리가 존재하지 않습니다."));

        ProductEntity productEntity = productMapper.toEntity(create, category);
        productRepository.save(productEntity);

        ProductOptionEntity productOptionEntity = productOptionMapper.toEntity(
            new CreateOption(create.getOptionName(), create.getQuantity()), productEntity);
        productOptionRepository.save(productOptionEntity);

        return productEntity.getId();
    }

    @Transactional
    public Long updateProduct(Product.UpdateProduct update, Long id) {

        ProductEntity productEntity = productRepository.findById(id)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "상품이 존재하지 않습니다."));

        CategoryEntity category = categoryRepository.findById(update.getCategoryId())
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 카테고리가 존재하지 않습니다."));

//        productRepository.save(productMapper.toUpdate(update, productEntity));
        productMapper.toUpdate(update, productEntity, category);
        return productEntity.getId();
    }

    public Long deleteProduct(Long id) {
        ProductEntity productEntity = productRepository.findById(id)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "상품이 존재하지 않습니다."));

        productRepository.delete(productEntity);
        return productEntity.getId();
    }

}

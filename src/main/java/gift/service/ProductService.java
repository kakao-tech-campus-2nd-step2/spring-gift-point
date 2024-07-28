package gift.service;

import gift.database.ProductFacadeRepository;
import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import gift.exceptionAdvisor.exceptions.GiftBadRequestException;
import gift.model.Category;
import gift.model.GiftOption;
import gift.model.Product;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProductService {

    private final ProductFacadeRepository productFacadeRepository;

    public ProductService(ProductFacadeRepository productFacadeRepository) {
        this.productFacadeRepository = productFacadeRepository;
    }

    public List<ProductResponse> readAll() {
        List<Product> productList = productFacadeRepository.findAll();
        return productList.stream().map(ProductResponse::new).toList();
    }

    public ProductResponse read(long id) {
        Product product = productFacadeRepository.getProduct(id);
        return new ProductResponse(product);
    }

    public void create(ProductRequest productRequest) {

        checkKakao(productRequest.getName());

        Product product = new Product(null, productRequest.getName(), productRequest.getPrice(),
            productRequest.getImageUrl());

        GiftOption giftOption = new GiftOption(null, productRequest.getGiftOptionName(),
            productRequest.getGiftOptionQuantity());
        product.addGiftOption(giftOption);

        Category category = new Category(productRequest.getCategoryId());
        product.updateCategory(category);

        productFacadeRepository.saveProduct(product);
    }


    public void update(long id, ProductRequest productRequest) {

        Product product = productFacadeRepository.getProduct(id);

        product.update(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());

        checkKakao(product.getName());
        productFacadeRepository.saveProduct(product);
    }

    public void delete(long id) {
        productFacadeRepository.deleteProduct(id);
    }

    private void checkKakao(String productName) {
        if (productName.contains("카카오")) {
            throw new GiftBadRequestException("카카오 문구는 MD 협의 이후 사용할 수 있습니다.");
        }
    }

    public List<ProductResponse> readProduct(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return null;
        //return productFacadeRepository.findPageList(pageable).stream()
        //    .map(ProductResponse::new)
        //    .toList();
    }
}

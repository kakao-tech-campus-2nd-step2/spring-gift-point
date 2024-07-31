package gift.product;

import gift.category.Category;
import gift.category.CategoryRepository;
import gift.option.Option;
import gift.option.OptionRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<ProductReadResponse> getProductPages(int pageNum, int size, List<String> sort, Long categoryId) {
        String sortBy = sort.get(0);
        String sortDirection = sort.get(1);

        System.out.println(sortBy);
        System.out.println(sortDirection);
        Pageable pageable = PageRequest.of(pageNum, size, Sort.by(Sort.Order.asc(sortBy)));
        if (Objects.equals(sortDirection, "desc")) {
            pageable = PageRequest.of(pageNum, size, Sort.by(Sort.Order.desc(sortBy)));
        }
        if(categoryId == 0){
            return productRepository.findAll(pageable).map(ProductReadResponse::new).stream().toList();
        }

        return productRepository.findByCategoryId(pageable, categoryId).map(ProductReadResponse::new).stream().toList();
    }

    public Product findByID(Long id) {
        return productRepository.findById(id).orElseThrow();
    }

    public Product insertNewProduct(ProductOptionRequest productOptionRequest) {
        ProductRequest newProduct = new ProductRequest(productOptionRequest);
        List<OptionRequest> optionRequest = productOptionRequest.options;
        Long categoryID = newProduct.getCategoryID();
        Category category = categoryRepository.findById(categoryID).orElseThrow();

        Product product = new Product(newProduct.getName(), newProduct.getPrice(), newProduct.getImageUrl(), category);
        category.addProduct(product);
        optionRequest.forEach(opt -> product.addOptions(new Option(opt, product)));
        //Option option = new Option(optionRequest, product);
        //product.addOptions(option);

        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, ProductRequest updateProduct) {
        Product product = findByID(id);
        product.setName(updateProduct.getName());
        product.setPrice(updateProduct.getPrice());
        product.setImageUrl(updateProduct.getImageUrl());

        product.getCategory().removeProduct(product);
        Category category = categoryRepository.findById(updateProduct.getCategoryID()).orElseThrow();
        product.setCategory(category);
        category.addProduct(product);

        return product;
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow();
        product.getCategory().removeProduct(product);
        productRepository.deleteById(id);
    }

    public List<ProductResponse> findAllProducts() {
        return productRepository.findAll().stream().map(ProductResponse::new).toList();
    }
}

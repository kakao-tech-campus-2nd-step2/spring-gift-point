package gift.service;

import gift.dto.PageRequestDTO;
import gift.dto.productDTOs.CustomProductPageDTO;
import gift.dto.productDTOs.InputProductDTO;
import gift.dto.productDTOs.ProductDTO;
import gift.dto.productDTOs.UpdateProductDTO;
import gift.model.entity.Category;
import gift.model.entity.Product;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OptionService optionService;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, OptionService optionService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.optionService = optionService;
    }

    //전체 조회
    public Page<ProductDTO> getAllProducts(PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage(),
                pageRequestDTO.getSize(), pageRequestDTO.getSort());
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(ProductDTO::getProductDTO);
    }

    //전체 조회
    public CustomProductPageDTO getAllProductsByCustomPage(PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage(), pageRequestDTO.getSize(), pageRequestDTO.getSort());
        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductDTO> productDTOs = productPage.stream()
                .map(ProductDTO::getProductDTO)
                .toList();
        return new CustomProductPageDTO(productDTOs, productPage.getNumber(), productPage.getTotalPages(), productPage.getTotalElements());
    }

    //하나 조회
    public ProductDTO getProductDTOById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 상품이 없습니다."));
        ProductDTO productDTO = new ProductDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getCategory().getId());
        return productDTO;
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 상품이 없습니다."));
    }

    //저장
    public void saveProduct(InputProductDTO inputProductDTO) {
        Category category = categoryRepository.findByName(inputProductDTO.getCategory())
                .orElseThrow(() -> new NoSuchElementException("해당 카테고리가 없습니다."));

        String optionString = inputProductDTO.getOption();
        String optionQuantity = inputProductDTO.getQuantity();

        Product product = new Product(
                inputProductDTO.getName(),
                inputProductDTO.getPrice(),
                inputProductDTO.getImageUrl(),
                category
        );
        productRepository.save(product);

        Long productID = productRepository.findByName(inputProductDTO.getName()).get().getId();
        optionService.addOptions(optionString, optionQuantity, productID);
    }

    //삭제
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public void updateProduct(Long id, UpdateProductDTO updateProductDTO) {
        Product oldProduct = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 상품이 없습니다."));
        Category category = categoryRepository.findByName(updateProductDTO.getCategory())
                .orElseThrow(() -> new NoSuchElementException("해당 카테고리가 없습니다."));
        Product updatedProduct = oldProduct.update(
                updateProductDTO.getName(),
                updateProductDTO.getPrice(),
                updateProductDTO.getImageUrl(),
                category);
        productRepository.save(updatedProduct);
    }

    public int getPreviousPage(Page<ProductDTO> productPage) {
        if (productPage.hasPrevious()) {
            Pageable previousPageable = productPage.previousPageable();
            return previousPageable.getPageNumber();
        }
        return -1;
    }

    public int getNextPage(Page<ProductDTO> productPage) {
        if (productPage.hasNext()) {
            Pageable nextPageable = productPage.nextPageable();
            return nextPageable.getPageNumber();
        }
        return -1;
    }
}
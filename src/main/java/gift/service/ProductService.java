package gift.service;

import gift.dto.product.ProductPageDTO;
import gift.dto.product.SaveProductDTO;
import gift.dto.product.ResponseProductDTO;

import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import gift.exception.exception.BadRequestException;
import gift.exception.exception.NotFoundException;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static java.util.Arrays.stream;


@Service
@Validated
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;
    private final CategoryRepository categoryRepository;

    public ProductPageDTO getAllProductsInCategory(Pageable pageable, int categoryId) {
        findCategoryById(categoryId);
        Page<ResponseProductDTO> page = productRepository.findByCategoryId(pageable, categoryId);
        return pageToResponse(page);
    }

    private Category findCategoryById(int categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("해당 카테고리가 없음"));
    }
  
    private ProductPageDTO pageToResponse(Page<ResponseProductDTO> page) {
        return new ProductPageDTO(
                page.getContent(),
                page.getPageable().getPageNumber(),
                page.getTotalElements(),
                page.getPageable().getPageSize(),
                page.isLast());
    }

    @Transactional
    public ResponseProductDTO saveProduct(SaveProductDTO productDTO) {
        Category category = findCategoryById(productDTO.categoryId());
        checkDuplicateName(productDTO);
        if (productDTO.option().isBlank()) throw new BadRequestException("하나의 옵션은 존재 해야 함");

        Product product = new Product(productDTO, category);
        Product saveProduct = productRepository.save(product);
        category.addProduct(product);

        List<String> optionList = stream(productDTO.option().split(",")).toList();
        saveOptionToProduct(optionList, saveProduct);
        return saveProduct.toResponseDTO();
    }

    private void checkDuplicateName(SaveProductDTO productDTO) {
        productRepository.findByNameAndCategory(productDTO.name(), productDTO.categoryId()).ifPresent(c -> {
            throw new BadRequestException("해당 카테고리에 같은 이름의 물품이 존재");
        });
    }

    private void saveOptionToProduct(List<String> optionList, Product saveProduct) {
        optionList.stream()
                .distinct()
                .map(str -> new Option(saveProduct, str))
                .filter(this::isValidOption)
                .forEach(optionRepository::save);
    }

    private boolean isValidOption(@Validated Option option) {
        return optionRepository.findByProductNameAndOption(option.getProduct().getName(), option.getOption()).isEmpty();
    }

    @Transactional
    public ResponseProductDTO deleteProduct(int id) {
        Product product = findProductById(id);
        product.getCategory().deleteProduct(product);
        optionRepository.deleteByProductId(id);
        productRepository.deleteById(id);
        return product.toResponseDTO();
    }

    private Product findProductById(int id) {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("존재하지 않는 id입니다."));
    }

    public ResponseProductDTO getProductByID(int id) {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("해당 물건이 없습니다.")).toResponseDTO();
    }

    @Transactional
    public ResponseProductDTO updateProduct(int id, SaveProductDTO saveProductDTO) {
        Product product = findProductById(id);
        checkDuplicateName(saveProductDTO);
        product.getCategory().deleteProduct(product);
        product = product.modifyProduct(saveProductDTO);
        product.getCategory().addProduct(product);
        return product.toResponseDTO();
    }

}

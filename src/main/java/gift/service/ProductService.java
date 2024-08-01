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
        categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("해당 카테고리가 없음"));
        Page<ResponseProductDTO> page = productRepository.findByCategoryId(pageable, categoryId);
        return pageToResponse(page);
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
        Category category = categoryRepository.findById(productDTO.categoryId()).orElseThrow(() -> new NotFoundException("해당 카테고리가 없음"));
        productRepository.findByNameAndCategory(productDTO.name(), productDTO.categoryId()).ifPresent(c -> {
            throw new BadRequestException("해당 카테고리에 같은 이름의 물품이 존재");
        });
        if (productDTO.option().isBlank()) throw new BadRequestException("하나의 옵션은 존재 해야 함");

        Product product = new Product(productDTO, category);
        Product saveProduct = productRepository.save(product);
        category.addProduct(product);

        List<String> optionList = stream(productDTO.option().split(",")).toList();
        optionList.stream()
                .distinct()
                .map(str -> new Option(saveProduct, str))
                .filter(this::isValidOption)
                .forEach(optionRepository::save);
        return saveProduct.toResponseDTO();
    }

    private boolean isValidOption(@Validated Option option) {
        return optionRepository.findByProductNameAndOption(option.getProduct().getName(), option.getOption()).isEmpty();
    }

    @Transactional
    public ResponseProductDTO deleteProduct(int id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("존재하지 않는 id입니다."));
        product.getCategory().deleteProduct(product);
        optionRepository.deleteByProductId(id);
        productRepository.deleteById(id);
        return product.toResponseDTO();
    }


    public ResponseProductDTO getProductByID(int id) {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("해당 물건이 없습니다.")).toResponseDTO();
    }

    @Transactional
    public ResponseProductDTO updateProduct(int id, SaveProductDTO saveProductDTO) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("물건이 없습니다."));
        productRepository.findByNameAndCategory(saveProductDTO.name(), product.getCategory().getId()).ifPresent(c -> {
            throw new BadRequestException("해당 카테고리에 같은 이름의 물품이 존재");
        });
        product.getCategory().deleteProduct(product);
        product = product.modifyProduct(saveProductDTO);
        product.getCategory().addProduct(product);
        return product.toResponseDTO();
    }

}

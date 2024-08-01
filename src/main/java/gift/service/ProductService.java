package gift.service;

import gift.dto.betweenClient.option.OptionRequestDTO;
import gift.dto.betweenClient.product.ProductPostRequestDTO;
import gift.dto.betweenClient.product.ProductResponseDTO;
import gift.dto.betweenClient.product.ProductRequestDTO;
import gift.entity.Category;
import gift.entity.Product;
import gift.exception.BadRequestExceptions.BadRequestException;
import gift.exception.BadRequestExceptions.InvalidIdException;
import gift.exception.BadRequestExceptions.NoSuchProductIdException;
import gift.exception.InternalServerExceptions.InternalServerException;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final WishRepository wishRepository;
    private final CategoryRepository categoryRepository;
    private final OptionService optionService;
    private final OptionRepository optionRepository;

    public ProductService(ProductRepository productRepository, WishRepository wishRepository,
            CategoryRepository categoryRepository, OptionService optionService,
            OptionRepository optionRepository) {
        this.productRepository = productRepository;
        this.wishRepository = wishRepository;
        this.categoryRepository = categoryRepository;
        this.optionService = optionService;
        this.optionRepository = optionRepository;
    }

    @Transactional
    public void addProduct(ProductPostRequestDTO productPostRequestDTO) throws RuntimeException {
        try {
            Category category = categoryRepository.findByName(productPostRequestDTO.categoryName())
                    .orElseThrow(() -> new BadRequestException("그러한 카테코리를 찾을 수 없습니다."));
            Product product = productPostRequestDTO.convertToProduct(category);
            categoryRepository.save(product.getCategory());
            productRepository.save(product);
            optionService.addOption(product.getId(), new OptionRequestDTO(productPostRequestDTO.optionName(), productPostRequestDTO.optionQuantity()));
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("잘못된 제품 값을 입력했습니다. 입력 칸 옆의 설명을 다시 확인해주세요");
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getProductList(Pageable pageable) throws RuntimeException {
        Page<Product> productPage = productRepository.findAll(pageable);
        List<ProductResponseDTO> productResponseDTOList = productPage.getContent().stream()
                .map(ProductResponseDTO::convertToProductResponseDTO).toList();
        return new PageImpl<>(productResponseDTOList, productPage.getPageable(),
                productPage.getTotalElements());
    }

    @Transactional
    public void updateProduct(Long id, ProductRequestDTO productRequestDTO) throws RuntimeException {
        try {
            if (!id.equals(productRequestDTO.id()))
                throw new InvalidIdException("올바르지 않은 id입니다.");

            Category categoryToReplace = categoryRepository.findByName(productRequestDTO.categoryName()).orElseThrow(() -> new BadRequestException("그러한 카테코리를 찾을 수 없습니다."));
            Product productInDb = productRepository.findById(id).orElseThrow(() -> new NoSuchProductIdException("id가 %d인 상품은 존재하지 않습니다.".formatted(id)));
            Product productToUpdate = productRequestDTO.convertToProduct(categoryToReplace);

            productInDb.changeProduct(productToUpdate.getName(), productToUpdate.getPrice(), productToUpdate.getImageUrl(), productToUpdate.getCategory());
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }


    @Transactional
    public void deleteProduct(Long id) throws RuntimeException {
        try {
            wishRepository.deleteAllByProductId(id); // 외래키 제약조건
            optionRepository.deleteAllByProductId(id); // 외래키 제약조건
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchProductIdException("id가 %d인 상품은 존재하지 않습니다.".formatted(id));
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }
}
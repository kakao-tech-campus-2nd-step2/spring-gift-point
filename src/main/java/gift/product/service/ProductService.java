package gift.product.service;

import gift.category.service.CategoryService;
import gift.global.dto.PageInfoDto;
import gift.option.dto.OptionRequestDto;
import gift.option.dto.OptionResponseDto;
import gift.option.service.OptionService;
import gift.product.dto.CreateProductRequestDto;
import gift.product.dto.FullOptionsProductResponseDto;
import gift.product.dto.ProductRequestDto;
import gift.product.dto.ProductResponseDto;
import gift.product.entity.Options;
import gift.product.entity.Product;
import gift.product.repository.ProductRepository;
import jakarta.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ProductService {

    private final ProductRepository productRepository;
    private final OptionService optionService;
    private final CategoryService categoryService;

    @Autowired
    public ProductService(ProductRepository productRepository, OptionService optionService,
        CategoryService categoryService) {
        this.productRepository = productRepository;
        this.optionService = optionService;
        this.categoryService = categoryService;
    }

    // 로직을 변경했습니다. 옵션 추가를 무조건 product에서만 가능하게 해서 종속적인 관계로 구성합니다.
    // entity 단에서 강제로 options에 최소 하나 이상의 원소를 넣게 하려고 하였는데,
    // 그렇게 되면 option이 존재하지 않는 product의 id를 추가할 수도 있어서 entity 단에서 강제로 options에 최소 하나 이상의 원소를 넣게 하는 것을 포기하고
    // 제품을 추가하는 service에서 무조건 하나 이상을 넣도록 하였습니다.
    @Transactional
    public void insertProduct(CreateProductRequestDto createProductRequestDto) {
        var actualCategory = categoryService.selectCategory(createProductRequestDto.categoryId())
            .toCategory();

        var product = Product.of(createProductRequestDto.name(), createProductRequestDto.price(),
            createProductRequestDto.imageUrl(), actualCategory);
        var actualProduct = productRepository.save(product);

        var optionRequestDto = new OptionRequestDto(createProductRequestDto.optionName(),
            createProductRequestDto.optionQuantity());

        insertOption(actualProduct.getProductId(), optionRequestDto);
    }

    // repository를 호출해서 DB에 담긴 로우를 반환하는 함수
    @Transactional(readOnly = true)
    public ProductResponseDto selectProduct(long productId) {
        // 불러오면서 확인
        var product = productRepository.findById(productId)
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 제품입니다."));

        // 불러온 product의 category와 option을 dto로 변환
        return ProductResponseDto.fromProduct(product);
    }

    // 다른 service에서 사용할 로우를 반환하는 함수 (중복되어 사용자에게 불필요한 정보 포함)
    @Transactional(readOnly = true)
    public FullOptionsProductResponseDto selectFullOptionProduct(long productId) {
        var product = productRepository.findById(productId)
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 제품입니다."));

        return FullOptionsProductResponseDto.fromProduct(product);
    }

    // 전체 제품 정보를 반환하는 함수
    @Transactional(readOnly = true)
    public List<ProductResponseDto> selectProducts(PageInfoDto pageInfoDto) {
        var pageable = pageInfoDto.toPageInfo().toPageRequest();
        var products = productRepository.findAll(pageable);

        return products.stream().map(ProductResponseDto::fromProduct).toList();
    }

    // repository를 호출해서 특정 로우를 파라메터로 전달된 DTO로 교체하는 함수
    @Transactional
    public void updateProduct(long productId, ProductRequestDto productRequestDto,
        long categoryId) {
        // 불러오면서 확인
        var product = productRepository.findById(productId)
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 제품입니다."));

        var category = categoryService.selectCategory(categoryId).toCategory();

        product.updateProduct(productRequestDto.name(), productRequestDto.price(),
            productRequestDto.imageUrl(), category);
    }

    // option을 넣는 함수. product 입장에서는 변경
    @Transactional
    public void insertOption(long productId, OptionRequestDto optionRequestDto) {
        var actualProduct = productRepository.findById(productId)
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 제품입니다."));

        // 옵션 테이블에 옵션 추가
        var actualOption = optionService.insertOption(optionRequestDto, productId);
        // options에도 옵션 추가
        actualProduct.addNewOption(actualOption);
    }

    // option의 개수를 하나 차감시키는 메서드
    @Transactional
    public void subtractOption(long productId, long optionId, int quantity) {
        var product = productRepository.findById(productId)
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 제품입니다."));

        var option = product.getOptions().getOption(optionId);
        option.subtractQuantity(quantity);
    }

    // repository를 호출해서 특정 로우를 제거하는 함수
    @Transactional
    public void deleteProduct(long productId) {
        verifyExistence(productId);
        productRepository.deleteById(productId);
    }

    // delete에서만 사용하는 검사 메서드
    private void verifyExistence(long productId) {
        if (!productRepository.existsById(productId)) {
            throw new NoSuchElementException("삭제하려는 제품이 없습니다.");
        }
    }
}

package gift.service;

import gift.domain.ProductOption;
import gift.repository.ProductOptionRepository;
import gift.repository.ProductRepository;
import gift.web.dto.request.order.CreateOrderRequest;
import gift.web.dto.request.productoption.CreateProductOptionRequest;
import gift.web.dto.request.productoption.SubtractProductOptionQuantityRequest;
import gift.web.dto.request.productoption.UpdateProductOptionRequest;
import gift.web.dto.response.productoption.CreateProductOptionResponse;
import gift.web.dto.response.productoption.ReadAllProductOptionsResponse;
import gift.web.dto.response.productoption.ReadProductOptionResponse;
import gift.web.dto.response.productoption.SubtractProductOptionQuantityResponse;
import gift.web.dto.response.productoption.UpdateProductOptionResponse;
import gift.web.validation.exception.client.AlreadyExistsException;
import gift.web.validation.exception.client.ResourceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductOptionService {

    private final ProductOptionRepository productOptionRepository;
    private final ProductRepository productRepository;

    public ProductOptionService(ProductOptionRepository productOptionRepository,
        ProductRepository productRepository) {
        this.productOptionRepository = productOptionRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public CreateProductOptionResponse createOption(Long productId, CreateProductOptionRequest request) {
        validateExistsProduct(productId);
        String optionName = request.getName();
        validateOptionNameExists(productId, optionName);

        ProductOption createdOption = productOptionRepository.save(request.toEntity(productId));

        return CreateProductOptionResponse.fromEntity(createdOption);
    }

    /**
     * 상품이 존재하는지 확인합니다.
     * @param productId 상품 아이디
     */
    private void validateExistsProduct(Long productId) {
        productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("상품 아이디: ", productId.toString()));
    }

    /**
     * 상품 옵션 이름이 이미 존재하는지 확인합니다.<br>
     * 이미 존재한다면 {@link AlreadyExistsException} 을 발생시킵니다.
     * @param productId 상품 아이디
     * @param name 상품 옵션 이름
     */
    private void validateOptionNameExists(Long productId, String name) {
        validateOptionNameExists(null, productId, name);
    }

    /**
     * 상품 옵션 이름이 이미 존재하는지 확인합니다.<br>
     * 이미 존재한다면 {@link AlreadyExistsException} 을 발생시킵니다.
     * @param optionId 현재 상품 옵션 아이디 (옵션 새로 생성 시 null)
     * @param productId 상품 아이디
     * @param name 상품 옵션 이름
     */
    private void validateOptionNameExists(Long optionId, Long productId, String name) {
        if(optionId == null) {
            optionId = -1L;
        }
        productOptionRepository.findDuplicatedProductOption(optionId, productId, name)
            .ifPresent(duplicatedOptionId -> {
                throw new AlreadyExistsException(name);
            });
    }

    /**
     * 해당 상품에 옵션이 존재하지 않는 경우 최초 옵션 등록을 위해 사용됩니다.
     * @param productId 상품 아이디
     * @param request 상품 옵션 생성 요청
     * @return 상품 옵션 생성 응답
     */
    public List<CreateProductOptionResponse> createInitialOptions(Long productId, List<CreateProductOptionRequest> request) {
        List<ProductOption> productOptions = request.stream()
            .map(productOption -> productOption.toEntity(productId))
            .toList();
        validateDuplicateOptionNames(productOptions);

        List<ProductOption> createdOptions = productOptionRepository.saveAll(productOptions);

        return createdOptions.stream()
            .map(CreateProductOptionResponse::fromEntity)
            .toList();
    }

    /**
     * 상품 옵션 이름에 중복이 존재하는지 검열합니다<br>
     * 중복이 존재한다면 {@link IllegalStateException}을 발생시킵니다.
     * @param productOptions 상품 옵션 리스트
     */
    private void validateDuplicateOptionNames(List<ProductOption> productOptions) {
        long originalCount = productOptions.size();
        long distinctCount = productOptions.stream()
            .map(ProductOption::getName)
            .distinct()
            .count();

        if (originalCount != distinctCount) {
            throw new IllegalStateException("상품 옵션 이름에 중복이 존재합니다");
        }
    }

    public ReadAllProductOptionsResponse readAllOptions(Long productId) {
        List<ReadProductOptionResponse> options = productOptionRepository.findAllByProductId(productId)
            .stream()
            .map(ReadProductOptionResponse::fromEntity)
            .toList();
        return ReadAllProductOptionsResponse.from(options);
    }

    @Transactional
    public UpdateProductOptionResponse updateOption(Long optionId, Long productId, UpdateProductOptionRequest request) {
        String optionName = request.getName();
        validateOptionNameExists(optionId, productId, optionName);

        ProductOption option = productOptionRepository.findById(optionId)
            .orElseThrow(() -> new ResourceNotFoundException("상품 옵션", optionId.toString()));

        ProductOption updateParam = request.toEntity();
        option.update(updateParam);

        return UpdateProductOptionResponse.fromEntity(option);
    }

    @Transactional
    public SubtractProductOptionQuantityResponse subtractOptionStock(Long optionId, SubtractProductOptionQuantityRequest request) {
        ProductOption option = productOptionRepository.findById(optionId)
            .orElseThrow(() -> new ResourceNotFoundException("상품 옵션", optionId.toString()));

        option.subtractQuantity(request.getQuantity());

        return SubtractProductOptionQuantityResponse.fromEntity(option);
    }

    public SubtractProductOptionQuantityResponse subtractOptionStock(CreateOrderRequest request) {
        return subtractOptionStock(request.getOptionId(), new SubtractProductOptionQuantityRequest(request.getQuantity()));
    }

    @Transactional
    public void deleteOption(Long optionId) {
        ProductOption option = productOptionRepository.findById(optionId)
            .orElseThrow(() -> new ResourceNotFoundException("상품 옵션", optionId.toString()));

        productOptionRepository.delete(option);
    }

    @Transactional
    public void deleteAllOptionsByProductId(Long productId) {
        productOptionRepository.deleteAllByProductId(productId);
    }
}

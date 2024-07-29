package gift.service;

import gift.dto.giftorder.GiftOrderRequest;
import gift.dto.giftorder.GiftOrderResponse;
import gift.dto.option.OptionAddRequest;
import gift.dto.option.OptionResponse;
import gift.dto.option.OptionUpdateRequest;
import gift.exception.DuplicatedNameException;
import gift.exception.NotFoundElementException;
import gift.model.Option;
import gift.model.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;
    private final GiftOrderService giftOrderService;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository, GiftOrderService giftOrderService) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
        this.giftOrderService = giftOrderService;
    }

    public OptionResponse addOption(OptionAddRequest optionAddRequest) {
        optionNameValidation(optionAddRequest.productId(), optionAddRequest.name());
        var option = saveOptionWithOptionRequest(optionAddRequest);
        return getOptionResponseFromOption(option);
    }

    public void updateOption(Long id, OptionUpdateRequest optionUpdateRequest) {
        var option = findOptionById(id);
        option.updateOptionInfo(optionUpdateRequest.name(), optionUpdateRequest.quantity());
        optionRepository.save(option);
    }

    @Transactional(readOnly = true)
    public List<OptionResponse> getOptions(Long productId, Pageable pageable) {
        return optionRepository.findAllByProductId(productId, pageable)
                .stream()
                .map(this::getOptionResponseFromOption)
                .toList();
    }

    public void deleteOption(Long optionId) {
        if (!optionRepository.existsById(optionId)) {
            throw new NotFoundElementException("존재하지 않는 상품 옵션의 ID 입니다.");
        }
        giftOrderService.deleteAllByOptionId(optionId);
        optionRepository.deleteById(optionId);
    }

    public void deleteAllByProductId(Long productId) {
        optionRepository.deleteAllByProductId(productId);
    }

    public void makeDefaultOption(Product product) {
        var option = new Option(product, "기본", 1000);
        optionRepository.save(option);
    }

    public GiftOrderResponse orderOption(Long memberId, GiftOrderRequest giftOrderRequest) {
        var option = subtractOptionQuantity(giftOrderRequest.optionId(), giftOrderRequest.quantity());
        return giftOrderService.addGiftOrder(memberId, option, giftOrderRequest);
    }

    private Option subtractOptionQuantity(Long id, Integer quantity) {
        var option = optionRepository.findByIdWithLock(id)
                .orElseThrow(() -> new NotFoundElementException(id + "를 가진 상품 옵션이 존재하지 않습니다."));
        option.subtract(quantity);
        return optionRepository.save(option);
    }

    private Option saveOptionWithOptionRequest(OptionAddRequest optionAddRequest) {
        var product = findProductById(optionAddRequest.productId());
        var option = new Option(product, optionAddRequest.name(), optionAddRequest.quantity());
        return optionRepository.save(option);
    }

    private OptionResponse getOptionResponseFromOption(Option option) {
        return OptionResponse.of(option.getId(), option.getName(), option.getQuantity());
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundElementException(id + "를 가진 상품이 존재하지 않습니다."));
    }

    private Option findOptionById(Long id) {
        return optionRepository.findById(id)
                .orElseThrow(() -> new NotFoundElementException(id + "를 가진 상품 옵션이 존재하지 않습니다."));
    }

    private void optionNameValidation(Long productId, String name) {
        if (optionRepository.existsOptionByProductIdAndName(productId, name)) {
            throw new DuplicatedNameException("이미 존재하는 상품의 상품 옵션입니다.");
        }
    }
}

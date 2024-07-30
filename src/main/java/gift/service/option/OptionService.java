package gift.service.option;

import gift.dto.option.OptionRequest;
import gift.dto.option.OptionResponse;
import gift.model.product.Product;
import gift.model.option.Option;
import gift.repository.product.ProductRepository;
import gift.repository.option.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void addOptionToGift(Long giftId, OptionRequest.Create optionRequest) {
        Product product = productRepository.findById(giftId)
                .orElseThrow(() -> new NoSuchElementException("해당 상품을 찾을 수 없습니다 id :  " + giftId));
        Option option = optionRequest.toEntity();

        checkDuplicateOptionName(product, option.getName());

        product.addOption(option);
        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public OptionResponse.InfoList getAllOptions() {
        return OptionResponse.InfoList.fromEntity(optionRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<OptionResponse.Info> getOptionsByGiftId(Long giftId) {
        Product product = productRepository.findById(giftId)
                .orElseThrow(() -> new NoSuchElementException("해당 상품을 찾을 수 없습니다 id :  " + giftId));

        return product.getOptions().stream()
                .map(OptionResponse.Info::fromEntity)
                .toList();
    }

    @Transactional
    public void updateOptionToGift(Long giftId, Long optionId, OptionRequest.Update optionRequest) {
        Product product = productRepository.findById(giftId)
                .orElseThrow(() -> new NoSuchElementException("해당 상품을 찾을 수 없습니다 id :  " + giftId));
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new NoSuchElementException("해당 옵션을 찾을 수 없습니다 id :  " + optionId));

        checkOptionInGift(product, optionId);
        if (optionRequest.name() != null) {
            checkDuplicateOptionName(product, optionRequest.name());
            option.modify(optionRequest.name());
        }
        if (optionRequest.quantity() != null) {
            option.modify(optionRequest.quantity());
        }
    }

    @Transactional
    @Retryable(
            value = {ObjectOptimisticLockingFailureException.class},
            maxAttempts = 50,
            backoff = @Backoff(delay = 200)
    )
    public void subtractOptionToGift(Long giftId, Long optionId, int quantity) {
        Product product = productRepository.findById(giftId)
                .orElseThrow(() -> new NoSuchElementException("해당 상품을 찾을 수 없습니다 id :  " + giftId));
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new NoSuchElementException("해당 옵션을 찾을 수 없습니다 id :  " + optionId));

        checkOptionInGift(product, optionId);
        option.subtract(quantity);
        optionRepository.save(option);
    }

    @Transactional
    public void deleteOptionFromGift(Long giftId, Long optionId) {
        Product product = productRepository.findById(giftId)
                .orElseThrow(() -> new NoSuchElementException("해당 상품을 찾을 수 없습니다 id :  " + giftId));
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new NoSuchElementException("해당 옵션을 찾을 수 없습니다 id :  " + optionId));

        checkOptionInGift(product, optionId);

        product.removeOption(option);
        productRepository.save(product);
        optionRepository.delete(option);
    }

    public void checkOptionInGift(Product product, Long optionId) {
        if (!product.hasOption(optionId)) {
            throw new NoSuchElementException("해당 상품에 해당 옵션이 없습니다!");
        }
    }


    public void checkDuplicateOptionName(Product product, String optionName) {
        List<Option> options = product.getOptions();

        boolean isDuplicate = options.stream()
                .anyMatch(option -> option.getName().equals(optionName));

        if (isDuplicate) {
            throw new IllegalArgumentException("중복된 옵션 이름이 있습니다!");
        }

    }
}

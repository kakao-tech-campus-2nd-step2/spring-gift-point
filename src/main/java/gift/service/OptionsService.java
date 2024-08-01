package gift.service;

import gift.exception.option.DeleteOptionsException;
import gift.exception.option.DuplicateOptionsException;
import gift.exception.option.FailedRetryException;
import gift.exception.option.NotFoundOptionsException;
import gift.exception.product.NotFoundProductException;
import gift.model.Options;
import gift.model.Product;
import gift.repository.OptionsRepository;
import gift.repository.ProductRepository;
import gift.response.OptionResponse;
import gift.response.ProductOptionsResponse;
import gift.response.ProductResponse;
import java.util.List;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.ExhaustedRetryException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OptionsService {

    private final ProductRepository productRepository;
    private final OptionsRepository optionsRepository;

    public OptionsService(ProductRepository productRepository,
        OptionsRepository optionsRepository) {
        this.productRepository = productRepository;
        this.optionsRepository = optionsRepository;
    }

    public ProductOptionsResponse getAllProductOptions(Long productId) {
        productRepository.findById(productId)
            .orElseThrow(NotFoundProductException::new);
        List<Options> options = optionsRepository.findAllByProductId(productId);
        List<OptionResponse> optionResponse = options.stream()
            .map(OptionResponse::createOptionResponse)
            .toList();
        return new ProductOptionsResponse(optionResponse);
    }

    public Options getOption(Long id) {
        return optionsRepository.findById(id)
            .orElseThrow(NotFoundOptionsException::new);
    }

    public ProductOptionsResponse getProductOption(Product product, Long optionId) {
        List<Options> options = List.of(optionsRepository.findById(optionId)
            .orElseThrow(NotFoundOptionsException::new));

        List<OptionResponse> optionResponse = options.stream()
            .map(OptionResponse::createOptionResponse)
            .toList();
        ProductResponse productResponse = ProductResponse.createProductResponse(product);
        return new ProductOptionsResponse( optionResponse);
    }

    @Transactional
    public Options addOption(String name, Integer quantity, Long productId) {
        return productRepository.findById(productId)
            .map(product -> {
                    if (optionsRepository.findByNameAndProductId(name, productId).isPresent()) {
                        throw new DuplicateOptionsException();
                    }
                    return optionsRepository.save(new Options(name, quantity, product));
                }
            ).orElseThrow(NotFoundProductException::new);
    }

    @Transactional
    public Options updateOption(Long id, String name, Integer quantity, Long productId) {
        productRepository.findById(productId)
            .orElseThrow(NotFoundProductException::new);

        return optionsRepository.findById(id)
            .map(options -> {
                options.updateOption(name, quantity);
                return options;
            }).orElseThrow(NotFoundOptionsException::new);
    }

    @Transactional
    @Retryable( //@RetryOptions가 @Transactional보다 먼저 적용되게 설정됨
        retryFor = {ObjectOptimisticLockingFailureException.class},
        maxAttempts = 100,
        backoff = @Backoff(100)
    )
    public void subtractQuantity(Long id, Integer subQuantity, Long productId) {
        productRepository.findById(productId)
            .orElseThrow(NotFoundProductException::new);

        optionsRepository.findByIdForUpdate(id)
            .ifPresentOrElse(options ->
                    options.subtractQuantity(subQuantity),
                () -> {
                    throw new NotFoundOptionsException();
                });
    }

    @Transactional
    public void deleteOption(Long id, Long productId) {
        if (optionsRepository.optionsCountByProductId(productId) < 2) {
            throw new DeleteOptionsException();
        }

        optionsRepository.findById(id)
            .map(options -> {
                optionsRepository.delete(options);
                return options.getId();
            }).orElseThrow(NotFoundOptionsException::new);
    }

    @Transactional
    public void deleteAllOptions(Long productId) {
        optionsRepository.deleteAllByProductId(productId);
    }

    @Recover
    private void failedSubtractingOptionQuantity(RuntimeException e) {
        if (e instanceof ExhaustedRetryException || e instanceof ObjectOptimisticLockingFailureException) {
            throw new FailedRetryException();
        }
        throw e;
    }
}

package gift.option.application;

import gift.exception.type.ConcurrencyException;
import gift.exception.type.NotFoundException;
import gift.option.application.command.OptionCreateCommand;
import gift.option.application.command.OptionSubtractQuantityCommand;
import gift.option.application.command.OptionUpdateCommand;
import gift.option.domain.Option;
import gift.option.domain.OptionRepository;
import gift.product.domain.Product;
import gift.product.domain.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void subtractOptionQuantity(OptionSubtractQuantityCommand command) {
        Option option = optionRepository.findByIdWithLock(command.id())
                .orElseThrow(() -> new NotFoundException("해당 옵션이 존재하지 않습니다."));

        try {
            option.subtractQuantity(command.quantity());
            optionRepository.save(option);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new ConcurrencyException("동시성 문제가 발생했습니다. 다시 시도해주세요.");
        }
    }

    public List<OptionServiceResponse> findOptionsByProductId(Long productId) {
        return optionRepository.findAllByProductId(productId).stream()
                .map(OptionServiceResponse::from)
                .toList();
    }

    public Long save(OptionCreateCommand command) {
        Product product = productRepository.findById(command.productId())
                .orElseThrow(() -> new NotFoundException("해당 상품이 존재하지 않습니다."));

        Option option = optionRepository.save(command.toOption());
        product.addOption(option);

        return option.getId();
    }

    public void update(OptionUpdateCommand command) {
        Option option = optionRepository.findById(command.id())
                .orElseThrow(() -> new NotFoundException("해당 옵션이 존재하지 않습니다."));

        Product product = productRepository.findById(command.productId())
                .orElseThrow(() -> new NotFoundException("해당 상품이 존재하지 않습니다."));

        option.update(command.name(), command.quantity(), product);
    }

    public void delete(Long optionId, Long productId) {
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new NotFoundException("해당 옵션이 존재하지 않습니다."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 상품이 존재하지 않습니다."));

        product.removeOption(option);
        optionRepository.delete(option);
    }
}

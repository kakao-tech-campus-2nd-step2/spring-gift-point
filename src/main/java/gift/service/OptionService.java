package gift.service;

import gift.dto.request.OptionRequest;
import gift.dto.request.OrderRequest;
import gift.entity.Option;
import gift.exception.OptionNotFoundException;
import gift.repository.OptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptionService {

    private final OptionRepository optionRepository;

    public OptionService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    @Transactional
    public void subtractOptionQuantity(Long targetOptionId, int subtractQuantity) {
        Option targetOption = optionRepository.findByIdWithPessimisticWriteLock(targetOptionId)
                .orElseThrow(() -> new OptionNotFoundException(targetOptionId));

        targetOption.subtract(subtractQuantity);
    }

    public Option saveOption(OptionRequest optionRequest) {
        Option option = new Option(optionRequest.name(), optionRequest.quantity());
        return optionRepository.save(option);
    }

    public Long getProductIdByOptionId(OrderRequest orderRequest) {
        return optionRepository.findById(orderRequest.optionId())
                .map(Option::getId)
                .orElseThrow(() -> new OptionNotFoundException(orderRequest.optionId()));
    }
}

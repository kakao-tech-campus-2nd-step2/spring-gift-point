package gift.option.application;

import gift.exception.type.ConcurrencyException;
import gift.exception.type.NotFoundException;
import gift.option.application.command.OptionSubtractQuantityCommand;
import gift.option.domain.Option;
import gift.option.domain.OptionRepository;
import jakarta.transaction.Transactional;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
public class OptionService {

    private final OptionRepository optionRepository;

    public OptionService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
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
}

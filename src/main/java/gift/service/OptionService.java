package gift.service;

import gift.domain.option.Option;
import gift.domain.option.OptionRequest;
import gift.domain.option.OptionResponse;
import gift.domain.product.Product;
import gift.repository.OptionRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OptionService {

    private final OptionRepository OptionRepository;

    public OptionService(OptionRepository optionRepository) {
        this.OptionRepository = optionRepository;
    }

    public List<OptionResponse> findAllByProductId(Long productId) {
        return OptionRepository.findAllByProduct(new Product(productId))
            .stream()
            .map(OptionResponse::new)
            .toList();
    }

    @Transactional
    public OptionResponse save(Long productId, OptionRequest optionRequest) {
        OptionRepository.findAllByProductAndName(new Product(productId), optionRequest.name())
            .ifPresent((option) -> {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "해당 상품에 이미 동일한 이름의 옵션이 존재합니다: " + option.getName());
            });
        Option option = OptionRepository.save(optionRequest.toOption(productId));
        return new OptionResponse(option);
    }

    @Transactional
    public OptionResponse update(Long productId, Long optionId, OptionRequest optionRequest) {
        OptionRepository.findAllByProductAndName(new Product(productId), optionRequest.name())
            .ifPresent((option) -> {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "해당 상품에 이미 동일한 이름의 옵션이 존재합니다: " + option.getName());
            });
        Option option = OptionRepository.findById(optionId).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 옵션을 찾을 수 없습니다"));
        option.update(optionRequest);
        OptionRepository.save(option);
        return new OptionResponse(option);
    }

    @Transactional
    public OptionResponse subtractQuantity(Long optionId, Long quantity) {
        Option option = OptionRepository.findById(optionId).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 옵션을 찾을 수 없습니다"));
        if (option.getQuantity() < quantity) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "상품 수량이 부족합니다");
        }
        if (quantity <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "차감할 수량은 1 이상이여야 합니다");
        }
        option.subtractQuantity(quantity);
        OptionRepository.save(option);
        return new OptionResponse(option);
    }

    @Transactional
    public void delete(Long productId, Long optionId) {
        OptionRepository.deleteById(optionId);
    }
}

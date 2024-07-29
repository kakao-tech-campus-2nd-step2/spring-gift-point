package gift.main.service;

import gift.main.Exception.CustomException;
import gift.main.Exception.ErrorCode;
import gift.main.dto.OptionChangeQuantityRequest;
import gift.main.dto.OptionListRequest;
import gift.main.dto.OptionRequest;
import gift.main.dto.OptionResponse;
import gift.main.entity.Option;
import gift.main.entity.Product;
import gift.main.repository.OptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OptionService {

    private final OptionRepository optionRepository;

    public OptionService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    public List<OptionResponse> findAllOption(long productId) {
        List<OptionResponse> options = validOptions(productId)
                .stream().map(option -> new OptionResponse(option))
                .collect(Collectors.toList());

        return options;
    }

    @Transactional
    public void registerOptionsFirstTime(Product product, OptionListRequest optionListRequest) {
        optionListRequest.optionRequests().stream()
                .map(optionRequest -> new Option(optionRequest, product))
                .forEach(option -> optionRepository.save(option));
    }

    @Transactional
    public void deleteOption(long productId, long optionId) {
        List<Option> options = validOptions(productId);

        if (options.size() <= 1) {
            throw new CustomException(ErrorCode.CANNOT_DELETED_OPTION);
        }
        optionRepository.deleteById(optionId);
    }

    @Transactional
    public void addOption(long productId, OptionRequest optionRequest) {
        List<Option> options = validOptions(productId);

        options.stream().forEach(option -> option.isDuplicate(optionRequest));

        Product product = options.get(0).getProduct();
        Option newOption = new Option(optionRequest, product);
        optionRepository.save(newOption);
    }

    @Transactional
    public void updateOption(long productId, long optionId, OptionRequest optionRequest) {
        Option targetOption = validOption(optionId);
        List<Option> options = validOptions(productId);

        options.stream()
                .forEach(option -> option.isDuplicate(optionId, optionRequest));

        targetOption.updateValue(optionRequest);
    }

    @Transactional
    public void removeOptionQuantity(long optionId, OptionChangeQuantityRequest optionQuantityRequest) {
        Option targetOption = validOption(optionId);
        targetOption.sellOption(optionQuantityRequest);
    }

    @Transactional
    public void removeOptionQuantity(long optionId, int quantity) {
        Option targetOption = validOption(optionId);
        targetOption.sellOption(quantity);
    }

    private Option validOption(Long optionId) {
        Option targetOption = optionRepository.findById(optionId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_OPTION));
        return targetOption;
    }

    private List<Option> validOptions(Long productId) {
        List<Option> options = optionRepository.findAllByProductId(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_OPTION));
        return options;
    }
}

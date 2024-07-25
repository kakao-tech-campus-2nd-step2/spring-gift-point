package gift.service;

import gift.domain.Option;
import gift.domain.Product;
import gift.dto.request.AddOptionRequest;
import gift.dto.request.SubtractOptionRequest;
import gift.dto.response.MessageResponse;
import gift.exception.CustomException;
import gift.repository.OptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static gift.constant.Message.ADD_OPTION_SUCCESS_MSG;
import static gift.exception.ErrorCode.DATA_NOT_FOUND;

@Service
public class OptionService {

    private final OptionRepository optionRepository;

    public OptionService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }


    public List<Option> getOptions(Product product) {
        return optionRepository.findAllByProduct(product);
    }

    public MessageResponse addOption(Product product, AddOptionRequest addOptionRequest) {
        optionRepository.save(new Option(addOptionRequest, product));
        return new MessageResponse(ADD_OPTION_SUCCESS_MSG);
    }

    public void subtractOptionQuantity(Product product, SubtractOptionRequest subtractOptionRequest) {
        Option option = optionRepository.findAllByProductAndName(product, subtractOptionRequest.optionName()).orElseThrow(() -> new CustomException(DATA_NOT_FOUND));
        option.subtract(subtractOptionRequest.amount());
    }
}

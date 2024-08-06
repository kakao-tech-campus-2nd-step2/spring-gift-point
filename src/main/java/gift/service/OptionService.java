package gift.service;

import gift.domain.Option;
import gift.domain.Product;
import gift.dto.request.AddOptionRequest;
import gift.dto.request.OrderRequest;
import gift.dto.response.MessageResponse;
import gift.dto.response.ProductOptionResponse;
import gift.exception.CustomException;
import gift.repository.OptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static gift.constant.SuccessMessage.ADD_OPTION_SUCCESS_MSG;
import static gift.exception.ErrorCode.DATA_NOT_FOUND;

@Service
public class OptionService {

    private final OptionRepository optionRepository;

    public OptionService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }


    public List<ProductOptionResponse> getOptions(Product product) {
        List<Option> options = optionRepository.findAllByProduct(product);
        return options.stream().map(ProductOptionResponse::new).toList();
    }

    public MessageResponse addOption(Product product, AddOptionRequest addOptionRequest) {
        optionRepository.save(new Option(addOptionRequest, product));
        return new MessageResponse(ADD_OPTION_SUCCESS_MSG);
    }
}

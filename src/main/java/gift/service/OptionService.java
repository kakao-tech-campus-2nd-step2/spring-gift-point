package gift.service;

import gift.domain.Option;
import gift.domain.Product;
import gift.dto.request.OptionRequestDto;
import gift.dto.response.OptionResponseDto;
import gift.exception.customException.DenyDeleteException;
import gift.exception.customException.EntityNotFoundException;
import gift.exception.customException.NameDuplicationException;
import gift.repository.option.OptionRepository;
import gift.repository.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static gift.exception.exceptionMessage.ExceptionMessage.*;

@Service
@Transactional(readOnly = true)
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public OptionResponseDto saveOption(OptionRequestDto optionRequestDto, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND));

        optionRepository.findOptionByNameAndProductId(optionRequestDto.optionName(), productId)
                .ifPresent(
                        e -> {
                            throw new NameDuplicationException(OPTION_NAME_DUPLICATION);
                        }
                );

        Option option = new Option(optionRequestDto.optionName(), optionRequestDto.optionQuantity());

        option.addProduct(product);

        Option savedOption = optionRepository.save(option);

        return OptionResponseDto.from(savedOption);
    }

    public List<OptionResponseDto> findOptionsByProduct(Long productId) {
        return optionRepository.findOptionsByProductId(productId).stream()
                .map(OptionResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OptionResponseDto updateOption(OptionRequestDto optionRequestDto, Long optionId) {
        Option findOption = optionRepository.findOptionByIdForUpdate(optionId)
                .orElseThrow(() -> new EntityNotFoundException(OPTION_NOT_FOUND));

        findOption.update(optionRequestDto);

        return OptionResponseDto.from(findOption);
    }

    @Transactional
    public OptionResponseDto updateOptionQuantity(Long optionId, int quantity) {
        Option findOption = optionRepository.findOptionByIdForUpdate(optionId)
                .orElseThrow(() -> new EntityNotFoundException(OPTION_NOT_FOUND));

        findOption.updateQuantity(quantity);

        return OptionResponseDto.from(findOption);
    }

    @Transactional
    public OptionResponseDto deleteOneOption(Long productId, Long optionId) {
        Option option = optionRepository.findById(optionId).orElseThrow(() -> new EntityNotFoundException(OPTION_NOT_FOUND));

        Long count = optionRepository.countOptionByProductId(productId);

        if (count == 1) {
            throw new DenyDeleteException();
        }

        optionRepository.delete(option);

        return OptionResponseDto.from(option);
    }
}

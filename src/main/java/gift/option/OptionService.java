package gift.option;

import gift.exception.AlreadyExistOption;
import gift.exception.NotFoundOption;
import gift.product.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    public OptionResponseDto postOption(OptionRequestDto optionRequestDto, Long productId) {
        Option option = new Option(
            optionRequestDto.name(),
            optionRequestDto.quantity(),
            productRepository.findById(productId).get()
        );
        if (optionRepository.findByName(option.getName()).isPresent()) {
            throw new AlreadyExistOption("동일한 이름의 옵션이 이미 존재합니다");
        }

        optionRepository.saveAndFlush(option);

        return new OptionResponseDto(
            option.getId(),
            option.getName(),
            option.getQuantity()
        );
    }

    public List<OptionResponseDto> findOption(Long productId) {
        List<Option> options =  optionRepository.findAllByProductId(productId);
        return options.stream()
            .map(option -> new OptionResponseDto(option.getId(), option.getName(), option.getQuantity()))
            .collect(Collectors.toList());
    }

    public OptionResponseDto updateOption(Long optionId, OptionRequestDto optionRequestDto) throws NotFoundOption {
        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new NotFoundOption("해당 옵션을 찾을 수 없습니다"));

        option.update(optionRequestDto.name(), optionRequestDto.quantity());
        optionRepository.saveAndFlush(option);

        return new OptionResponseDto(
            option.getId(),
            option.getName(),
            option.getQuantity()
        );
    }

    public void deleteOptionById(Long optionId) throws NotFoundOption {
        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new NotFoundOption("해당 옵션을 찾을 수 없습니다"));
        optionRepository.delete(option);
    }

    public OptionResponseDto substractQuantity(Long optionId, int quantity) {
        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new NotFoundOption("해당 옵션을 찾을 수 없습니다"));
        option.substract(quantity);
        return new OptionResponseDto(
            option.getId(),
            option.getName(),
            option.getQuantity()
        );
    }

}

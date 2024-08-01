package gift.service;

import gift.dto.OptionRequestDTO;
import gift.dto.OptionResponseDTO;
import gift.model.Option;
import gift.model.Product;
import gift.repository.OptionRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OptionService {

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ProductService productService;

    public List<OptionResponseDTO> getOptionsByProductId(Long productId) {
        Product product = productService.findProductEntityById(productId); // 명세에 따른 수정: productService를 통한 product 조회
        List<Option> options = optionRepository.findByProduct(product);
        return options.stream().map(OptionResponseDTO::fromEntity).collect(Collectors.toList());
    }

    public OptionResponseDTO createOption(OptionRequestDTO optionRequestDTO) {
        Product product = productService.findProductEntityById(optionRequestDTO.getProductId()); // 명세에 따른 수정: productService를 통한 product 조회
        Option option = optionRequestDTO.toEntity(product);
        option = optionRepository.save(option); // 수정: 저장된 엔티티 반환 후 OptionResponseDTO 변환
        return OptionResponseDTO.fromEntity(option);
    }

    public Optional<OptionResponseDTO> updateOption(Long optionId, OptionRequestDTO optionRequestDTO) {
        Optional<Option> option = optionRepository.findById(optionId);
        if (option.isPresent()) {
            Option existingOption = option.get();
            existingOption.setName(optionRequestDTO.getName());
            existingOption.setQuantity(optionRequestDTO.getQuantity());
            optionRepository.save(existingOption);
            return Optional.of(OptionResponseDTO.fromEntity(existingOption));
        }
        return Optional.empty();
    }

    public boolean deleteOption(Long id) {
        if (optionRepository.findById(id).isPresent()) {
            optionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void subtractOptionQuantity(Long optionId, int quantityToSubtract) {
        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new IllegalArgumentException("옵션을 찾을 수 없습니다."));
        option.subtractQuantity(quantityToSubtract);
        optionRepository.save(option);
    }

    public Option findOptionById(Long optionId) {
        return optionRepository.findById(optionId)
            .orElseThrow(() -> new IllegalArgumentException("옵션 정보를 찾을 수 없습니다."));
    }
}

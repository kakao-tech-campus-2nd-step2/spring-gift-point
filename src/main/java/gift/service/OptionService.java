package gift.service;

import gift.domain.Option;
import gift.domain.Option.Options;
import gift.domain.Product;
import gift.dto.requestdto.OptionCreateRequestDTO;
import gift.dto.requestdto.OptionNameUpdateRequestDTO;
import gift.dto.responsedto.OptionResponseDTO;
import gift.repository.JpaOptionRepository;
import gift.repository.JpaProductRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OptionService {
    private final JpaOptionRepository jpaOptionRepository;
    private final JpaProductRepository jpaProductRepository;

    public OptionService(JpaOptionRepository jpaOptionRepository,
        JpaProductRepository jpaProductRepository) {
        this.jpaOptionRepository = jpaOptionRepository;
        this.jpaProductRepository = jpaProductRepository;
    }

    @Transactional(readOnly = true)
    public List<OptionResponseDTO> getAllCategoriesByProductId(Long productId) {
        Product product = getProduct(productId);
        Options optionList = new Options(jpaOptionRepository.findAllByProduct(product));
        optionList.validOptionCount();

        return optionList.getOptionList()
            .stream()
            .map(OptionResponseDTO::of)
            .toList();
    }

    public Long addOption(Long productId, OptionCreateRequestDTO optionCreateRequestDTO) {
        Product product = getProduct(productId);
        Options optionList = new Options(jpaOptionRepository.findAllByProduct(product));
        optionList.validDuplicateName(optionCreateRequestDTO.name());
        Option option = optionCreateRequestDTO.toEntity(product);
        return jpaOptionRepository.save(option).getId();
    }

    public Long updateOptionName(Long optionId,
        OptionNameUpdateRequestDTO optionNameUpdateRequestDTO) {
        Option option = getOption(optionId);
        option.updateName(optionNameUpdateRequestDTO.name());
        return option.getId();
    }

    public Long deleteOption(Long optionId) {
        Option option = getOption(optionId);
        Options optionList = new Options(
            jpaOptionRepository.findAllByProduct(option.getProduct()));
        optionList.validDelete();
        jpaOptionRepository.delete(option);
        return option.getId();
    }

    public void subtractOptionQuantity(Long optionId, int count){
        Option option = getOption(optionId);
        if (option.getQuantity() < count){
            throw new IllegalArgumentException("현재 남은 옵션 개수보다 많습니다.");
        }
        option.subtract(count);
    }

    private Option getOption(Long optionId) {
        return jpaOptionRepository.findById(optionId)
            .orElseThrow(() -> new NoSuchElementException("id가 잘못되었습니다."));
    }

    private Product getProduct(Long productId) {
        return jpaProductRepository.findById(productId)
            .orElseThrow(() -> new NoSuchElementException("id가 잘못되었습니다."));
    }
}
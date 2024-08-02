package gift.Mapper;

import gift.Entity.OptionEntity;
import gift.DTO.OptionDTO;
import gift.Repository.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OptionServiceMapper {

    @Autowired
    private OptionRepository optionRepository;

    public OptionEntity findOptionEntityById(Long id) {
        return optionRepository.findById(id).orElseThrow(() -> new RuntimeException("Option을 찾을 수 없습니다."));
    }

    public OptionDTO convertToDTO(OptionEntity optionEntity) {
        return new OptionDTO(
                optionEntity.getId(),
                optionEntity.getName(),
                optionEntity.getQuantity(),
                optionEntity.getProduct() != null ? optionEntity.getProduct().getId() : null
        );
    }

    public void validateOptionNameUniqueness(String name, Long productId) {
        List<OptionEntity> options = optionRepository.findByProductId(productId);
        for (OptionEntity option : options) {
            if (option.getName().equals(name)) {
                throw new RuntimeException("동일한 상품 내에서 옵션 이름이 중복될 수 없습니다.");
            }
        }
    }
}

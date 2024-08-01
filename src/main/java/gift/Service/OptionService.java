package gift.Service;

import gift.Entity.OptionEntity;
import gift.Entity.ProductEntity;
import gift.DTO.OptionDTO;
import gift.Repository.OptionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import gift.Mapper.OptionServiceMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OptionService {

    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    private OptionServiceMapper optionServiceMapper;

    // DB에 접근하기 때문에 트랜잭션 처리 추가
    @Transactional
    public OptionDTO createOption(OptionDTO optionDTO) {
        optionServiceMapper.validateOptionNameUniqueness(optionDTO.getName(), optionDTO.getProductId());
        OptionEntity optionEntity = new OptionEntity(
                optionDTO.getName(),
                optionDTO.getQuantity(),
                new ProductEntity()
        );
        optionEntity = optionRepository.save(optionEntity);
        return optionServiceMapper.convertToDTO(optionEntity);
    }

    public OptionDTO getOptionById(Long id) {
        OptionEntity optionEntity = optionServiceMapper.findOptionEntityById(id);
        return optionServiceMapper.convertToDTO(optionEntity);
    }

    public List<OptionDTO> getAllOptions() {
        List<OptionEntity> optionEntities = optionRepository.findAll();
        return optionEntities.stream().map(optionServiceMapper::convertToDTO).collect(Collectors.toList());
    }

    // DB에 접근하기 때문에 트랜잭션 처리 추가
    @Transactional
    public OptionDTO updateOption(Long id, OptionDTO optionDTO) {
        OptionEntity optionEntity = optionServiceMapper.findOptionEntityById(id);
        if (!optionEntity.getName().equals(optionDTO.getName())) {
            optionServiceMapper.validateOptionNameUniqueness(optionDTO.getName(), optionDTO.getProductId());
        }
        optionEntity.setName(optionDTO.getName());
        optionEntity.setQuantity(optionDTO.getQuantity());
        optionEntity = optionRepository.save(optionEntity);
        return optionServiceMapper.convertToDTO(optionEntity);
    }

    @Transactional
    public OptionDTO subtractQuantity(Long id, Long subtractQuantity, OptionDTO optionDTO) {
        OptionEntity optionEntity = optionServiceMapper.findOptionEntityById(id);
        if (!optionEntity.getName().equals(optionDTO.getName())) {
            optionServiceMapper.validateOptionNameUniqueness(optionDTO.getName(), optionDTO.getProductId());
        }
        if (subtractQuantity > optionEntity.getQuantity()) {
            throw new RuntimeException("감소시키려는 수량보다 남은 재고가 적습니다.");
        }
        optionEntity.setQuantity(optionEntity.getQuantity() - subtractQuantity);
        optionEntity.setName(optionDTO.getName());
        optionEntity = optionRepository.save(optionEntity);
        return optionServiceMapper.convertToDTO(optionEntity);
    }

    // DB에 접근하기 때문에 트랜잭션 처리 추가
    @Transactional
    public void deleteOption(Long id) {
        optionRepository.deleteById(id);
    }
}

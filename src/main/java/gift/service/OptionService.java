package gift.service;

import gift.dto.OptionDTO;
import gift.entity.OptionEntity;
import gift.entity.ProductEntity;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    public List<OptionEntity> readProductAllOption(Long productId) {
        ProductEntity product = productRepository.findById(productId)
            .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다. ID: " + productId));
        return optionRepository.findByProductId(productId);
    }

    // 옵션이 존재 하지 않는 경우에 대한 예외처리
    public OptionEntity readProductOption(Long productId, Long optionId) {
        ProductEntity product = productRepository.findById(productId)
            .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다. ID: " + productId));

        OptionEntity optionEntity = optionRepository.findById(optionId)
            .orElseThrow(() -> new EntityNotFoundException("해당 옵션이 존재하지 않습니다. ID: " + optionId));
        return optionEntity;
    }

    @Transactional
    public void createOption(Long productId, OptionDTO optionDTO) {
        ProductEntity product = productRepository.findById(productId)
            .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다. ID: " + productId));

        OptionEntity optionEntity = new OptionEntity(optionDTO.getName(), optionDTO.getQuantity(), product);
        optionRepository.save(optionEntity);
    }

    @Transactional
    public void editOption(Long productId, Long optionId, OptionDTO optionDTO) {
        ProductEntity product = productRepository.findById(productId)
            .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다. ID: " + productId));

        OptionEntity optionEntity = optionRepository.findById(optionId)
            .orElseThrow(() -> new EntityNotFoundException("해당 옵션이 존재하지 않습니다. ID: " + optionId));

        optionEntity.update(optionDTO.getName(), optionDTO.getQuantity());

        optionRepository.save(optionEntity);
    }

    @Transactional
    public void deleteOption(Long productId, Long optionId) {
        ProductEntity product = productRepository.findById(productId)
            .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다. ID: " + productId));

        OptionEntity optionEntity = optionRepository.findById(optionId)
            .orElseThrow(() -> new EntityNotFoundException("해당 옵션이 존재하지 않습니다. ID: " + optionId));
        optionRepository.delete(optionEntity);
    }

    @Transactional
    public void subtractOptionQuantity(Long optionId, int amountToSubtract) {
        OptionEntity option = optionRepository.findById(optionId)
            .orElseThrow(() -> new NoSuchElementException("해당 옵션이 존재하지 않습니다. ID: " + optionId));
        option.subtractQuantity(amountToSubtract);
        optionRepository.save(option);
    }
}

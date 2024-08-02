package gift.service;

import gift.domain.Option;
import gift.domain.Option.OptionRequest;
import gift.domain.Option.OptionResponse;
import gift.entity.OptionEntity;
import gift.entity.ProductEntity;
import gift.error.AlreadyExistsException;
import gift.error.NotFoundException;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    //해당 상품의 옵션들 조회
    @Transactional(readOnly = true)
    public List<OptionResponse> getAllOptionByProductId(Long productId) {
        ProductEntity productEntity = productRepository.findById(productId)
            .orElseThrow(() -> new NotFoundException("Product not found"));
        return productEntity.getOptionEntities().stream()
            .map(OptionResponse::from).toList();
    }

    //해당 상품 내의 단일 옵션 조회
    @Transactional(readOnly = true)
    public OptionResponse getOptionByProductIdAndId(Long productId, Long id) {
        ProductEntity productEntity = productRepository.findById(productId)
            .orElseThrow(() -> new NotFoundException("Product not found"));
        return productEntity.getOptionEntities().stream()
            .filter(o -> o.getId().equals(id))
            .findAny()
            .map(OptionResponse::from)
            .orElseThrow(() -> new NotFoundException("Option not found"));

    }

    //해당 상품 내의 옵션을 이름으로 조회
    @Transactional(readOnly = true)
    public OptionResponse getOptionByProductIdAndName(Long productId, String name) {
        ProductEntity productEntity = productRepository.findById(productId)
            .orElseThrow(() -> new NotFoundException("Product not found"));
        return productEntity.getOptionEntities().stream()
            .filter(o -> o.getName().equals(name))
            .findAny()
            .map(OptionResponse::from)
            .orElseThrow(() -> new NotFoundException("Option not found"));
    }

    //옵션 추가 기능
    @Transactional
    public void addOption(Long productId, OptionRequest request) {
        validateOptionUniqueness(request, null);
        ProductEntity productEntity = productRepository.findById(productId)
            .orElseThrow(() -> new NotFoundException("Product not found"));
        OptionEntity optionEntity = new OptionEntity(
            request.name(),
            request.quantity(),
            productEntity
        );
        productEntity.getOptionEntities().add(optionEntity);
        optionRepository.save(optionEntity);
    }

    //옵션 수정 기능
    @Transactional
    public void updateOption(Long optionId, OptionRequest request) {
        validateOptionUniqueness(request, optionId);
        OptionEntity optionEntity = optionRepository.findById(optionId)
            .orElseThrow(() -> new NotFoundException("Option not found"));
        optionEntity.updateOptionEntity(
            request.name(),
            request.quantity()
        );
        optionRepository.save(optionEntity);
    }

    //옵션 수량 감소 기능
    @Transactional
    public void subQuantity(Long id, Long amoutToSubtract) {
        OptionEntity optionEntity = optionRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Option not found"));
        if(optionEntity.getQuantity() < amoutToSubtract) {
            throw new IllegalArgumentException("옵션 수량이 줄이고자 하는 수량보다 적습니다.");
        }
        optionEntity.subtractQuantity(amoutToSubtract);
        optionRepository.save(optionEntity);
    }

    //옵션 삭제 기능
    @Transactional
    public void deleteOption(Long id) {
        optionRepository.deleteById(id);
    }

    private void validateOptionUniqueness(OptionRequest request, Long id) {
        if(optionRepository.existsByNameAndIdNot(request.name(), id)) {
            throw new AlreadyExistsException("Already Exists Option");
        }
    }

}

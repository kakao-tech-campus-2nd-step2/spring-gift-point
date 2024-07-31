package gift.service;

import gift.dto.OptionResponseDto;
import gift.entity.Option;
import gift.exception.OptionException;
import gift.repository.OptionRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class OptionService {

    private final OptionRepository optionRepository;

    public OptionService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    public List<OptionResponseDto> findAllByProductId(Long id){
        return optionRepository.findByProductId(id).stream()
            .map(op -> new OptionResponseDto(op.getId(),op.getName(),op.getQuantity()))
            .collect(Collectors.toList());
    }

    public Optional<Option> findById(Long id){
        return optionRepository.findById(id);
    }

//    public Option save(Option option){
//        return optionRepository.save(option);
//    }

    public Option save(Option option) {
        if (optionRepository.existsByNameAndProductId(option.getName(), option.getProduct().getId())) {
            throw new OptionException("동일한 상품 내에 중복된 옵션이 있습니다.");
        }
        return optionRepository.save(option);
    }

    public Option update(Option option) {
        if (optionRepository.existsByNameAndProductId(option.getName(), option.getProduct().getId()) && !option.getId().equals(option.getId())) {
            throw new OptionException("동일한 상품 내에 중복된 옵션이 있습니다.");
        }
        return optionRepository.save(option);
    }

    public List<Option> findByIds(List<Long> ids) {
        return optionRepository.findAllById(ids);
    }

    public void deleteById(Long id){
        optionRepository.deleteById(id);
    }

    public void decreaseQuantity(Long id, int quantity){
        Option option = optionRepository.findById(id)
            .orElseThrow(() -> new OptionException("옵션을 찾을 수 없습니다."));
        if(option.getQuantity() < quantity){
            throw new OptionException("수량이 부족합니다.");
        }

        option.setQuantity(option.getQuantity() - quantity);
        optionRepository.save(option);
    }

    public boolean existsByNameAndProductId(String name, Long productId) {
        return optionRepository.existsByNameAndProductId(name, productId);
    }




}

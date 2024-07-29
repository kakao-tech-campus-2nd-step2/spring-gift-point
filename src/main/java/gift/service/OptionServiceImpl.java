package gift.service;

import gift.dto.OptionDTO;
import gift.model.Option;
import gift.model.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OptionServiceImpl implements OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OptionServiceImpl(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<OptionDTO> getOptionsByProductId(Long productId) {
        return optionRepository.findByProductId(productId).stream()
            .map(OptionDTO::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public OptionDTO getOptionById(Long id) {
        return optionRepository.findById(id).map(OptionDTO::convertToDTO).orElse(null);
    }

    @Override
    public void saveOption(OptionDTO optionDTO) {
        Product product = productRepository.findById(optionDTO.getProductId()).orElseThrow();
        Option option = new Option(optionDTO.getName(), optionDTO.getQuantity(), optionDTO.getPrice(), product, optionDTO.getMaxQuantity()); // 최대 옵션 수량 추가
        optionRepository.save(option);
    }

    @Override
    public void updateOption(Long id, OptionDTO optionDTO) {
        Option option = optionRepository.findById(id).orElseThrow();
        option.setName(optionDTO.getName());
        option.setQuantity(optionDTO.getQuantity());
        option.setPrice(optionDTO.getPrice());
        option.setMaxQuantity(optionDTO.getMaxQuantity()); // 최대 옵션 수량 추가
        optionRepository.save(option);
    }

    @Override
    public void deleteOption(Long id) {
        optionRepository.deleteById(id);
    }
}

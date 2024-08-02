package gift.service;

import gift.dto.option.OptionQuantityDTO;
import gift.dto.option.OptionResponseDTO;
import gift.dto.option.SaveOptionDTO;
import gift.entity.Option;
import gift.entity.Product;
import gift.exception.exception.BadRequestException;
import gift.exception.exception.NotFoundException;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OptionService {
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;


    public OptionResponseDTO saveOption(int productId, SaveOptionDTO saveOptionDTO) {
        Product product = findProductById(productId);
        checkDuplicateOption(productId, saveOptionDTO);
        Option option = new Option(product, saveOptionDTO.name());
        return optionRepository.save(option).toResponseDTO();
    }

    public OptionResponseDTO deleteOption(int productId, int optionId) {
        Product product = findProductById(productId);
        Option option = findOptionById(optionId);
        checkContains(product, option);

        option.getProduct().deleteOption(option);
        optionRepository.deleteById(optionId);
        return option.toResponseDTO();
    }

    @Transactional
    public OptionResponseDTO updateOption(int productId, int optionId, SaveOptionDTO saveOptionDTO) {
        Option option = findOptionById(optionId);
        Product product = findProductById(productId);
        checkContains(product, option);
        product.getOptions().remove(option);

        checkDuplicateOption(productId, saveOptionDTO);
        option = option.changeOption(saveOptionDTO);
        product.addOptions(option);

        return option.toResponseDTO();
    }

    public List<OptionResponseDTO> getOptions(int productId) {
        findProductById(productId);
        return optionRepository.findByProductId(productId);
    }

    @Transactional
    public OptionResponseDTO refillQuantity(int productId, int optionId, OptionQuantityDTO optionQuantityDTO) {
        Product product = findProductById(productId);
        Option option = findOptionById(optionId);
        checkContains(product, option);

        option = option.addQuantity(optionQuantityDTO.quantity());
        return option.toResponseDTO();
    }

    private Product findProductById(int productId) {
        return productRepository.findById(productId).orElseThrow(() -> new NotFoundException("해당 물품이 없음"));
    }

    private void checkDuplicateOption(int productId, SaveOptionDTO saveOptionDTO) {
        optionRepository.findByOption(productId, saveOptionDTO.name()).ifPresent(c -> {
            throw new BadRequestException("이미 존재하는 옵션");
        });
    }

    private static void checkContains(Product product, Option option) {
        if (!product.getOptions().contains(option)) throw new NotFoundException("제품id에 해당하는 옵션id가 없음");
    }

    private Option findOptionById(int optionId) {
        return optionRepository.findById(optionId).orElseThrow(() -> new NotFoundException("해당 옵션이 없음"));
    }

}

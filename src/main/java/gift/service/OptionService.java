package gift.service;

import gift.dto.OptionRequestDto;
import gift.dto.OptionResponseDto;
import gift.entity.Option;
import gift.entity.Product;
import gift.exception.BusinessException;
import gift.exception.ServiceException;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptionService {

    private final OptionRepository optionRepository;

    private final ProductRepository productRepository;

    @Autowired
    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    public List<OptionResponseDto> getAllOptions() {
        List<Option> options = optionRepository.findAll();

        return options.stream()
            .map(OptionResponseDto::new)
            .collect(Collectors.toList());
    }

    public List<OptionResponseDto> getOptionsByProductId(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ServiceException("존재하지 않은 상품입니다.", HttpStatus.NOT_FOUND));

        return product.getOptions().stream().map(OptionResponseDto::new)
            .collect(Collectors.toList());
    }

    public void addOptionToProduct(Long productId, OptionRequestDto request) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ServiceException("존재하지 않은 상품입니다.", HttpStatus.NOT_FOUND));

        if (optionRepository.existsByNameAndProductId(request.getName(), productId)) {
            throw new ServiceException("동일한 상품 내의 옵션 이름은 중복될 수 없습니다.", HttpStatus.CONFLICT);
        }

        var option = new Option(request.getName(), request.getQuantity(), product);
        optionRepository.save(option);
    }

    @Transactional
    public void subtractOptionQuantity(Long optionId, Long requestQuantity) {
        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new BusinessException("해당 id에 대한 옵션이 없습니다."));
        Long currentQuantity = option.getQuantity();

        if (currentQuantity - requestQuantity < 0) {
            throw new BusinessException("수량은 0보다 작을 수 없습니다.");
        }

        option.subtract(requestQuantity);
    }

    public void deleteOption(Long productId, Long optionId) {
        if (!optionRepository.existsByIdAndProductId(optionId, productId)) {
            throw new ServiceException("존재하지 않는 상품 또는 옵션입니다.", HttpStatus.NOT_FOUND);
        }

        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new ServiceException("존재하지 않는 옵션입니다.", HttpStatus.NOT_FOUND));

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ServiceException("존재하지 않은 상품입니다.", HttpStatus.NOT_FOUND));

        List<Option> options = optionRepository.findByProductId(productId);
        if (options.size() <= 1) {
            throw new ServiceException("상품에는 최소 하나 이상의 옵션이 있어야 합니다.", HttpStatus.BAD_REQUEST);
        }

        optionRepository.delete(option);
    }
}
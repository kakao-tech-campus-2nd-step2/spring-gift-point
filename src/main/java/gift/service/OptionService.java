package gift.service;

import static gift.controller.option.OptionMapper.toOptionResponse;

import gift.controller.option.OptionMapper;
import gift.controller.option.OptionRequest;
import gift.controller.option.OptionResponse;
import gift.domain.Option;
import gift.exception.OptionAlreadyExistsException;
import gift.exception.OptionNotExistsException;
import gift.repository.OptionRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductService productService;

    public OptionService(OptionRepository optionRepository, ProductService productService) {
        this.optionRepository = optionRepository;
        this.productService = productService;
    }

    @Transactional(readOnly = true)
    public Page<OptionResponse> findAll(Pageable pageable) {
        Page<Option> optionPage = optionRepository.findAll(pageable);
        List<OptionResponse> optionResponses = optionPage.stream()
            .map(OptionMapper::toOptionResponse).toList();
        return new PageImpl<>(optionResponses, pageable, optionPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<OptionResponse> findAllByProductId(UUID productId, Pageable pageable) {
        Page<Option> optionPage = optionRepository.findAllByProductId(productId, pageable);
        List<OptionResponse> optionResponses = optionPage.stream()
            .map(OptionMapper::toOptionResponse).toList();
        return new PageImpl<>(optionResponses, pageable, optionPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public OptionResponse getOptionResponseById(UUID id) {
        Option target = optionRepository.findById(id)
            .orElseThrow(OptionNotExistsException::new);
        return toOptionResponse(target);
    }

    @Transactional(readOnly = true)
    public Option findById(UUID id) {
        return optionRepository.findById(id)
            .orElseThrow(OptionNotExistsException::new);
    }

    @Transactional
    public OptionResponse save(UUID productId, OptionRequest option) {
        optionRepository.findByNameAndProduct(option.name(), productService.find(productId)).ifPresent(p -> {
            throw new OptionAlreadyExistsException();
        });
        return toOptionResponse(optionRepository.save(
            new Option(option.name(), option.quantity(), productService.find(productId))));
    }

    @Transactional
    public OptionResponse update(UUID id, OptionRequest option) {
        Option target = optionRepository.findById(id)
            .orElseThrow(OptionNotExistsException::new);
        optionRepository.findByNameAndProduct(target.getName(), productService.find(target.getProductId())).ifPresent(p -> {
            throw new OptionAlreadyExistsException();
        });
        target.updateDetails(option.name(), option.quantity());
        return toOptionResponse(target);
    }

    @Transactional
    public OptionResponse subtract(UUID optionId, Integer quantity) {
        Option target = optionRepository.findById(optionId).orElseThrow(OptionNotExistsException::new);
        return toOptionResponse(target.subtractQuantity(quantity));
    }

    @Transactional
    public void delete(UUID id) {
        optionRepository.findById(id).orElseThrow(OptionNotExistsException::new);
        optionRepository.deleteById(id);
    }
}
package gift.option;

import gift.product.Product;
import gift.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OptionService {
    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    public List<OptionResponse> findAllProductOptions(Long productID) {
        return optionRepository.findAllByProductId(productID).stream().map(OptionResponse::new).toList();
    }

    public OptionResponse insertProductNewOption(Long productID, OptionRequest optionRequest) {
        if(checkIfDuplicatedOption(productID, optionRequest)) throw new IllegalArgumentException("이미 존재하는 옵션명입니다.");
        Product product = productRepository.findById(productID).orElseThrow();
        Option option = new Option(optionRequest, product);
        product.addOptions(option);
        return new OptionResponse(optionRepository.save(option));
    }

    public List<OptionResponse> insertProductNewOptions(Long productID, List<OptionRequest> optionRequests) {
        List<OptionResponse> responses = new ArrayList<>();
        optionRequests.forEach((optionRequest) -> responses.add(insertProductNewOption(productID, optionRequest)));
        return responses;
    }

    @Transactional
    public OptionResponse updateOption(Long productID, Long optionId, OptionRequest optionRequest) {
        if(checkIfDuplicatedOption(productID, optionRequest)) throw new IllegalArgumentException("이미 존재하는 옵션명입니다.");
        Product product = productRepository.findById(productID).orElseThrow();
        Option option = optionRepository.findById(optionId).orElseThrow();
        product.removeOption(option);
        option.updateOption(optionRequest);
        product.addOptions(option);
        return new OptionResponse(option);
    }

    public void deleteOption(Long productID, Long optionId) {
        Product product = productRepository.findById(productID).orElseThrow();
        product.removeOption(optionRepository.findById(optionId).orElseThrow());
        optionRepository.deleteById(optionId);

    }

    private boolean checkIfDuplicatedOption(Long productID, OptionRequest option){
        return optionRepository.existsByNameAndProductId(option.getOptionName(), productID);
    }

    public OptionResponse findByOptionID(Long optionID) {
        return new OptionResponse(optionRepository.findById(optionID).orElseThrow());
    }

    @Transactional
    public OptionResponse subtractOptionQuantity(Long optionID, Long quantity){
        Option option = optionRepository.findById(optionID).orElseThrow();
        if(option.getQuantity() <= quantity) throw new IllegalArgumentException("현재 옵션의 수량 보다 큰 값을 뺄 수 없습니다.");
        option.subtractQuantity(quantity);
        return new OptionResponse(option);
    }
}

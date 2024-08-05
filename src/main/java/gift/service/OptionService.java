package gift.service;

import gift.DTO.Option.OptionRequest;
import gift.DTO.Option.OptionResponse;
import gift.domain.Option;
import gift.domain.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class OptionService {
    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository){
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }
    /*
     * 한 상품에 대한 새로운 옵션을 저장하는 로직
     */
    @Transactional
    public OptionResponse save(Long productId, OptionRequest optionRequest){
        System.out.println("1 = " + 1);
        Product product = productRepository.findById(productId).orElseThrow(NoSuchFieldError::new);
        Option option = new Option(optionRequest.getName(), optionRequest.getQuantity(), product);

        Option savedOption = optionRepository.save(option);
        return new OptionResponse(savedOption, productId);
    }
    /*
     * 한 상품의 옵션을 가져오는 로직
     */
    public List<OptionResponse> findOptions(Long productId){
        List<OptionResponse> answer = new ArrayList<>();
        List<Option> options = optionRepository.findByProductId(productId);

        for (Option option : options) {
            answer.add(new OptionResponse(option, productId));
        }

        return answer;
    }
    /*
     * 옵션을 수정하는 로직
     */
    @Transactional
    public void update(Long option_id, OptionRequest optionRequest){
        Option option = optionRepository.findById(option_id).orElseThrow(NoSuchFieldError::new);
        option.update(optionRequest.getName(), optionRequest.getQuantity());
    }
    /*
     * 옵션을 삭제하는 로직
     */
    @Transactional
    public void delete(Long productId, Long optionId){
        List<Option> options = optionRepository.findByProductId(productId);

        if(options.size() == 1)
            return;
        optionRepository.deleteById(optionId);
    }
}

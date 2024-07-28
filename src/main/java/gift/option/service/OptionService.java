package gift.option.service;

import gift.option.domain.Option;
import gift.option.dto.OptionRequest;
import gift.option.dto.OptionResponse;
import gift.option.repository.OptionJpaRepository;
import gift.product.domain.Product;
import gift.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OptionService {
    private final OptionJpaRepository optionJpaRepository;
    private final ProductRepository productRepository;
    public OptionService(OptionJpaRepository optionJpaRepository, ProductRepository productRepository) {
        this.optionJpaRepository = optionJpaRepository;
        this.productRepository = productRepository;
    }

    // 1. option create
    @Transactional
    public void createOption(Long productId, OptionRequest optionRequest) {
        System.out.println("123123123optionRequest.getName() = " + optionRequest.getName());
        System.out.println("optionRequest.getQuantity() = " + optionRequest.getQuantity());

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 다음 id의 상품은 존재하지 않음 : " + productId));

        List<Option> options = optionJpaRepository.findAllByProduct(product);
        for (Option option : options) {
            if (option.getName().equals(optionRequest.getName())) {
                throw new IllegalArgumentException("[ERROR] 중복된 옵션 존재 : " + option.getName());
            }
        }

        if (optionRequest.getQuantity() < 0 || optionRequest.getQuantity() > 100000000) {
            throw new IllegalArgumentException("[ERROR] 옵션의 수량은 0 이상 1억 미만이어야 함");
        }

        Option option = new Option(optionRequest.getName(), optionRequest.getQuantity(), product);

        checkForDuplicateOption(option);

        optionJpaRepository.save(option);
    }

    // 2. option read
    @Transactional(readOnly = true)
    public List<Option> getAllOptionByProduct(Product product) {

        return optionJpaRepository.findAllByProduct(product);
    }

    @Transactional(readOnly = true)
    public Option getOptionById(Long optionId) {
        return optionJpaRepository.findById(optionId).orElse(null);
    }

    // 3. option update
    @Transactional
    public void updateOption(Long productId, OptionRequest optionRequest, Long optionId) {
        // 상품 id로 상품 조회하고 해당 상품의 옵션들 중 optionId와 같은 옵션을 찾기
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 다음 id의 상품은 존재하지 않음 : " + productId));

        List<Option> options = optionJpaRepository.findAllByProduct(product);
        Option targetOption = null;
        for (Option option : options) {
            if (option.getId().equals(optionId)) {
                targetOption = option;
                break;
            }
        }

        // valid
        if (targetOption == null || optionRequest.getQuantity() < 0 || optionRequest.getQuantity() > 100000000) {
            throw new IllegalArgumentException("[ERROR] 옵션의 수량은 0 이상 1억 미만이어야 함");
        }

        // update
        targetOption.update(optionRequest.getName(), optionRequest.getQuantity());
        System.out.println("targetOption.getName() = " + targetOption.getName());
        System.out.println("targetOption.getQuantity() = " + targetOption.getQuantity());

        checkForDuplicateOption(targetOption);

        optionJpaRepository.save(targetOption);
    }

    // 4. option delete
    @Transactional
    public Long deleteOption(Long optionId, Long productId) {
        // 상품 id로 상품 조회하고 해당 상품의 옵션들 중 optionId와 같은 옵션을 찾기
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 다음 id의 상품은 존재하지 않음 : " + productId));

        List<Option> options = optionJpaRepository.findAllByProduct(product);
        Option targetOption = null;
        for (Option option : options) {
            if (option.getId().equals(optionId)) {
                targetOption = option;
                break;
            }
        }

        // valid
        if (targetOption == null) {
            throw new IllegalArgumentException("[ERROR] 다음 id의 옵션은 존재하지 않음 : " + optionId);
        }

        // delete
        optionJpaRepository.delete(targetOption);

        return productId;
    }

    // 중복성 검사
    public void checkForDuplicateOption(Option option) {
        List<Option> options = optionJpaRepository.findAll();
        for (Option o : options) {
            if (!o.getId().equals(option.getId()) && o.equals(option)) {
                throw new IllegalArgumentException("[ERROR] 중복된 옵션 존재 : " + option.getName());
            }
        }
    }

    public List<OptionResponse> getOptions(Long productId) {
        // 상품 id로 상품 조회하고 해당 상품의 옵션들 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 다음 id의 상품은 존재하지 않음 : " + productId));
        List<Option> options = optionJpaRepository.findAllByProduct(product);

        // 옵션들을 OptionResponse로 변환하여 반환
        return options.stream()
                .map(option -> new OptionResponse(option.getId(), option.getName(), option.getQuantity(), product.getName()))
                .collect(Collectors.toList());
    }
}

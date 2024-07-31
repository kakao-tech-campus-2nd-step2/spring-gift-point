package gift.doamin.product.service;

import gift.doamin.product.dto.OptionForm;
import gift.doamin.product.entity.Option;
import gift.doamin.product.entity.Product;
import gift.doamin.product.exception.ProductNotFoundException;
import gift.doamin.product.repository.JpaProductRepository;
import gift.doamin.product.repository.OptionRepository;
import java.util.NoSuchElementException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptionService {

    private final JpaProductRepository productRepository;
    private final OptionRepository optionRepository;

    public OptionService(JpaProductRepository productRepository,
        OptionRepository optionRepository) {
        this.productRepository = productRepository;
        this.optionRepository = optionRepository;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @productService.isOwner(authentication.getName(), #productId)")
    public void create(Long productId, OptionForm optionForm) {
        Product product = productRepository.findById(productId)
            .orElseThrow(ProductNotFoundException::new);

        validateDuplicatedName(productId, optionForm);

        optionRepository.save(new Option(product, optionForm.getName(), optionForm.getQuantity()));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @productService.isOwner(authentication.getName(), #productId)")
    public void update(Long productId, Long optionId, OptionForm optionForm) {
        Product product = productRepository.findById(productId)
            .orElseThrow(ProductNotFoundException::new);

        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new NoSuchElementException("해당 옵션이 존재하지 않습니다."));

        validateDuplicatedName(productId, optionForm);

        option.update(optionForm);
        optionRepository.save(option);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @productService.isOwner(authentication.getName(), #productId)")
    public void delete(Long productId, Long optionId) {
        productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        optionRepository.deleteById(optionId);
    }

    @Transactional
    public void subtractQuantity(Long optionId, int quantity) {
        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new NoSuchElementException("해당 옵션이 존재하지 않습니다."));
        option.subtract(quantity);
    }

    private void validateDuplicatedName(Long productId, OptionForm optionForm) {
        if (optionRepository.existsByProductIdAndName(productId, optionForm.getName())) {
            throw new IllegalArgumentException("옵션 이름은 중복될 수 없습니다.");
        }
    }
}

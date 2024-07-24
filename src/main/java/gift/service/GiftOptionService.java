package gift.service;

import gift.database.JpaGiftOptionRepository;
import gift.database.JpaProductRepository;
import gift.dto.GiftOptionRequest;
import gift.dto.GiftOptionResponse;
import gift.exceptionAdvisor.exceptions.ProductNoSuchException;
import gift.model.GiftOption;
import gift.model.Product;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GiftOptionService {

    private final JpaGiftOptionRepository jpaGiftOptionRepository;
    private final JpaProductRepository jpaProductRepository;

    public GiftOptionService(JpaGiftOptionRepository jpaGiftOptionRepository,
        JpaProductRepository jpaProductRepository) {
        this.jpaGiftOptionRepository = jpaGiftOptionRepository;
        this.jpaProductRepository = jpaProductRepository;
    }

    public List<GiftOptionResponse> readAll(Long productId) {

        Product product = getProduct(productId);

        return product.getGiftOptionList().stream().map(GiftOptionResponse::new).toList();
    }


    public void create(Long productId, GiftOptionRequest giftOptionRequest) {

        Product product = getProduct(productId);

        GiftOption giftOption = new GiftOption(null, giftOptionRequest.getName(),
            giftOptionRequest.getQuantity());

        product.addGiftOption(giftOption);

        jpaGiftOptionRepository.save(giftOption);

    }

    public void update(Long productId, Long id, GiftOptionRequest giftOptionRequest) {
        GiftOption giftOption = new GiftOption(id,giftOptionRequest.getName(),
            giftOptionRequest.getQuantity());

    }


    public void delete(Long productId, Long id) {
        jpaGiftOptionRepository.deleteGiftOptionById(id);
    }

    private Product getProduct(Long productId) {
        return jpaProductRepository.findById(productId).orElseThrow(ProductNoSuchException::new);
    }
}

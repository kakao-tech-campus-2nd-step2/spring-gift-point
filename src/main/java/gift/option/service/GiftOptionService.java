package gift.option.service;

import gift.option.dto.GiftOptionRequest;
import gift.option.dto.GiftOptionResponse;
import gift.option.entity.GiftOption;
import gift.repository.JpaGiftOptionRepository;
import gift.repository.JpaProductRepository;
import gift.exceptionAdvisor.exceptions.GiftNotFoundException;
import gift.product.entity.Product;
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
        return jpaProductRepository.findById(productId).orElseThrow(()->new GiftNotFoundException("상품이 존재하지않습니다."));
    }
}

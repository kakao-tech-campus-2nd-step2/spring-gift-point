package gift.service;

import gift.constants.ErrorMessage;
import gift.dto.OptionEditRequest;
import gift.dto.OptionResponse;
import gift.dto.OptionSaveRequest;
import gift.dto.OptionSubtractRequest;
import gift.entity.Option;
import gift.entity.Product;
import gift.repository.OptionJpaDao;
import gift.repository.ProductJpaDao;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptionService {

    private final ProductJpaDao productJpaDao;
    private final OptionJpaDao optionJpaDao;

    public OptionService(ProductJpaDao productJpaDao, OptionJpaDao optionJpaDao) {
        this.productJpaDao = productJpaDao;
        this.optionJpaDao = optionJpaDao;
    }

    /**
     * productId에 해당하는 상품의 옵션들을 리스트로 반환.
     *
     * @param productId
     * @return List
     */
    public List<OptionResponse> getProductOptionList(Long productId) {
        Product product = findProductById(productId);
        return product.getOptions().stream()
            .map(OptionResponse::new)
            .toList();
    }

    public OptionResponse findOptionById(Long optionId) {
        Option option = optionJpaDao.findById(optionId)
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.OPTION_NOT_EXISTS_MSG));
        return new OptionResponse(option);
    }

    public void saveOption(OptionSaveRequest saveRequest) {
        Product product = findProductById(saveRequest.getProductId());
        Option option = saveRequest.toEntity(product);

        if (product.isOptionNameDuplicate(option)) {
            throw new IllegalArgumentException(ErrorMessage.OPTION_NAME_ALREADY_EXISTS_MSG);
        }
        optionJpaDao.save(saveRequest.toEntity(product));
    }

    @Transactional
    public void editOption(OptionEditRequest editRequest) {
        Product product = findProductById(editRequest.getProductId());
        product.updateOption(editRequest);
    }

    /**
     * 상품의 옵션을 삭제하는 메서드. 단, 옵션이 1개일 때는 삭제하지 않는다.
     */
    public void deleteOption(Long productId, Long optionId) {
        Product product = findProductById(productId);
        if (product.canDeleteOption(optionId)) {
            optionJpaDao.deleteById(optionId);
        }
    }

    @Transactional
    public void subtractOption(OptionSubtractRequest subtractRequest) {
        findOptionWithLock(subtractRequest.getId());
        Product product = findProductById(subtractRequest.getProductId());
        product.subtractOption(subtractRequest);
    }

    /**
     * productId에 해당하는 상품이 존재하면 반환하고 아니면 NoSuchElementException
     */
    private Product findProductById(Long productId) {
        return productJpaDao.findById(productId)
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.PRODUCT_NOT_EXISTS_MSG));
    }

    /**
     * Pessimistic Lock 을 걸기위한 조회
     */
    private Option findOptionWithLock(Long optionId) {
        return optionJpaDao.findByIdForUpdate(optionId)
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.OPTION_NOT_EXISTS_MSG));
    }
}

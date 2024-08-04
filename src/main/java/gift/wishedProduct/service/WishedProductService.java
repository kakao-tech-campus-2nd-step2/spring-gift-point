package gift.wishedProduct.service;

import gift.member.entity.Member;
import gift.product.entity.Product;
import gift.product.exception.NoSuchProductException;
import gift.product.repository.ProductRepository;
import gift.wishedProduct.dto.AddWishedProductRequest;
import gift.wishedProduct.dto.GetWishedProductResponse;
import gift.wishedProduct.dto.WishedProductDto;
import gift.wishedProduct.entity.WishedProduct;
import gift.wishedProduct.exception.NoSuchWishedProductException;
import gift.wishedProduct.repository.WishedProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WishedProductService {

    private final WishedProductRepository wishedProductRepository;
    private final ProductRepository productRepository;

    @Autowired
    public WishedProductService(WishedProductRepository wishedProductRepository, ProductRepository productRepository) {
        this.wishedProductRepository = wishedProductRepository;
        this.productRepository = productRepository;
    }

    public Page<GetWishedProductResponse> getWishedProducts(Member member, Pageable pageable) {
        return wishedProductRepository.findByMember(member, pageable)
            .map(wishedProduct -> wishedProduct.toGetWishedProductResponse());
    }

    public WishedProductDto addWishedProduct(Member member, AddWishedProductRequest addWishedProductRequest) {
        Product product = productRepository.findById(addWishedProductRequest.productId())
            .orElseThrow(NoSuchProductException::new);
        WishedProduct wishedProduct = new WishedProduct(member, product);
        return wishedProductRepository.save(wishedProduct).toDto();
    }

    public WishedProductDto deleteWishedProduct(long id) {
        WishedProductDto deletedWishedProductDto = wishedProductRepository.findById(id)
            .orElseThrow(NoSuchWishedProductException::new)
            .toDto();
        wishedProductRepository.deleteById(id);
        return deletedWishedProductDto;
    }
}

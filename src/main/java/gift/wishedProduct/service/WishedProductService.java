package gift.wishedProduct.service;

import gift.product.entity.Product;
import gift.wishedProduct.entity.WishedProduct;
import gift.member.dto.MemberDto;
import gift.wishedProduct.dto.AddWishedProductRequest;
import gift.wishedProduct.dto.GetWishedProductResponse;
import gift.wishedProduct.dto.WishedProductDto;
import gift.product.exception.NoSuchProductException;
import gift.wishedProduct.exception.NoSuchWishedProductException;
import gift.product.repository.ProductRepository;
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

    public Page<GetWishedProductResponse> getWishedProducts(MemberDto memberDto, Pageable pageable) {
        return wishedProductRepository.findByMember(memberDto.toEntity(), pageable)
            .map(wishedProduct -> wishedProduct.toGetWishedProductResponse());
    }

    public WishedProductDto addWishedProduct(MemberDto memberDto, AddWishedProductRequest addWishedProductRequest) {
        Product product = productRepository.findById(addWishedProductRequest.productId())
            .orElseThrow(NoSuchProductException::new);
        WishedProduct wishedProduct = new WishedProduct(memberDto.toEntity(), product);
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

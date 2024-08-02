package gift.service;

import gift.domain.Product;
import gift.domain.WishedProduct;
import gift.dto.member.MemberDto;
import gift.dto.wishedProduct.AddWishedProductRequest;
import gift.dto.wishedProduct.GetWishedProductResponse;
import gift.dto.wishedProduct.WishedProductDto;
import gift.exception.NoSuchProductException;
import gift.exception.NoSuchWishedProductException;
import gift.repository.ProductRepository;
import gift.repository.WishedProductRepository;
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
        return wishedProductRepository.save(wishedProduct).toDTO();
    }

    public WishedProductDto deleteWishedProduct(long id) {
        WishedProductDto deletedWishedProductDto = wishedProductRepository.findById(id)
            .orElseThrow(NoSuchWishedProductException::new)
            .toDTO();
        wishedProductRepository.deleteById(id);
        return deletedWishedProductDto;
    }
}

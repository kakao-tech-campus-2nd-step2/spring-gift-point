package gift.service;

import gift.domain.Product;
import gift.domain.WishedProduct;
import gift.dto.MemberDTO;
import gift.dto.WishedProductDTO;
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

    public Page<WishedProductDTO> getWishedProducts(MemberDTO memberDTO, Pageable pageable) {
        return wishedProductRepository.findByMember(memberDTO.toEntity(), pageable)
            .map(wishedProduct -> wishedProduct.toDTO());
    }

    public WishedProductDTO addWishedProduct(MemberDTO memberDTO, WishedProductDTO wishedProductDTO) {
        Product product = productRepository.findById(wishedProductDTO.productId()).orElseThrow(NoSuchProductException::new);
        WishedProduct wishedProduct = new WishedProduct(memberDTO.toEntity(), product, wishedProductDTO.amount());
        return wishedProductRepository.save(wishedProduct).toDTO();
    }

    public WishedProductDTO deleteWishedProduct(long id) {
        WishedProductDTO deletedWishedProductDTO = wishedProductRepository.findById(id)
            .orElseThrow(NoSuchWishedProductException::new)
            .toDTO();
        wishedProductRepository.deleteById(id);
        return deletedWishedProductDTO;
    }

    public WishedProductDTO updateWishedProduct(WishedProductDTO wishedProductDTO) {
        long id = wishedProductDTO.id();
        int amount = wishedProductDTO.amount();
        if (amount == 0) {
            return deleteWishedProduct(id);
        }
        WishedProduct wishedProduct = wishedProductRepository.findById(id)
            .orElseThrow(NoSuchWishedProductException::new);
        wishedProduct.setAmount(amount);
        return wishedProductRepository.save(wishedProduct).toDTO();
    }
}

package gift.service.gift;


import gift.dto.gift.ProductRequest;
import gift.dto.gift.ProductResponse;
import gift.dto.option.OptionRequest;
import gift.dto.paging.PagingResponse;
import gift.model.category.Category;
import gift.model.gift.Product;
import gift.model.option.Option;
import gift.repository.category.CategoryRepository;
import gift.repository.gift.ProductRepository;
import gift.repository.option.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;


    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, OptionRepository optionRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public PagingResponse<ProductResponse> getAllGifts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by("id").ascending());
        Page<Product> gifts = productRepository.findAll(pageRequest);
        List<ProductResponse> productRespons = gifts.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
        return new PagingResponse<>(page, productRespons, size, gifts.getTotalElements(), gifts.getTotalPages());
    }

    @Transactional(readOnly = true)
    public ProductResponse getGift(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 상품을 찾을 수 없습니다 id :  " + id));
        return ProductResponse.from(product);
    }

    @Transactional
    public ProductResponse addGift(ProductRequest.Create giftRequest) {
        Category category = categoryRepository.findById(giftRequest.categoryId())
                .orElseThrow(() -> new NoSuchElementException("해당 카테고리 id가 없습니다."));

        List<Option> options = giftRequest.options().stream().map(OptionRequest.Create::toEntity).toList();

        Product product = new Product(giftRequest.name(), giftRequest.price(), giftRequest.imageUrl(), category, options);
        return ProductResponse.from(productRepository.save(product));
    }

    @Transactional
    public void updateGift(ProductRequest.Update giftRequest, Long id) {
        Category category = categoryRepository.findById(giftRequest.categoryId())
                .orElseThrow(() -> new NoSuchElementException("해당 카테고리 id가 없습니다."));
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 Gift가 없습니다. id : " + id));
        product.modify(giftRequest.name(), giftRequest.price(), giftRequest.imageUrl(), category);
        productRepository.save(product);
    }

    @Transactional
    public void deleteGift(Long id) {
        productRepository.deleteById(id);
    }
}





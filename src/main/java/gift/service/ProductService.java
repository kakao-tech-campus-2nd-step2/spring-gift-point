package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.product.ModifyProductDTO;
import gift.dto.product.ProductWithOptionDTO;
import gift.dto.product.SaveProductDTO;
import gift.dto.product.ShowProductDTO;

import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import gift.exception.exception.BadRequestException;
import gift.exception.exception.UnAuthException;
import gift.exception.exception.NotFoundException;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;


@Service
@Validated
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;
    private final CategoryRepository categoryRepository;

    public Page<ProductWithOptionDTO> getAllProductsWithOption(Pageable pageable) {
        return optionRepository.findAllWithOption(pageable);
    }

    public Page<ShowProductDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAllProduct(pageable);
    }

    @Transactional
    public Product saveProduct(SaveProductDTO productDTO) {
        Category category = categoryRepository.findById(productDTO.categoryId()).orElseThrow(() -> new NotFoundException("해당 카테고리가 없음"));
        productRepository.findByNameAndCategory(productDTO.name(), category.getName()).ifPresent(c -> {
            throw new BadRequestException("해당 카테고리에 같은 이름의 물품이 존재");
        });
        if (productDTO.option().isBlank()) throw new BadRequestException("하나의 옵션은 존재 해야 함");

        Product product = new Product(productDTO, category);
        Product saveProduct = productRepository.save(product);
        category.addProduct(product);

        List<String> optionList = stream(productDTO.option().split(",")).toList();
        optionList.stream()
                .distinct()
                .map(str -> new Option(saveProduct, str))
                .filter(this::isValidOption)
                .forEach(option -> optionRepository.save(option));
        return product;
    }

    private boolean isValidOption(@Validated Option option) {
        return optionRepository.findByProductNameAndOption(option.getProduct().getName(), option.getOption()).isEmpty();
    }

    public void deleteProduct(int id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("존재하지 않는 id입니다."));
        product.getCategory().deleteProduct(product);
        optionRepository.deleteByProductId(id);
        productRepository.deleteById(id);
    }


    public ShowProductDTO getProductByID(int id) {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("해당 물건이 없습니다.")).toDTO();

    }

    @Transactional
    public void updateProduct(ModifyProductDTO modifyProductDTO) {
        Product product = productRepository.findById(modifyProductDTO.id()).orElseThrow(() -> new NotFoundException("물건이 없습니다."));
        product.modifyProduct(modifyProductDTO);
    }

}

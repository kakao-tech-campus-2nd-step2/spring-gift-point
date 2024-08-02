package gift.service;

import gift.dto.OptionDto;
import gift.entity.Category;
import gift.dto.ProductDto;
import gift.entity.Option;
import gift.entity.Product;
import gift.exception.ProductNotFoundException;
import gift.repository.ProductRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    private final OptionService optionService;

    @Autowired
    public  ProductService(ProductRepository productRepository, CategoryService categoryService, OptionService optionService){
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.optionService = optionService;

    }

    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void addProduct(ProductDto productDto) {
        Category category = categoryService.getCategory(productDto.getCategoryId());
        Product product = new Product(productDto.getName(), productDto.getPrice(), productDto.getImageUrl());
        product.setCategory(category);

        Set<Option> options = convertOptionDtosToOptions(productDto.getOptions(), product);
        product.setOptions(options);

        category.addProduct(product);
        try {
            productRepository.save(product);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("같은 상품 내에 있는 옵션들의 이름은 중복될 수 없습니다.", e);
        }
    }

    public void updateProduct(Long id, ProductDto productDto) {
        Category category = categoryService.getCategory(productDto.getCategoryId());
        Product product = productRepository.findById(id)
                .orElseThrow(() -> ProductNotFoundException.of(id));
        product.edit(productDto.getName(), productDto.getPrice(), productDto.getImageUrl());

        Set<Option> options = convertOptionDtosToOptions(productDto.getOptions(), product);
        product.edit(productDto.getName(), productDto.getPrice(), productDto.getImageUrl());
        product.setOptions(options);
        product.setCategory(category);
        for (Option option : options) {
            option.setProduct(product);
        }
        productRepository.save(product);
    }


    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> ProductNotFoundException.of(id));

        Category category = product.getCategory();
        if (category != null) {
            category.removeProduct(product); // Remove product from category
        }
        productRepository.delete(product);
    }

    private Set<Option> convertOptionDtosToOptions(List<OptionDto> optionDtos, Product product) {
        Set<Option> options = new HashSet<>();
        for (OptionDto optionDto : optionDtos) {
            Option option = new Option();
            option.setName(optionDto.getName());
            option.setQuantity(optionDto.getQuantity());
            option.setProduct(product);
            options.add(option);
        }
        return options;
    }

}

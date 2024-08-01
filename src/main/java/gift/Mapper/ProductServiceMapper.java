package gift.Mapper;

import gift.DTO.ProductDTO;
import gift.DTO.WishDTO;
import gift.DTO.CategoryDTO;
import gift.DTO.OptionDTO;
import gift.Entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProductServiceMapper {

    public ProductDTO convertToDTO(ProductEntity productEntity) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(productEntity.getId());
        productDTO.setName(productEntity.getName());
        productDTO.setPrice(productEntity.getPrice());
        productDTO.setImageUrl(productEntity.getImageUrl());
        productDTO.setWishes(convertWishesToDTOs(productEntity.getWishes()));
        productDTO.setCategory(convertToCategoryDTO(productEntity.getCategory()));
        productDTO.setOptions(convertOptionsToDTOs(productEntity.getOptions()));
        return productDTO;
    }

    public List<ProductDTO> convertToProductDTOs(List<ProductEntity> productEntities) {
        return productEntities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProductEntity convertToEntity(ProductDTO productDTO) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(productDTO.getId());
        productEntity.setName(productDTO.getName());
        productEntity.setPrice(productDTO.getPrice());
        productEntity.setImageUrl(productDTO.getImageUrl());
        productEntity.setWishes(convertToWishEntities(productDTO.getWishes()));
        productEntity.setCategory(convertToCategoryEntity(productDTO.getCategory()));
        productEntity.setOptions(convertToOptionEntities(productDTO.getOptions()));
        return productEntity;
    }

    private List<WishDTO> convertWishesToDTOs(List<WishEntity> wishEntities) {
        return Optional.ofNullable(wishEntities).orElse(List.of())
                .stream()
                .map(wishEntity -> new WishDTO(
                        wishEntity.getId(),
                        wishEntity.getUser().getId(),
                        wishEntity.getProduct().getId(),
                        wishEntity.getProductName()))
                .collect(Collectors.toList());
    }

    private CategoryDTO convertToCategoryDTO(CategoryEntity categoryEntity) {
        if (categoryEntity == null) {
            return null;
        }
        return new CategoryDTO(
                categoryEntity.getId(),
                categoryEntity.getName());
    }

    private List<OptionDTO> convertOptionsToDTOs(List<OptionEntity> optionEntities) {
        return Optional.ofNullable(optionEntities).orElse(List.of())
                .stream()
                .map(optionEntity -> new OptionDTO(
                        optionEntity.getId(),
                        optionEntity.getName(),
                        optionEntity.getQuantity(),
                        optionEntity.getProduct() != null ? optionEntity.getProduct().getId() : null))
                .collect(Collectors.toList());
    }

    public List<WishEntity> convertToWishEntities(List<WishDTO> wishDTOs) {
        return Optional.ofNullable(wishDTOs).orElse(List.of())
                .stream()
                .map(wishDTO -> {
                    WishEntity wishEntity = new WishEntity();
                    wishEntity.setId(wishDTO.getId());
                    return wishEntity;
                })
                .collect(Collectors.toList());
    }

    public CategoryEntity convertToCategoryEntity(CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            return null;
        }
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(categoryDTO.getId());
        categoryEntity.setName(categoryDTO.getName());
        return categoryEntity;
    }

    public List<OptionEntity> convertToOptionEntities(List<OptionDTO> optionDTOs) {
        return Optional.ofNullable(optionDTOs).orElse(List.of())
                .stream()
                .map(optionDTO -> {
                    OptionEntity optionEntity = new OptionEntity();
                    optionEntity.setId(optionDTO.getId());
                    optionEntity.setName(optionDTO.getName());
                    optionEntity.setQuantity(optionDTO.getQuantity());
                    optionEntity.setProduct(new ProductEntity(optionDTO.getProductId()));
                    return optionEntity;
                })
                .collect(Collectors.toList());
    }
}

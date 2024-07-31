package gift.DTO;

import gift.Model.Entity.Product;

public record ResponseProductListOfCategoryDTO(
        Long id,
        String name,
        int price,
        String imageUrl
){
    public static ResponseProductListOfCategoryDTO of (Product product){
        return new ResponseProductListOfCategoryDTO(product.getId()
                , product.getName().getValue()
                , product.getPrice().getValue()
                , product.getImageUrl().getValue()
        );
    }
}

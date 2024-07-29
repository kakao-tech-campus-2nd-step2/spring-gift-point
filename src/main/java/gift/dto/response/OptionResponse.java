package gift.dto.response;

import gift.domain.Option;

public record OptionResponse(Long id, String name, int quantity, ProductResponse productResponse) {
    public static OptionResponse from(final Option option){
        return new OptionResponse(option.getId(), option.getName(), option.getQuantity(), ProductResponse.from(option.getProduct()));
    }

    public Option toEntity(){
        return new Option(this.id,this.name,this.quantity,productResponse.toEntity());
    }
}

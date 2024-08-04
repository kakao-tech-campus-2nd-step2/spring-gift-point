package gift.domain.OptionDomain;

import jakarta.persistence.Embeddable;

@Embeddable
public class OptionQuantity {
    private Long optionQuantity;

    public OptionQuantity(Long optionQuantity){
        validOptionQuantity(optionQuantity);
        this.optionQuantity = optionQuantity;
    }

    public OptionQuantity() {

    }

    public Long getoptionQuantity() {
        return optionQuantity;
    }

    public void validOptionQuantity(Long optionQuantity){
        if(optionQuantity < 1){
            throw new IllegalArgumentException("수량은 최소 1입니다.");
        }
        if(optionQuantity > 100_000_000){
            throw new IllegalArgumentException("수량은 최대 1억입니다.");
        }
    }

    public void subtract(Long subtractNumber) throws IllegalAccessException {
        optionQuantity -= subtractNumber;
        validOptionQuantity(optionQuantity);
    }
}

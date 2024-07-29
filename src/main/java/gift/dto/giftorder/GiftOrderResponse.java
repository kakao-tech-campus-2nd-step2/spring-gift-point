package gift.dto.giftorder;

import gift.dto.option.OptionInformation;

import java.time.LocalDateTime;

public record GiftOrderResponse(Long id, OptionInformation optionInformation, Integer quantity, LocalDateTime orderDateTime, String message) {
    public static GiftOrderResponse of(Long id, OptionInformation optionInformation, Integer quantity, LocalDateTime orderDateTime, String message) {
        return new GiftOrderResponse(id, optionInformation, quantity, orderDateTime, message);
    }
}

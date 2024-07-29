package gift.option.presentation.request;

import gift.option.application.command.OptionCreateCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OptionCreateRequest(
        @NotBlank(message = "옵션 이름은 필수 항목입니다.")
        String name,

        @NotNull(message = "옵션 수량은 필수 항목입니다.")
        Integer quantity
){
        public OptionCreateCommand toCommand() {
                return new OptionCreateCommand(
                        name,
                        quantity
                );
        }
}

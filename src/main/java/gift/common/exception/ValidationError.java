package gift.common.exception;

import org.springframework.validation.FieldError;

public record ValidationError(String field, String message) {

    public static ValidationError of(final FieldError fieldError) {
        return ValidationError.builder()
                .field(fieldError.getField())
                .message(fieldError.getDefaultMessage())
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String field;
        private String message;

        public Builder field(String field) {
            this.field = field;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public ValidationError build() {
            return new ValidationError(field, message);
        }
    }
}

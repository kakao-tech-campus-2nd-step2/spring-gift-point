package gift.dto.common.apiResponse;

public class ApiResponseBody {
    public static class SuccessBody<D> {
        private String status;
        private String message;
        private D data;

        public SuccessBody(String status, String message, D data) {
            this.status = status;
            this.message = message;
            this.data = data;
        }

        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public D getData() {
            return data;
        }
    }

    public static class FailureBody {
        private String status;
        private String message;
        private String code;

        public FailureBody(String status, String message, String code) {
            this.status = status;
            this.message = message;
            this.code = code;
        }

        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public String getCode() {
            return code;
        }
    }

}

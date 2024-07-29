package gift.dto.response;

import java.util.List;

public record MultipleErrorResponse(int statusCode, List<String> messages, String path){
}

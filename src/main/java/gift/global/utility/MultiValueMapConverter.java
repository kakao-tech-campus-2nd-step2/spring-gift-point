package gift.global.utility;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import gift.global.model.MultiValueMapConvertibleDto;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

// dto를 MultiValueMap으로 변환해주는 클래스
public class MultiValueMapConverter {

    // 재귀로 인해 쓸데없는 호출이 많이 일어나지 않도록 static으로 따로 빼주기
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 파라미터를 convert (String, String 고정)
    public static MultiValueMap<String, String> paramConvert(Object dto) {
        // 변환 가능하도록 명시한 dto만 변환하기
        if (!(dto instanceof MultiValueMapConvertibleDto)) {
            throw new IllegalArgumentException("요청 변환 중 오류가 발생했습니다.");
        }

        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        try {
            Map<String, String> dtoMap = objectMapper.convertValue(dto, new TypeReference<>() {
            });
            var dtoMultiValueMap = new LinkedMultiValueMap<String, String>();
            dtoMultiValueMap.setAll(dtoMap);

            return dtoMultiValueMap;
        } catch (Exception e) {
            throw new IllegalArgumentException("요청 변환 중 오류가 발생했습니다.");
        }
    }

    // request body를 convert(String, Object). 재귀적으로 모든 필드도 convert
    public static MultiValueMap<String, Object> bodyConvert(Object dto) {
        if (!(dto instanceof MultiValueMapConvertibleDto)) {
            throw new IllegalArgumentException("요청 변환 중 오류가 발생했습니다.");
        }

        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        try {
            Map<String, Object> dtoMap = objectMapper.convertValue(dto, new TypeReference<>() {
            });
            var dtoMultiValueMap = new LinkedMultiValueMap<String, Object>();

            dtoMultiValueMap.setAll(dtoMap.entrySet().stream().collect(Collectors.toMap(Entry::getKey, entry -> {
                // value 내부에 변환 가능한 dto가 추가로 존재한다면 해당 dto에 대해 메서드 재호출
                var value = entry.getValue();
                if (value instanceof String) {
                    return bodyConvert(value);
                }

                // 아니라면 그냥 반환. 만약 MultiValueMapConvertibleDto도 아니면서 dto인 경우라면 요청 중 알아서 예외 처리가 될 것.
                return entry.getValue();
            })));

            return dtoMultiValueMap;
        } catch (Exception e) {
            throw new IllegalArgumentException("요청 변환 중 오류가 발생했습니다.");
        }
    }
}

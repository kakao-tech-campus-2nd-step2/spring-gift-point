package gift.controller.api;

import gift.dto.option.OptionAddRequest;
import gift.dto.option.OptionResponse;
import gift.dto.option.OptionUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "상품 옵션 API")
public interface OptionApi {

    @Operation(summary = "상품에 옵션을 추가한다.")
    ResponseEntity<Void> addOption(OptionAddRequest optionAddRequest);

    @Operation(summary = "기존 옵션을 수정한다.")
    ResponseEntity<Void> updateOption(Long id, OptionUpdateRequest optionUpdateRequest);

    @Operation(summary = "모든 옵션을 페이지 단위로 조회한다.")
    ResponseEntity<List<OptionResponse>> getOptions(Long productId, Pageable pageable);

    @Operation(summary = "특정 옵션을 삭제한다.")
    ResponseEntity<Void> deleteOption(Long id);
}

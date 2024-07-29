package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.dto.option.OptionQuantityDTO;
import gift.dto.option.OrderResponseDTO;
import gift.dto.option.SaveOptionDTO;
import gift.dto.option.UpdateOptionDTO;
import gift.service.OptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OptionController {
    private final OptionService optionService;

    @PostMapping("/api/option")
    @ResponseStatus(HttpStatus.CREATED)
    public String addOption(@RequestBody SaveOptionDTO saveOptionDTO) {
        optionService.saveOption(saveOptionDTO);
        return "옵션 저장";
    }

    @DeleteMapping("/api/option/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public String deleteOption(@PathVariable int id) {
        optionService.deleteOption(id);
        return "옵션 삭제";
    }

    @PutMapping("/api/option")
    public String updateOption(@RequestBody UpdateOptionDTO updateOptionDTO) {
        optionService.updateOption(updateOptionDTO);
        return "옵션 수정";
    }

    @PostMapping("/api/option/refill")
    public String refill(@RequestBody OptionQuantityDTO optionQuantityDTO) {
        optionService.refillQuantity(optionQuantityDTO);
        return "수량 증가";
    }

    @PostMapping("/api/option/order")
    public OrderResponseDTO order(@RequestHeader("Authorization") String token, @RequestBody OptionQuantityDTO optionQuantityDTO) {
        return optionService.order(optionQuantityDTO, token);
    }
}

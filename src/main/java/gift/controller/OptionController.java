package gift.controller;

import gift.dto.OptionRequest;
import gift.model.Option;
import gift.model.Product;
import gift.service.OptionService;
import gift.service.ProductService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@RestController
@RequestMapping("/api/options")
public class OptionController {

    @Autowired
    private final OptionService optionService;

    @Autowired
    private ProductService productService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @PostMapping("/{product_id}")
    public ResponseEntity<?> addOption(@PathVariable("product_id") Long productId, @RequestBody OptionRequest optionRequest){
        Optional<Product> product = productService.getProductById(productId);
        List<Option> options = new ArrayList<>();
        Option option = new Option(optionRequest, product.get());
        options.add(option);
        optionService.addOption(options);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{option_id}")
    public ResponseEntity<?> deleteOption(@PathVariable("option_id") Long optionId){
        optionService.deleteOption(optionId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    /*
    @PostMapping("/update")
    public String updateOption(@RequestParam String name){
        Option option = optionService.getOptionByName(name);
        Option newoption = new Option()
    }*/

    @GetMapping("/{product_id}")
    public ResponseEntity<?> getOptionById(@PathVariable("product_id") Long id){
        List<Option> options = optionService.getOptionByProductID(id);
        Map<String, Object> response = new HashMap<>();
        response.put("options", options);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}

package gift.controller;

import gift.model.Option;
import gift.model.Product;
import gift.service.OptionService;
import gift.service.ProductService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/options")
public class OptionController {

    @Autowired
    private final OptionService optionService;

    @Autowired
    private ProductService productService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @PostMapping("/add")
    public String addOption(@RequestParam Long productId, @RequestParam List<String> optionNames, @RequestParam List<Long> optionQuantities, RedirectAttributes redirectAttributes){
        Optional<Product> product = productService.getProductById(productId);
        List<Option> options = new ArrayList<>();
        for (int i = 0; i < optionNames.size(); i++) {
            String optionName = optionNames.get(i);
            Long optionQuantity = optionQuantities.get(i);
            Option option = new Option(optionName, optionQuantity, product.get());
            options.add(option);
        }
        optionService.addOption(options);
        redirectAttributes.addFlashAttribute("message", "Option added successfully!");
        return "redirect:/products/view/" + productId;
    }

    @PostMapping("/delete/{productId}")
    public String deleteOption(@PathVariable("productId") Long productId, RedirectAttributes redirectAttributes){
        optionService.deleteOption(productId);
        return "redirect:/products/view/" + productId;
    }
    /*
    @PostMapping("/update")
    public String updateOption(@RequestParam String optionName){
        Option option = optionService.getOptionByName(optionName);
        Option newoption = new Option()
    }*/

    @GetMapping("/{id}")
    public ResponseEntity<Option> getOptionById(@PathVariable("id") Long id){
        Optional<Option> option = optionService.getOptionById(id);
        return option.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}

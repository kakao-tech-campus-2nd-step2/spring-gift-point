package gift.controller;

import gift.repository.ProductRepository;
import gift.model.Product;

import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ThymeleafProductController {

    @Autowired
    private ProductRepository productDatabase;

    // 모든 상품을 조회하여 모델에 추가하고, product-list 뷰를 반환하는 메서드
    @GetMapping
    public String getAllProducts(Model model) {
        List<Product> products = productDatabase.findAll();
        model.addAttribute("products", products);
        return "product-list";
    }


    // 새로운 상품을 추가하기 위한 폼을 만들고 모델에 추가
    // 현재 기본 생성자를 protected 로 변경해둬서 어플리케이션 실행에 지장 없도록 해당 메서드 주석 처리
//    @GetMapping("/new")
//    public String createProductForm(Model model) {
//        Product product = new Product();
//        model.addAttribute("product", product);
//        return "create-product";
//    }


    // 유효성 검사 후 통과하면 새로운 상품 추가
    @PostMapping
    public String addProduct(@Valid @ModelAttribute("product") Product product, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // 유효성 검사에 실패한 경우, 오류 메세지와 함께 다시 폼을 보여줌
            return "create-product";
        }
        // 유효성 검사 성공 시, 데이터를 저장
        productDatabase.save(product);
        // 데이터 저장 후, 제품 목록 페이지로 리다이렉션
        return "redirect:/products";
    }

    // 상품 수정 폼 생성 후 기존 상품 데이터를 모델에 추가
    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable("id") Long id, Model model) {
        Optional<Product> productOpt = productDatabase.findById(id);
        if (productOpt.isEmpty()) {
            return "redirect:/products";
        }
        model.addAttribute("product", productOpt.get());
        return "edit-product";
    }

    // 유효성 검증 후 통과하면 상품 정보 업데이트
    @PutMapping("/{id}")
    public String updateProduct(@PathVariable("id") Long id,@Valid @ModelAttribute Product product, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "edit-product";
        }
      
        Optional<Product> existingProductOpt = productDatabase.findById(id);
        if (existingProductOpt.isPresent()) {
            Product existingProduct = existingProductOpt.get();
            existingProduct.setName(product.getName());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setImageUrl(product.getImageUrl());
            productDatabase.save(existingProduct);
        }
        return "redirect:/products";
    }

    // 상품 삭제
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        Optional<Product> productOpt = productDatabase.findById(id);
        if (productOpt.isPresent()) {
            productDatabase.deleteById(id);
        }
        return "redirect:/products";
    }

}

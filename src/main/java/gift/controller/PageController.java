package gift.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/*
 * 페이지 연결을 위한 Controller
 */
@Controller
public class PageController {
    /*
     * 메인 페이지
     */
    @GetMapping("/home")
    public String home(Model model){
        if (model.containsAttribute("token")) {
            String token = (String) model.getAttribute("token");
            model.addAttribute("token", token);
        }
        return "index.html";
    }
    /*
     * 로그인 페이지
     */
    @GetMapping("/login")
    public String login(){
        return "login.html";
    }
    /*
     * 회원 가입 페이지
     */
    @GetMapping("/register")
    public String register(){
        return "register.html";
    }
    /*
     * 위시 리스트 페이지
     */
    @GetMapping("/wishList")
    public String wishList(){
        return "wishList.html";
    }
    /*
     * 상품 관리 페이지
     */
    @GetMapping("/adminProducts")
    public String manageProduct(){
        return "adminProduct.html";
    }
    /*
     * 유저 관리 페이지
     */
    @GetMapping("/adminUsers")
    public String manageUser(){
        return "adminUser.html";
    }
    /*
     * 카테고리 관리 페이지
     */
    @GetMapping("/adminCategories")
    public String manageCategory(){
        return "adminCategory.html";
    }
    /*
     * 옵션 관리 페이지
     */
    @GetMapping("/adminOptions")
    public String manageOption(@RequestParam Long product_id, Model model){
        model.addAttribute("product_id", product_id);
        return "adminOption.html";
    }
    /*
     * 주문 페이지
     */
    @GetMapping("/orders")
    public String orderProduct(@RequestParam Long product_id, Model model){
        model.addAttribute("product_id", product_id);
        return "orderProduct.html";
    }
}

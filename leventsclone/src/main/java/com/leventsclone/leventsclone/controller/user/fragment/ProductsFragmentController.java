package com.leventsclone.leventsclone.controller.user.fragment;

import com.leventsclone.leventsclone.data.use.CartUse;
import com.leventsclone.leventsclone.data.use.OderUse;
import com.leventsclone.leventsclone.service.impl.CookieSer;
import com.leventsclone.leventsclone.service.impl.CartSer;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping(value = "/fragments-product")
public class ProductsFragmentController {
    private final CookieSer cookieSer;
    private final CartSer oderSer;

    @Autowired
    public ProductsFragmentController(CookieSer cookieSer, CartSer oderSer) {
    this.oderSer = oderSer;
    this.cookieSer = cookieSer;
    }


    @GetMapping(value = "/oder-get")
    public String addCart(Model model, HttpServletRequest request, HttpServletResponse response) {
        List<CartUse> dataCart = new ArrayList<>();
        Cookie[] cookies =  request.getCookies();
        if (cookies != null) {
            if(!cookieSer.checkContainCookie(cookies)) {
                String codeCheck = oderSer.getCodeCart();
                while (!cookieSer.checkCode(codeCheck)) {
                    codeCheck = oderSer.getCodeCart();
                }
                Cookie cookie = cookieSer.createNewCookie(codeCheck);
                oderSer.createOder(cookie.getValue());
                response.addCookie(cookie);
            }else if(!cookieSer.checkExpireCookie(cookies)) {
                String codeCart = cookieSer.getCookie(cookies).getValue();
                oderSer.clean(codeCart);
                Cookie cookie = cookieSer.updateCookie(cookies);
                response.addCookie(cookie);
            } else  {
                String codeCart = cookieSer.getCookie(cookies).getValue();
                dataCart = oderSer.getAllDataCart(codeCart);
            }
        } else  {
            String codeCheck = oderSer.getCodeCart();
            while (!cookieSer.checkCode(codeCheck)) {
                codeCheck = oderSer.getCodeCart();
            }
            Cookie cookie = cookieSer.createNewCookie(codeCheck);
            oderSer.createOder(cookie.getValue());
            response.addCookie(cookie);
        }
        Set<CartUse> result = new HashSet<>(dataCart);
        model.addAttribute("dataCart", result);
        return  "user/fragments/common/Oder :: oder";
    };

    @PostMapping(value = "/oder-delete")
    public  String deleteProductInCart(
            Model model,
            @RequestParam(value = "OptionSize") Optional<Long> idOptionSize,
            HttpServletRequest request
    ) {
        List<CartUse> dataCart = new ArrayList<>();
        if(cookieSer.checkContainCookie(request.getCookies())) {
            String codeCart = cookieSer.getCookie(request.getCookies()).getValue();
            oderSer.deleteProductToCart(
                    codeCart,
                    idOptionSize.orElseThrow());
            dataCart = oderSer.getAllDataCart(codeCart);
        }
        model.addAttribute("dataCart", dataCart);
        return  "user/fragments/common/Oder :: oder";
    }




    @PostMapping(value = "/oder-add")
    public String addCart(
            Model model,
            @RequestParam(value = "option") Optional<Long> idOption,
            @RequestParam(value = "size") Optional<Long> idSize,
            @RequestParam(value = "quantity") Optional<Integer> quantity,
            HttpServletRequest request
             ) {

        List<CartUse> dataCart = new ArrayList<>();
        if(cookieSer.checkContainCookie(request.getCookies())) {
            String codeCart = cookieSer.getCookie(request.getCookies()).getValue();
         oderSer.addProductToCart(
                    codeCart
                    ,idOption.orElseThrow()
                    , idSize.orElseThrow()
                    , quantity.orElseThrow());

            dataCart = oderSer.getAllDataCart(codeCart);
        }
        model.addAttribute("dataCart", dataCart);
        return  "user/fragments/common/Oder :: oder";
    };
}

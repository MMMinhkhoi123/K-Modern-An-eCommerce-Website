package com.leventsclone.leventsclone.api.user;


import com.leventsclone.leventsclone.data.response.InventoryRes;
import com.leventsclone.leventsclone.data.use.VoucherUse;
import com.leventsclone.leventsclone.service.impl.CartSer;
import com.leventsclone.leventsclone.service.impl.CookieSer;
import com.leventsclone.leventsclone.service.impl.VoucherSer;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "cart")
public class CartApi {

    private final VoucherSer voucherSer;
    private final CookieSer cookieSer;

    private final CartSer cartSer;

    @Autowired
    public CartApi(CookieSer cookieSer, CartSer cartSer, VoucherSer voucherSer) {
        this.cookieSer = cookieSer;
        this.cartSer = cartSer;
        this.voucherSer = voucherSer;
    }

    @PutMapping("/add-voucher/{code}")
    public Boolean addVoucher(HttpServletRequest request, @PathVariable Optional<String> code) {
        Cookie cookie = cookieSer.getCookie(request.getCookies());
        VoucherUse voucherUse =  voucherSer.getByCodeR(code.orElseThrow());
        cartSer.addVoucher(voucherUse, cookie.getValue());
        return  true;
    }

    @DeleteMapping("/delete-all-voucher")
    public Boolean deleteAllVoucher(HttpServletRequest request) {
        Cookie cookie = cookieSer.getCookie(request.getCookies());
        cartSer.cleanVoucher(cookie.getValue());
        return  true;
    }


    @GetMapping("/checkInventory")
    public ResponseEntity<List<InventoryRes>> checkInventory(HttpServletRequest request) {
        Cookie cookie = cookieSer.getCookie(request.getCookies());
        return  ResponseEntity.ok(cartSer.checkInventory(cookie.getValue()));
    }

}

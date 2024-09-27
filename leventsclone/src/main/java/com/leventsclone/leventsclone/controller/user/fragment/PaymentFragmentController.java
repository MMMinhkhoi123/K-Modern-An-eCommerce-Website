package com.leventsclone.leventsclone.controller.user.fragment;

import com.leventsclone.leventsclone.data.dto.InfoOrderDto;
import com.leventsclone.leventsclone.data.dto.ResultDto;
import com.leventsclone.leventsclone.data.use.AuthenticatedUse;
import com.leventsclone.leventsclone.data.use.CartUse;
import com.leventsclone.leventsclone.data.use.VoucherUse;
import com.leventsclone.leventsclone.entity.Orders;
import com.leventsclone.leventsclone.entity.User;
import com.leventsclone.leventsclone.entity.Voucher;
import com.leventsclone.leventsclone.service.impl.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Controller
@RequestMapping("/pay-order")
public class PaymentFragmentController {

    private final OrderSer orderSer;

    private final UserVoucherSer userVoucherSer;
    private final CookieSer cookieSer;
    private final VoucherSer voucherSer;
    private final UserSer userSer;
    private final CartSer cartSer;

    @Autowired
    public PaymentFragmentController(VoucherSer voucherSer,OrderSer orderSer, CookieSer cookieSer, CartSer cartSer, UserSer userSer, UserVoucherSer userVoucher) {
        this.orderSer = orderSer;
        this.cookieSer = cookieSer;
        this.cartSer = cartSer;
        this.voucherSer = voucherSer;
        this.userSer = userSer;
        this.userVoucherSer = userVoucher;
    }


    @MessageMapping("/order")
    @SendTo("/order/new")
    public  String notifyOrder() {
        return  "New Order";
    }



    @PostMapping("/save")
    public String saveOrder(Model model, @RequestBody Optional<InfoOrderDto> infoOrderDto, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Cookie cookie = cookieSer.getCookie(cookies);

        User user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser")) {
            user = userSer.getByEmail(authentication.getName()).orElseThrow();
            model.addAttribute("authenticated", userSer.getByUserName(authentication.getName()));
        }

        ResultDto resultDto = orderSer.save(infoOrderDto.orElseThrow(), user, cookie);

        Orders data =  orderSer .getByCode(resultDto.getCode());
        int total = orderSer.getTotalByCode(resultDto.getCode());
        int totalSet = data.getSumOrder();
        int ship = orderSer.getPriceShipByCode(resultDto.getCode());
        int total_old = orderSer.getTotalOldByCode(resultDto.getCode());


        Orders orders = orderSer.getByCode(resultDto.getCode());
        if(orders.getUser() != null) {
            model.addAttribute("userMember", userSer.getByUserName(orders.getUser().getEmailUser()));
        }
        model.addAttribute("total_old", total_old);
        model.addAttribute("total_products", total);
        model.addAttribute("ship", ship);
        model.addAttribute("total", totalSet);
        model.addAttribute("result",resultDto);

        model.addAttribute("voucherUses",orderSer.getVoucherUse(resultDto.getCode()));
        model.addAttribute("dataCartCheckout", orderSer.getInfoByCode(resultDto.getCode()));
        model.addAttribute("infoOrder", orderSer.getDetailByCode(resultDto.getCode()));
        return  "user/Page/checkouts :: #checkout";
    }


    @PutMapping("/update-notify-email/{code}/{value}")
    public String updateEmailGetNotify(Model model,@PathVariable("code") Optional<String> code, @PathVariable("value") Optional<String> email) {
        orderSer.updateEmail(code.orElseThrow(), email.orElseThrow());
        Orders orders = orderSer.getByCode(code.orElseThrow());
        model.addAttribute("emailNotify", orders.getInfoOrder().getEmailNotifyOrder());
        return  "user/fragments/checkout/InfoResult :: #result__update";
    }






    @PutMapping("/add-voucher/{code}/{value}")
    public String addVoucher(HttpServletRequest request, @PathVariable("code") Optional<String> code, @PathVariable("value") Optional<Integer> value,Model model) {
        Cookie cookie = cookieSer.getCookie(request.getCookies());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser")) {
            model.addAttribute("authenticated", userSer.getByUserName(authentication.getName()));
            if(!voucherSer.checkByCode(code.orElseThrow())) {
                model.addAttribute("errorVoucher", "Mã không hợp lệ");
            }else  {
                if(voucherSer.checkByCode(code.orElseThrow())) {
                    VoucherUse voucherUse = voucherSer.getByCodeR(code.orElseThrow());
                    Voucher voucher = voucherSer.getByIdR(voucherUse.getId());
                    User user = userSer.getByEmail(authentication.getName()).orElseThrow();
                    if(userVoucherSer.getByUserAndVoucher(user, voucher)) {
                        if(!cartSer.checkUseVoucher(voucherUse, cookie.getValue())) {
                            model.addAttribute("errorVoucher", "Mã đang được xữ dụng");
                        }else  {
                            cartSer.addVoucher(voucherUse, cookie.getValue());
                        }
                    } else  {
                        model.addAttribute("errorVoucher", "Mã không hợp lệ");
                    }
                } else  {
                    model.addAttribute("errorVoucher", "Mã không hợp lệ");
                }
            }
        }

        List<CartUse> dataCart = cartSer.getAllDataCart(cookie.getValue());
        List<VoucherUse> voucherUses = cartSer.getVoucherUseCart(cookie.getValue());

        int totalProducts = cartSer.getTotalProducts(cookie.getValue());
        int total_old = cartSer.getTotalOld(cookie.getValue(), value.orElseThrow());

        Integer totalResult = totalProducts;
        Authentication authentications = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser")) {
            AuthenticatedUse authenticatedUse = userSer.getByUserName(authentications.getName());
            totalResult = (int) (total_old - (total_old * ((double) authenticatedUse.getMemberUse().getPercent() / 100)));
            model.addAttribute("authenticated", authenticatedUse);
        }

        model.addAttribute("total_products", totalProducts);
        model.addAttribute("total_old", total_old);
        model.addAttribute("total", totalResult);
        model.addAttribute("dataCartCheckout", dataCart);
        model.addAttribute("voucherUses",voucherUses);
        model.addAttribute("dataCart", new ArrayList<>());
        model.addAttribute("codeCart", cookie.getValue());
        return "user/fragments/checkout/detailProduct :: detailProduct";
    }




    @DeleteMapping("/delete-voucher/{code}/{value}")
    public String deleteVoucher(HttpServletRequest request,
                                @PathVariable("value") Optional<Integer> value,
                                @PathVariable("code") Optional<String> code,Model model) {

        Cookie cookie = cookieSer.getCookie(request.getCookies());
        cartSer.deleteVoucher(code.orElseThrow(), cookie.getValue());
        List<CartUse> dataCart = cartSer.getAllDataCart(cookie.getValue());
        List<VoucherUse> voucherUses = cartSer.getVoucherUseCart(cookie.getValue());

        int totalProducts = cartSer.getTotalProducts(cookie.getValue());
        int total_old = cartSer.getTotalOld(cookie.getValue(), value.orElseThrow());


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer totalResult = totalProducts;
        if(!authentication.getName().equals("anonymousUser")) {
            AuthenticatedUse authenticatedUse = userSer.getByUserName(authentication.getName());
            totalResult = (int) (total_old - (total_old * ((double) authenticatedUse.getMemberUse().getPercent() / 100)));
            model.addAttribute("authenticated", authenticatedUse);
        }


        model.addAttribute("total_old", total_old);
        model.addAttribute("total_products", totalProducts);
        model.addAttribute("total", totalResult);
        model.addAttribute("dataCartCheckout", dataCart);
        model.addAttribute("voucherUses",voucherUses);
        model.addAttribute("dataCart", new ArrayList<>());
        model.addAttribute("codeCart", cookie.getValue());

        return "user/fragments/checkout/detailProduct :: detailProduct";
    }
}

package com.leventsclone.leventsclone.controller.user.Views;

import com.leventsclone.leventsclone.data.dto.ResultDto;
import com.leventsclone.leventsclone.data.use.AuthenticatedUse;
import com.leventsclone.leventsclone.data.use.CartUse;
import com.leventsclone.leventsclone.data.use.VoucherUse;
import com.leventsclone.leventsclone.entity.Orders;
import com.leventsclone.leventsclone.entity.User;
import com.leventsclone.leventsclone.service.impl.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Controller
@RequestMapping(value = "/pages")
public class PagesController {
    private final CookieSer cookieSer;

    private final AddressSer addressSer;
    private final CartSer cartSer;

    private final UserSer userSer;

    private final OrderSer orderSer;

    private final FeedbackSer feedbackSer;

    private final MemberSer memberSer;

    @Autowired
    public PagesController(MemberSer memberSer, UserSer userSer,OrderSer orderSer,FeedbackSer feedbackSer, CookieSer cookieSer, CartSer oderSer, AddressSer addressSer) {
        this.addressSer = addressSer;
        this.cookieSer = cookieSer;
        this.cartSer = oderSer;
        this.feedbackSer = feedbackSer;
        this.orderSer = orderSer;
        this.userSer = userSer;
        this.memberSer = memberSer;
    }
    public void  authentication(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser")) {
            AuthenticatedUse user = userSer.getByUserName(authentication.getName());
            model.addAttribute("authenticated", user );
            model.addAttribute("members", memberSer.getAll());
        }
    }



    @GetMapping(value = "/shipping")
    public String goPageShipping(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Cookie cookie = cookieSer.getCookie(cookies);
        List<CartUse> list =  cartSer.getAllDataCart(cookie.getValue());
        cartSer.cleanVoucher(cookie.getValue());

        AtomicReference<Integer> sum = new AtomicReference<>(0);

        cartSer.getAllDataCart(cookie.getValue()).forEach((e) -> {
            sum.set((int) (sum.get() + (e.getOptionUse().getPriceCorrect() + e.getSumPrice())));
        });

        if(!list.isEmpty()) {
            model.addAttribute("packaging", true);
        }
        Integer sumGet = sum.get();
        model.addAttribute("total", sumGet);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser")) {
            User user = userSer.getByEmail(authentication.getName()).orElseThrow();
            model.addAttribute("addressDefault", addressSer.getByUserActive(user));
            model.addAttribute("authenticated", userSer.getByUserName(authentication.getName()));
        }
        model.addAttribute("cookie", cookie.getValue());
        return "user/Page/Shipping";
    }

    @GetMapping(value = "/checkout/rs/{idOrder}")
    public String GoResult(@PathVariable(value = "idOrder") Optional<String> idOrder, Model model) {

        if(orderSer.checkCode(idOrder.orElseThrow())) {
            Orders orders = orderSer.getByCode(idOrder.orElseThrow());
            ResultDto resultDto = new ResultDto();
            resultDto.setCode(idOrder.orElseThrow());
            resultDto.setTypeTransport(orders.getInfoOrder().getTypeTransportOrder());

            authentication(model);

            int total =orderSer.getTotalByCode(orders.getCodeOrder());
            int ship = orderSer.getPriceShipByCode(orders.getCodeOrder());
            int totalSet = orderSer.getTotalSetByCode(orders.getCodeOrder());
            int total_old = orderSer.getTotalOldByCode(orders.getCodeOrder());

            if(orders.getUser() != null) {
                model.addAttribute("userMember", userSer.getByUserName(orders.getUser().getEmailUser()));
            }

            if(orders.getInfoOrder().getEmailNotifyOrder() != null){
                model.addAttribute("emailNotify", orders.getInfoOrder().getEmailNotifyOrder());
            }
            model.addAttribute("total_products", total);
            model.addAttribute("total_old", total_old);
            model.addAttribute("ship", ship);
            model.addAttribute("total", orders.getSumOrder());

            model.addAttribute("dataCartCheckout", orderSer.getInfoByCode(idOrder.orElseThrow()));
            model.addAttribute("voucherUses",orderSer.getVoucherUse(idOrder.orElseThrow()));
            model.addAttribute("result",resultDto);
            model.addAttribute("infoOrder", orderSer.getDetailByCode(idOrder.orElseThrow()));
            return  "user/Page/checkouts";
        }
        return  "redirect:/";
    }



    private Integer total = 0;
    @GetMapping(value = "/checkout/{valueCart}")
    public String goPageCheckOut(@PathVariable(value = "valueCart") Optional<String> data, Model model) {
        if(data.isPresent()) {
            total = 0;
            List<CartUse> dataCart = cartSer.getAllDataCart(data.orElseThrow());
            List<VoucherUse> voucherUses = cartSer.getVoucherUseCart(data.orElseThrow());
            // get total =
            dataCart.forEach((e) -> {
                total = total + e.getOptionUse().getPriceCorrect()  *   e.getQuantity();
            });
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            model.addAttribute("total_products", total);

            AtomicInteger priceVoucherSubtract = new AtomicInteger();
            voucherUses.forEach((e) -> {
                if(Objects.equals(e.getType(), "PERCENT")) {
                    int priceGet = (int) (total * Double.valueOf (e.getPercent() / 100));
                    priceVoucherSubtract.set((int) (priceVoucherSubtract.get() + priceGet));
                }else  {
                    priceVoucherSubtract.set((int) (priceVoucherSubtract.get() + e.getPrice()));
                }
            });

            int priceShip = 35000;
            if(total > 1000000) {
                priceShip = 0;
            }
            int total_old = (total + priceShip) - priceVoucherSubtract.get();
            model.addAttribute("total_old", total_old);

            if(!authentication.getName().equals("anonymousUser")) {
                AuthenticatedUse authenticatedUse = userSer.getByUserName(authentication.getName());
                total = (int) (total_old - (total_old *  Double.valueOf((double) authenticatedUse.getMemberUse().getPercent() / 100)));
                model.addAttribute("authenticated", authenticatedUse);
            }else  {
                total = total + priceShip;
            }
            model.addAttribute("total", total);
            model.addAttribute("dataCartCheckout", dataCart);
            model.addAttribute("voucherUses",voucherUses);
            model.addAttribute("codeCart", data.orElseThrow());
            model.addAttribute("dataCart", new ArrayList<>());
        }
        return "user/Page/checkouts";
    }






    @GetMapping(value = "/checkout-change-ship/{valueCart}/{value}")
    public String getDetailChangeMethodShip(@PathVariable(value = "valueCart") Optional<String> data,
                                            @PathVariable(value = "value") Optional<Integer> value,
                                            Model model) {
        if(data.isPresent()) {
            total = 0;
            List<CartUse> dataCart = cartSer.getAllDataCart(data.orElseThrow());

            List<VoucherUse> voucherUses = cartSer.getVoucherUseCart(data.orElseThrow());
            // get total =
            dataCart.forEach((e) -> {
                total = total + (Integer.parseInt(String.valueOf(e.getOptionUse().getPriceCorrect()).split("\\.")[0])  *   e.getQuantity());
            });

            model.addAttribute("total_products", total);

            AtomicInteger priceVoucherSubtract = new AtomicInteger();
            voucherUses.forEach((e) -> {
                if(Objects.equals(e.getType(), "PERCENT")) {
                    int priceGet = (int) (total * (e.getPercent() / 100));
                    priceVoucherSubtract.set((int) (priceVoucherSubtract.get() + priceGet));
                }else  {
                    priceVoucherSubtract.set((int) (priceVoucherSubtract.get() + e.getPrice()));
                }
            });

            int total_old = (total + value.orElseThrow()) - priceVoucherSubtract.get();
            model.addAttribute("total_old", total_old);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if(!authentication.getName().equals("anonymousUser")) {
                AuthenticatedUse authenticatedUse = userSer.getByUserName(authentication.getName());
                total = (int) (total_old - (total_old * ((double) authenticatedUse.getMemberUse().getPercent() / 100)));
                model.addAttribute("authenticated", authenticatedUse);
            }  else  {
                total = total + value.orElseThrow();
            }
            model.addAttribute("ship", value.orElseThrow());
            model.addAttribute("total", total);
            model.addAttribute("dataCartCheckout", dataCart);
            model.addAttribute("voucherUses",voucherUses);
            model.addAttribute("codeCart", data.orElseThrow());
            model.addAttribute("dataCart", new ArrayList<>());
        }
        return "user/fragments/checkout/detailProduct :: detailProduct";
    }




    @GetMapping(value = "/levents-crew")
    public String goPageCrew(Model model) {
        authentication(model);
        model.addAttribute("feedbacks",feedbackSer.getAll());
        model.addAttribute("dataCart", new ArrayList<>());
        return "user/Page/crews";
    }
}

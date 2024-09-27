package com.leventsclone.leventsclone.controller.admin.customer;

import com.leventsclone.leventsclone.controller.common.AuthenticatedData;
import com.leventsclone.leventsclone.data.use.AuthenticatedUse;
import com.leventsclone.leventsclone.service.impl.OrderSer;
import com.leventsclone.leventsclone.service.impl.UserSer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/admin/customer")
public class customerController {

    private final   OrderSer orderSer;

    private final AuthenticatedData authenticatedData;

    public customerController(OrderSer orderSer, AuthenticatedData authenticatedData) {
        this.orderSer = orderSer;
        this.authenticatedData = authenticatedData;
    }


    @GetMapping("")
    public String getHome(Model model, @RequestParam("state") Optional<String> state) {
        authenticatedData.authentication(model);
        if(state.isPresent()) {
            model.addAttribute("customers", orderSer.getInfoByState(state.orElseThrow()));
        }else {
            model.addAttribute("customers", orderSer.getInfoCustomer());
        }
        return "admin/PageAdmin/cutomer/customer";
    }

    @GetMapping("/search")
    public String getDataSearch(Model model, @RequestParam("q") Optional<String> data, @RequestParam("state") Optional<String> sate) {
        authenticatedData.authentication(model);
        model.addAttribute("customers", orderSer.searchInfoByCode(data.orElseThrow(), sate.orElseThrow()));
        return "admin/fragmentsAdmin/customer/customer :: customer";
    }

    @GetMapping("/state/{key}")
    public String getState(Model model, @PathVariable("key") Optional<String> data) {
        authenticatedData.authentication(model);
        model.addAttribute("customers", orderSer.getInfoByState(data.orElseThrow()));
        return "admin/fragmentsAdmin/customer/customer :: customer";
    }

}

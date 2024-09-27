package com.leventsclone.leventsclone.controller.admin.report;

import com.leventsclone.leventsclone.controller.common.AuthenticatedData;
import com.leventsclone.leventsclone.data.use.EstimateMonthlyUse;
import com.leventsclone.leventsclone.service.impl.OrderSer;
import com.leventsclone.leventsclone.service.impl.UserSer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/admin/report")
public class reportController {
    private final AuthenticatedData authenticatedData;
    private final OrderSer orderSer;

    private final UserSer userSer;

    public  reportController(AuthenticatedData data, OrderSer orderSer, UserSer userSer) {
        authenticatedData = data;
        this.orderSer = orderSer;
        this.userSer = userSer;
    }

    @GetMapping("")
    public String getIndex(Model model) {
        authenticatedData.authentication(model);
        model.addAttribute("datas",orderSer.estimateMonthlyOrder(2024));
        model.addAttribute("option", "ORDER");
        model.addAttribute("year", 2024);
        return  "admin/PageAdmin/report/reportData";
    }

    @PostMapping("/filter")
    public String postData(Model model,
                           @RequestParam("option")Optional<String> option,
                           @RequestParam("year") Optional<Integer> year
    ) {
        authenticatedData.authentication(model);
        List<EstimateMonthlyUse> estimateMonthlyUses = new LinkedList<>();
        switch (option.orElseThrow()) {
            case "ORDER" -> {
                estimateMonthlyUses = orderSer.estimateMonthlyOrder(year.orElseThrow());
            }
            case "USER" -> {
                estimateMonthlyUses = userSer.estimateMonthlyUser(year.orElseThrow());
            }
            case "PROFIT" ->  {
                estimateMonthlyUses = orderSer.estimateMonthlyProfit(year.orElseThrow());
            }
            case "PRODUCT" -> {
                estimateMonthlyUses = orderSer.estimateMonthlyProduct(year.orElseThrow());
            }
        }
        model.addAttribute("option", option.orElseThrow());
        model.addAttribute("year", year.orElseThrow());
        model.addAttribute("datas",estimateMonthlyUses);
        return  "admin/PageAdmin/report/reportData :: #admin__screen-frame2";
    }
}

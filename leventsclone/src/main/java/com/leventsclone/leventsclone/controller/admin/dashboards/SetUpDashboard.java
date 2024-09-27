package com.leventsclone.leventsclone.controller.admin.dashboards;

import com.leventsclone.leventsclone.controller.common.AuthenticatedData;
import com.leventsclone.leventsclone.data.use.EstimateUse;
import com.leventsclone.leventsclone.service.impl.OrderSer;
import com.leventsclone.leventsclone.service.impl.UserSer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/admin/dashboard")
public class SetUpDashboard {

    private final AuthenticatedData authenticatedData;
    private final OrderSer orderSer;

    private final UserSer userSer;


    public  SetUpDashboard(AuthenticatedData data,OrderSer orderSer, UserSer userSer) {
        authenticatedData = data;
        this.orderSer = orderSer;
        this.userSer = userSer;
    }

    @GetMapping("")
    public String getDashboard(Model model) {
        authenticatedData.authentication(model);
        model.addAttribute("order",orderSer.estimateOrder("WEEK"));
        model.addAttribute("product", orderSer.estimateOption("WEEK"));
        model.addAttribute("money",orderSer.estimateUseProfit("WEEK"));
        model.addAttribute("user",userSer.estimateUser("WEEK"));
        model.addAttribute("key","WEEK");
        return  "admin/PageAdmin/Dashboard/dashboard";
    }


    @GetMapping("filter-overview/{number}")
    public String filterOverView(Model model, @PathVariable(value = "number")Optional<String> value) {
        authenticatedData.authentication(model);
        EstimateUse estimateProduct = orderSer.estimateOption(value.orElseThrow());
        EstimateUse estimateUser = userSer.estimateUser(value.orElseThrow());
        EstimateUse estimateOrder = orderSer.estimateOrder(value.orElseThrow());
        EstimateUse estimateMoney =orderSer.estimateUseProfit(value.orElseThrow());


        model.addAttribute("product", estimateProduct);
        model.addAttribute("user", estimateUser);
        model.addAttribute("order",estimateOrder);
        model.addAttribute("money",estimateMoney);
        model.addAttribute("key",value.orElseThrow());

        return  "admin/fragmentsAdmin/dashboard/overview :: overview";
    }


}

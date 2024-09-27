package com.leventsclone.leventsclone.controller.admin.order;

import com.leventsclone.leventsclone.controller.common.AuthenticatedData;
import com.leventsclone.leventsclone.data.request.OderDefectReq;
import com.leventsclone.leventsclone.data.response.OrderRes;
import com.leventsclone.leventsclone.data.use.AuthenticatedUse;
import com.leventsclone.leventsclone.service.impl.CloudinarySer;
import com.leventsclone.leventsclone.service.impl.OrderSer;
import com.leventsclone.leventsclone.service.impl.UserSer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Controller
@RequestMapping("/admin/order")
public class orderController {

    private final OrderSer orderSer;

    private final UserSer userSer;

    private final AuthenticatedData authenticatedData;



    public orderController(OrderSer orderSer, UserSer userSer, AuthenticatedData authenticatedData) {
        this.orderSer = orderSer;
        this.userSer = userSer;
        this.authenticatedData = authenticatedData;
    }



    @GetMapping("/test-form")
    public String order(Model model) {
        authenticatedData.authentication(model);
        model.addAttribute("content", "khoi");
        return  "StateOrder";
    }





    @GetMapping("")
    public String getHome(Model model,
                          @RequestParam("type") Optional<String> typeDetail,
                          @RequestParam("target") Optional<String> target,
                          @RequestParam("state") Optional<String> state,
                          @RequestParam("q") Optional<String> key,
                          @RequestParam("pay") Optional<String> pay,
                          @RequestParam("date") Optional<String> data,
                          @RequestParam("detail") Optional<String> detail) {
        authenticatedData.authentication(model);
        String type = "view";
        Map<String, String> booleanMap = new HashMap<>();
        List<OrderRes> orderRes = orderSer.getAll();
        if(state.isPresent()) {
            if(!state.orElseThrow().isEmpty()) {
                    booleanMap.put("state", state.orElseThrow());
                orderRes = orderSer.getOrderByCode(state.orElseThrow());
            }
        } else {
            booleanMap.put("state", "ALL");
        }

        if(key.isPresent()) {
            if(!key.orElseThrow().isEmpty()) {
                booleanMap.put("search", key.orElseThrow());
                orderRes = orderSer.filterByKey(orderRes, key.orElseThrow());
            }
        }
        if(pay.isPresent()) {
            if(!pay.orElseThrow().isEmpty()) {
                booleanMap.put("pay", pay.orElseThrow());
                orderRes = orderSer.filterByTypePay(orderRes, pay.orElseThrow());
                System.out.println(orderRes.size());
            }
        }
        if(data.isPresent()) {
            if(!data.orElseThrow().isEmpty()) {
                booleanMap.put("date", data.orElseThrow());
                orderRes = orderSer.filterByDate(orderRes, data.orElseThrow());
            }
        }
        if(detail.isPresent()) {
            model.addAttribute("orderSpecific", orderSer.getByCodeF(detail.orElseThrow()));
            model.addAttribute("optional", typeDetail.orElseThrow());
            model.addAttribute("code", detail.orElseThrow());
            model.addAttribute("productsOrder", orderSer.getDetailOrder(detail.orElseThrow()));
            model.addAttribute("situation", orderSer.getSituation(detail.orElseThrow()));
            if(typeDetail.orElseThrow().equals("products")) {
                model.addAttribute("target", target.orElseThrow());
            }

            if(typeDetail.orElseThrow().equals("products")) {
                model.addAttribute("sum", orderSer.getSumByCode(detail.orElseThrow()));
                if(target.orElseThrow().equals("product")) {
                    model.addAttribute("productsOrder", orderSer.getDetailOrder(detail.orElseThrow()));
                } else  {
                    model.addAttribute("vouchersOrder", orderSer.getVoucherUse(detail.orElseThrow()));
                }
            }
            type = "detail";
        }
        model.addAttribute("type", type);
        model.addAttribute("keys",booleanMap);
        model.addAttribute("orders",orderRes);
        return "admin/PageAdmin/order/order";
    }

    @GetMapping("/detail/{detail}")
    public String getDetail(Model model, @PathVariable("detail") Optional<String> detail) {
        authenticatedData.authentication(model);
        model.addAttribute("type", "detail");
        model.addAttribute("target", "product");
        model.addAttribute("optional", "products");
        model.addAttribute("orderSpecific", orderSer.getByCodeF(detail.orElseThrow()));
        model.addAttribute("code", detail.orElseThrow());
        model.addAttribute("sum", orderSer.getSumByCode(detail.orElseThrow()));
        model.addAttribute("situation", orderSer.getSituation(detail.orElseThrow()));
        model.addAttribute("productsOrder", orderSer.getDetailOrder(detail.orElseThrow()));
        return  "admin/PageAdmin/order/order :: #admin__screen";
    }


    @GetMapping("/filter-option")
    public String getFilter(Model model,
                            @RequestParam("state") Optional<String> state,
                            @RequestParam("q") Optional<String> key,
                            @RequestParam("pay") Optional<String> pay,
                            @RequestParam("date") Optional<String> data
    ) {
        authenticatedData.authentication(model);
        List<OrderRes> orderRes = orderSer.getAll();
        if(state.isPresent()) {
            if(!state.orElseThrow().isEmpty()) {
                orderRes = orderSer.getOrderByCode(state.orElseThrow());
            }
        }
        if(key.isPresent()) {
            if(!key.orElseThrow().isEmpty()) {
                orderRes = orderSer.filterByKey(orderRes, key.orElseThrow());
            }
        }
        if(pay.isPresent()) {
            if(!pay.orElseThrow().isEmpty()) {
                orderRes = orderSer.filterByTypePay(orderRes, pay.orElseThrow());
                System.out.println(orderRes.size());
            }
        }
        if(data.isPresent()) {
            if(!data.orElseThrow().isEmpty()) {
                orderRes = orderSer.filterByDate(orderRes, data.orElseThrow());
            }
        }
        model.addAttribute("orders",orderRes);
        return "admin/fragmentsAdmin/order/order :: order";
    }

    @GetMapping("/products-tab/{key}/{code}")
    public String getProductsTab(Model model, @PathVariable("key") Optional<String> type, @PathVariable("code") Optional<String> code) {
        authenticatedData.authentication(model);
        if(type.isPresent()) {
            model.addAttribute("target", type.orElseThrow());
            model.addAttribute("sum", orderSer.getSumByCode(code.orElseThrow()));
            model.addAttribute("code", code.orElseThrow());
            if(type.orElseThrow().equals("product")) {
                model.addAttribute("productsOrder", orderSer.getDetailOrder(code.orElseThrow()));
                return  "admin/fragmentsAdmin/order/detail/detaiorder/productsDetail :: products";
            } else  {
                model.addAttribute("vouchersOrder", orderSer.getVoucherUse(code.orElseThrow()));
                return  "admin/fragmentsAdmin/order/detail/detaiorder/voucherDetail :: voucher";
            }
        }
        return  null;
    }

    @PutMapping("/updateState-defect/{key}/{code}")
    public String updateStateDefect(Model model,
                                    @PathVariable("key") Optional<String> key,
                                    @PathVariable("code") Optional<String> code,
                                    @RequestBody Optional<List<OderDefectReq>> oderDefectReq) {
        authenticatedData.authentication(model);

        model.addAttribute("orderSpecific", orderSer.updateStateDefect(code.orElseThrow(), key.orElseThrow(), oderDefectReq.orElseThrow()));
        model.addAttribute("optional", "transaction");
        model.addAttribute("code", code.orElseThrow());
        model.addAttribute("type", "detail");
        model.addAttribute("situation", orderSer.getSituation(code.orElseThrow()));
        return  "admin/fragmentsAdmin/order/detail/transactionDetail :: transaction";
    }

    @PutMapping("/updateState/{key}/{code}")
    public String updateState(Model model, @PathVariable("key") Optional<String> key, @PathVariable("code") Optional<String> code) {
        authenticatedData.authentication(model);
        model.addAttribute("orderSpecific",  orderSer.updateState(code.orElseThrow(), key.orElseThrow()));
        model.addAttribute("optional", "transaction");
        model.addAttribute("code", code.orElseThrow());
        model.addAttribute("type", "detail");
        model.addAttribute("situation", orderSer.getSituation(code.orElseThrow()));
        return  "admin/fragmentsAdmin/order/detail/transactionDetail :: transaction";
    }

    @GetMapping("/products-tab-large/{key}/{code}")
    public String getProductsTabLarge(Model model, @PathVariable("key") Optional<String> type,
                                      @PathVariable("code") Optional<String> code) {
        authenticatedData.authentication(model);

        model.addAttribute("optional", type.orElseThrow());
        model.addAttribute("code", code.orElseThrow());
        model.addAttribute("situation", orderSer.getSituation(code.orElseThrow()));
        model.addAttribute("productsOrder", orderSer.getDetailOrder(code.orElseThrow()));
        model.addAttribute("orderSpecific", orderSer.getByCodeF(code.orElseThrow()));

        if (type.orElseThrow().equals("transaction")) {
            return "admin/fragmentsAdmin/order/detail/transactionDetail :: transaction";
        }
        if(type.orElseThrow().equals("products")) {
            model.addAttribute("target", "product");
            model.addAttribute("sum", orderSer.getSumByCode(code.orElseThrow()));
            model.addAttribute("productsOrder", orderSer.getDetailOrder(code.orElseThrow()));
            return  "admin/fragmentsAdmin/order/detail/detailOrder :: detailOrders";
        }
        return  null;
    }


    @PostMapping("/upload-img-pay/{code}")
    public String uploadPayment(Model model, @RequestParam("file")MultipartFile multipartFile, @PathVariable("code") Optional<String> code
                                    ) {
        authenticatedData.authentication(model);
        orderSer.uploadImgPay(code.orElseThrow(), multipartFile);
        model.addAttribute("optional", "transaction");
        model.addAttribute("code", code.orElseThrow());
        model.addAttribute("situation", orderSer.getSituation(code.orElseThrow()));
        model.addAttribute("productsOrder", orderSer.getDetailOrder(code.orElseThrow()));
        model.addAttribute("orderSpecific", orderSer.getByCodeF(code.orElseThrow()));
        model.addAttribute("orders",orderSer.getByCodeF(code.orElseThrow()));
        return  "admin/fragmentsAdmin/order/detail/transactionDetail :: transaction";
    }

    @PostMapping("/upload-img-refund/{code}")
    public String uploadRefund(Model model, @RequestParam("file")MultipartFile multipartFile, @PathVariable("code") Optional<String> code
    ) {
        authenticatedData.authentication(model);
        orderSer.uploadImgRefund(code.orElseThrow(), multipartFile);
        model.addAttribute("optional", "transaction");
        model.addAttribute("code", code.orElseThrow());
        model.addAttribute("situation", orderSer.getSituation(code.orElseThrow()));
        model.addAttribute("productsOrder", orderSer.getDetailOrder(code.orElseThrow()));
        model.addAttribute("orderSpecific", orderSer.getByCodeF(code.orElseThrow()));
        model.addAttribute("orders",orderSer.getByCodeF(code.orElseThrow()));
        return  "admin/fragmentsAdmin/order/detail/transactionDetail :: transaction";
    }



    @GetMapping("/filter-option-overview/{state}")
    public String getFilterOverview(Model model, @PathVariable("state") Optional<String> state
    ) {
        authenticatedData.authentication(model);
        List<OrderRes> orderRes = orderSer.getOrderByCode(state.orElseThrow());
        model.addAttribute("orders",orderRes);
        return "admin/fragmentsAdmin/order/order :: order";
    }






}

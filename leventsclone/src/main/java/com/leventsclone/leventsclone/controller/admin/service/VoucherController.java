package com.leventsclone.leventsclone.controller.admin.service;

import com.leventsclone.leventsclone.controller.common.AuthenticatedData;
import com.leventsclone.leventsclone.data.use.*;
import com.leventsclone.leventsclone.service.impl.UserSer;
import com.leventsclone.leventsclone.service.impl.VoucherSer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/admin/vouchers")
public class VoucherController {

    private  final VoucherSer voucherSer;

    private final AuthenticatedData authenticatedData;

    public VoucherController(VoucherSer voucherSer, AuthenticatedData authenticatedData) {
        this.voucherSer = voucherSer;
        this.authenticatedData = authenticatedData;
    }

    private void handleAttribute(Model model,
                                 List<VoucherUse> productUses,
                                 VoucherUse VoucherUse,
                                 String type,
                                 String action) {
        model.addAttribute("type", type);
        model.addAttribute("action", action);
        model.addAttribute("voucher", VoucherUse);
        model.addAttribute("vouchers", productUses);
    }




    private void sort(List<VoucherUse> colorUses) {
        Comparator<VoucherUse> resComparator = new Comparator<VoucherUse>() {
            @Override
            public int compare(VoucherUse o1, VoucherUse o2) {
                return Double.compare(o2.getId() , o1.getId());
            }
        };
        colorUses.sort(resComparator);
    }

    @GetMapping("")
    public String getHome(Model model,
                          @RequestParam("update") Optional<String> updatePr,
                          @RequestParam("action")Optional<String> actionPr) {
        authenticatedData.authentication(model);
        List<VoucherUse> list = voucherSer.getAll();
        VoucherUse voucherUse = new VoucherUse();
        String action = "screen";
        String type = null;

        if(actionPr.isPresent()) {
            type = "add";
            action = "edit";
            sort(list);
        }
        if(updatePr.isPresent()) {
            type = "update";
            action = "edit";
            voucherUse = voucherSer.getById(Long.valueOf(updatePr.orElseThrow()));
            sort(list);
        }
        model.addAttribute("errors", new HashMap<>());
        handleAttribute( model, list, voucherUse, type, action);
        return "admin/PageAdmin/Service/Voucher";
    }

    @GetMapping(value = "/open-from-create")
    public String openFromCreate(Model model) {
        authenticatedData.authentication(model);
        List<VoucherUse> list = voucherSer.getAll();
        sort(list);
        handleAttribute( model, list, new VoucherUse(), "add", "edit");
        return  "admin/PageAdmin/Service/Voucher :: #admin__screen";
    }

    @GetMapping(value = "/open-from-update/{id}")
    public String openFromCreate(Model model, @PathVariable("id") Optional<Long> id) {
        authenticatedData.authentication(model);
        List<VoucherUse> list = voucherSer.getAll();
        sort(list);
        handleAttribute( model, list, voucherSer.getById(id.orElseThrow()), "update", "edit");
        return  "admin/PageAdmin/Service/Voucher :: #admin__screen";
    }

    @PostMapping("/save")
    public String saveVoucher(Model model, @RequestBody Optional<VoucherUse> voucherUse) {
        authenticatedData.authentication(model);
        VoucherUse voucherUse1 =  voucherUse.orElseThrow();
        Map<String, String> listError = new HashMap<>();
        if(voucherSer.checkByCode(voucherUse1.getCode())) {
            listError.put("code", "Mã phân biệt đã được sữ dụng");
        }
        if(Objects.equals(voucherUse1.getType(), "PERCENT")) {
            if(voucherUse1.getPercent() > 100 || voucherUse1.getPercent() < 0) {
                listError.put("value", "Giá trị khả dụng 0 - 100 (%)");
            }
        } else  {
            if(voucherUse1.getPrice() < 0) {
                listError.put("value", "Mức giảm từ  0 (VNĐ)");
            }
        }
        if(voucherUse1.getPriceCondition() < 0) {
            listError.put("condition", "Mức tiền áp dụng từ 0 (VNĐ)");
        }
        if(listError.isEmpty()) {
            voucherSer.save(voucherUse1);
            voucherUse1 = new VoucherUse();
            model.addAttribute("success", "Tạo mã khuyến mãi thành công");
        }
        List<VoucherUse> list = voucherSer.getAll();
        sort(list);
        model.addAttribute("errors", listError);
        handleAttribute( model, list, voucherUse1, "add", "edit");
        return  "admin/PageAdmin/Service/Voucher :: #admin__screen";
    }



    @PutMapping("/update")
    public String updateVoucher(Model model, @RequestBody Optional<VoucherUse> voucherUse) {
        authenticatedData.authentication(model);
        VoucherUse voucherUse1 =  voucherUse.orElseThrow();
        Map<String, String> listError = new HashMap<>();
        VoucherUse voucherOld = voucherSer.getById(voucherUse1.getId());
        if(!Objects.equals(voucherOld.getCode(), voucherUse1.getCode())) {
            if(voucherSer.checkByCode(voucherUse1.getCode())) {
                listError.put("code", "Mã phân biệt đã được sữ dụng");
            }
        }
        if(Objects.equals(voucherUse1.getType(), "PERCENT")) {
            if(voucherUse1.getPercent() > 100 || voucherUse1.getPercent() < 0) {
                listError.put("value", "Giá trị khả dụng 0 - 100 (%)");
            }
        } else  {
            if(voucherUse1.getPrice() < 0) {
                listError.put("value", "Mức giảm từ  0 (VNĐ)");
            }
        }
        if(voucherUse1.getPriceCondition() < 0) {
            listError.put("condition", "Mức tiền áp dụng từ 0 (VNĐ)");
        }
        if(listError.isEmpty()) {
            voucherSer.update(voucherUse1);
            voucherUse1 = voucherSer.getById(voucherUse1.getId());
            model.addAttribute("success", "Cập nhật khuyến mãi thành công");
        }
        List<VoucherUse> list = voucherSer.getAll();
        sort(list);
        model.addAttribute("errors", listError);
        handleAttribute( model, list, voucherUse1, "update", "edit");
        return  "admin/PageAdmin/Service/Voucher :: #admin__screen";
    }


    @DeleteMapping("/delete")
    public String delete(Model model,@RequestBody Optional<List<Long>> id) {
        authenticatedData.authentication(model);
        voucherSer.delete(id.orElseThrow());
        List<VoucherUse> list = voucherSer.getAll();
        handleAttribute( model, list, new VoucherUse(), "screen", "screen");
        return "admin/fragmentsAdmin/service/voucher/voucher :: voucher";
    }
}



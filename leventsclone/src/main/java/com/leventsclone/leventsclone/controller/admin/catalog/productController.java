package com.leventsclone.leventsclone.controller.admin.catalog;

import com.leventsclone.leventsclone.controller.common.AuthenticatedData;
import com.leventsclone.leventsclone.data.use.AuthenticatedUse;
import com.leventsclone.leventsclone.data.use.ProductUse;
import com.leventsclone.leventsclone.data.use.TypeProductUse;
import com.leventsclone.leventsclone.service.impl.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping(value = "/admin/product")
public class productController {
    private final ProductSer productSer;
    private final TypeProductSer typeProductSer;
    private final AuthenticatedData auth;

    public productController(ProductSer productSer, TypeProductSer typeProductSer, AuthenticatedData authentication) {
        this.typeProductSer = typeProductSer;
        this.productSer = productSer;
        this.auth = authentication;
    }

    private void handleAttribute(Model model,
                                 ProductUse productUse,
                                 List<TypeProductUse> typeProductUses,
                                 String type, String action,
                                 List<ProductUse> productUseList) {
        model.addAttribute("typeProducts", typeProductUses);
        model.addAttribute("type",type);
        model.addAttribute("product", productUse);
        model.addAttribute("action",action);
        model.addAttribute("products", productUseList);
    }



    @GetMapping("")
    public String getProduct(Model model,
                             @RequestParam(value = "action") Optional<String> target,
                             @RequestParam(value = "update") Optional<String> update,
                             @RequestParam(value = "detail") Optional<String> detail) {
        auth.authentication(model);
        String type = null;
        String action = "screen";

        List<ProductUse> productUses = productSer.getAll();
        ProductUse productUse = new ProductUse();

        if(update.isPresent()) {
            productUse = productSer.getById(Long.parseLong(update.orElseThrow()));
                action = "edit";
                type = "update";
        }
        if(target.isPresent()) {
            action = "edit";
            type = "add";
        }
        if(detail.isPresent()) {
            productUse =  productSer.getById(Long.parseLong(detail.orElseThrow()));
             action = "detail";
            type = "detail";
        }
        handleAttribute(model,
                productUse,
                typeProductSer.getAll(),
                type,
                action,
                productUses);
        return "admin/PageAdmin/catalog/product";
    }


    @DeleteMapping("/delete")
    public String deleteProduct(Model model,@RequestBody Optional<List<String>> list) {
        auth.authentication(model);
        list.orElseThrow().forEach((e) -> {
            productSer.delete(Long.parseLong(e));
        });

        List<ProductUse> productUses = productSer.getAll();
        handleAttribute(
                model
                ,null,
                null,
                null,
                "screen",
                productUses
        );
        return "admin/fragmentsAdmin/catalog/product/product :: product";
    }



    @GetMapping("/Data-List/{type}")
    public String getDataProduct(@PathVariable(value = "type") Model model) {
        auth.authentication(model);
        List<ProductUse> productUses = productSer.getAll();
        model.addAttribute("products", productUses);
        return "admin/fragmentsAdmin/catalog/product/product :: #product--list-fran";
    }

    @GetMapping("/detail/{id}")
    public String getDetailProduct(Model model, @PathVariable(value = "id") Optional<Long> id) {
        auth.authentication(model);
        handleAttribute(
                model
                ,productSer.getById(id.orElseThrow()),
                null,
                "detail",
                "detail",
                null
        );
        return "admin/PageAdmin/catalog/product :: #admin__screen";
    }

    @GetMapping("/Open-Form-update/{id}")
    public String getFormProductUpdate(Model model, @PathVariable(value = "id") Optional<Long> id) {
        auth.authentication(model);
        handleAttribute(
                model
                ,productSer.getById(id.orElseThrow()),
                typeProductSer.getAll(),
                "update",
                "edit",
                null
                );
        return "admin/PageAdmin/catalog/product :: #admin__screen";
    }

    @GetMapping("/Open-Form-add")
    public String getFormProduct(Model model) {
        auth.authentication(model);
        handleAttribute(model
                ,new ProductUse(),
                typeProductSer.getAll(),
                "add",
                "edit",
                null);
        return "admin/PageAdmin/catalog/product :: #admin__screen";
    }

    @PutMapping("/update")
    public String updateProduct(Model model, @RequestBody Optional<ProductUse> productUse) {
        auth.authentication(model);
        ProductUse productUse1 = productUse.orElseThrow();
        model.addAttribute("state", "Cập nhật sản phẩm thành công");
        productSer.update(productUse1);
        handleAttribute(model,
                productUse.orElseThrow(),
                typeProductSer.getAll(),
                "update",
                "edit",
                null);
        return "admin/PageAdmin/catalog/product :: #product__add--fra";
    }



    @PostMapping("/save")
    public String addProduct(Model model, @RequestBody Optional<ProductUse> productUse) {
        auth.authentication(model);
        model.addAttribute("state", "Thêm sản phẩm thành công");
        productSer.save( productUse.orElseThrow());
        handleAttribute(model,
                new ProductUse(),
                typeProductSer.getAll(),
                "add",
                "edit",
                null
        );
        return "admin/PageAdmin/catalog/product :: #product__add--fra";
    }
}

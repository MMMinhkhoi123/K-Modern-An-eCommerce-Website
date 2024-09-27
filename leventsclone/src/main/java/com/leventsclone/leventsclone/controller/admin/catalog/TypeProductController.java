package com.leventsclone.leventsclone.controller.admin.catalog;

import com.leventsclone.leventsclone.controller.common.AuthenticatedData;
import com.leventsclone.leventsclone.data.response.DirectoryRes;
import com.leventsclone.leventsclone.data.use.AuthenticatedUse;
import com.leventsclone.leventsclone.data.use.TypeProductUse;
import com.leventsclone.leventsclone.service.impl.DirectorySer;
import com.leventsclone.leventsclone.service.impl.TypeProductSer;
import com.leventsclone.leventsclone.service.impl.UserSer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping(value = "/admin/type_product")
public class TypeProductController {
    private final TypeProductSer typeProductSer;
    private final DirectorySer directorySer;

    private final AuthenticatedData auth;

    public TypeProductController(TypeProductSer typeProductSer, DirectorySer directorySer, AuthenticatedData authenticatedData) {
        this.typeProductSer = typeProductSer;
        this.directorySer = directorySer;
        this.auth = authenticatedData;
    }




    @GetMapping("")
    public String getPageTypeProduct(Model model,
                                     @RequestParam("update") Optional<String> updatePr,
                                     @RequestParam("action") Optional<String> actionPr) {
        auth.authentication(model);
        String action = "screen";
        String type = null;
        TypeProductUse productUse = new TypeProductUse();
        List<DirectoryRes> directoryResList = new ArrayList<>();
        List<TypeProductUse> productUseList = typeProductSer.getAll();
        if(actionPr.isPresent()) {
                action = "edit";
                type = "add";
                directoryResList = directorySer.getAll();
        }
        if(updatePr.isPresent()) {
                action = "edit";
                type = "update";
                directoryResList = directorySer.getAll();
                productUse = typeProductSer.getById(Long.valueOf(updatePr.orElseThrow()));
        }
        handleAttribute(model,
                productUse,
                directoryResList,
                type,
                action,
                productUseList);
        return "admin/PageAdmin/catalog/TypeProduct";
    }




    @PutMapping("/update")
    public String updateTypeProduct(
            @RequestBody Optional<TypeProductUse> typeProductUse,
            Model model) {
        auth.authentication(model);
        String errorUrl = null;
        TypeProductUse typeProduct = typeProductUse.orElseThrow();
        TypeProductUse typeProductUse1 = typeProductSer.getById(typeProduct.getId());
        String url = typeProduct.getKey();
        String leve1 =  url.replaceAll(" ", "-");
        String leve2 =   leve1.replaceAll("/", "-");

        if(!Objects.equals(leve2, typeProductUse1.getKey())) {
            if(typeProductSer.getByUrl(leve2).isPresent()) {
                errorUrl = "Tên đường dẫn đã được sữ dụng";
            }
        }
        if(errorUrl == null) {
            typeProductSer.update(typeProduct);
            model.addAttribute("state", "Cập nhật loại sản phẩm thành công");
        }
        model.addAttribute("errorUrl", errorUrl);

        List<TypeProductUse> productUseList = typeProductSer.getAll();
        handleAttribute(model,
                typeProduct,
                directorySer.getAll(),
                "update",
                "edit",
                productUseList
        );
        return "admin/PageAdmin/catalog/TypeProduct :: #admin__add-type-product";
    }


    @PostMapping("/save")
    public String addTypeProduct(@RequestBody Optional<TypeProductUse> typeProductUse, Model model) {
        auth.authentication(model);
        String errorUrl = null;
        TypeProductUse typeProductUse1 = typeProductUse.orElseThrow();
        TypeProductUse typeProduct = new TypeProductUse();
        if(typeProductSer.getByUrl(typeProductUse1.getKey()).isPresent()) {
            errorUrl = "Tên đường dẫn đã được sữ dụng";
            typeProduct = typeProductUse.orElseThrow();
        }else  {
            typeProductUse1.setKey(typeProductUse1.getKey());
            typeProductSer.save(typeProductUse1);
            model.addAttribute("state", "Tạo loại sản phẩm thành công");
        }
        model.addAttribute("errorUrl", errorUrl);
        List<TypeProductUse> productUseList = typeProductSer.getAll();
        handleAttribute(model,
                typeProduct,
                directorySer.getAll(),
                "add",
                "edit",
                productUseList
                );
        return "admin/PageAdmin/catalog/TypeProduct :: #admin__add-type-product";
    }

    private void handleAttribute(Model model,
                                 TypeProductUse typeProductUse,
                                 List<DirectoryRes> directoryRes,
                                 String type,
                                 String action,
                                 List<TypeProductUse> typeProductUses) {
        model.addAttribute("directorys", directoryRes);
        model.addAttribute("type",type);
        model.addAttribute("typeProductc", typeProductUse);
        model.addAttribute("action",action);
        model.addAttribute("typeProduct", typeProductUses);
    }

    @DeleteMapping(value = "/delete/{type}")
    public String delete(
            @PathVariable("type") Optional<String> type,
            @RequestBody Optional<List<String>> list,
            Model model) {
        auth.authentication(model);
        list.orElseThrow().forEach((e) -> {
            typeProductSer.delete(Long.parseLong(e));
        });
        List<TypeProductUse> productUseList = typeProductSer.getAll();
        handleAttribute(model,
                null,
                null,
                null,
                type.orElseThrow(),
                productUseList
        );
        return "admin/fragmentsAdmin/catalog/typeproduct/typeproduct :: typeProduct";
    }


    @GetMapping("/open-form-update/{id}")
    public String getFormTypeProductUpdate(
            @PathVariable(value = "id") Optional<Long> id, Model model) {
        auth.authentication(model);
        List<TypeProductUse> productUseList = typeProductSer.getAll();
        handleAttribute(model,
                typeProductSer.getById(id.orElseThrow()),
                directorySer.getAll(),
                "update",
                "edit",
                productUseList);
        return "admin/PageAdmin/catalog/TypeProduct ::  #admin__screen";
    }


    @GetMapping("/open-form-create")
    public String getFormTypeProduct(Model model) {
        auth.authentication(model);
        List<TypeProductUse> productUseList = typeProductSer.getAll();

        handleAttribute(model,
                new TypeProductUse(),
                directorySer.getAll(),
                "add",
                "edit",
                productUseList);
        return "admin/PageAdmin/catalog/TypeProduct :: #admin__screen";
    }
}

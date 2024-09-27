package com.leventsclone.leventsclone.controller.admin.catalog;

import com.leventsclone.leventsclone.controller.common.AuthenticatedData;
import com.leventsclone.leventsclone.data.use.AuthenticatedUse;
import com.leventsclone.leventsclone.data.use.SizeUse;
import com.leventsclone.leventsclone.data.use.TypeSizeUse;
import com.leventsclone.leventsclone.service.impl.SizeSer;
import com.leventsclone.leventsclone.service.impl.UserSer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping(value = "/admin/size")
public class SizeController {

    private final SizeSer sizeSer;


    private final AuthenticatedData auth;

    public SizeController(SizeSer sizeSer, AuthenticatedData authenticatedData)
    {
        this.sizeSer = sizeSer;
        this.auth = authenticatedData;
    }
    private void handleAttribute(Model model,
                                 SizeUse sizeUse,
                                 List<TypeSizeUse> typeSizeUses,
                                 String type,
                                 String action,
                                 List<SizeUse> sizeUses) {
        model.addAttribute("typeSizes", typeSizeUses);
        model.addAttribute("type",type);
        model.addAttribute("size", sizeUse);
        model.addAttribute("action",action);
        model.addAttribute("sizes", sizeUses);
    }


    @GetMapping("")
    public String getPageSize(Model model,
                              @RequestParam("update") Optional<String> updatePr,
                              @RequestParam("action") Optional<String> actionPr) {
        auth.authentication(model);
        List<SizeUse> sizeUses =  sizeSer.getAll();
       List<TypeSizeUse> typeSizeUses = new ArrayList<>();
       SizeUse sizeUse = new SizeUse();
        String action = "screen";
        String type = null;
        if(actionPr.isPresent()) {
            action = "edit";
            type = "add";
            typeSizeUses = sizeSer.getAllTypeSize();
        }
        if(updatePr.isPresent()) {
            action = "edit";
            type = "update";
            typeSizeUses = sizeSer.getAllTypeSize();
            sizeUse = sizeSer.getById(Long.valueOf(updatePr.orElseThrow()));
        }

        handleAttribute(model,
                sizeUse,
                typeSizeUses,
                type,
                action,
                sizeUses
                );
        return "admin/PageAdmin/catalog/size";
    }



    @GetMapping("/open-from-update/{id}")
    public String getFromSizeUpdate(@PathVariable(value = "id") Optional<Long> id, Model model) {
        auth.authentication(model);
        handleAttribute(model,
                sizeSer.getById(id.orElseThrow()),
                sizeSer.getAllTypeSize(),
                "update",
                "edit",
                sizeSer.getAll()
        );
        return "admin/PageAdmin/catalog/size :: #admin__screen";
    }



    @GetMapping("/open-from-create")
    public String getFromSize(Model model) {
        auth.authentication(model);
        handleAttribute(model,
                new SizeUse(),
                sizeSer.getAllTypeSize(),
                "add",
                "edit",
                sizeSer.getAll()
        );
        return "admin/PageAdmin/catalog/size :: #admin__screen";
    }


    @PutMapping("/update")
    public String updateSize(@RequestBody Optional<SizeUse> data, Model model) {
        auth.authentication(model);
        String errorName = null;
        SizeUse sizeUseNew = data.orElseThrow();
        SizeUse sizeUseOld = sizeSer.getById(data.orElseThrow().getId());
        if(!Objects.equals(sizeUseOld.getIdType(), sizeUseNew.getIdType())) {
            sizeUseOld.setIdType(sizeUseNew.getIdType());
        }
        if(!Objects.equals(sizeUseOld.getName(), sizeUseNew.getName())) {
            sizeUseOld.setName(sizeUseNew.getName());
            try {
                sizeSer.getByName(sizeUseNew.getName());
                errorName = "Tên kích thước đã được sữ dụng";
            }catch (Exception ignored) {
            }
        }
        if(errorName == null) {
            sizeSer.update(sizeUseOld);
            model.addAttribute("success", "Cập nhật kích thước thành công");
        }
        model.addAttribute("size", sizeUseNew);
        model.addAttribute("errName", errorName);


        handleAttribute(model,
                sizeUseNew,
                sizeSer.getAllTypeSize(),
                "update",
                "edit",
                sizeSer.getAll()
        );
        return "admin/PageAdmin/catalog/size :: #size-section__edit";
    }




    @PostMapping("/save")
    public String addSize(@RequestBody Optional<SizeUse> data, Model model) {
        auth.authentication(model);
        String errorName = null;
        SizeUse sizeUse = new SizeUse();
        if(data.isPresent()) {
            sizeUse = data.orElseThrow();
            try {
                sizeSer.getByName(sizeUse.getName());
                errorName = "Tên kích thước đã được sữ dụng";
            }catch (Exception ex) {
                sizeSer.save(sizeUse);
                sizeUse = new SizeUse();
                model.addAttribute("success", "Thêm kích thước thành công");
            }
        }
        model.addAttribute("errName", errorName);

             handleAttribute(model,
                sizeUse,
                sizeSer.getAllTypeSize(),
                "add",
                "edit",
                     sizeSer.getAll()
        );
        return "admin/PageAdmin/catalog/size :: #size-section__edit";
    }


    @DeleteMapping("/delete")
    public String deleteSize(
            @RequestBody Optional<List<String>> lists,
            Model model) {
        auth.authentication(model);
        lists.orElseThrow().forEach((e) -> {
            sizeSer.delete(Long.parseLong(e.trim()));
        });
        handleAttribute(model,
                null,
                null,
                null,
                "screen",
                sizeSer.getAll()
        );
        return "admin/fragmentsAdmin/catalog/size/size :: size";
    }

}

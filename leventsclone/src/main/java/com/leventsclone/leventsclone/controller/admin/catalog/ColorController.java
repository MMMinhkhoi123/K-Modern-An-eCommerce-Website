package com.leventsclone.leventsclone.controller.admin.catalog;


import com.leventsclone.leventsclone.controller.common.AuthenticatedData;
import com.leventsclone.leventsclone.data.dto.ColorDto;
import com.leventsclone.leventsclone.data.use.AuthenticatedUse;
import com.leventsclone.leventsclone.data.use.ColorUse;
import com.leventsclone.leventsclone.data.use.TypeProductUse;
import com.leventsclone.leventsclone.service.impl.ColorSer;
import com.leventsclone.leventsclone.service.impl.UserSer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/admin/color")
public class ColorController {

    private final ColorSer colorSer;
    private final AuthenticatedData authenticatedData;

    public ColorController(ColorSer colorSer, AuthenticatedData authenticatedData) {
        this.colorSer = colorSer;
        this.authenticatedData = authenticatedData;
    }


    private void handleAttribute(Model model,
                                 ColorUse colorUse,
                                 String type,
                                 String action,
                                 List<ColorUse> colorUses) {
        model.addAttribute("type",type);
        model.addAttribute("color", colorUse);
        model.addAttribute("action",action);
        model.addAttribute("colors", colorUses);
    }


    @GetMapping("")
    public String getPageColor(Model model,
                               @RequestParam("action")Optional<String> actionPr,
                               @RequestParam("update")Optional<String> updatePr
                               ) {
        authenticatedData.authentication(model);
        model.addAttribute("typeProductc", new TypeProductUse());
        List<ColorUse> colorUses = colorSer.getAll();
        ColorUse colorUse = new ColorUse();
        String action = "screen";
        String type = null;
        if(actionPr.isPresent()) {
            action = "edit";
            type = "add";
        }
        if(updatePr.isPresent()) {
            action = "edit";
            type = "update";
            colorUse = colorSer.getColorById(Long.valueOf(updatePr.orElseThrow()));
        }
        handleAttribute(model,
                colorUse,
                type,
                action,
                colorUses);
        return "admin/PageAdmin/catalog/color";
    }



    @GetMapping("/Open-From-Create")
    public String getFormCreate(Model model) {
        authenticatedData.authentication(model);
        List<ColorUse> colorUses = colorSer.getAll();
        handleAttribute(model,
                new ColorUse(),
                "add",
                "edit",
                colorUses);
        return "admin/PageAdmin/catalog/color :: #admin__screen";
    }

    @PutMapping("/update")
    public String update(
            @RequestBody Optional<ColorDto> colorDto,
            Model model)
    {
        authenticatedData.authentication(model);
        String errolName = null;
        String errolCode = null;
        ColorUse colorUse = colorSer.getColorById(colorDto.orElseThrow().getId());
        ColorDto colorDto1 = colorDto.orElseThrow();

        if(!colorUse.getName().equals(colorDto1.getName())) {
            if(colorSer.checkColorName(colorDto1.getName())) {
                errolName = "Tên màu sắc đã được sữ dụng";
            }else  {
                colorUse.setName(colorDto1.getName());
            }
        }
        if(!colorUse.getCode().equals(colorDto1.getCode())) {
            if(colorSer.checkColorCode(colorDto1.getCode())) {
                errolCode = "Mã màu sắc đã được sữ dụng";
            }else  {
                colorUse.setCode(colorDto1.getCode());
            }
        }

        if(errolCode == null && errolName == null) {
            colorSer.update(colorDto1);
            model.addAttribute("success", "Cập nhật sắc thành công");
        }

        model.addAttribute("errorName", errolName);
        model.addAttribute("errorCode", errolCode);

        List<ColorUse> colorUses = colorSer.getAll();
        handleAttribute(model,
                colorUse,
                "update",
                "edit",
                colorUses
        );
        return "admin/PageAdmin/catalog/color :: #parent__color-section--edit";
    }

    @PostMapping("/save")
    public String create(
            @RequestBody Optional<ColorDto> colorDto,
            Model model)
    {
        authenticatedData.authentication(model);
        String errolName = null;
        String errolCode = null;
        ColorDto colorDto1 = colorDto.orElseThrow();
        if(colorSer.checkColorName(colorDto1.getName())) {
            errolName = "Tên màu sắc đã được sữ dụng";
        }
        if(colorSer.checkColorCode(colorDto1.getCode())) {
            errolCode = "Mã màu sắc đã được sữ dụng";
        }
        if(errolCode == null && errolName == null) {


            colorSer.save(colorDto.orElseThrow());


            model.addAttribute("success", "Thêm màu sắc thành công");
        }

        ColorUse colorUse = new ColorUse();
        colorUse.setName(colorDto1.getName());
        colorUse.setCode(colorDto1.getCode());

        model.addAttribute("errorName", errolName);
        model.addAttribute("errorCode", errolCode);

        List<ColorUse> colorUses = colorSer.getAll();
        handleAttribute(model,
                colorUse,
                "add",
                "edit",
                colorUses
                );
        return "admin/PageAdmin/catalog/color :: #parent__color-section--edit";
    }

    @GetMapping("/Open-from-update/{id}")
    public String getFormUpdate(Model model, @PathVariable(value = "id") Optional<String> id) {
        authenticatedData.authentication(model);
        handleAttribute(model,
                colorSer.getColorById(Long.valueOf(id.orElseThrow())),
                "update",
                "edit",
                colorSer.getAll());
        return "admin/PageAdmin/catalog/color :: #admin__screen";
    }



    @DeleteMapping("/delete")
    public String deleteColor(
            Model model,
            @RequestBody Optional<List<String>> list ) {

        authenticatedData.authentication(model);

        list.orElseThrow().forEach((e) -> {
            colorSer.delete(Long.valueOf(e));
        });

        handleAttribute(model,
                 null,
                null,
                "screen",
                colorSer.getAll());
        return "admin/fragmentsAdmin/catalog/color/color :: color";
    }

}

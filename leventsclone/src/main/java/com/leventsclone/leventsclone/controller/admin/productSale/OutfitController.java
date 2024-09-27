package com.leventsclone.leventsclone.controller.admin.productSale;

import com.leventsclone.leventsclone.controller.common.AuthenticatedData;
import com.leventsclone.leventsclone.data.use.*;
import com.leventsclone.leventsclone.service.impl.OptionSer;
import com.leventsclone.leventsclone.service.impl.OutfitSer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Predicate;

@Controller
@RequestMapping(value = "/admin/outfit")
public class OutfitController {
    private final OutfitSer outfitSer;

    private final AuthenticatedData auth;

    private final OptionSer optionSer;


    public OutfitController(OutfitSer outfitSer, AuthenticatedData auth, OptionSer optionSer) {
        this.outfitSer = outfitSer;
        this.auth = auth;
        this.optionSer = optionSer;
    }


    private void handleAttribute(Model model,
                                 List<OptionUse> optionUses,
                                 OutfitUse outfitUse,
                                 String type,
                                 String action,
                                 List<OutfitUse> outfitUses,
                                 Set<OptionSizeUSe> optionSizeUSes) {
        model.addAttribute("type", type);
        model.addAttribute("action", action);
        model.addAttribute("outfit", outfitUse);
        model.addAttribute("outfits", outfitUses);
        model.addAttribute("options", optionUses);
        model.addAttribute("sizes", optionSizeUSes);
    }




    @GetMapping(value = "")
    public String getPageHome(Model model
    , @RequestParam("action")Optional<String> actionPr,
                              @RequestParam("update")Optional<String> update,
                              @RequestParam("detail")Optional<Long> idOutfit,
                              @RequestParam("mix")Optional<Long> idOutfitMix
                              ) {
        auth.authentication(model);
        String type = "";
        String action = "screen";
        OutfitUse outfitUse = null;
        List<OptionUse> list = new ArrayList<>();
        List<OutfitUse> outfitUses = outfitSer.getAllR();
        if(actionPr.isPresent()) {
            action = "edit";
            type = "add";
            list = optionSer.getAll();
        }
        if(update.isPresent()) {
            action = "edit";
            type = "update";
            outfitUse = outfitSer.getById(Long.parseLong(update.orElseThrow()));
            list = optionSer.getAll();
        }
        if(idOutfit.isPresent() && idOutfitMix.isEmpty()) {
            action = "detail";
            type = "detail";
            outfitUse = outfitSer.getById(idOutfit.orElseThrow());
            list = outfitUse.getOptionUses();
        }

        if(idOutfitMix.isPresent()) {
            action = "mix";
            type = "detail";

            outfitUse = outfitSer.getById(idOutfit.orElseThrow());
            Predicate<OptionUse> predicate = optionUse -> {
                Predicate<OptionUse> optionUsePredicate = predicate1 -> Objects.equals(predicate1.getIdOpt(), optionUse.getIdOpt());
                List<OptionUse> optionUses1 =  outfitSer.getById(idOutfit.orElseThrow()).getOptionUses().stream().filter(optionUsePredicate).toList();
                return  optionUses1.isEmpty();
            };
             list = optionSer.getAll().stream().filter(predicate).toList();
        }
        handleAttribute(model,
                list,
                outfitUse,
                 type,
                action,
                outfitUses,
                new HashSet<>());
        return  "admin/PageAdmin/productSale/outfit";
    }


    @GetMapping("/getSizeProduct/{idOpt}")
    public String getSizeProduct(Model model,@PathVariable("idOpt") Optional<Long> idOpt) {
        auth.authentication(model);
        OptionUse optionUse = optionSer.getDetailById(idOpt.orElseThrow());
        Set<OptionSizeUSe> optionSizeUSes = optionUse.getOptionSizeUSes();
        handleAttribute(model,
                new LinkedList<>(),
                null,
                "add",
                "edit",
                new LinkedList<>(),
                optionSizeUSes);
        return  "admin/fragmentsAdmin/productsSale/outfit/addoutfit :: #form__size";
    }

    @PutMapping("/update")
    public String update(Model model,
                         @RequestParam("height") Optional<String> height,
                         @RequestParam("weight") Optional<String> weight,
                         @RequestParam("images") Optional<List<String>> images,
                         @RequestParam("idOutfit") Optional<Long> idOutfit) {
        auth.authentication(model);

        String errorImg = null;


        if(images.orElseThrow().isEmpty()) {
            errorImg = "Bạn chưa chọn hình ảnh";
        }
        if(errorImg == null ) {
              outfitSer.update(height.orElseThrow(), weight.orElseThrow(), images.orElseThrow(), idOutfit.orElseThrow());

            model.addAttribute("success", "Cập nhật bộ phối thành công");
        }

        OutfitUse  outfitUse = outfitSer.getById(idOutfit.orElseThrow());

        model.addAttribute("errorImg", errorImg);
        handleAttribute(model,
                null,
                outfitUse,
                "update",
                "edit",
                outfitSer.getAllR(), null);
        return  "admin/PageAdmin/productSale/outfit :: #admin__screen";
    }

    @PostMapping(value = "/save")
    public String save(Model model,
                       @RequestParam("height") Optional<String> height,
                       @RequestParam("weight") Optional<String> weight,
                       @RequestParam("idOptionSize") Optional<Long> idOptionSize,
                       @RequestParam("images") Optional<List<String>> images) {
        auth.authentication(model);
        String errorImg = null;
        if(images.orElseThrow().isEmpty()) {
            errorImg = "Bạn chưa chọn hình ảnh";
        }
        if(errorImg == null) {
            outfitSer.Save(height.orElseThrow(), weight.orElseThrow(), images.orElseThrow(), idOptionSize.orElseThrow());
            model.addAttribute("success", "Bộ phối đã được tạo thành công");
        }
        model.addAttribute("errorImg", errorImg);

        handleAttribute(model,
                null,
                null,
                "add",
                "edit",
                outfitSer.getAllR(),
                new HashSet<>());
        return  "admin/PageAdmin/productSale/outfit :: #admin__screen";
    }


    @PostMapping(value = "/add-mix")
    public String save(Model model,
                       @RequestParam("array") Optional<List<Long>> listIdOption,
                       @RequestParam("idOutfit") Optional<Long> idOutfit) {
        auth.authentication(model);
        outfitSer.addProductMix(idOutfit.orElseThrow(), listIdOption.orElseThrow());
        OutfitUse outfitUse = outfitSer.getById(idOutfit.orElseThrow());
        handleAttribute(model,
                outfitUse.getOptionUses(),
                outfitUse,
                "detail",
                "detail",
                outfitSer.getAllR(),
                new HashSet<>());
        return  "admin/PageAdmin/productSale/outfit :: #admin__screen";
    }


    @DeleteMapping(value = "/delete-mix/{idOutfit}")
    public String deleteMix(Model model,
                         @PathVariable("idOutfit") Optional<Long> idOutfit,
                         @RequestBody Optional<List<Long>> ids) {
        auth.authentication(model);
        outfitSer.deleteMix(ids.orElseThrow(), idOutfit.orElseThrow());

        OutfitUse outfitUse = outfitSer.getById(idOutfit.orElseThrow());

        handleAttribute(model,
                outfitUse.getOptionUses(),
                outfitUse,
                "detail",
                "detail",
                outfitSer.getAllR(),
                new HashSet<>());
        return  "admin/PageAdmin/productSale/outfit :: #admin__screen";
    }

    @DeleteMapping(value = "/delete")
    public String delete(Model model, @RequestBody Optional<List<Long>> id) {
        auth.authentication(model);
        outfitSer.delete(id.orElseThrow());
        handleAttribute(model,
             null,
                null,
                null,
                "screen",
                outfitSer.getAllR(),
                new HashSet<>());
        return  "admin/fragmentsAdmin/productsSale/outfit/outfit  :: outfit";
    }


    @GetMapping(value = "/open-form-create")
    public String openFormCreate(Model model) {
        auth.authentication(model);
        handleAttribute(model,
              optionSer.getAll(),
                null,
                "add",
                "edit",
                outfitSer.getAllR(), null);
        return  "admin/PageAdmin/productSale/outfit :: #admin__screen";
    }

    @GetMapping(value = "/open-form-update/{id}")
    public String openFormCreateUpdate(Model model, @PathVariable(value = "id") Optional<Long> id) {
        auth.authentication(model);
        OutfitUse outfitUse = outfitSer.getById(id.orElseThrow());
        List<OptionUse> optionUses = optionSer.getAll();
        Set<OptionSizeUSe> optionSizeUSes = outfitUse.getOptionUse().getOptionSizeUSes();
        handleAttribute(model,
                optionUses,
                outfitUse,
                "update",
                "edit",
                outfitSer.getAllR(), optionSizeUSes);
        return  "admin/PageAdmin/productSale/outfit :: #admin__screen";
    }

    @GetMapping(value = "/open-detail/{idOutfit}")
    public String openDetail(Model model, @PathVariable(value = "idOutfit") Optional<Long> id) {
        auth.authentication(model);
        OutfitUse outfitUse = outfitSer.getById(id.orElseThrow());
        handleAttribute(model,
                outfitUse.getOptionUses(),
                outfitUse,
                "detail",
                "detail",
                new LinkedList<>(),
                new HashSet<>());
        return  "admin/PageAdmin/productSale/outfit :: #admin__screen";
    }

    @GetMapping(value = "/open-detail-add-mix/{idOutfit}")
    public String openAddMixDetail(Model model, @PathVariable(value = "idOutfit") Optional<Long> id) {
        auth.authentication(model);
        OutfitUse outfitUse = outfitSer.getById(id.orElseThrow());
        Predicate<OptionUse> predicate = optionUse -> {
            Predicate<OptionUse> optionUsePredicate = predicate1 -> Objects.equals(predicate1.getIdOpt(), optionUse.getIdOpt());
            List<OptionUse> optionUses1 = outfitUse.getOptionUses().stream().filter(optionUsePredicate).toList();
            return  optionUses1.isEmpty();
        };
        List<OptionUse> list = optionSer.getAll().stream().filter(predicate).toList();

        handleAttribute(model,
                list,
                outfitUse,
                "detail",
                "mix",
                new LinkedList<>(),
                new HashSet<>());
        return  "admin/PageAdmin/productSale/outfit :: #admin__screen";
    }
}


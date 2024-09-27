package com.leventsclone.leventsclone.controller.admin.productSale;

import com.leventsclone.leventsclone.controller.common.AuthenticatedData;
import com.leventsclone.leventsclone.data.use.*;
import com.leventsclone.leventsclone.entity.User;
import com.leventsclone.leventsclone.service.impl.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/admin/feedbacks")
public class FeedbackController {
    private final FeedbackSer feedbackSer;

    private final OptionSer optionSer;
    private final AuthenticatedData authenticatedData;

    public FeedbackController(OptionSer optionSer, FeedbackSer feedbackSer, AuthenticatedData userSer) {
        this.feedbackSer = feedbackSer;
        this.authenticatedData = userSer;
        this.optionSer = optionSer;
    }


    private void handleAttribute(Model model,
                                 List<OptionUse> optionUses,
                                 FeedbackUse feedbackUse,
                                 String type,
                                 String action,
                                 List<FeedbackUse> FeedbackUse) {
        model.addAttribute("type", type);
        model.addAttribute("action", action);
        model.addAttribute("feedback", feedbackUse);
        model.addAttribute("feedbacks", FeedbackUse);
        model.addAttribute("options", optionUses);
    }

    @GetMapping("/open-form-update/{id}")
    public String getFromUpdate(Model model, @PathVariable(value = "id")  Optional<Long> id) {
        authenticatedData.authentication(model);
        FeedbackUse feedbackUse = feedbackSer.getById(id.orElseThrow());
        List<FeedbackUse> imagesUseList = feedbackSer.getAll();
        List< OptionUse> optionUses = optionSer.getAll();
        handleAttribute(model,
                optionUses,
                feedbackUse,
                "update",
                "edit",
                imagesUseList);
        return  "admin/PageAdmin/productSale/feedback :: #admin__screen";
    }

    @GetMapping("/open-form-create")
    public String getFromCreate(Model model) {
        authenticatedData.authentication(model);
        List< OptionUse> optionUses = optionSer.getAll();
        FeedbackUse feedbackUse = new FeedbackUse();
        List<FeedbackUse> imagesUseList = feedbackSer.getAll();

        handleAttribute(model,
                optionUses,
                feedbackUse,
                "add",
                "edit",
                imagesUseList);
        return  "admin/PageAdmin/productSale/feedback :: #admin__screen";
    }

    @GetMapping("")
    public String  getHome(Model model,
                           @RequestParam("update") Optional<Long> id,
                           @RequestParam("action") Optional<String> option) {
        authenticatedData.authentication(model);
        List< OptionUse> optionUses = optionSer.getAll();
        FeedbackUse  feedbackUse = new FeedbackUse();
        String type = null;
        String action = "screen";

        if(option.isPresent()) {
            type = "add";
            action = "edit";
        }

        if(id.isPresent()) {
            type = "update";
            action = "edit";
            feedbackUse = feedbackSer.getById(id.orElseThrow());
        }

        handleAttribute(model,
                optionUses,
                feedbackUse,
                type,
                action,
                feedbackSer.getAll());
        return  "admin/PageAdmin/productSale/feedback";
    }

    @DeleteMapping(value = "/delete")
    public String delete(Model model, @RequestBody Optional<List<Long>> ids) {
        authenticatedData.authentication(model);
        feedbackSer.delete(ids.orElseThrow());
        List< OptionUse> optionUses = optionSer.getAll();
        FeedbackUse feedbackUse = new FeedbackUse();
        String type = null;
        String action = "screen";

        handleAttribute(model,
                optionUses,
                feedbackUse,
                type,
                action,
                feedbackSer.getAll());
        return  "admin/fragmentsAdmin/productsSale/feedback/feedback :: feedback";
    }

    @PostMapping("/save")
    public String  save(Model model,
                        @RequestParam(value = "optionId")Optional<Long> idOption,
                        @RequestParam(value = "file")Optional<MultipartFile> multipartFile) {
        authenticatedData.authentication(model);

        FeedbackUse feedbackUse = new FeedbackUse();
        String type = "add";
        String action = "edit";

        if(multipartFile.isPresent()) {
            feedbackSer.save(multipartFile.orElseThrow(), idOption.orElseThrow());
            model.addAttribute("success", "Thêm hình khách hàng thành công");
        }
        List< OptionUse> optionUses = optionSer.getAll();
        handleAttribute(model,
                optionUses,
                feedbackUse,
                type,
                action, null);

        return  "admin/PageAdmin/productSale/feedback :: #admin__screen";
    }

}

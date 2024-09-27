package com.leventsclone.leventsclone.controller.user.Views;

import com.leventsclone.leventsclone.data.use.AuthenticatedUse;
import com.leventsclone.leventsclone.data.use.OderUse;
import com.leventsclone.leventsclone.service.impl.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashSet;
import java.util.Set;

@Controller
public class HomeController {

    private final OptionSer optionSer;
    private final OutfitSer outfitSer;
    private final CollectionSer collectionSer;
    private final UserSer userSer;
    private final MemberSer memberSer;
    private final FeedbackSer feedbackSer;


    public  HomeController(OptionSer optionSer,MemberSer memberSer,UserSer userSer,FeedbackSer feedbackSer, CollectionSer collectionSer, OutfitSer outfitSer) {
        this.collectionSer = collectionSer;
        this.outfitSer = outfitSer;
        this.feedbackSer = feedbackSer;
        this.userSer = userSer;
        this.memberSer = memberSer;
        this.optionSer = optionSer;
    }


    @GetMapping(value = "/")
    public String testMethod(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser")) {
           AuthenticatedUse user = userSer.getByUserName(authentication.getName());
             model.addAttribute("authenticated", user );
             if(user.getRole().equalsIgnoreCase("admin") || user.getRole().equalsIgnoreCase("seller")) {
                 return "redirect:admin/dashboard";
             }
        }
        Set<OderUse> dataCart = new HashSet<>();
        model.addAttribute("option", optionSer.getBestSeller());
        model.addAttribute("dataCart", dataCart);
        model.addAttribute("members", memberSer.getAll());
        model.addAttribute("bestSeller", optionSer.getBestSeller());
        model.addAttribute("newArrive", optionSer.getNewArrive());
        model.addAttribute("ourCollection", collectionSer.getALLSwiper(6));
        model.addAttribute("mix", outfitSer.getAllSwiper());
        model.addAttribute("feedbacks", feedbackSer.getSwiper());
        return "user/Page/Index";
    }

}

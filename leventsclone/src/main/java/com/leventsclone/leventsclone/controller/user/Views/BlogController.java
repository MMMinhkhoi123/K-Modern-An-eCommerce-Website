package com.leventsclone.leventsclone.controller.user.Views;

import com.leventsclone.leventsclone.data.use.AuthenticatedUse;
import com.leventsclone.leventsclone.service.impl.MemberSer;
import com.leventsclone.leventsclone.service.impl.OutfitSer;
import com.leventsclone.leventsclone.service.impl.UserSer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping(value = "/blogs")
public class BlogController {

    private  final OutfitSer outfitSer;

    private final UserSer userSer;

    private  final MemberSer memberSer;


    @Autowired
    public BlogController(OutfitSer outfitSer, UserSer userSer, MemberSer memberSer) {
        this.outfitSer = outfitSer;
        this.userSer = userSer;
        this.memberSer = memberSer;
    }


    public void  authentication(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser")) {
            AuthenticatedUse user = userSer.getByUserName(authentication.getName());
            model.addAttribute("authenticated", user );
            model.addAttribute("members", memberSer.getAll());
        }
    }

    @GetMapping("/outfit")
    public  String  getPageOutFit(Model model) {
        authentication(model);
        model.addAttribute("mix", outfitSer.getAllR());
        model.addAttribute("active", "all");
        model.addAttribute("dataCart", new ArrayList<>());
        return  "user/Page/Outfit";
    }

    @GetMapping("/outfit/{id}")
    public  String  getPageOutFitSpecific(@PathVariable(value = "id") Optional<Long> id, Model model) {
        authentication(model);
        model.addAttribute("outfit",outfitSer.getById(id.orElseThrow()));
        model.addAttribute("dataCart", new ArrayList<>());
        return "user/Page/DetailOutfit";
    }

    @GetMapping("/outfit/tagged/{name}")
    public  String getPageOutfitType(Model model, @PathVariable(value = "name") String name) {
        authentication(model);
        model.addAttribute("mix", outfitSer.getByType(name));
        model.addAttribute("active", name);
        model.addAttribute("dataCart", new ArrayList<>());
        return  "user/Page/Outfit";
    }

}

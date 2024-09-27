package com.leventsclone.leventsclone.controller.user.Views;

import com.leventsclone.leventsclone.data.use.AuthenticatedUse;
import com.leventsclone.leventsclone.service.impl.CollectionSer;
import com.leventsclone.leventsclone.service.impl.MemberSer;
import com.leventsclone.leventsclone.service.impl.UserSer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Set;


@Controller
public class CollectionsController {
    private final CollectionSer collectionSer;

    private final UserSer userSer;

    private final MemberSer memberSer;

    public CollectionsController(CollectionSer collectionSer, UserSer userSer, MemberSer memberSer) {
        this.collectionSer = collectionSer;
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

    @GetMapping(value = "/collections")
    public String getAllCollection(Model model) {
        authentication(model);
        model.addAttribute("collections", collectionSer.getAll());
        model.addAttribute("dataCart", new ArrayList<>());
        return "user/Page/Collections";
    }
}

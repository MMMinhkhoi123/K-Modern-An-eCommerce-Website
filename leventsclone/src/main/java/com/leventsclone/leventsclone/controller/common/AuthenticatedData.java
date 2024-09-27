package com.leventsclone.leventsclone.controller.common;

import com.leventsclone.leventsclone.data.use.AuthenticatedUse;
import com.leventsclone.leventsclone.service.impl.UserSer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class AuthenticatedData {

    private final UserSer userSer;

    public AuthenticatedData(UserSer userSer) {
        this.userSer = userSer;
    }

    public void  authentication(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser")) {
            AuthenticatedUse user = userSer.getByUserName(authentication.getName());
            model.addAttribute("authenticated", user );
        }
    }
}

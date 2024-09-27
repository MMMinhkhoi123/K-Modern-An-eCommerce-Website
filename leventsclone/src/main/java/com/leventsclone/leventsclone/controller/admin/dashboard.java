package com.leventsclone.leventsclone.controller.admin;

import com.leventsclone.leventsclone.data.use.AuthenticatedUse;
import com.leventsclone.leventsclone.service.impl.UserSer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/check-authentication")
public class dashboard {

    private  final UserSer userSer;

    public  dashboard(UserSer userSer) {
        this.userSer = userSer;
    }

    @GetMapping("/admin-check")
    public String getDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthenticatedUse authenticatedUse = null;

        if (!authentication.getName().equals("anonymousUser")) {
            authenticatedUse = userSer.getByUserName(authentication.getName());
            model.addAttribute("authenticated", authenticatedUse);

            if (authenticatedUse.getRole().equalsIgnoreCase("admin") || authenticatedUse.getRole().equalsIgnoreCase("seller")) {
                return "redirect:admin/dashboard";
            }else  {
                return "redirect:/";
            }
        } else  {
            System.out.println("rkogko");
            return "redirect:/";
        }
    }
}

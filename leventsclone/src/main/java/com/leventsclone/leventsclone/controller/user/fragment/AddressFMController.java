package com.leventsclone.leventsclone.controller.user.fragment;

import com.leventsclone.leventsclone.data.use.AddressUse;
import com.leventsclone.leventsclone.entity.Address;
import com.leventsclone.leventsclone.entity.User;
import com.leventsclone.leventsclone.service.impl.AddressSer;
import com.leventsclone.leventsclone.service.impl.UserSer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(value = "/addressNew")
public class AddressFMController {

    private  final AddressSer addressSer;

    private  final UserSer userSer;


    public AddressFMController(AddressSer addressSer, UserSer userSer) {
        this.addressSer = addressSer;
        this.userSer = userSer;
    }



    @DeleteMapping("/delete/{id}")
    public String deleteAddress(@PathVariable("id") Optional<Long> id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser")) {
            addressSer.delete(id.orElseThrow());
            model.addAttribute("authenticated", userSer.getByUserName(authentication.getName()));
        }
        return "/user/fragments/authenticated/address :: address";
    }

    @PostMapping("/save")
    public String addAddress(@RequestBody Optional<AddressUse> addressUse, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser")) {
            User user = userSer.getByEmail(authentication.getName()).orElseThrow();
            addressSer.save(user, addressUse.orElseThrow());
            model.addAttribute("authenticated", userSer.getByUserName(authentication.getName()));
        }
        return "/user/fragments/authenticated/address :: address";
    }

}

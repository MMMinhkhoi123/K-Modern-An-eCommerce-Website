package com.leventsclone.leventsclone.controller.user.Views;

import com.leventsclone.leventsclone.data.use.OderUse;
import com.leventsclone.leventsclone.data.use.OptionUse;
import com.leventsclone.leventsclone.data.use.ProductSizeColorUSe;
import com.leventsclone.leventsclone.data.use.RatingUse;
import com.leventsclone.leventsclone.entity.Event;
import com.leventsclone.leventsclone.entity.User;
import com.leventsclone.leventsclone.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
@RequestMapping(value = "/products")
public class DetailController {
    private final UserSer userSer;

    private final UserWishlistSer userWishlistSer;

    private final RatingSer ratingSer;

    private final EventSer eventSer;

    private final MemberSer memberSer;

    private final OptionSer optionSer;


    @Autowired
    public DetailController(
            OptionSer optionSer,
            MemberSer memberSer, UserSer userSer, UserWishlistSer userWishlistSer, RatingSer userAssessSer, EventSer eventSer) {
        this.userSer = userSer;
        this.userWishlistSer = userWishlistSer;
        this.ratingSer = userAssessSer;
        this.eventSer = eventSer;
        this.memberSer = memberSer;
        this.optionSer = optionSer;
    }

//    @GetMapping(value = "/{name}")
//    public  String getDetailProducts(
//            @PathVariable(name = "name") Optional<String> nameProduct,
//            Model model)
//    {
//        ProductSizeColorUSe productSizeColorUSe = productSizeColorSer.getDetailByNameAndColor(nameProduct.orElseThrow());
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Long idProduct = productSizeColorUSe.getProductUse().getId();
//        Long idColor = productSizeColorUSe.getColorUse().getId();
//        if(!authentication.getName().equals("anonymousUser")) {
//            User user = userSer.getByEmail(authentication.getName()).orElseThrow();
//            model.addAttribute("members", memberSer.getAll());
//            model.addAttribute("inWishList", userWishlistSer.checkProductContain(user, idProduct, idColor ));
//            model.addAttribute("authenticated", userSer.getByUserName(authentication.getName()));
//        } else  {
//            model.addAttribute("inWishList", false);
//        }
//        if(productSizeColorUSe.getProductUse().getIdEvent() == null) {
//            model.addAttribute("pathName", "All Product");
//            model.addAttribute("path", "/collections/all-products");
//        } else  {
//            Event event = eventSer.getByIdF(productSizeColorUSe.getProductUse().getIdEvent());
//            model.addAttribute("pathName", event.getNameEvent());
//            model.addAttribute("path", "/collections/" + event.getPathEvent());
//        }
////        model.addAttribute("ratings", ratingSer.getAllByPage(0, idProduct, idColor));
//        String name = nameProduct.orElseThrow().replaceAll("-", " ");
//        model.addAttribute("url", name);
//        model.addAttribute("product", productSizeColorUSe);
//        model.addAttribute("bestSeller", optionSer.getBestSeller());
//        model.addAttribute("newArrive", optionSer.getNewArrive());
//        Set<OderUse> list = new HashSet<>();
//        model.addAttribute("dataCart", list);
//        return "user/Page/Detail";
//    }


    @GetMapping(value = "")
    public  String getDetailProduct(
            @RequestParam(name = "option") Optional<Long> idOption,
            Model model)
    {

        OptionUse optionUse = optionSer.getDetailById(idOption.orElseThrow());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!authentication.getName().equals("anonymousUser")) {
            User user = userSer.getByEmail(authentication.getName()).orElseThrow();
            model.addAttribute("members", memberSer.getAll());
            model.addAttribute("inWishList", userWishlistSer.checkProductContain(user, idOption.orElseThrow()));
            model.addAttribute("ratingResult", new RatingUse());
            model.addAttribute("userAssess", ratingSer.getRatingByUserAndOption(user,idOption.orElseThrow()));
            model.addAttribute("authenticated", userSer.getByUserName(authentication.getName()));

        } else  {
            model.addAttribute("inWishList", false);
        }

        if(optionUse.getProductUse().getIdEvent() == null) {
            model.addAttribute("pathName", "All Product");
            model.addAttribute("path", "/collections/all-products");
        } else  {
            Event event = eventSer.getByIdF(optionUse.getProductUse().getIdEvent());
            model.addAttribute("pathName", event.getNameEvent());
            model.addAttribute("path", "/collections/" + event.getPathEvent());
        }
        String name = optionUse.getProductUse().getName();
        model.addAttribute("url", name);

        Set<OderUse> list = new HashSet<>();
        model.addAttribute("dataCart", list);
        model.addAttribute("option", optionUse);
        model.addAttribute("ratings", ratingSer.getAllByPage(0, idOption.orElseThrow()));

        model.addAttribute("bestSeller", optionSer.getBestSeller());
        model.addAttribute("newArrive", optionSer.getNewArrive());
        return "user/Page/Detail";
    }




}

package com.leventsclone.leventsclone.controller.admin.customer;

import com.leventsclone.leventsclone.controller.common.AuthenticatedData;
import com.leventsclone.leventsclone.data.use.AuthenticatedUse;
import com.leventsclone.leventsclone.data.use.RatingUse;
import com.leventsclone.leventsclone.service.impl.RatingSer;
import com.leventsclone.leventsclone.service.impl.UserSer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/rating")
public class ratingController {

    private final RatingSer ratingSer;

    private final AuthenticatedData authenticatedData;

    public ratingController(AuthenticatedData authenticatedData,RatingSer ratingSer) {
        this.ratingSer = ratingSer;
        this.authenticatedData = authenticatedData;
    }

    @GetMapping("")
    public String getHome(Model model) {
        authenticatedData.authentication(model);
        List<RatingUse> ratingUses = ratingSer.getAll();
        model.addAttribute("ratings", ratingUses);
        return "admin/PageAdmin/cutomer/rating";
    }

    @PostMapping("/update")
    public String updateRating(Model model,
                               @RequestParam("state") Optional<Boolean> state,
                               @RequestParam("idRating") Optional<Long> idRating) {
        authenticatedData.authentication(model);
        ratingSer.updateRating(state.orElseThrow(), idRating.orElseThrow());
        List<RatingUse> ratingUses = ratingSer.getAll();
        model.addAttribute("ratings", ratingUses);
        return "admin/fragmentsAdmin/customer/rating/rating :: rating";
    }
}

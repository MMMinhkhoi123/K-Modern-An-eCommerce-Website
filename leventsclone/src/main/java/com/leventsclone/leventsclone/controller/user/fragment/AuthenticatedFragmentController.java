package com.leventsclone.leventsclone.controller.user.fragment;

import com.leventsclone.leventsclone.data.response.RatingRes;
import com.leventsclone.leventsclone.data.use.OptionUse;
import com.leventsclone.leventsclone.data.use.ProductSizeColorUSe;
import com.leventsclone.leventsclone.data.use.RatingUse;
import com.leventsclone.leventsclone.entity.User;
import com.leventsclone.leventsclone.service.impl.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@Controller
@RequestMapping(value = "/fragments")
public class AuthenticatedFragmentController {

    private final UserSer userSer;
    private final OrderSer orderSer;
    private final UserWishlistSer userWishlistSer;

    private final RatingSer ratingSer;

    private final  SocketSer socketSer;

    private final OptionSer optionSer;

    public AuthenticatedFragmentController(OptionSer optionSer,SocketSer socketSer, RatingSer userAssessSer, UserSer userSer, OrderSer orderSer, UserWishlistSer userWishlistSer){
        this.orderSer  = orderSer;
        this.userSer = userSer;
        this.userWishlistSer = userWishlistSer;
        this.ratingSer = userAssessSer;
        this.socketSer = socketSer;
        this.optionSer = optionSer;
    }

    @GetMapping("/myInfo")
    public String getMyInfo(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser")) {
            model.addAttribute("authenticated", userSer.getByUserName(authentication.getName()));
        }
        return "user/fragments/authenticated/individual :: individual";
    }

    @GetMapping("/myOrder")
    public String getMyOder(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser")) {
            model.addAttribute("authenticated", userSer.getByUserName(authentication.getName()));
            User user = userSer.getByEmail(authentication.getName()).orElseThrow();
            model.addAttribute("myOrders", orderSer.getOrdersByKey("handling", user));
        }
        return "user/fragments/authenticated/order :: myorder";
    }

    @GetMapping("/myAddress")
    public String getMyAddress(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser")) {
            model.addAttribute("authenticated", userSer.getByUserName(authentication.getName()));
        }
        return "user/fragments/authenticated/address :: address";
    }

    @GetMapping("/myWishlist")
    public String getMyWishlist(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser")) {
            model.addAttribute("authenticated", userSer.getByUserName(authentication.getName()));
        }
        return "user/fragments/authenticated/wishlist :: wishlist";
    }

    @GetMapping("/myMember")
    public String getMyMember(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser")) {
            model.addAttribute("authenticated", userSer.getByUserName(authentication.getName()));
        }
        return "user/fragments/authenticated/member";
    }


    @GetMapping("/myMemberDetail")
    public String getMemberDetail(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser")) {
            model.addAttribute("authenticated", userSer.getByUserName(authentication.getName()));
        }
        return "user/fragments/authenticated/member/memberDetail";
    }

    @GetMapping("/myMemberChange")
    public String getMemberChange(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser")) {
            model.addAttribute("authenticated", userSer.getByUserName(authentication.getName()));
        }
        return "user/fragments/authenticated/member/memberChange";
    }



    @GetMapping("/myVoucher")
    public String getMyVoucher(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser")) {
            model.addAttribute("authenticated", userSer.getByUserName(authentication.getName()));
        }
        return "user/fragments/authenticated/voucher :: voucher";
    }


    @PutMapping("/destroyOrder/{code}/{key}")
    public String destroyOrder(Model model, @PathVariable("code")Optional<String> code,  @PathVariable("key")Optional<String> key) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        orderSer.updateState(code.orElseThrow(),"TỪ CHỐI".trim());
        socketSer.notifyCancelOrder(code.orElseThrow());
        if(!authentication.getName().equals("anonymousUser")) {
            model.addAttribute("authenticated", userSer.getByUserName(authentication.getName()));
            User user = userSer.getByEmail(authentication.getName()).orElseThrow();
            model.addAttribute("myOrders", orderSer.getOrdersByKey(key.orElseThrow(), user));
        }
        return "user/fragments/authenticated/order/orderHandling";
    }




    @GetMapping("/myOrderKey/{key}")
    public String getMyOrder(Model model, @PathVariable("key")Optional<String> key) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser")) {
            model.addAttribute("authenticated", userSer.getByUserName(authentication.getName()));
            User user = userSer.getByEmail(authentication.getName()).orElseThrow();
            model.addAttribute("myOrders", orderSer.getOrdersByKey(key.orElseThrow(), user));
        }
        switch (key.orElseThrow()) {
            case "handling" -> {
                return "user/fragments/authenticated/order/orderHandling";
            }
            case "issue" -> {
                return "user/fragments/authenticated/order/oderIssue";
            }
            case "finish" -> {
                return "user/fragments/authenticated/order/oderSuccess";
            }
            default -> {
                return  null;
            }
        }
    }


    @DeleteMapping("/deleteWishlist-fm")
    public String delete(@RequestParam("idOpt")Optional<Long> idOption,
                         Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser")) {
            User user = userSer.getByEmail(authentication.getName()).orElseThrow();
            userWishlistSer.deleteWishlist(user,idOption.orElseThrow() );
            model.addAttribute("authenticated", userSer.getByUserName(authentication.getName()));
        }
        return  "user/fragments/authenticated/wishlist :: wishlist";
    }


    @GetMapping("/getWishlist")
    public String getDataWishList(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser")) {
            model.addAttribute("authenticated", userSer.getByUserName(authentication.getName()));
        }
        return  "user/fragments/authenticated/wishlist :: wishlist";
    }




    @PostMapping("/addAssess")
    public String addAssess(Model model, @RequestParam("title") Optional<String> title,
                            @RequestParam("content") Optional<String> content,
                            @RequestParam("count") Optional<Integer> cont,
                            @RequestParam("option") Optional<Long> idOption,
                            @RequestParam("order") Optional<String> codeOrder,
                            @RequestParam("file")Optional<MultipartFile> multipartFile) {
        OptionUse optionUse = optionSer.getDetailById(idOption.orElseThrow());

        model.addAttribute("option", optionUse);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser")) {
            User user = userSer.getByEmail(authentication.getName()).orElseThrow();
                ratingSer.save(title.orElseThrow(),user,content.orElseThrow(),multipartFile,idOption.orElseThrow(), codeOrder.orElseThrow(), cont.orElseThrow());
            model.addAttribute("authenticated", userSer.getByUserName(authentication.getName()));
        }
        model.addAttribute("ratings", ratingSer.getAllByPage(0, idOption.orElseThrow()));
        return  "user/fragments/detail/SideBar-Assess :: #viewOption__all--empty-parent";
    }


    @PostMapping("/getPageAssess")
    public String getPageAssess(Model model,
                            @RequestParam("page") Optional<Integer> page,
                                @RequestParam("count") Optional<String> count,
                            @RequestParam("option") Optional<Long> idOption) {
        OptionUse optionUse = optionSer.getDetailById(idOption.orElseThrow());
        model.addAttribute("option", optionUse);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser")) {
            model.addAttribute("authenticated", userSer.getByUserName(authentication.getName()));
        }
        RatingRes assessRes = null;
        if(!count.orElseThrow().equals("all")) {
            assessRes =  ratingSer.getAllByPageSet(page.orElseThrow(), Integer.parseInt(count.orElseThrow()), idOption.orElseThrow() );
        }else  {
            assessRes = ratingSer.getAllByPage(page.orElseThrow(), idOption.orElseThrow());
        }
        model.addAttribute("ratings", assessRes);
        return  "user/fragments/detail/SideBar-Assess :: #viewOption__all--empty-parent";
    }

    @PostMapping("/getPageAssess-star")
    public String getPageAssessStar(Model model,
                                @RequestParam("img") Optional<Boolean> imgSet,
                                @RequestParam("count") Optional<String> count,
                                @RequestParam("option") Optional<Long> idOption) {
        OptionUse optionUse = optionSer.getDetailById(idOption.orElseThrow());
        model.addAttribute("option", optionUse);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser")) {
            model.addAttribute("authenticated", userSer.getByUserName(authentication.getName()));
        }
        RatingRes assessRes = null;
        if(!count.orElseThrow().equals("all")) {
             assessRes =  ratingSer.getAllByPageSet(0, Integer.parseInt(count.orElseThrow()), idOption.orElseThrow() );
        }else  {
            assessRes = ratingSer.getAllByPage(0, idOption.orElseThrow());
        }
        model.addAttribute("ratings", assessRes);
        return  "user/fragments/detail/SideBar-Assess :: #viewOption__all--empty-parent";
    }

    @GetMapping("/getOrderRating")
    public String getRatingByOrder(Model model, @RequestParam("code") Optional<String> CodeOrder, @RequestParam("option") Optional<Long> idOption) {
        OptionUse optionUse = optionSer.getDetailById(idOption.orElseThrow());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userSer.getByEmail(authentication.getName()).orElseThrow();
        List<RatingUse> ratingUses = ratingSer.getRatingByUserAndOption(user, idOption.orElseThrow());
        Predicate<RatingUse> ratingUsePredicate = ratingUse -> Objects.equals(ratingUse.getOrderRes().getCode(), CodeOrder.orElseThrow());
        List<RatingUse> ratingUsesFilter = ratingUses.stream().filter(ratingUsePredicate).toList();

        model.addAttribute("option", optionUse);
        model.addAttribute("userAssess", ratingSer.getRatingByUserAndOption(user,idOption.orElseThrow()));
        model.addAttribute("ratingResult", ratingUsesFilter.get(0));
        return  "user/fragments/detail/SideBar-Assess :: #RatingSelectRom";
    }


    @GetMapping("/getOrderRating-clean")
    public String cleanFrom(Model model, @RequestParam("option") Optional<Long> idOption) {
        OptionUse optionUse = optionSer.getDetailById(idOption.orElseThrow());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userSer.getByEmail(authentication.getName()).orElseThrow();
        List<RatingUse> ratingUses = ratingSer.getRatingByUserAndOption(user, idOption.orElseThrow());
        model.addAttribute("option", optionUse);
        model.addAttribute("userAssess", ratingSer.getRatingByUserAndOption(user,idOption.orElseThrow()));
        model.addAttribute("ratingResult", new RatingUse());
        return  "user/fragments/detail/SideBar-Assess :: #RatingSelectRom";
    }

}

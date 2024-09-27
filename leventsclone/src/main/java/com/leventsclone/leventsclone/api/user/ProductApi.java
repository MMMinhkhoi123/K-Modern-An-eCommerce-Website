package com.leventsclone.leventsclone.api.user;

import com.leventsclone.leventsclone.entity.User;
import com.leventsclone.leventsclone.service.impl.UserSer;
import com.leventsclone.leventsclone.service.impl.UserWishlistSer;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class ProductApi {

    private  final UserWishlistSer userWishlistSer;

    private final UserSer userSer;

    public  ProductApi(UserWishlistSer userWishlistSer, UserSer userSer) {
        this.userWishlistSer = userWishlistSer;
        this.userSer = userSer;
    }

    @PostMapping(value = "/AddWishList")
    public ResponseEntity<Boolean> addWishList(@RequestParam("idOpt")Optional<Long> idOption) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser")) {
            User user = userSer.getByEmail(authentication.getName()).orElseThrow();
            userWishlistSer.addWishlist(user, idOption.orElseThrow());
        }
        return  ResponseEntity.ok(true);
    }

    @DeleteMapping(value = "/deleteWishList")
    public ResponseEntity<Boolean> deleteWishList(@RequestParam("idOpt")Optional<Long> idOption) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser")) {
            User user = userSer.getByEmail(authentication.getName()).orElseThrow();
            userWishlistSer.deleteWishlist(user, idOption.orElseThrow());
        }
        return  ResponseEntity.ok(true);
    }
}
